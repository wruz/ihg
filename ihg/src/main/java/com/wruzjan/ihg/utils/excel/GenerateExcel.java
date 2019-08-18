package com.wruzjan.ihg.utils.excel;

import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.DocumentException;
import com.wruzjan.ihg.utils.Utils;
import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.Protocol;
import com.wruzjan.ihg.utils.pdf.GeneratePDF;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.*;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GenerateExcel {

    private WritableCellFormat fontFormat;
    private WritableCellFormat times;
    private String inputFile;

    public void setOutputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public void write(Address address, Protocol protocol) throws IOException, WriteException, BiffException {
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("pl", "PL"));

        //copy template
        Workbook workbook;
        if(protocol.get_worker_name().equals("Szymon Mączyński")){
            workbook = Workbook.getWorkbook(new File(Utils.SIEMIANOWICE_XLS_SZYMON), wbSettings);
        } else {
            workbook = Workbook.getWorkbook(new File(Utils.SIEMIANOWICE_XLS_MACIEJ), wbSettings);
        }
        WritableWorkbook copy = Workbook.createWorkbook(new File(inputFile), workbook);
        WritableSheet excelSheet = copy.getSheet(0);

        //fill data
        prepareSimpleContent(excelSheet, address, protocol);

        copy.write();
        copy.close();
        workbook.close();
    }

    private void prepareSimpleContent(WritableSheet sheet, Address address, Protocol protocol)
            throws WriteException {

        addNumber(sheet, 6, 40, protocol.get_co2());

        float requiredValue = 0;

        if(protocol.is_kitchen_enabled()){
            addNumber(sheet, 3, 17, protocol.get_kitchen_airflow_windows_closed());
            addNumber(sheet, 5, 17, protocol.get_kitchen_airflow_microventilation());
            if(protocol.get_kitchen_grid_dimension_round() == 0){
                addNumber(sheet, 2, 17, protocol.get_kitchen_grid_dimension_x() * protocol.get_kitchen_grid_dimension_y());
            } else {
                addNumber(sheet, 2, 17, (protocol.get_kitchen_grid_dimension_round()/2)*(protocol.get_kitchen_grid_dimension_round()/2)*Math.PI);
            }
            requiredValue = requiredValue + 70;
        }

        if(protocol.is_bathroom_enabled()){
            addNumber(sheet, 3, 19, protocol.get_bathroom_airflow_windows_closed());
            addNumber(sheet, 5, 19, protocol.get_bathroom_airflow_microventilation());
            if(protocol.get_bathroom_grid_dimension_round() == 0){
                addNumber(sheet, 2, 19, protocol.get_bathroom_grid_dimension_x() * protocol.get_bathroom_grid_dimension_y());
            } else {
                addNumber(sheet, 2, 19, (protocol.get_bathroom_grid_dimension_round()/2)*(protocol.get_bathroom_grid_dimension_round()/2)*Math.PI);
            }
            requiredValue = requiredValue + 50;
        }

        if(protocol.is_toilet_enabled()){
            addNumber(sheet, 3, 21, protocol.get_toilet_airflow_windows_closed());
            addNumber(sheet, 5, 21, protocol.get_toilet_airflow_microventilation());
            if(protocol.get_toilet_grid_dimension_round() == 0){
                addNumber(sheet, 2, 21, protocol.get_toilet_grid_dimension_x()*protocol.get_toilet_grid_dimension_y());
            } else {
                addNumber(sheet, 2, 21, (protocol.get_toilet_grid_dimension_round()/2)*(protocol.get_toilet_grid_dimension_round()/2)*Math.PI);
            }

            requiredValue = requiredValue + 30;
        }

        addNumber(sheet, 3, 27, requiredValue);

        if(protocol.is_flue_enabled()){
            addNumber(sheet, 3, 23, protocol.get_flue_airflow_windows_closed());
            addNumber(sheet, 5, 23, protocol.get_flue_airflow_microventilation());
        }

        addNumber(sheet, 3, 10, protocol.get_temp_outside());
        addNumber(sheet, 10, 10, protocol.get_temp_inside());

//        WritableImage companyStamp = new WritableImage(1, 54, 3.5, 4, new File(Utils.SIGNATURE_PATH));
//        sheet.addImage(companyStamp);

        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        times = new WritableCellFormat(times10pt);
        times.setVerticalAlignment(VerticalAlignment.CENTRE);
        times.setAlignment(Alignment.CENTRE);
        times.setWrap(true);

        addCaption(sheet, 11, 17, protocol.get_kitchen_comments());
        addCaption(sheet, 11, 19, protocol.get_bathroom_comments());
        addCaption(sheet, 11, 21, protocol.get_toilet_comments());
        addCaption(sheet, 11, 23, protocol.get_flue_comments());

        addCaption(sheet, 1, 33, protocol.get_gas_fittings_comments());
        addCaption(sheet, 1, 43, protocol.get_equipment_comments());
        addCaption(sheet, 1, 47, protocol.get_comments_for_user());
        addCaption(sheet, 1, 51, protocol.get_comments_for_manager());

        addCaption(sheet, 1, 7, address.getName());
        addCaption(sheet, 5, 7, address.getStreet().trim() + ", " + address.getBuilding() + "/" + address.getFlat()+", "+address.getCity());
        addCaption(sheet, 10, 7, getCurrentTimeStamp());

        if(protocol.is_gas_fittings_present()){
            if (protocol.is_gas_fittings_working()) {
                addCaption(sheet, 3, 30, "szczelna");
            } else {
                addCaption(sheet, 3, 30, "nieszczelna");
            }
        } else {
            addCaption(sheet, 3, 30, "brak");
        }

        if (protocol.is_gas_cooker_present()) {
            if (protocol.is_gas_cooker_working()) {
                addCaption(sheet, 6, 36, "szczelna");
            } else {
                addCaption(sheet, 6, 36, "nieszczelna");
            }
        } else {
            addCaption(sheet, 6, 36, "brak");
        }
        if (protocol.is_bathroom_bake_present()) {
            if (protocol.is_bathroom_bake_working()) {
                addCaption(sheet, 6, 38, "szczelna");
            } else {
                addCaption(sheet, 6, 38, "nieszczelna");
            }
        } else {
            addCaption(sheet, 6, 38, "brak");
        }

    }

    private void addCaption(WritableSheet sheet, int column, int row, String s)
            throws RowsExceededException, WriteException {
        Label label;
        label = new Label(column, row, s, sheet.getCell(column, row).getCellFormat());
        sheet.addCell(label);
    }

    private void addNumberWithFormat(WritableSheet sheet, int column, int row,
                           Float fNum) throws WriteException, RowsExceededException {
        Number number;
        number = new Number(column, row, fNum, times);
        sheet.addCell(number);
    }

    private void addNumber(WritableSheet sheet, int column, int row,
                           double fNum) throws WriteException, RowsExceededException {
        Number number;
        number = new Number(column, row, fNum);
        sheet.addCell(number);
    }

    private void addLabel(WritableSheet sheet, int column, int row, String s)
            throws WriteException, RowsExceededException {
        Label label;
        label = new Label(column, row, s, times);
        sheet.addCell(label);
    }

    public String generate(Address address, Protocol protocol, boolean forceSave) throws Exception {
        GenerateExcel test = new GenerateExcel();
        String str_path = Environment.getExternalStorageDirectory().toString() + "/IHG/" + address.getCity() + "/";
        if (address.getDistrinct().isEmpty()) {
            str_path = str_path + "inne";
        } else {
            str_path = str_path + address.getDistrinct().trim();
        }
        str_path = str_path + "/" + address.getStreet().trim() + "/" + new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
        boolean success = (new File(str_path).mkdirs());
        str_path = str_path + "/" + address.getStreet().trim() + "_" + address.getBuilding().trim() + "_" + address.getFlat().trim() + "_" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) + ".xls";

        //prevent from override files
        if(!forceSave){
            if(new File(str_path).exists()){
                throw new Exception("próba nadpisania pliku dla adresu: "+address.getCity()+", "
                        +address.getStreet()+" "
                        +address.getBuilding()+"/"
                        +address.getFlat());
            }
        }

        test.setOutputFile(str_path);
        test.write(address, protocol);

        return str_path;
    }

    private String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }
}
