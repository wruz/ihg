package com.wruzjan.ihg.utils.threading;

import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.opencsv.CSVWriter;
import com.wruzjan.ihg.R;
import com.wruzjan.ihg.utils.DateUtils;
import com.wruzjan.ihg.utils.dao.AddressDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolNewPaderewskiegoDataSource;
import com.wruzjan.ihg.utils.dao.StreetAndIdentifierDataSource;
import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.DailyReport;
import com.wruzjan.ihg.utils.model.ProtocolNewPaderewskiego;
import com.wruzjan.ihg.utils.model.StreetAndIdentifier;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wruzjan.ihg.utils.DateUtils.DATABASE_DATE_FORMAT;

public class GenerateNewPaderewskiegoDailyReportAsyncTask extends BaseAsyncTask<Date, String> {

    private static final String TAG = "GenerateDailyReport";

    @NonNull
    private final Application application;
    @NonNull
    private final AddressDataSource addressDataSource;
    @NonNull
    private final StreetAndIdentifierDataSource streetAndIdentifierDataSource;
    @NonNull
    private final ProtocolNewPaderewskiegoDataSource protocolDataSource;

    public GenerateNewPaderewskiegoDailyReportAsyncTask(
            @NonNull Application application,
            @NonNull AddressDataSource addressDataSource,
            @NonNull StreetAndIdentifierDataSource streetAndIdentifierDataSource,
            @NonNull ProtocolNewPaderewskiegoDataSource protocolDataSource
    ) {
        this.application = application;
        this.addressDataSource = addressDataSource;
        this.streetAndIdentifierDataSource = streetAndIdentifierDataSource;
        this.protocolDataSource = protocolDataSource;
    }

    @Override
    protected String doInBackground(Date... dates) {
        Date startDate = dates[0];
        Date endDate = dates[1];

        List<ProtocolNewPaderewskiego> protocols = protocolDataSource.getNewPaderewskiegoProtocolsFromDateRange(DATABASE_DATE_FORMAT.format(startDate), DATABASE_DATE_FORMAT.format(endDate));
        Map<Integer, Address> addresses = new HashMap<>();
        for (ProtocolNewPaderewskiego protocol : protocols) {
            Address address = addressDataSource.getAddressById(protocol.get_address_id());
            if (address != null && !addresses.containsKey(protocol.get_address_id())) {
                addresses.put(protocol.get_address_id(), address);
            }
        }

        if (protocols.isEmpty() || addresses.isEmpty()) {
            return "";
        }

        String reportDirectoryPath = getReportDirectoryPath(startDate);
        String reportFilePath = reportDirectoryPath + "/" + DateUtils.CSV_FILE_NAME_DATE_FORMAT.format(startDate) + ".csv";

        Writer writer = null;

        try {
            File folder = new File(reportDirectoryPath);
            File file = new File(reportFilePath);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            if (!file.exists()) {
                folder.createNewFile();
            }

            writer = new BufferedWriter(new FileWriter(file));
            CSVWriter csvWriter = new CSVWriter(writer);

            csvWriter.writeNext(new String[]{
                    "lokatorID",
                    "ulica",
                    "dom",
                    "mieszkanie",
                    "miasto",
                    "data przeglądu",
                    "data poprzedniego przeglądu",
                    "kuchnia uwagi",
                    "łazienka uwagi",
                    "WC uwagi",
                    "spalinowy uwagi",
                    "instalacja gazowa",
                    "kuchenka gazowa",
                    "piec łazienkowy",
                    "uwagi gaz",
                    "tlenek węgla",
                    "il. nawiewników",
                    "uwagi lokator",
                    "uwagi spółdzielnia"
            });

            ProtocolNewPaderewskiego previousProtocol = protocolDataSource.getProtocolBefore(protocols.get(0).get_id());

            for (int i = 0; i < protocols.size(); i++) {
                ProtocolNewPaderewskiego protocol = protocols.get(i);
                Address address = addresses.get(protocol.get_address_id());
                if (address != null) {
                    DailyReport bean = mapToDailyReport(protocol, (i > 0) ? protocols.get(i - 1) : previousProtocol, address);

                    csvWriter.writeNext(new String[]{
                            bean.getLocatorId(),
                            bean.getStreet(),
                            bean.getHouseNumber(),
                            bean.getFlatNumber(),
                            bean.getCity(),
                            bean.getInspectionDate(),
                            bean.getPreviousInspectionDate(),
                            bean.getKitchenComments(),
                            bean.getBathroomComments(),
                            bean.getToiletComments(),
                            bean.getFlueComments(),
                            bean.getGas(),
                            bean.getGasCooker(),
                            bean.getBathroomBake(),
                            bean.getGasComments(),
                            bean.getCo2(),
                            bean.getVentCount(),
                            bean.getCommentsForUser(),
                            bean.getCommentsForManager()
                    });
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error during processing: ", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error during processing - close: ", e);
                }
            }
        }

