package com.wruzjan.ihg.utils.printer;

import android.os.Looper;
import android.util.Log;

import com.wruzjan.ihg.utils.ParseBitmap;
import com.wruzjan.ihg.utils.Utils;
import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.Protocol;
import com.zebra.android.comm.ZebraPrinterConnection;
import com.zebra.android.comm.BluetoothPrinterConnection;
import com.zebra.android.printer.PrinterLanguage;
import com.zebra.android.printer.ZebraPrinter;
import com.zebra.android.printer.ZebraPrinterFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BluetoothConnection {

    protected ZebraPrinter printer;
    public static ParseBitmap bitmapParser;
    boolean closed_less = false;
    String micro_less_comment = "";


    private void sendCPCLOverBluetooth(final String theBtMacAddress) {
        try {
            // Instantiate connection for given Bluetooth® MAC Address.
            ZebraPrinterConnection thePrinterConn = new BluetoothPrinterConnection(theBtMacAddress);

            // Initialize
//            Looper.prepare();

            // Open the connection - physical connection is established here.
            thePrinterConn.open();

            printer = ZebraPrinterFactory.getInstance(thePrinterConn);
            PrinterLanguage pl = printer.getPrinterControlLanguage();

            String TicketID = "23948234";
            // This example prints "This is a ZPL test." near the top of the label.
            //String zplData = "^XA^FO20,20^A0N,25,25^FDThis is a ZPL test.^FS^XZ";
            //It is taking 50 units for each line so forumula is
            //(1 blank line on top + number of lines in middle +1 blank line on bottom)* 1st argument of ML command = result
            //put result as 4th argumenet of ! command"


            //Make sure the data got to the printer before closing the connection
//            Thread.sleep(5000);

            // Close the connection to release resources.
            thePrinterConn.close();

//            Looper.myLooper().quit();
        } catch (Exception e) {
            // Handle communications error here.
            Log.d("IHG_DEBUG", e.getMessage());
        }
    }

    public void sendCpclOverBluetooth(final String theBtMacAddress, final Address address, final Protocol protocol) {

        new Thread(new Runnable() {
            public void run() {
                try {

                    //calculations
                    double kitchenMicro;
                    double kitchenClosed;
                    if(protocol.get_kitchen_grid_dimension_round()==0){
                        kitchenMicro = protocol.get_kitchen_grid_dimension_x()*protocol.get_kitchen_grid_dimension_y()*protocol.get_kitchen_airflow_microventilation()*0.36-70;
                        kitchenClosed = protocol.get_kitchen_grid_dimension_x()*protocol.get_kitchen_grid_dimension_y()*protocol.get_kitchen_airflow_windows_closed()*0.36-70;
                    } else {
                        kitchenMicro = ((protocol.get_kitchen_grid_dimension_round()/2)*(protocol.get_kitchen_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_kitchen_airflow_microventilation()-70;
                        kitchenClosed = ((protocol.get_kitchen_grid_dimension_round()/2)*(protocol.get_kitchen_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_kitchen_airflow_windows_closed()-70;
                    }
                    kitchenMicro = round(kitchenMicro, 2);
                    String kitchenMicroMeas;
//                    if((protocol.get_kitchen_grid_dimension_x()*protocol.get_kitchen_grid_dimension_y()*protocol.get_kitchen_airflow_microventilation()*0.36)>70){
                    if(kitchenMicro>0){
                        kitchenMicroMeas = "nad. ";
                    } else {
                        kitchenMicroMeas = "nie. ";
                        if(protocol.is_kitchen_enabled())
                            micro_less_comment = addCommentsForManager("kuchni", micro_less_comment);
                    }

                    kitchenClosed = round(kitchenClosed, 2);
                    String kitchenClosedMeas;
//                    if((protocol.get_kitchen_grid_dimension_x()*protocol.get_kitchen_grid_dimension_y()*protocol.get_kitchen_airflow_windows_closed()*0.36)>70){
                    if(kitchenClosed>0){
                        kitchenClosedMeas = "nad. ";
                    } else {
                        kitchenClosedMeas = "nie. ";
                        if(protocol.is_kitchen_enabled())
                            closed_less = true;
                    }

                    double bathMicro;
                    double bathClosed;
                    if(protocol.get_bathroom_grid_dimension_round()==0){
                        bathMicro = protocol.get_bathroom_grid_dimension_x()*protocol.get_bathroom_grid_dimension_y()*protocol.get_bathroom_airflow_microventilation()*0.36-50;
                        bathClosed = protocol.get_bathroom_grid_dimension_x()*protocol.get_bathroom_grid_dimension_y()*protocol.get_bathroom_airflow_windows_closed()*0.36-50;
                    } else {
                        bathMicro = ((protocol.get_bathroom_grid_dimension_round()/2)*(protocol.get_bathroom_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_bathroom_airflow_microventilation()-50;
                        bathClosed = ((protocol.get_bathroom_grid_dimension_round()/2)*(protocol.get_bathroom_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_bathroom_airflow_windows_closed()-50;
                    }

                    bathMicro = round(bathMicro, 2);
                    String bathMicroMeas;
                    if(bathMicro>0){
                        bathMicroMeas = "nad. ";
                    } else {
                        bathMicroMeas = "nie. ";
                        if(protocol.is_bathroom_enabled())
                            micro_less_comment = addCommentsForManager("łazience", micro_less_comment);
                    }

                    bathClosed = round(bathClosed, 2);
                    String bathClosedMeas;
                    if(bathClosed>0){
                        bathClosedMeas = "nad. ";
                    } else {
                        bathClosedMeas = "nie. ";
                        if(protocol.is_bathroom_enabled())
                            closed_less = true;
                    }

                    double toiletMicro;
                    double toiletClosed;
                    if(protocol.get_toilet_grid_dimension_round()==0){
                        toiletMicro = protocol.get_toilet_grid_dimension_x()*protocol.get_toilet_grid_dimension_y()*protocol.get_toilet_airflow_microventilation()*0.36-30;
                        toiletClosed = protocol.get_toilet_grid_dimension_x()*protocol.get_toilet_grid_dimension_y()*protocol.get_toilet_airflow_windows_closed()*0.36-30;
                    } else {
                        toiletMicro = ((protocol.get_toilet_grid_dimension_round()/2)*(protocol.get_toilet_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_toilet_airflow_microventilation()-30;
                        toiletClosed = ((protocol.get_toilet_grid_dimension_round()/2)*(protocol.get_toilet_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_toilet_airflow_windows_closed()-30;
                    }

                    toiletMicro = round(toiletMicro, 2);
                    String toiletMicroMeas;
                    if(toiletMicro>0){
                        toiletMicroMeas = "nad. ";
                    } else {
                        toiletMicroMeas = "nie. ";
                        if(protocol.is_bathroom_enabled())
                            micro_less_comment = addCommentsForManager("toalecie", micro_less_comment);
                    }

                    toiletClosed = round(toiletClosed, 2);
                    String toiletClosedMeas;
                    if(toiletClosed>0){
                        toiletClosedMeas = "nad. ";
                    } else {
                        toiletClosedMeas = "nie. ";
                        if(protocol.is_toilet_enabled())
                            closed_less = true;
                    }

                    double flueMicro = (protocol.get_flue_airflow_microventilation()*70.3)-35;
                    flueMicro = round(flueMicro, 2);
                    String flueMicroMeas;
                    if((protocol.get_flue_airflow_microventilation()*70.3)>35){
                        flueMicroMeas = "nad. ";
                    } else {
                        flueMicroMeas = "nie. ";
                        if(protocol.is_flue_enabled())
                            micro_less_comment = addCommentsForManager("przewodzie", micro_less_comment);
                    }
                    double flueClosed = (protocol.get_flue_airflow_windows_closed()*70.3)-35;
                    flueClosed = round(flueClosed, 2);
                    String flueClosedMeas;
                    if((protocol.get_flue_airflow_windows_closed()*70.3)>35){
                        flueClosedMeas = "nad. ";
                    } else {
                        flueClosedMeas = "nie. ";
                        if(protocol.is_flue_enabled())
                            closed_less = true;
                    }

                    protocol.set_comments_for_manager(addCommentsForUser(micro_less_comment, protocol.get_comments_for_manager()));
                    if(closed_less){
                        protocol.set_comments_for_user(addCommentsForUser("proponowany montaż nawiewników", protocol.get_comments_for_user()));
                    }

                    // Instantiate connection for given Bluetooth® MAC Address.
                    ZebraPrinterConnection thePrinterConn = new BluetoothPrinterConnection(theBtMacAddress);

                    // Initialize
                    Looper.prepare();
                    // Open the connection - physical connection is established here.
                    thePrinterConn.open();

                    String cpclData = "! 0 200 200 1050 1\r\n"

                            + "TEXT CALIBRI8.CPF 0 10 20 dane osobowe:\r\n"
                            + "TEXT CALIBRI8.CPF 0 10 40 " + address.getName() + "\r\n"

                            + "TEXT CALIBRI8.CPF 0 10 70 adres:\r\n"
                            + "TEXT CALIBRI8.CPF 0 10 90 " + address.getStreet() + " " + address.getBuilding() +"/" + address.getFlat() + "\r\n"
//                            + "TEXT 0 3 10 140" + address.getCity() + "\r\n"

                            + "TEXT CALIBRI8.CPF 0 10 120 data:\r\n"
                            + "TEXT CALIBRI8.CPF 0 60 120 " + new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime()) + "\r\n"

//                            + "TEXT CALIBRI8.CPF 0 10 220 temperatura zew.:\r\n"
//                            + "TEXT 0 3 10 240 " + protocol.get_temp_outside() + "\r\n"
//
//                            + "TEXT CALIBRI8.CPF 0 240 220 temperatura wew.:\r\n"
//                            + "TEXT 0 3 240 240 " + protocol.get_temp_inside() + "\r\n"

                            + "TEXT CALIBRI8.CPF 0 10 150 instalacja gazowa:\r\n"

//                            + "TEXT CALIBRI8.CPF 0 10 340 kuchnia gazowa:\r\n"

//                            + "TEXT CALIBRI8.CPF 0 10 400 piec lazienkowy:\r\n"

                            //tlenek węgla
                            + "TEXT CALIBRI8.CPF 0 10 200 tlenek węgla:\r\n"
                            + "TEXT CALIBRI8.CPF 0 160 200 " + protocol.get_co2() + " ppm" +  "\r\n"

                            //kuchnia
                            + "TEXT CALIBRI8.CPF 0 10 230 kuchnia (o. zamknięte): \r\n"
                            + "TEXT CALIBRI8.CPF 0 10 260 kuchnia (mikrouchył): \r\n"

                            //łazienka
                            + "TEXT CALIBRI8.CPF 0 10 290 łazienka (o. zamknięte): \r\n"
                            + "TEXT CALIBRI8.CPF 0 10 320 łazienka (mikrouchył): \r\n"

                            //WC
                            + "TEXT CALIBRI8.CPF 0 10 350 toaleta (o. zamknięte): \r\n"
                            + "TEXT CALIBRI8.CPF 0 10 380 toaleta (mikrouchył): \r\n"

                            //przewód
                            + "TEXT CALIBRI8.CPF 0 10 410 przewód (o. zamknięte): \r\n"
                            + "TEXT CALIBRI8.CPF 0 10 440 przewód (mikrouchył): \r\n"

                            //uwagi
//                            + "TEXT CALIBRI8.CPF 0 10 820 uwagi do kuchni: \r\n"
//
//                            + "TEXT CALIBRI8.CPF 0 10 920 uwagi do lazienki: \r\n"
//
//                            + "TEXT CALIBRI8.CPF 0 10 1020 uwagi do WC: \r\n"
//
//                            + "TEXT CALIBRI8.CPF 0 10 1120 uwagi do przewodu: \r\n"

                            //uwagi do instalacji gazowej
                            + "TEXT CALIBRI8.CPF 0 10 470 uwagi do instalcji gazowej: \r\n"

//                            uwagi do urządzeń
                            + "TEXT CALIBRI8.CPF 0 10 560 uwagi: \r\n"

                            //zalecenia
                            + "TEXT CALIBRI8.CPF 0 10 650 zalecenia dla użytkownika: \r\n"

                            //dane osobowe
                            + "TEXT CALIBRI8.CPF 0 10 780 przeglądu dokonał:\r\n"
                            + "TEXT CALIBRI8.CPF 0 10 800" + protocol.get_worker_name() + "\r\n"

                            + "TEXT CALIBRI8.CPF 0 10 830 podpis mieszkańca:\r\n"
                            + "TEXT CALIBRI8.CPF 0 10 950 .\r\n";

                            if(protocol.is_kitchen_enabled()){
                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 230 230" + kitchenClosedMeas+kitchenClosed + "\r\n"
                                        + "TEXT CALIBRI8.CPF 0 225 260" + kitchenMicroMeas+kitchenMicro + "\r\n";
                            } else {
                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 230 230 - \r\n"
                                        + "TEXT CALIBRI8.CPF 0 225 260 - \r\n";
                            }

                            if(protocol.is_bathroom_enabled()){
                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 230 290" + bathClosedMeas+bathClosed + "\r\n"
                                        + "TEXT CALIBRI8.CPF 0 225 320" + bathMicroMeas+bathMicro + "\r\n";
                            } else {
                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 230 290 - \r\n"
                                        + "TEXT CALIBRI8.CPF 0 225 320 - \r\n";
                            }

                            if(protocol.is_toilet_enabled()){
                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 225 350" + toiletClosedMeas+toiletClosed + "\r\n"
                                        + "TEXT CALIBRI8.CPF 0 225 380" + toiletMicroMeas+toiletMicro + "\r\n";
                            } else {
                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 225 350 - \r\n"
                                        + "TEXT CALIBRI8.CPF 0 225 380 - \r\n";
                            }

                            if(protocol.is_flue_enabled()){
                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 235 410" + flueClosedMeas+flueClosed + "\r\n"
                                        + "TEXT CALIBRI8.CPF 0 225 440" + flueMicroMeas+flueMicro + "\r\n";
                            } else {
                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 235 410 - \r\n"
                                        + "TEXT CALIBRI8.CPF 0 225 440 - \r\n";
                            }

//                            if(protocol.get_kitchen_comments().isEmpty()){
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 840 brak\r\n";
//                            } else {
//                                if(protocol.get_kitchen_comments().length()<45){
//                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 840" + protocol.get_kitchen_comments() + "\r\n";
//                                } else {
//                                    if(protocol.get_kitchen_comments().length()<90){
//                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 840" + protocol.get_kitchen_comments().substring(0, 45)+"-" + "\r\n";
//                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 860" +"-"+protocol.get_kitchen_comments().substring(45, protocol.get_kitchen_comments().length()) + "\r\n";
//                                    } else {
//                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 840" + protocol.get_kitchen_comments().substring(0, 45)+"-" + "\r\n";
//                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 860" + "-"+protocol.get_kitchen_comments().substring(45, 90)+"-" + "\r\n";
//                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 880" + "-"+protocol.get_kitchen_comments().substring(90, protocol.get_kitchen_comments().length()) + "\r\n";
//                                    }
//                                }
//                            }
//
//                            if(protocol.get_bathroom_comments().isEmpty()){
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 940 brak\r\n";
//                            } else {
//                                if(protocol.get_bathroom_comments().length()<45){
//                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 940" + protocol.get_bathroom_comments() + "\r\n";
//                                } else {
//                                    if(protocol.get_bathroom_comments().length()<90){
//                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 940" + protocol.get_bathroom_comments().substring(0, 45)+"-" + "\r\n";
//                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 960" +"-"+protocol.get_bathroom_comments().substring(45, protocol.get_bathroom_comments().length()) + "\r\n";
//                                    } else {
//                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 940" + protocol.get_bathroom_comments().substring(0, 45)+"-" + "\r\n";
//                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 960" +"-"+protocol.get_bathroom_comments().substring(45, 90)+"-" + "\r\n";
//                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 980" +"-"+protocol.get_bathroom_comments().substring(90, protocol.get_bathroom_comments().length()) + "\r\n";
//                                    }
//                                }
//                            }
//
//                            if(protocol.get_toilet_comments().isEmpty()){
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1040 brak\r\n";
//                            } else {
//                                if(protocol.get_toilet_comments().length()<45){
//                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1040" + protocol.get_toilet_comments() + "\r\n";
//                                } else {
//                                    if(protocol.get_toilet_comments().length()<90){
//                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1040" + protocol.get_toilet_comments().substring(0, 45)+"-" + "\r\n";
//                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1060" +"-"+protocol.get_toilet_comments().substring(45, protocol.get_toilet_comments().length()) + "\r\n";
//                                    } else {
//                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1040" + protocol.get_toilet_comments().substring(0, 45)+"-" + "\r\n";
//                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1060" +"-"+protocol.get_toilet_comments().substring(45, 90)+"-" + "\r\n";
//                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1080" +"-"+protocol.get_toilet_comments().substring(90, protocol.get_toilet_comments().length()) + "\r\n";
//                                    }
//                                }
//                            }
//
//                            if(protocol.get_flue_comments().isEmpty()){
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1140 brak\r\n";
//                            } else {
//                                if(protocol.get_flue_comments().length()<45){
//                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1140" + protocol.get_flue_comments() + "\r\n";
//                                } else {
//                                    if(protocol.get_flue_comments().length()<90){
//                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1140" + protocol.get_flue_comments().substring(0, 45)+"-" + "\r\n";
//                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1160" +"-"+protocol.get_flue_comments().substring(45, protocol.get_flue_comments().length()) + "\r\n";
//                                    } else {
//                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1140" + protocol.get_flue_comments().substring(0, 45)+"-" + "\r\n";
//                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1160" +"-"+protocol.get_flue_comments().substring(45, 90)+"-" + "\r\n";
//                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1180" +"-"+protocol.get_flue_comments().substring(90, protocol.get_flue_comments().length()) + "\r\n";
//                                    }
//                                }
//                            }
                            if(protocol.get_gas_fittings_comments().isEmpty()){
                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 490 brak\r\n";
                            } else {
                                if(protocol.get_gas_fittings_comments().length()<35){
                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 490" + protocol.get_gas_fittings_comments() + "\r\n";
                                } else {
                                    if(protocol.get_gas_fittings_comments().length()<70){
                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 490" + protocol.get_gas_fittings_comments().substring(0, 35)+"-" + "\r\n";
                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 510" +"-"+protocol.get_gas_fittings_comments().substring(35, protocol.get_gas_fittings_comments().length()) + "\r\n";
                                    } else {
                                        if(protocol.get_gas_fittings_comments().length()<105){
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 490" + protocol.get_gas_fittings_comments().substring(0, 35)+"-" + "\r\n";
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 510" +"-"+protocol.get_gas_fittings_comments().substring(35, 70)+"-" + "\r\n";
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 530" +"-"+protocol.get_gas_fittings_comments().substring(70, protocol.get_gas_fittings_comments().length()) + "\r\n";
                                        } else {
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 490" + protocol.get_gas_fittings_comments().substring(0, 35)+"-" + "\r\n";
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 510" +"-"+protocol.get_gas_fittings_comments().substring(35, 70)+"-" + "\r\n";
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 530" +"-"+protocol.get_gas_fittings_comments().substring(70, 105) + "\r\n";
                                        }
                                    }
                                }
                            }

                            if(protocol.get_equipment_comments().isEmpty()){
                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 580 brak\r\n";
                            } else {
                                if(protocol.get_equipment_comments().length()<35){
                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 580" + protocol.get_equipment_comments() + "\r\n";
                                } else {
                                    if(protocol.get_equipment_comments().length()<70){
                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 580" + protocol.get_equipment_comments().substring(0, 35)+"-" + "\r\n";
                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 600" +"-"+protocol.get_equipment_comments().substring(35, protocol.get_equipment_comments().length()) + "\r\n";
                                    } else {
                                        if(protocol.get_equipment_comments().length()<105){
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 580" + protocol.get_equipment_comments().substring(0, 35)+"-" + "\r\n";
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 600" +"-"+protocol.get_equipment_comments().substring(35, 70)+"-" + "\r\n";
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 620" +"-"+protocol.get_equipment_comments().substring(70, protocol.get_equipment_comments().length()) + "\r\n";
                                        } else {
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 580" + protocol.get_equipment_comments().substring(0, 35)+"-" + "\r\n";
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 600" +"-"+protocol.get_equipment_comments().substring(35, 70)+"-" + "\r\n";
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 620" +"-"+protocol.get_equipment_comments().substring(70, 105) + "\r\n";
                                        }
                                    }
                                }
                            }

                            if(protocol.get_comments_for_user().isEmpty()){
                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 670 brak\r\n";
                            } else {
                                if(protocol.get_comments_for_user().length()<35){
                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 670" + protocol.get_comments_for_user() + "\r\n";
                                } else {
                                    if(protocol.get_comments_for_user().length()<70){
                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 670" + protocol.get_comments_for_user().substring(0, 35)+"-" + "\r\n";
                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 690" +"-"+protocol.get_comments_for_user().substring(35, protocol.get_comments_for_user().length()) + "\r\n";
                                    } else {
                                        if(protocol.get_comments_for_user().length()<105){
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 670" + protocol.get_comments_for_user().substring(0, 35)+"-" + "\r\n";
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 690" +"-"+protocol.get_comments_for_user().substring(35, 70)+"-" + "\r\n";
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 710" +"-"+protocol.get_comments_for_user().substring(70, protocol.get_comments_for_user().length()) + "\r\n";
                                        } else {
                                            if(protocol.get_comments_for_user().length()<140){
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 670" + protocol.get_comments_for_user().substring(0, 35)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 690" +"-"+protocol.get_comments_for_user().substring(35, 70)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 710" +"-"+protocol.get_comments_for_user().substring(70, 105)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 730" +"-"+protocol.get_comments_for_user().substring(105, protocol.get_comments_for_user().length()) + "\r\n";
                                            } else {
                                                if(protocol.get_comments_for_user().length()<175){
                                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 670" + protocol.get_comments_for_user().substring(0, 35)+"-" + "\r\n";
                                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 690" +"-"+protocol.get_comments_for_user().substring(35, 70)+"-" + "\r\n";
                                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 710" +"-"+protocol.get_comments_for_user().substring(70, 105)+"-" + "\r\n";
                                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 730" +"-"+protocol.get_comments_for_user().substring(105, 140)+"-" + "\r\n";
                                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 750" +"-"+protocol.get_comments_for_user().substring(140, protocol.get_comments_for_user().length()) + "\r\n";
                                                } else {
                                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 670" + protocol.get_comments_for_user().substring(0, 35)+"-" + "\r\n";
                                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 690" +"-"+protocol.get_comments_for_user().substring(35, 70)+"-" + "\r\n";
                                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 710" +"-"+protocol.get_comments_for_user().substring(70, 105)+"-" + "\r\n";
                                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 730" +"-"+protocol.get_comments_for_user().substring(105, 140)+"-" + "\r\n";
                                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 750" +"-"+protocol.get_comments_for_user().substring(140, 175) + "\r\n";
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if(protocol.is_gas_fittings_present()){
                                if(protocol.is_gas_fittings_working()){
                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 170 szczelna\r\n";
                                } else {
                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 170 nieszczelna\r\n";
                                }
                            } else {
                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 170 brak\r\n";
                            }

//                            if(protocol.is_gas_cooker_working()){
//                                cpclData = cpclData + "TEXT 0 3 10 360 szczelna\r\n";
//                            } else {
//                                cpclData = cpclData + "TEXT 0 3 10 360 nieszczelna\r\n";
//                            }
//                            if(protocol.is_bathroom_bake_working()){
//                                cpclData = cpclData + "TEXT 0 3 10 420 szczelny\r\n";
//                            } else {
//                                cpclData = cpclData + "TEXT 0 3 10 420 nieszczelny\r\n";
//                            }
//                    cpclData = cpclData + "FORM\r\n";
                    cpclData = cpclData +  "PRINT\r\n";

                    // Send the data to printer as a byte array.
                    //use Cp1250 encoding for polish characters
                    thePrinterConn.write(decodeText(cpclData, "CP1250"));
//                    thePrinterConn.write(cpclData.getBytes());

                    Thread.sleep(500);

                    //signature
//                    String str = bitmapParser.ExtractGraphicsDataForCPCL(0,0);
//                    String zplData = "! 0 200 200 230 1\r\n"
//                            + str
//                            +"PRINT\r\n";

                    // Send the data to printer as a byte array.
//                    thePrinterConn.write(zplData.getBytes());

//                    Thread.sleep(500);

                    // Close the connection to release resources.
                    thePrinterConn.close();

                    Looper.myLooper().quit();



                } catch (Exception e) {
                    // Handle communications error here.
                    e.printStackTrace();

                }
            }
        }).start();
    }

    private byte[] decodeText(String text, String encoding) throws CharacterCodingException, UnsupportedEncodingException {
        Charset charset = Charset.forName(encoding);
        CharsetDecoder decoder = charset.newDecoder();
        CharsetEncoder encoder = charset.newEncoder();
        ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(text));
        CharBuffer cbuf = decoder.decode(bbuf);
        String s = cbuf.toString();
        return s.getBytes(encoding);
    }

    private double round(double value, int places) {
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
            if(userComments.length()+comment.length()+2<= Utils.USER_COMMENTS_LENGTH){
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
