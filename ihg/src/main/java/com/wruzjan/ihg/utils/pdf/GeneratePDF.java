package com.wruzjan.ihg.utils.pdf;

import android.os.Environment;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.wruzjan.ihg.utils.Utils;
import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.Protocol;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GeneratePDF {

    private static void fill(AcroFields form, Address address, Protocol protocol)
            throws IOException, DocumentException {

        //load unicode font for polish characters
        BaseFont bf = BaseFont.createFont(Environment.getExternalStorageDirectory().toString()+"/IHG/fonts/arial_unicode.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        form.addSubstitutionFont(bf);

        form.setField("name", address.getName());
        form.setField("address", address.getStreet()+" "+address.getBuilding()+"/"+address.getFlat());
//        +", "+address.getCity());
        form.setField("date", new SimpleDateFormat("yyyy MMM dd").format(Calendar.getInstance().getTime()));
        form.setField("temp_outside", Double.toString(round(protocol.get_temp_outside(), 2)));
        form.setField("temp_inside", Double.toString(round(protocol.get_temp_inside(), 2)));

        double sum_closed_more = 0;
        double sum_closed_less = 0;
        double sum_micro_more = 0;
        double sum_micro_less = 0;
        double f = (double) 0.36;
        double kitchen_closed_m3 = 0;
        double kitchen_micro_m3 = 0;
        double bath_closed_m3 = 0;
        double bath_micro_m3 = 0;
        double toilet_closed_m3 = 0;
        double toilet_micro_m3 = 0;
        double flue_closed_m3 = 0;
        double flue_micro_m3 = 0;
        double requiredValue = 0;

        boolean closed_less = false;
        String micro_less_comment = "";

        form.setField("kitchen_comments", protocol.get_kitchen_comments());

        if(protocol.is_kitchen_enabled()){
            requiredValue = requiredValue +70;

            if(protocol.get_kitchen_grid_dimension_round() == 0){
                form.setField("kitchen_dim", protocol.get_kitchen_grid_dimension_x()+"\rx\r"+protocol.get_kitchen_grid_dimension_y());
            } else {
                form.setField("kitchen_dim", Double.toString(protocol.get_kitchen_grid_dimension_round()));
            }
            form.setField("kitchen_closed_m", Double.toString(round(protocol.get_kitchen_airflow_windows_closed(), 2)));
            form.setField("kitchen_micro_m", Double.toString(round(protocol.get_kitchen_airflow_microventilation(), 2)));
            form.setField("kitchen_closed_m", Double.toString(round(protocol.get_kitchen_airflow_windows_closed(), 2)));
            kitchen_closed_m3 = protocol.get_kitchen_airflow_windows_closed();
            kitchen_micro_m3 = protocol.get_kitchen_airflow_microventilation();
            double kitchen_dim_x = protocol.get_kitchen_grid_dimension_x();
            double kitchen_dim_y = protocol.get_kitchen_grid_dimension_y();
            double kitchen_dim_round = protocol.get_kitchen_grid_dimension_round();
            if(protocol.get_kitchen_grid_dimension_round()==0){
                kitchen_closed_m3 = kitchen_closed_m3 * f * kitchen_dim_x * kitchen_dim_y;
                kitchen_micro_m3 = kitchen_micro_m3 * f * kitchen_dim_x * kitchen_dim_y;
            } else {
                kitchen_closed_m3 = ((kitchen_dim_round/2)*(kitchen_dim_round/2)*Math.PI)*f*kitchen_closed_m3;
                kitchen_micro_m3 = kitchen_micro_m3 * f * ((kitchen_dim_round/2)*(kitchen_dim_round/2)*Math.PI);
            }
            form.setField("kitchen_closed_m3", Double.toString(round(kitchen_closed_m3, 2)));
            form.setField("kitchen_micro_m3", Double.toString(round(kitchen_micro_m3, 2)));

            if(kitchen_closed_m3>70){
                //nadmiar
                form.setField("kitchen_closed_more", Double.toString(round(kitchen_closed_m3 - 70, 2)));
//                form.setField("kitchen_closed_less", "0");
                sum_closed_more = kitchen_closed_m3-70;
            } else {
                //niedobór
//                form.setField("kitchen_closed_more", "0");
                form.setField("kitchen_closed_less", Double.toString(round(kitchen_closed_m3-70, 2)));
                sum_closed_less = kitchen_closed_m3-70;
                closed_less = true;
            }

            if(kitchen_micro_m3>70){
                //nadmiar
                form.setField("kitchen_micro_more", Double.toString(round(kitchen_micro_m3-70, 2)));
//                form.setField("kitchen_micro_less", "0");
                sum_micro_more = kitchen_micro_m3-70;

            } else {
                //niedobór
//                form.setField("kitchen_micro_more", "0");
                form.setField("kitchen_micro_less", Double.toString(round(kitchen_micro_m3-70, 2)));
                sum_micro_less = kitchen_micro_m3-70;
                micro_less_comment = addCommentsForManager("kuchni", micro_less_comment);
            }
        }

        form.setField("bath_comments", protocol.get_bathroom_comments());

        if(protocol.is_bathroom_enabled()){
            requiredValue = requiredValue +50;
            if(protocol.get_bathroom_grid_dimension_round()==0){
                form.setField("bath_dim", protocol.get_bathroom_grid_dimension_x()+"\rx\r"+protocol.get_bathroom_grid_dimension_y());
            } else {
                form.setField("bath_dim", Double.toString(protocol.get_bathroom_grid_dimension_round()));
            }
            form.setField("bath_closed_m", Double.toString(round(protocol.get_bathroom_airflow_windows_closed(), 2)));
            form.setField("bath_micro_m", Double.toString(round(protocol.get_bathroom_airflow_microventilation(), 2)));
            bath_closed_m3 = protocol.get_bathroom_airflow_windows_closed();
            bath_micro_m3 = protocol.get_bathroom_airflow_microventilation();
            double bath_dim_x = protocol.get_bathroom_grid_dimension_x();
            double bath_dim_y = protocol.get_bathroom_grid_dimension_y();
            double bath_dim_round = protocol.get_bathroom_grid_dimension_round();

            if(protocol.get_bathroom_grid_dimension_round()==0){
                bath_closed_m3 = bath_closed_m3 * f * bath_dim_x * bath_dim_y;
                bath_micro_m3 = bath_micro_m3 * f * bath_dim_x * bath_dim_y;
            } else {
                bath_closed_m3 = bath_closed_m3 * f * ((bath_dim_round/2)*(bath_dim_round/2)*Math.PI);
                bath_micro_m3 = bath_micro_m3 * f * ((bath_dim_round/2)*(bath_dim_round/2)*Math.PI);
            }

            form.setField("bath_closed_m3", Double.toString(round(bath_closed_m3, 2)));
            form.setField("bath_micro_m3", Double.toString(round(bath_micro_m3, 2)));

            if(bath_closed_m3>50){
                //nadmiar
                form.setField("bath_closed_more", Double.toString(round(bath_closed_m3-50, 2)));
//                form.setField("bath_closed_less", "0");
                sum_closed_more = sum_closed_more + bath_closed_m3-50;
            } else {
                //niedobór
//                form.setField("bath_closed_more", "0");
                form.setField("bath_closed_less", Double.toString(round(bath_closed_m3-50, 2)));
                sum_closed_less = sum_closed_less + bath_closed_m3-50;
                closed_less = true;
            }

            if(bath_micro_m3>50){
                //nadmiar
                form.setField("bath_micro_more", Double.toString(round(bath_micro_m3-50, 2)));
//                form.setField("bath_micro_less", "0");
                sum_micro_more = sum_micro_more + bath_micro_m3-50;

            } else {
                //niedobór
//                form.setField("bath_micro_more", "0");
                form.setField("bath_micro_less", Double.toString(round(bath_micro_m3-50, 2)));
                sum_micro_less = sum_micro_less + bath_micro_m3-50;
                micro_less_comment = addCommentsForManager("łazience", micro_less_comment);
            }
        }

        form.setField("toilet_comments", protocol.get_toilet_comments());
        if(protocol.is_toilet_enabled()){
            requiredValue = requiredValue +30;
            if(protocol.get_toilet_grid_dimension_round() == 0){
                form.setField("toilet_dim", protocol.get_toilet_grid_dimension_x()+"\r x \r"+protocol.get_toilet_grid_dimension_y());
            } else {
                form.setField("toilet_dim", Double.toString(protocol.get_toilet_grid_dimension_round()));
            }
            form.setField("toilet_closed_m", Double.toString(round(protocol.get_toilet_airflow_windows_closed(), 2)));
            form.setField("toilet_micro_m", Double.toString(round(protocol.get_toilet_airflow_microventilation(), 2)));
            toilet_closed_m3 = protocol.get_toilet_airflow_windows_closed();
            toilet_micro_m3 = protocol.get_toilet_airflow_microventilation();
            double toilet_dim_x = protocol.get_toilet_grid_dimension_x();
            double toilet_dim_y = protocol.get_toilet_grid_dimension_y();
            double toilet_dim_round = protocol.get_toilet_grid_dimension_round();

            if(protocol.get_toilet_grid_dimension_round()==0){
                toilet_closed_m3 = toilet_closed_m3 * f * toilet_dim_x * toilet_dim_y;
                toilet_micro_m3 = toilet_micro_m3 * f * toilet_dim_x * toilet_dim_y;
            } else {
                toilet_closed_m3 = toilet_closed_m3 * f * ((toilet_dim_round/2)*(toilet_dim_round/2)*Math.PI);
                toilet_micro_m3 = toilet_micro_m3 * f * ((toilet_dim_round/2)*(toilet_dim_round/2)*Math.PI);
            }

            form.setField("toilet_closed_m3", Double.toString(round(toilet_closed_m3, 2)));
            if(toilet_closed_m3>30){
                //nadmiar
                form.setField("toilet_closed_more", Double.toString(round(toilet_closed_m3-30, 2)));
//                form.setField("toilet_closed_less", "0");
                sum_closed_more = sum_closed_more + toilet_closed_m3-30;
            } else {
                //niedobór
//                form.setField("toilet_closed_more", "0");
                form.setField("toilet_closed_less", Double.toString(round(toilet_closed_m3-30, 2)));
                sum_closed_less = sum_closed_less + toilet_closed_m3-30;
                closed_less = true;
            }

            form.setField("toilet_micro_m3", Double.toString(round(toilet_micro_m3, 2)));
            if(toilet_micro_m3>30){
                //nadmiar
                form.setField("toilet_micro_more", Double.toString(round(toilet_micro_m3-30, 2)));
//                form.setField("toilet_micro_less", "0");
                sum_micro_more = sum_micro_more + toilet_micro_m3-30;
            } else {
                //niedobór
//                form.setField("toilet_micro_more", "0");
                form.setField("toilet_micro_less", Double.toString(round(toilet_micro_m3-30, 2)));
                sum_micro_less = sum_micro_less + toilet_micro_m3-30;
                micro_less_comment = addCommentsForManager("toalecie", micro_less_comment);
            }
        }

        form.setField("required", Double.toString(requiredValue));

        form.setField("flue_comments", protocol.get_flue_comments());
        if(protocol.is_flue_enabled()){
            form.setField("flue_closed_m", Double.toString(round(protocol.get_flue_airflow_windows_closed(), 2)));
            flue_closed_m3 = protocol.get_flue_airflow_windows_closed()*70.3f;
            form.setField("flue_closed_m3", Double.toString(round(flue_closed_m3, 2)));
            form.setField("flue_micro_m", Double.toString(round(protocol.get_flue_airflow_microventilation(), 2)));
            flue_micro_m3 = protocol.get_flue_airflow_microventilation()*70.3f;
            form.setField("flue_micro_m3", Double.toString(round(flue_micro_m3, 2)));
            if(flue_closed_m3>35){
                //nadmiar
                form.setField("flue_closed_more", Double.toString(round(flue_closed_m3-35, 2)));
//                form.setField("flue_closed_less", "0");
                sum_closed_more = sum_closed_more + (flue_closed_m3-35);
            } else {
                //niedobór
//                form.setField("flue_closed_more", "0");
                form.setField("flue_closed_less", Double.toString(round(flue_closed_m3-35, 2)));
                sum_closed_less = sum_closed_less + (flue_closed_m3-35);
                closed_less = true;
            }
            if(flue_micro_m3>35){
                //nadmiar
                form.setField("flue_micro_more", Double.toString(round(flue_micro_m3-35, 2)));
//                form.setField("flue_micro_less", "0");
                sum_micro_more = sum_micro_more + (flue_micro_m3-35);
            } else {
                //niedobór
//                form.setField("flue_micro_more", "0");
                form.setField("flue_micro_less", Double.toString(round(flue_micro_m3-30, 2)));
                sum_micro_less = sum_micro_less + (flue_micro_m3-30);
                micro_less_comment = addCommentsForManager("przewodzie", micro_less_comment);
            }
        }


        protocol.set_comments_for_manager(addCommentsForUser(micro_less_comment, protocol.get_comments_for_manager()));
        if(closed_less){
            protocol.set_comments_for_user(addCommentsForUser("proponowany montaż nawiewników", protocol.get_comments_for_user()));
        }

        form.setField("measured_1", Double.toString(round(kitchen_closed_m3+bath_closed_m3+toilet_closed_m3+flue_closed_m3-sum_closed_more, 2)));
        form.setField("measured_2", Double.toString(round(kitchen_micro_m3+bath_micro_m3+toilet_micro_m3+flue_micro_m3, 2)));
        form.setField("measured_3", Double.toString(round(sum_closed_more+sum_closed_less, 2)));
        form.setField("measured_4", Double.toString(round(sum_micro_more+sum_micro_less, 2)));

        form.setField("co", Double.toString(round(protocol.get_co2(), 2)));

        form.setField("comments_1", protocol.get_gas_fittings_comments());
        form.setField("comments_2", protocol.get_equipment_comments());

        form.setField("user_comments", protocol.get_comments_for_user());
        form.setField("manager_comments", protocol.get_comments_for_manager());

        if(protocol.is_gas_fittings_present()){
            if(protocol.is_gas_fittings_working()){
                form.setField("gas_fittings_working", "szczelna");
            } else {
                form.setField("gas_fittings_working", "nieszczelna");
            }
        } else {
            form.setField("gas_fittings_working", "brak");
        }

        if(protocol.is_gas_cooker_present()){
            if(protocol.is_gas_cooker_working()){
                form.setField("gas_cooker_working", "sprawna");
            } else {
                form.setField("gas_cooker_working", "niesprawna");
            }
        } else {
            form.setField("gas_cooker_working", "brak");
        }

        if(protocol.is_bathroom_bake_present()){
            if(protocol.is_bathroom_bake_working()){
                form.setField("bath_bake_working", "sprawny");
            } else {
                form.setField("bath_bake_working", "niesprawny");
            }
        } else {
            form.setField("bath_bake_working", "brak");
        }

    }

    public String generatePdf(Address address, Protocol protocol) throws Exception {
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
        if (protocol.get_worker_name().equals("Szymon Mączyński")) {
            reader = new PdfReader(Utils.SIEMIANOWICE_PDF_SZYMON);
        } else {
            reader = new PdfReader(Utils.SIEMIANOWICE_PDF_MACIEJ);
        }
        stamper = new PdfStamper(reader,
                new FileOutputStream(str_path));

        fill(stamper.getAcroFields(), address, protocol);
        stamper.setFormFlattening(true);

        PdfContentByte content = stamper.getOverContent(reader.getNumberOfPages());
//        Image image = Image.getInstance(Utils.SIGNATURE_PATH);
//
//        image.setAbsolutePosition(95,125);
//        image.scaleAbsolute(110,38);
//        content.addImage(image);

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

    private static String addCommentsForManager(String room, String managerComments){
        if(managerComments.contains("proponowane prześwietlenie przewodu w ")){
            managerComments = managerComments + ", " + room;
        } else {
            managerComments = managerComments + "proponowane prześwietlenie przewodu w " + room;
        }
        return managerComments;
    }

    private static String addCommentsForUser(String comment, String userComments){
        if(!userComments.contains(comment)){
            if(userComments.length()+comment.length()+2<=Utils.USER_COMMENTS_LENGTH){
                if(userComments.length()==0){
                    userComments = comment;
                } else {
                    userComments = userComments + ", " + comment;
                }
            }
        }
        return userComments;
    }

}
