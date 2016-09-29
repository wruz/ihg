package com.wruzjan.ihg.utils.excel;

import android.os.Environment;
import android.util.Log;

import com.wruzjan.ihg.utils.Utils;
import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.ProtocolPaderewskiego;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import jxl.Cell;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.CellFormat;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

public class GenerateExcelPaderewskiego {

    private WritableCellFormat fontFormat;
    private WritableCellFormat times;
    private String inputFile;

    public void setOutputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public void write(Address address, ProtocolPaderewskiego protocol) throws IOException, WriteException, BiffException {
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("pl", "PL"));

        //copy template
        Workbook workbook;
        if(protocol.get_worker_name().equals("Szymon Mączyński")){
            workbook = Workbook.getWorkbook(new File(Utils.PADEREWSKIEGO_XLS_SZYMON), wbSettings);
        } else {
            workbook = Workbook.getWorkbook(new File(Utils.PADEREWSKIEGO_XLS_MACIEJ), wbSettings);
        }
        WritableWorkbook copy = Workbook.createWorkbook(new File(inputFile), workbook);
        WritableSheet excelSheet = copy.getSheet(0);

        //fill data
        prepareSimpleContent(excelSheet, address, protocol);

        copy.write();
        copy.close();
        workbook.close();
    }

    private void prepareSimpleContent(WritableSheet sheet, Address address, ProtocolPaderewskiego protocol)
            throws WriteException {

        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        times = new WritableCellFormat(times10pt);
        times.setVerticalAlignment(VerticalAlignment.CENTRE);
        times.setAlignment(Alignment.CENTRE);
        times.setWrap(true);

        addCaption(sheet, 0, 3, getCurrentTimeStamp());
        addCaption(sheet, 4, 1, address.getName());
        addCaption(sheet, 0, 1, address.getStreet().trim() + ", " + address.getBuilding() + "/" + address.getFlat()+", "+address.getCity());
        addCaption(sheet, 2, 3, protocol.get_telephone());
        addCaption(sheet, 4, 3, Double.toString(protocol.get_temp_outside()));
        addCaption(sheet, 6, 3, Double.toString(protocol.get_temp_inside()));

        if(protocol.is_kitchen_enabled()){
            if(protocol.get_kitchen_grid_dimension_round() == 0){
                addCaption(sheet, 1, 5, Double.toString(round(protocol.get_kitchen_grid_dimension_x()*protocol.get_kitchen_grid_dimension_y()/10000, 2)));
            } else {
                addCaption(sheet, 1, 5, Double.toString(round((protocol.get_kitchen_grid_dimension_round()/200)*(protocol.get_kitchen_grid_dimension_round()/200)*Math.PI, 2)));
            }
            double airflowClosed;
            double airflowMicro;
            double airflowOpen;
            if(protocol.get_kitchen_grid_dimension_round()==0){
                airflowClosed = protocol.get_kitchen_grid_dimension_x()*protocol.get_kitchen_grid_dimension_y()*0.36*protocol.get_kitchen_airflow_windows_closed();
                airflowMicro = protocol.get_kitchen_grid_dimension_x()*protocol.get_kitchen_grid_dimension_y()*0.36*protocol.get_kitchen_airflow_microventilation();
                airflowOpen = protocol.get_kitchen_grid_dimension_x()*protocol.get_kitchen_grid_dimension_y()*0.36*protocol.get_kitchen_airflow_windows_open();
            } else {
                airflowClosed = ((protocol.get_kitchen_grid_dimension_round()/2)*(protocol.get_kitchen_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_kitchen_airflow_windows_closed();
                airflowMicro = ((protocol.get_kitchen_grid_dimension_round()/2)*(protocol.get_kitchen_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_kitchen_airflow_microventilation();
                airflowOpen = ((protocol.get_kitchen_grid_dimension_round()/2)*(protocol.get_kitchen_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_kitchen_airflow_windows_open();
            }
            addCaption(sheet, 1, 6, Double.toString(round(airflowOpen, 2)));
            addCaption(sheet, 1, 7, Double.toString(round(airflowClosed, 2)));
            addCaption(sheet, 1, 8, Double.toString(round(airflowMicro, 2)));
        }
        if(protocol.is_kitchen_cleaned()){
            addCaption(sheet, 0, 9, "✓ przeczyszczono");
        } else {
            addCaption(sheet, 0, 9, "☐ przeczyszczono");
        }
        if(protocol.is_kitchen_hood()){
            addCaption(sheet, 0, 10, "✓ okap elektryczny");
        } else {
            addCaption(sheet, 0, 10, "☐ okap elektryczny");
        }
        if(protocol.is_kitchen_vent()){
            addCaption(sheet, 0, 11, "✓ wentylator");
        } else {
            addCaption(sheet, 0, 11, "☐ wentylator");
        }
        if(protocol.is_kitchen_inaccessible()){
            addCaption(sheet, 0, 12, "✓ zabudowa, brak dostępu");
        } else {
            addCaption(sheet, 0, 12, "☐ zabudowa, brak dostępu");
        }
        if(protocol.is_kitchen_steady()){
            addCaption(sheet, 0, 13, "✓ kratka stala");
        } else {
            addCaption(sheet, 0, 13, "☐ kratka stala");
        }
        if(protocol.is_kitchen_not_cleaned()){
            addCaption(sheet, 0, 14, "✓ nie przeczyszczono");
        } else {
            addCaption(sheet, 0, 14, "☐ nie przeczyszczono");
        }
        if(protocol.is_kitchen_others()){
            addCaption(sheet, 0, 15, "✓ inne: "+protocol.get_kitchen_others_comments());
        } else {
            addCaption(sheet, 0, 15, "☐ inne");
        }

        if(protocol.is_toilet_enabled()){
            if(protocol.get_toilet_grid_dimension_round() == 0){
                addCaption(sheet, 3, 5, Double.toString(round(protocol.get_toilet_grid_dimension_x()*protocol.get_toilet_grid_dimension_y()/10000, 2)));
            } else {
                addCaption(sheet, 3, 5, Double.toString(round((protocol.get_toilet_grid_dimension_round()/200)*(protocol.get_toilet_grid_dimension_round()/200)*Math.PI, 2)));
            }
            double airflowClosed;
            double airflowMicro;
            double airflowOpen;
            if(protocol.get_toilet_grid_dimension_round()==0){
                airflowClosed = protocol.get_toilet_grid_dimension_x()*protocol.get_toilet_grid_dimension_y()*0.36*protocol.get_toilet_airflow_windows_closed();
                airflowMicro = protocol.get_toilet_grid_dimension_x()*protocol.get_toilet_grid_dimension_y()*0.36*protocol.get_toilet_airflow_microventilation();
                airflowOpen = protocol.get_toilet_grid_dimension_x()*protocol.get_toilet_grid_dimension_y()*0.36*protocol.get_toilet_airflow_windows_open();
            } else {
                airflowClosed = ((protocol.get_toilet_grid_dimension_round()/2)*(protocol.get_toilet_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_toilet_airflow_windows_closed();
                airflowMicro = ((protocol.get_toilet_grid_dimension_round()/2)*(protocol.get_toilet_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_toilet_airflow_microventilation();
                airflowOpen = ((protocol.get_toilet_grid_dimension_round()/2)*(protocol.get_toilet_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_toilet_airflow_windows_open();
            }
            addCaption(sheet, 3, 6, Double.toString(round(airflowOpen, 2)));
            addCaption(sheet, 3, 7, Double.toString(round(airflowClosed, 2)));
            addCaption(sheet, 3, 8, Double.toString(round(airflowMicro, 2)));
        }
        if(protocol.is_toilet_cleaned()){
            addCaption(sheet, 2, 9, "✓ przeczyszczono");
        } else {
            addCaption(sheet, 2, 9, "☐ przeczyszczono");
        }
        if(protocol.is_toilet_vent()){
            addCaption(sheet, 2, 10, "✓ wentylator");
        } else {
            addCaption(sheet, 2, 10, "☐ wentylator");
        }
        if(protocol.is_toilet_inaccessible()){
            addCaption(sheet, 2, 11, "✓ zabudowa, brak dostępu");
        } else {
            addCaption(sheet, 2, 11, "☐ zabudowa, brak dostępu");
        }
        if(protocol.is_toilet_steady()){
            addCaption(sheet, 2, 12, "✓ kratka stala");
        } else {
            addCaption(sheet, 2, 12, "☐ kratka stala");
        }
        if(protocol.is_toilet_not_cleaned()){
            addCaption(sheet, 2, 13, "✓ nie przeczyszczono");
        } else {
            addCaption(sheet, 2, 13, "☐ nie przeczyszczono");
        }
        if(protocol.is_toilet_others()){
            addCaption(sheet, 2, 14, "✓ inne: "+protocol.get_toilet_others_comments());
        } else {
            addCaption(sheet, 2, 14, "☐ inne");
        }

        if(protocol.is_bath_enabled()){
            if(protocol.get_bath_grid_dimension_round() == 0){
                addCaption(sheet, 5, 5, Double.toString(round(protocol.get_bath_grid_dimension_x()*protocol.get_bath_grid_dimension_y()/10000, 2)));
            } else {
                addCaption(sheet, 5, 5, Double.toString(round((protocol.get_bath_grid_dimension_round()/200)*(protocol.get_bath_grid_dimension_round()/200)*Math.PI, 2)));
            }
            double airflowClosed;
            double airflowMicro;
            double airflowOpen;
            if(protocol.get_bath_grid_dimension_round()==0){
                airflowClosed = protocol.get_bath_grid_dimension_x()*protocol.get_bath_grid_dimension_y()*0.36*protocol.get_bath_airflow_windows_closed();
                airflowMicro = protocol.get_bath_grid_dimension_x()*protocol.get_bath_grid_dimension_y()*0.36*protocol.get_bath_airflow_microventilation();
                airflowOpen = protocol.get_bath_grid_dimension_x()*protocol.get_bath_grid_dimension_y()*0.36*protocol.get_bath_airflow_windows_open();
            } else {
                airflowClosed = ((protocol.get_bath_grid_dimension_round()/2)*(protocol.get_bath_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_bath_airflow_windows_closed();
                airflowMicro = ((protocol.get_bath_grid_dimension_round()/2)*(protocol.get_bath_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_bath_airflow_microventilation();
                airflowOpen = ((protocol.get_bath_grid_dimension_round()/2)*(protocol.get_bath_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_bath_airflow_windows_open();
            }
            addCaption(sheet, 5, 6, Double.toString(round(airflowOpen, 2)));
            addCaption(sheet, 5, 7, Double.toString(round(airflowClosed, 2)));
            addCaption(sheet, 5, 8, Double.toString(round(airflowMicro, 2)));
        }
        if(protocol.is_bath_cleaned()){
            addCaption(sheet, 4, 9, "✓ przeczyszczono");
        } else {
            addCaption(sheet, 4, 9, "☐ przeczyszczono");
        }
        if(protocol.is_bath_vent()){
            addCaption(sheet, 4, 10, "✓ wentylator");
        } else {
            addCaption(sheet, 4, 10, "☐ wentylator");
        }
        if(protocol.is_bath_inaccessible()){
            addCaption(sheet, 4, 11, "✓ zabudowa, brak dostępu");
        } else {
            addCaption(sheet, 4, 11, "☐ zabudowa, brak dostępu");
        }
        if(protocol.is_bath_steady()){
            addCaption(sheet, 4, 12, "✓ kratka stala");
        } else {
            addCaption(sheet, 4, 12, "☐ kratka stala");
        }
        if(protocol.is_bath_not_cleaned()){
            addCaption(sheet, 4, 13, "✓ nie przeczyszczono");
        } else {
            addCaption(sheet, 4, 13, "☐ nie przeczyszczono");
        }
        if(protocol.is_bath_others()){
            addCaption(sheet, 4, 14, "✓ inne: "+protocol.get_bath_others_comments());
        } else {
            addCaption(sheet, 4, 14, "☐ inne");
        }

        addCaption(sheet, 7, 5, "X");
        if(protocol.is_flue_enabled()){
            double airflowClosed = protocol.get_flue_airflow_windows_closed()*70.3;
            double airflowMicro = protocol.get_flue_airflow_microventilation()*70.3;
            double airflowOpen = protocol.get_flue_airflow_windows_open()*70.3;
            addCaption(sheet, 7, 6, Double.toString(round(airflowOpen, 2)));
            addCaption(sheet, 7, 7, Double.toString(round(airflowClosed, 2)));
            addCaption(sheet, 7, 8, Double.toString(round(airflowMicro, 2)));
        }
        if(protocol.is_flue_cleaned()){
            addCaption(sheet, 6, 9, "✓ przeczyszczono");
        } else {
            addCaption(sheet, 6, 9, "☐ przeczyszczono");
        }
        if(protocol.is_flue_boiler()){
            addCaption(sheet, 6, 10, "✓ bojler/podgrzewacz przep.");
        } else {
            addCaption(sheet, 6, 10, "☐ bojler/podgrzewacz przep.");
        }
        if(protocol.is_flue_inaccessible()){
            addCaption(sheet, 6, 11, "✓ zabudowa, brak dostępu");
        } else {
            addCaption(sheet, 6, 11, "☐ zabudowa, brak dostępu");
        }
        if(protocol.is_flue_rigid()){
            addCaption(sheet, 6, 12, "✓ sztywna rura");
        } else {
            addCaption(sheet, 6, 12, "☐ sztywna rura");
        }
        if(protocol.is_flue_alufol()){
            addCaption(sheet, 6, 13, "✓ alufol");
        } else {
            addCaption(sheet, 6, 13, "☐ alufol");
        }
        if(protocol.is_flue_not_cleaned()){
            addCaption(sheet, 6, 14, "✓ nie przeczyszczono");
        } else {
            addCaption(sheet, 6, 14, "☐ nie przeczyszczono");
        }
        if(protocol.is_flue_others()){
            addCaption(sheet, 6, 15, "✓ inne: "+protocol.get_flue_others_comments());
        } else {
            addCaption(sheet, 6, 15, "☐ inne");
        }

        addCaption(sheet, 3, 20, protocol.get_comments());
        addCaption(sheet, 0, 17, protocol.get_comments_for_user());
        addCaption(sheet, 4, 17, Double.toString(protocol.get_wind_speed()));
        addCaption(sheet, 5, 17, protocol.get_wind_direction());
        addCaption(sheet, 7, 17, Double.toString(protocol.get_pressure()));
        addCaption(sheet, 3, 19, Double.toString(protocol.get_co2()));

        WritableImage signature = new WritableImage(6, 18, 1.75, 2, new File(Utils.SIGNATURE_PATH));
        sheet.addImage(signature);

        addCaption(sheet, 2, 18, Integer.toString(protocol.get_windows_all()));
        addCaption(sheet, 2, 19, Integer.toString(protocol.get_windows_micro()));
        addCaption(sheet, 2, 20, Integer.toString(protocol.get_windows_vent()));
        addCaption(sheet, 2, 21, Integer.toString(protocol.get_windows_no_micro()));

        if(protocol.is_eq_ch_gas_meter_working()){
            if(protocol.is_eq_gas_meter_working()){
                addCaption(sheet, 0, 23, "✓ gazomierz sprawny");
            } else {
                addCaption(sheet, 0, 23, "✓ gazomierz niesprawny");
            }
        } else {
            addCaption(sheet, 0, 23, "☐ gazomierz");
        }

        if(protocol.is_eq_ch_stove_working()){
            if(protocol.is_eq_stove_working()){
                addCaption(sheet, 0, 24, "✓ kuchnia gazowa 2/4 palnikowa sprawna");
            } else {
                addCaption(sheet, 0, 24, "✓ kuchnia gazowa 2/4 palnikowa niesprawna");
            }
        } else {
            addCaption(sheet, 0, 24, "☐ kuchnia gazowa 2/4 palnikowa");
        }

        if(protocol.is_eq_ch_bake_working()){
            if(protocol.is_eq_bake_working()){
                addCaption(sheet, 0, 25, "✓ piec lazienkowy sprawny");
            } else {
                addCaption(sheet, 0, 25, "✓ piec lazienkowy niesprawny");
            }
        } else {
            addCaption(sheet, 0, 25, "☐ piec lazienkowy");
        }

        if(protocol.is_eq_ch_combi_oven_working()){
            if(protocol.is_eq_combi_oven_working()){
                addCaption(sheet, 0, 26, "✓ piec dwufunkcyjny sprawny");
            } else {
                addCaption(sheet, 0, 26, "✓ piec dwufunkcyjny niesprawny");
            }
        } else {
            addCaption(sheet, 0, 26, "☐ piec dwufunkcyjny");
        }

        if(protocol.is_eq_ch_kitchen_term_working()){
            if(protocol.is_eq_kitchen_term_working()){
                addCaption(sheet, 0, 27, "✓ terma kuchanna sprawna");
            } else {
                addCaption(sheet, 0, 27, "✓ terma kuchenna niesprawna");
            }
        } else {
            addCaption(sheet, 0, 27, "☐ terma kuchenna");
        }

        if(protocol.is_eq_ch_others()){
            addCaption(sheet, 3, 22, "✓ inne");
            addCaption(sheet, 3, 23, protocol.get_eq_other());
        } else {
            addCaption(sheet, 3, 22, "☐ inne");
        }

    }

    public String generate(Address address, ProtocolPaderewskiego protocol, boolean forceSave) throws Exception {
        GenerateExcelPaderewskiego test = new GenerateExcelPaderewskiego();
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

    private void addCaption(WritableSheet sheet, int column, int row, String s)
            throws RowsExceededException, WriteException {
        Label label;
        label = new Label(column, row, s, sheet.getCell(column, row).getCellFormat());
        sheet.addCell(label);
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
