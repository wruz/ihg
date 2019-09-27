package com.wruzjan.ihg.utils.pdf;

import android.os.Environment;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.wruzjan.ihg.utils.Utils;
import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.ProtocolPaderewskiego;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GeneratePDFPaderewskiego {

    private static void fill(AcroFields form, Address address, ProtocolPaderewskiego protocol)
            throws IOException, DocumentException {

        //load unicode font for polish characters
        BaseFont bf = BaseFont.createFont(Environment.getExternalStorageDirectory().toString()+"/IHG/fonts/arial_unicode.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        form.addSubstitutionFont(bf);
        form.setField("name", address.getName());
        form.setField("address", address.getStreet()+" "+address.getBuilding()+"/"+address.getFlat()+", "+address.getCity());
        form.setField("date", new SimpleDateFormat("yyyy MMM dd").format(Calendar.getInstance().getTime()));
        form.setField("temp_outside", Double.toString(round(protocol.get_temp_outside(), 2)));
        form.setField("temp_inside", Double.toString(round(protocol.get_temp_inside(), 2)));
        form.setField("telephone", protocol.get_telephone());

        if(protocol.is_kitchen_enabled()){
            if(protocol.get_kitchen_grid_dimension_round() == 0){
                form.setField("kitchen_dim", Double.toString(round(protocol.get_kitchen_grid_dimension_x()*protocol.get_kitchen_grid_dimension_y()/10000, 2)));
            } else {
                form.setField("kitchen_dim", Double.toString(round((protocol.get_kitchen_grid_dimension_round()/200)*(protocol.get_kitchen_grid_dimension_round()/200)*Math.PI, 2)));
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
            form.setField("kitchen_open", Double.toString(round(airflowOpen, 2)));
            form.setField("kitchen_closed", Double.toString(round(airflowClosed, 2)));
            if(Double.toString(round(airflowMicro, 2)).equals("0.0")){
                form.setField("kitchen_micro", "-");
            } else {
                form.setField("kitchen_micro", Double.toString(round(airflowMicro, 2)));
            }
        } else {
            form.setField("kitchen_dim", "-");
            form.setField("kitchen_open", "-");
            form.setField("kitchen_closed", "-");
            form.setField("kitchen_micro", "-");
        }

        if(protocol.is_kitchen_cleaned()){
            form.setField("ch_kitchen_cleaned", "Tak");
        } else {
            form.setField("ch_kitchen_cleaned", "Nie");
        }
        if(protocol.is_kitchen_hood()){
            form.setField("ch_kitchen_hood", "Tak");
        } else {
            form.setField("ch_kitchen_hood", "Nie");
        }
        if(protocol.is_kitchen_vent()){
            form.setField("ch_kitchen_vent", "Tak");
        } else {
            form.setField("ch_kitchen_vent", "Nie");
        }
        if(protocol.is_kitchen_inaccessible()){
            form.setField("ch_kitchen_inaccessible", "Tak");
        } else {
            form.setField("ch_kitchen_inaccessible", "Nie");
        }
        if(protocol.is_kitchen_steady()){
            form.setField("ch_kitchen_steady", "Tak");
        } else {
            form.setField("ch_kitchen_steady", "Nie");
        }
        if(protocol.is_kitchen_not_cleaned()){
            form.setField("ch_kitchen_not_cleaned", "Tak");
        } else {
            form.setField("ch_kitchen_not_cleaned", "Nie");
        }
        if(protocol.is_kitchen_others()){
            form.setField("ch_kitchen_others", "Tak");
            form.setField("kitchen_others", protocol.get_kitchen_others_comments());
        } else {
            form.setField("ch_kitchen_others", "Nie");
        }

        if(protocol.is_toilet_enabled()){
            if(protocol.get_toilet_grid_dimension_round() == 0){
                form.setField("toilet_dim", Double.toString(round(protocol.get_toilet_grid_dimension_x()*protocol.get_toilet_grid_dimension_x()/10000, 2)));
            } else {
                form.setField("toilet_dim", Double.toString(round((protocol.get_toilet_grid_dimension_round()/200)*(protocol.get_toilet_grid_dimension_round()/200)*Math.PI ,2)));
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
            form.setField("toilet_open", Double.toString(round(airflowOpen, 2)));
            form.setField("toilet_closed", Double.toString(round(airflowClosed, 2)));
            if(Double.toString(round(airflowMicro, 2)).equals("0.0")){
                form.setField("toilet_micro", "-");
            } else {
                form.setField("toilet_micro", Double.toString(round(airflowMicro, 2)));
            }
        } else {
            form.setField("toilet_dim", "-");
            form.setField("toilet_open", "-");
            form.setField("toilet_closed", "-");
            form.setField("toilet_micro", "-");
        }

        if(protocol.is_toilet_cleaned()){
            form.setField("ch_toilet_cleaned", "Tak");
        } else {
            form.setField("ch_toilet_cleaned", "Nie");
        }
        if(protocol.is_toilet_vent()){
            form.setField("ch_toilet_vent", "Tak");
        } else {
            form.setField("ch_toilet_vent", "Nie");
        }
        if(protocol.is_toilet_inaccessible()){
            form.setField("ch_toilet_inaccessible", "Tak");
        } else {
            form.setField("ch_toilet_inaccessible", "Nie");
        }
        if(protocol.is_toilet_steady()){
            form.setField("ch_toilet_steady", "Tak");
        } else {
            form.setField("ch_toilet_steady", "Nie");
        }
        if(protocol.is_toilet_not_cleaned()){
            form.setField("ch_toilet_not_cleaned", "Tak");
        } else {
            form.setField("ch_toilet_not_cleaned", "Nie");
        }
        if(protocol.is_toilet_others()){
            form.setField("ch_toilet_others", "Tak");
            form.setField("toilet_others", protocol.get_toilet_others_comments());
        } else {
            form.setField("ch_toilet_others", "Nie");
        }

        if(protocol.is_bath_enabled()){
            if(protocol.get_bath_grid_dimension_round() == 0){
                form.setField("bath_dim", Double.toString(round(protocol.get_bath_grid_dimension_x()*protocol.get_bath_grid_dimension_x()/10000, 2)));
            } else {
                form.setField("bath_dim", Double.toString(round((protocol.get_bath_grid_dimension_round()/200)*(protocol.get_bath_grid_dimension_round()/200)*Math.PI, 2)));
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
            form.setField("bath_open", Double.toString(round(airflowOpen, 2)));
            form.setField("bath_closed", Double.toString(round(airflowClosed, 2)));
            if(Double.toString(round(airflowMicro, 2)).equals("0.0")){
                form.setField("bath_micro", "-");
            } else {
                form.setField("bath_micro", Double.toString(round(airflowMicro, 2)));
            }
        } else {
            form.setField("bath_dim", "-");
            form.setField("bath_open", "-");
            form.setField("bath_closed", "-");
            form.setField("bath_micro", "-");
        }
        if(protocol.is_bath_cleaned()){
            form.setField("ch_bath_cleaned", "Tak");
        } else {
            form.setField("ch_bath_cleaned", "Nie");
        }
        if(protocol.is_bath_vent()){
            form.setField("ch_bath_vent", "Tak");
        } else {
            form.setField("ch_bath_vent", "Nie");
        }
        if(protocol.is_bath_inaccessible()){
            form.setField("ch_bath_inaccessible", "Tak");
        } else {
            form.setField("ch_bath_inaccessible", "Nie");
        }
        if(protocol.is_bath_steady()){
            form.setField("ch_bath_steady", "Tak");
        } else {
            form.setField("ch_bath_steady", "Nie");
        }
        if(protocol.is_bath_not_cleaned()){
            form.setField("ch_bath_not_cleaned", "Tak");
        } else {
            form.setField("ch_bath_not_cleaned", "Nie");
        }
        if(protocol.is_bath_others()){
            form.setField("ch_bath_others", "Tak");
            form.setField("bath_others", protocol.get_bath_others_comments());
        } else {
            form.setField("ch_bath_others", "Nie");
        }

        if(protocol.is_flue_enabled()){
            double airflowClosed = protocol.get_flue_airflow_windows_closed()*70.3;
            double airflowMicro = protocol.get_flue_airflow_microventilation()*70.3;
            double airflowOpen = protocol.get_flue_airflow_windows_open()*70.3;
            form.setField("flue_open", Double.toString(round(airflowOpen, 2)));
            form.setField("flue_closed", Double.toString(round(airflowClosed, 2)));
            if(Double.toString(round(airflowMicro, 2)).equals("0.0")){
                form.setField("flue_micro", "-");
            } else {
                form.setField("flue_micro", Double.toString(round(airflowMicro, 2)));
            }
        } else {
            form.setField("flue_open", "-");
            form.setField("flue_closed", "-");
            form.setField("flue_micro", "-");
        }
        if(protocol.is_flue_cleaned()){
            form.setField("ch_flue_cleaned", "Tak");
        } else {
            form.setField("ch_flue_cleaned", "Nie");
        }
        if(protocol.is_flue_boiler()){
            form.setField("ch_flue_vent", "Tak");
        } else {
            form.setField("ch_flue_vent", "Nie");
        }
        if(protocol.is_flue_inaccessible()){
            form.setField("ch_flue_inaccessible", "Tak");
        } else {
            form.setField("ch_flue_inaccessible", "Nie");
        }
        if(protocol.is_flue_rigid()){
            form.setField("ch_flue_steady", "Tak");
        } else {
            form.setField("ch_flue_steady", "Nie");
        }
        if(protocol.is_flue_alufol()){
            form.setField("ch_flue_alufol", "Tak");
        } else {
            form.setField("ch_flue_alufol", "Nie");
        }
        if(protocol.is_flue_not_cleaned()){
            form.setField("ch_flue_not_cleaned", "Tak");
        } else {
            form.setField("ch_flue_not_cleaned", "Nie");
        }
        if(protocol.is_flue_others()){
            form.setField("ch_flue_others", "Tak");
            form.setField("flue_others", protocol.get_flue_others_comments());
        } else {
            form.setField("ch_flue_others", "Nie");
        }

        form.setField("comments", protocol.get_comments());
        form.setField("user_comments", protocol.get_comments_for_user());
        form.setField("wind_speed", Double.toString(protocol.get_wind_speed()));
        form.setField("wind_direction", protocol.get_wind_direction());
        form.setField("pressure", Double.toString(protocol.get_pressure()));
        form.setField("co2", Double.toString(protocol.get_co2()));

        form.setField("windows_all", Integer.toString(protocol.get_windows_all()));
        form.setField("windows_micro", Integer.toString(protocol.get_windows_micro()));
        form.setField("windows_blow", Integer.toString(protocol.get_windows_vent()));
        form.setField("windows_no_micro", Integer.toString(protocol.get_windows_no_micro()));

        if(protocol.is_eq_ch_gas_meter_working()){
            form.setField("ch_eq_gas_meter", "Tak");
            if(protocol.is_eq_gas_meter_working()){
                form.setField("eq_gas_meter", "sprawny");
            } else {
                form.setField("eq_gas_meter", "niesprawny");
            }
        } else {
            form.setField("ch_eq_gas_meter", "Nie");
        }

        if(protocol.is_eq_ch_stove_working()){
            form.setField("ch_eq_stove", "Tak");
            if(protocol.is_eq_stove_working()){
                form.setField("eq_stove", "sprawny");
            } else {
                form.setField("eq_stove", "niesprawny");
            }
        } else {
            form.setField("ch_eq_stove", "Nie");
        }

        if(protocol.is_eq_ch_bake_working()){
            form.setField("ch_eq_bake", "Tak");
            if(protocol.is_eq_bake_working()){
                form.setField("eq_bake", "sprawny");
            } else {
                form.setField("eq_bake", "niesprawny");
            }
        } else {
            form.setField("ch_eq_bake", "Nie");
        }

        if(protocol.is_eq_ch_combi_oven_working()){
            form.setField("ch_eq_combi_oven", "Tak");
            if(protocol.is_eq_combi_oven_working()){
                form.setField("eq_combi_oven", "sprawny");
            } else {
                form.setField("eq_combi_oven", "niesprawny");
            }
        } else {
            form.setField("ch_eq_combi_oven", "Nie");
        }

        if(protocol.is_eq_ch_kitchen_term_working()){
            form.setField("ch_eq_therm", "Tak");
            if(protocol.is_eq_kitchen_term_working()){
                form.setField("eq_therm", "sprawna");
            } else {
                form.setField("eq_therm", "niesprawna");
            }
        } else {
            form.setField("ch_eq_therm", "Nie");
        }

        if(protocol.is_eq_ch_others()){
            form.setField("ch_eq_others", "Tak");
            form.setField("eq_others", protocol.get_eq_other());
        } else {
            form.setField("ch_eq_others", "Nie");
        }

    }

    public String generatePdf(Address address, ProtocolPaderewskiego protocol) throws Exception {

        String str_path = Environment.getExternalStorageDirectory().toString() + "/IHG/" + address.getCity() + "/";
        if (address.getDistrinct().isEmpty()) {
            str_path = str_path + "inne";
        } else {
            str_path = str_path + address.getDistrinct().trim();
        }
        str_path = str_path + "/" + address.getStreet().trim() + "/" + new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
        boolean success = (new File(str_path).mkdirs());
        str_path = str_path + "/" + address.getStreet().trim() + "_" + address.getBuilding().trim() + "_" + address.getFlat().trim() + "_" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());

        String pdfExtension = ".pdf";
        String numberOfCopy = "";
        int i = 0;

        while((new File(str_path + numberOfCopy + pdfExtension).exists())) {
            i++;
            numberOfCopy = "(" + i + ")";
        }

        str_path = str_path + numberOfCopy + pdfExtension;

        PdfReader reader;
        PdfStamper stamper;

        if(protocol.get_worker_name().equals("Szymon Mączyński")){
            reader = new PdfReader(Utils.PADEREWSKIEGO_PDF_SZYMON);
        } else {
            reader = new PdfReader(Utils.PADEREWSKIEGO_PDF_MACIEJ);
        }
        stamper = new PdfStamper(reader,
                new FileOutputStream(str_path));

        AcroFields form = stamper.getAcroFields();
        form.setGenerateAppearances(true);
        fill(form, address, protocol);
        stamper.setFormFlattening(true);

        PdfContentByte content = stamper.getOverContent(reader.getNumberOfPages());
        Image image = Image.getInstance(Utils.SIGNATURE_PATH);

        image.setAbsolutePosition(435,423);
        image.scaleAbsolute(73,25);
        content.addImage(image);

        stamper.close();
        reader.close();

        return str_path;


    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

}
