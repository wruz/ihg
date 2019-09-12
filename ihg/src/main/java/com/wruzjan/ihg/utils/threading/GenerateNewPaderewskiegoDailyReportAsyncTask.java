package com.wruzjan.ihg.utils.threading;

import android.os.Environment;

import com.opencsv.CSVWriter;
import com.wruzjan.ihg.utils.DateUtils;
import com.wruzjan.ihg.utils.dao.AddressDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolNewPaderewskiegoDataSource;
import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.DailyReport;
import com.wruzjan.ihg.utils.model.ProtocolNewPaderewskiego;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;

import static com.wruzjan.ihg.utils.DateUtils.DATABASE_DATE_FORMAT;
import static com.wruzjan.ihg.utils.ProtocolUtils.BATHROOM_ACCEPTANCE_THRESHOLD;
import static com.wruzjan.ihg.utils.ProtocolUtils.FLUE_ACCEPTANCE_THRESHOLD;
import static com.wruzjan.ihg.utils.ProtocolUtils.KITCHEN_ACCEPTANCE_THRESHOLD;
import static com.wruzjan.ihg.utils.ProtocolUtils.TOILET_ACCEPTANCE_THRESHOLD;
import static com.wruzjan.ihg.utils.ProtocolUtils.calculateNewPaderwskiegoBathroomAirflowWindowsClosed;
import static com.wruzjan.ihg.utils.ProtocolUtils.calculateNewPaderwskiegoFlueAirflowWindowsClosed;
import static com.wruzjan.ihg.utils.ProtocolUtils.calculateNewPaderwskiegoKitchenAirflowWindowsClosed;
import static com.wruzjan.ihg.utils.ProtocolUtils.calculateNewPaderwskiegoToiletAirflowWindowsClosed;

public class GenerateNewPaderewskiegoDailyReportAsyncTask extends BaseAsyncTask<Date, String> {

    @NonNull
    private final AddressDataSource addressDataSource;
    @NonNull
    private final ProtocolNewPaderewskiegoDataSource protocolDataSource;

    public GenerateNewPaderewskiegoDailyReportAsyncTask(
            @NonNull AddressDataSource addressDataSource,
            @NonNull ProtocolNewPaderewskiegoDataSource protocolDataSource) {
        this.addressDataSource = addressDataSource;
        this.protocolDataSource = protocolDataSource;
    }

    @Override
    protected String doInBackground(Date... dates) {
        Date creationDate = dates[0];

        List<ProtocolNewPaderewskiego> protocols = protocolDataSource.getNewPaderewskiegoProtocolsByCreationDate(DATABASE_DATE_FORMAT.format(creationDate));
        if (protocols.isEmpty()) {
            return "";
        }

        String reportDirectoryPath = getReportDirectoryPath(creationDate);
        String reportFilePath = reportDirectoryPath + "/" + DateUtils.CSV_FILE_NAME_DATE_FORMAT.format(creationDate) + ".csv";

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
            CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

            csvWriter.writeNext(new String[] {
                    "lokatorID",
                    "ulica",
                    "dom",
                    "mieszkanie",
                    "miasto",
                    "data przeglądu",
                    "data poprzedniego przeglądu",
                    "kuchnia zamknięte",
                    "kuchnia uwagi",
                    "łazienka zamknięte",
                    "łazienka uwagi",
                    "WC zamknięte",
                    "WC uwagi",
                    "spalinowy zamknięte",
                    "spalinowy uwagi",
                    "instalacja gazowa zamknięte",
                    "instalacja gazowa uwagi",
                    "tlenek węgla",
                    "zalecenia dla lokatora",
                    "zalecenia dla zarządcy",
                    "uwagi SM"
            });

            for (int i = 0; i < protocols.size(); i++) {
                ProtocolNewPaderewskiego protocol = protocols.get(i);
                ProtocolNewPaderewskiego previousProtocol = (i > 0) ? protocols.get(i - 1) : null;

                Address address = addressDataSource.getAddressById(protocol.get_address_id());

                DailyReport bean = mapToDailyReport(protocol, previousProtocol, address);

                csvWriter.writeNext(new String[] {
                        bean.getLocatorId(),
                        bean.getStreet(),
                        bean.getHouseNumber(),
                        bean.getFlatNumber(),
                        bean.getCity(),
                        bean.getInspectionDate(),
                        bean.getPreviousInspectionDate(),
                        bean.getKitchen(),
                        bean.getKitchenComments(),
                        bean.getBathroom(),
                        bean.getBathroomComments(),
                        bean.getToilet(),
                        bean.getToiletComments(),
                        bean.getFlue(),
                        bean.getFlueComments(),
                        bean.getGas(),
                        bean.getGasComments(),
                        bean.getCo2(),
                        bean.getCommentsForUser(),
                        bean.getCommentsForManager()
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return reportFilePath;
    }

    private DailyReport mapToDailyReport(ProtocolNewPaderewskiego protocol, ProtocolNewPaderewskiego previousProtocol, Address address) {
        return DailyReport.Builder.builder()
                            .withStreet(address.getStreet())
                            .withHouseNumber(address.getBuilding())
                            .withFlatNumber(address.getFlat())
                            .withCity(address.getCity())
                            .withInspectionDate(protocol.get_created())
                            .withPreviousInspectionDate(previousProtocol != null ? previousProtocol.get_created() : null)
                            .withKitchen(determineOverflowOrUnderflowState(calculateNewPaderwskiegoKitchenAirflowWindowsClosed(protocol), KITCHEN_ACCEPTANCE_THRESHOLD))
                            .withKitchenComments(protocol.get_kitchen_comments())
                            .withBathroom(determineOverflowOrUnderflowState(calculateNewPaderwskiegoBathroomAirflowWindowsClosed(protocol), BATHROOM_ACCEPTANCE_THRESHOLD))
                            .withBathroomComments(protocol.get_bathroom_comments())
                            .withToilet(determineOverflowOrUnderflowState(calculateNewPaderwskiegoToiletAirflowWindowsClosed(protocol), TOILET_ACCEPTANCE_THRESHOLD))
                            .withToiletComments(protocol.get_toilet_comments())
                            .withFlue(determineOverflowOrUnderflowState(calculateNewPaderwskiegoFlueAirflowWindowsClosed(protocol), FLUE_ACCEPTANCE_THRESHOLD))
                            .withFlueComments(protocol.get_flue_comments())
                            .withGas(protocol.is_gas_cooker_working() ? "szczelna" : "nieszczelna")
                            .withGasComments(protocol.get_gas_fittings_comments())
                            .withCo2(Float.toString(protocol.get_co2()))
                            .withCommentsForUser(protocol.get_comments_for_user())
                            .withCommentsForManager(protocol.get_comments_for_manager())
                            .build();
    }

    private String determineOverflowOrUnderflowState(float airflow, int kitchenAcceptanceThreshold) {
        return airflow > kitchenAcceptanceThreshold ? "nadmiar" : "niedobór";
    }

    private String getReportDirectoryPath(Date creationDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(creationDate);
        return Environment.getExternalStorageDirectory().toString() + "/IHG/raporty/Siemanowice/Administracja/" + calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH);
    }
}