        return reportFilePath;
    }

    private DailyReport mapToDailyReport(@NonNull ProtocolNewPaderewskiego protocol, @Nullable ProtocolNewPaderewskiego previousProtocol, @NonNull Address address) throws ParseException {
        StreetAndIdentifier streetAndIdentifier = streetAndIdentifierDataSource.getByStreetIdentifier(address.getStreetAndIdentifierId());
        String streetName = streetAndIdentifier != null ? streetAndIdentifier.getStreetName() : address.getStreet();

        return DailyReport.newBuilder()
                .withLocatorId("")
                .withStreet(streetName)
                .withHouseNumber(address.getBuilding())
                .withFlatNumber(address.getFlat())
                .withCity(address.getCity())
                .withInspectionDate(DateUtils.fromDatabaseToCsvDate(protocol.get_created()))
                .withPreviousInspectionDate(previousProtocol != null ? DateUtils.fromDatabaseToCsvDate(previousProtocol.get_created()) : null)
                .withKitchenComments(getKitchenCommentIdsString(protocol))
                .withBathroomComments(getBathroomCommentIdsString(protocol))
                .withToiletComments(getToiletCommentIdsString(protocol))
                .withFlueComments(getFlueCommentIdsString(protocol))
                .withGas(getGasReportCode(protocol))
                .withGasCooker(getGasCookerReportCode(protocol))
                .withBathroomBake(getBathroomBakeReportCode(protocol))
                .withGasComments(protocol.get_gas_fittings_comments() != null ? protocol.get_gas_fittings_comments() : "")
                .withCo2(Float.toString(protocol.get_co2()))
                .withVentCount(Integer.toString(protocol.getVentCount()))
                .withCommentsForUser(protocol.get_comments_for_user())
                .withCommentsForManager(protocol.get_comments_for_manager())
                .withSmComments("")
                .build();
    }

    private String getKitchenCommentIdsString(ProtocolNewPaderewskiego protocol) {
        String[] entries = application.getResources().getStringArray(R.array.kitchen_comments);
        String[] reportCodes = application.getResources().getStringArray(R.array.kitchen_comment_report_codes);

        return getProtocolReportCodes(
                entries,
                reportCodes,
                protocol.get_kitchen_comments()
        );
    }

    private String getBathroomCommentIdsString(ProtocolNewPaderewskiego protocol) {
        String[] entries = application.getResources().getStringArray(R.array.bathroom_comments);
        String[] reportCodes = application.getResources().getStringArray(R.array.bathroom_comment_report_codes);

        return getProtocolReportCodes(
                entries,
                reportCodes,
                protocol.get_bathroom_comments()
        );
    }

    private String getToiletCommentIdsString(ProtocolNewPaderewskiego protocol) {
        String[] entries = application.getResources().getStringArray(R.array.toilet_comments);
        String[] reportCodes = application.getResources().getStringArray(R.array.toilet_comment_report_codes);

        return getProtocolReportCodes(
                entries,
                reportCodes,
                protocol.get_toilet_comments()
        );
    }

    private String getFlueCommentIdsString(ProtocolNewPaderewskiego protocol) {
        String[] entries = application.getResources().getStringArray(R.array.flue_comments);
        String[] reportCodes = application.getResources().getStringArray(R.array.flue_comment_report_codes);

        return getProtocolReportCodes(
                entries,
                reportCodes,
                protocol.get_flue_comments()
        );
    }


    private String getProtocolReportCodes(
            String[] entries,
            String[] reportCodes,
            String comments
    ) {
        String[] splitComments = comments.split(", ");
        ArrayList<String> protocolReportCodes = new ArrayList<>();
        for (String comment : splitComments) {
            for (int i = 0; i < entries.length; ++i) {
                if (entries[i].equalsIgnoreCase(comment)) {
                    protocolReportCodes.add(reportCodes[i]);
                }
            }
        }
        return TextUtils.join(",", protocolReportCodes);
    }

    private String getGasReportCode(ProtocolNewPaderewskiego protocol) {
        if (protocol.is_gas_fittings_present()) {
            return protocol.is_gas_fittings_working() ? "1" : "2";
        } else {
            return "3";
        }
    }

    private String getGasCookerReportCode(ProtocolNewPaderewskiego protocol) {
        if (protocol.is_gas_cooker_present()) {
            return protocol.is_gas_cooker_working() ? "1" : "2";
        } else {
            return "3";
        }
    }

    private String getBathroomBakeReportCode(ProtocolNewPaderewskiego protocol) {
        if (protocol.is_bathroom_bake_present()) {
            return protocol.is_bathroom_bake_working() ? "1" : "2";
        } else {
            return "3";
        }
    }

    private String getReportDirectoryPath(Date creationDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(creationDate);
        return Environment.getExternalStorageDirectory().toString() + "/IHG/raporty/NowyPaderewskiego/Administracja/" + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1);
    }
}
