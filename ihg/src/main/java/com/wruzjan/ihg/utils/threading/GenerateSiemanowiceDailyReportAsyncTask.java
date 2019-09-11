package com.wruzjan.ihg.utils.threading;

import android.os.Environment;

import com.opencsv.CSVWriter;
import com.wruzjan.ihg.utils.DateUtils;
import com.wruzjan.ihg.utils.dao.AddressDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolDataSource;
import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.Protocol;
import com.wruzjan.ihg.utils.model.DailyReport;

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

public class GenerateSiemanowiceDailyReportAsyncTask extends BaseAsyncTask<Date, String> {

    @NonNull
    private final AddressDataSource addressDataSource;
    @NonNull
    private final ProtocolDataSource protocolDataSource;

    public GenerateSiemanowiceDailyReportAsyncTask(
            @NonNull AddressDataSource addressDataSource,
            @NonNull ProtocolDataSource protocolDataSource) {
        this.addressDataSource = addressDataSource;
        this.protocolDataSource = protocolDataSource;
    }

    @Override
    protected String doInBackground(Date... dates) {
        Date creationDate = dates[0];

        List<Protocol> protocols = protocolDataSource.getSiemanowiceProtocolsByCreationDate(DATABASE_DATE_FORMAT.format(creationDate));
        if (protocols.isEmpty()) {
            return null;
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
            CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);

            csvWriter.writeNext(new String[] {
                    "lokatorID",
                    "ulica",
                    "dom",
                    "mieszkanie",
                    "miasto",
                    "data przeglądu",
                    "data poprzedniego przeglądu",
                    "kuchnia",
                    "kuchnia uwagi",
                    "łazienka",
                    "łazienka uwagi",
                    "WC",
                    "WC uwagi",
                    "spalinowy",
                    "spalinowy uwagi",
                    "instalacja gazowa",
                    "instalacja gazowa uwagi",
                    "tlenek węgla",
                    "zalecenia dla lokatora",
                    "zalecenia dla zarządcy",
                    "uwagi SM"
            });

            for (int i = 0; i < protocols.size(); i++) {
                Protocol protocol = protocols.get(i);
                Protocol previousProtocol = (i > 0) ? protocols.get(i - 1) : null;

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

    private DailyReport mapToDailyReport(Protocol protocol, Protocol previousProtocol, Address address) {
        return DailyReport.Builder.builder()
                            .withStreet(address.getStreet())
                            .withHouseNumber(address.getBuilding())
                            .withFlatNumber(address.getFlat())
                            .withCity(address.getCity())
                            .withInspectionDate(protocol.get_created())
                            .withPreviousInspectionDate(previousProtocol != null ? previousProtocol.get_created() : null)
                            .withKitchen("nadmiar") //TODO
                            .withKitchenComments(protocol.get_kitchen_comments())
                            .withBathroom("nadmiar") //TODO
                            .withBathroomComments(protocol.get_bathroom_comments())
                            .withToilet("nadmiar") // TODO
                            .withToiletComments(protocol.get_toilet_comments())
                            .withFlue("nadmiar") // TODO
                            .withFlueComments(protocol.get_flue_comments())
                            .withGas("nadmiar") // TODO
                            .withGasComments(protocol.get_gas_fittings_comments())
                            .withCo2(Float.toString(protocol.get_co2()))
                            .withCommentsForUser(protocol.get_comments_for_user())
                            .withCommentsForManager(protocol.get_comments_for_manager())
                            .build();
    }

    private String getReportDirectoryPath(Date creationDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(creationDate);
        return Environment.getExternalStorageDirectory().toString() + "/IHG/raporty/Siemanowice/Administracja/" + calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH);
    }
}
