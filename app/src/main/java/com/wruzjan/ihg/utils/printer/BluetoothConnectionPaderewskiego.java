package com.wruzjan.ihg.utils.printer;

import android.content.Context;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.wruzjan.ihg.EnterData2Activity;
import com.wruzjan.ihg.utils.ParseBitmap;
import com.wruzjan.ihg.utils.Utils;
import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.ProtocolPaderewskiego;
import com.zebra.android.comm.BluetoothPrinterConnection;
import com.zebra.android.comm.ZebraPrinterConnection;
import com.zebra.android.printer.PrinterLanguage;
import com.zebra.android.printer.ZebraPrinter;

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

public class BluetoothConnectionPaderewskiego {

    protected ZebraPrinter printer;
    public static ParseBitmap bitmapParser;

    public void sendCpclOverBluetooth(final String theBtMacAddress, final Address address, final ProtocolPaderewskiego protocol) {

        new Thread(new Runnable() {
            public void run() {
                try {

                    // Instantiate connection for given Bluetooth? MAC Address.
                    ZebraPrinterConnection thePrinterConn = new BluetoothPrinterConnection(theBtMacAddress);

                    // Initialize
                    Looper.prepare();
                    // Open the connection - physical connection is established here.
                    thePrinterConn.open();

                    // This example prints "This is a CPCL test." near the top of the label.
                    String cpclData = "! 0 200 200 1170 1\r\n"

                            + "TEXT CALIBRI8.CPF 0 10 20 dane osobowe:\r\n"
                            + "TEXT CALIBRI8.CPF 0 10 40 " + address.getName() + "\r\n"

                            + "TEXT CALIBRI8.CPF 0 10 70 adres:\r\n"
                            + "TEXT CALIBRI8.CPF 0 10 90 " + address.getStreet() + " " + address.getBuilding() +"/" + address.getFlat() + "\r\n"
//                            + "TEXT CALIBRI8.CPF 0 10 110" + address.getCity() + "\r\n"

                            + "TEXT CALIBRI8.CPF 0 10 120 data:\r\n"
                            + "TEXT CALIBRI8.CPF 0 70 120 " + new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime()) + "\r\n"

//                            + "TEXT CALIBRI8.CPF 0 10 170 temperatura zew.:\r\n"
//                            + "TEXT CALIBRI8.CPF 0 10 190 " + protocol.get_temp_outside() + "\r\n"
//
//                            + "TEXT CALIBRI8.CPF 0 240 170 temperatura wew.:\r\n"
//                            + "TEXT CALIBRI8.CPF 0 240 190 " + protocol.get_temp_inside() + "\r\n"

                            + "TEXT CALIBRI8.CPF 0 10 150 gazomierz:\r\n"

                            + "TEXT CALIBRI8.CPF 0 10 180 k. gazowa 2/4 palnikowa:\r\n"

                            + "TEXT CALIBRI8.CPF 0 10 210 piec łazienkowy:\r\n"

//                            + "TEXT CALIBRI8.CPF 0 10 310 piec dwufunkcyjny:\r\n"

//                            + "TEXT CALIBRI8.CPF 0 10 340 terma kuchenna:\r\n"

                            + "TEXT CALIBRI8.CPF 0 10 240 inne:\r\n"

                            //tlenek w?gla
                            + "TEXT CALIBRI8.CPF 0 10 330 tlenek węgla:\r\n"
                            + "TEXT CALIBRI8.CPF 0 140 330 " + protocol.get_co2() + " ppm" +  "\r\n"

                            //kuchnia
                            + "TEXT CALIBRI8.CPF 0 120 360 kuchnia \r\n"
                            + "TEXT CALIBRI8.CPF 0 10 390 okna zamknięte: \r\n"
                            + "TEXT CALIBRI8.CPF 0 10 420 okna otwarte: \r\n"
//                            + "TEXT CALIBRI8.CPF 0 10 580 mikrouchyl: \r\n"

                            //?azienka
                            + "TEXT CALIBRI8.CPF 0 120 450 łazienka \r\n"
                            + "TEXT CALIBRI8.CPF 0 10 480 okna zamknięte: \r\n"
                            + "TEXT CALIBRI8.CPF 0 10 510 okna otwarte: \r\n"
//                            + "TEXT CALIBRI8.CPF 0 10 700 mikrouchyl: \r\n"

                            //WC
                            + "TEXT CALIBRI8.CPF 0 120 540 toaleta \r\n"
                            + "TEXT CALIBRI8.CPF 0 10 570 okna zamknięte: \r\n"
                            + "TEXT CALIBRI8.CPF 0 10 600 okna otwarte: \r\n"
//                            + "TEXT CALIBRI8.CPF 0 10 820 mikrouchyl: \r\n"

                            //przew?d
                            + "TEXT CALIBRI8.CPF 0 80 630 przewód spalinowy \r\n"
                            + "TEXT CALIBRI8.CPF 0 10 660 okna zamknięte: \r\n"
                            + "TEXT CALIBRI8.CPF 0 10 690 okna otwarte: \r\n"
//                            + "TEXT CALIBRI8.CPF 0 10 940 mikrouchyl: \r\n"

                            //uwagi
//                            + "TEXT CALIBRI8.CPF 0 10 970 uwagi do kuchni: \r\n"
//
//                            + "TEXT CALIBRI8.CPF 0 10 1060 uwagi do lazienki: \r\n"
//
//                            + "TEXT CALIBRI8.CPF 0 10 1150 uwagi do WC: \r\n"
//
//                            + "TEXT CALIBRI8.CPF 0 10 1240 uwagi do przewodu: \r\n"

                            + "TEXT CALIBRI8.CPF 0 10 720 uwagi: \r\n"
                            + "TEXT CALIBRI8.CPF 0 10 870 zalecenia: \r\n"

                            //dane osobowe
                            + "TEXT CALIBRI8.CPF 0 10 1020 przeglądu dokonał:\r\n"
                            + "TEXT CALIBRI8.CPF 0 190 1020" + protocol.get_worker_name() + "\r\n"

                            + "TEXT CALIBRI8.CPF 0 10 1050 podpis mieszkańca:\r\n";

                    if(protocol.is_eq_ch_gas_meter_working()){
                        if(protocol.is_eq_gas_meter_working()){
                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 120 150 sprawny\r\n";
                        } else {
                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 120 150 niesprawny\r\n";
                        }
                    } else {
                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 120 150 brak\r\n";
                    }

                    if(protocol.is_eq_ch_stove_working()){
                        if(protocol.is_eq_stove_working()){
                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 250 180 sprawna\r\n";
                        } else {
                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 250 180 niesprawna\r\n";
                        }
                    } else {
                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 250 180 brak\r\n";
                    }

                    if(protocol.is_eq_ch_bake_working()){
                        if(protocol.is_eq_bake_working()){
                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 170 210 sprawny\r\n";
                        } else {
                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 170 210 niesprawny\r\n";
                        }
                    } else {
                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 170 210 brak\r\n";
                    }

//                    if(protocol.is_eq_ch_combi_oven_working()){
//                        if(protocol.is_eq_combi_oven_working()){
//                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 165 310 sprawny\r\n";
//                        } else {
//                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 165 310 niesprawny\r\n";
//                        }
//                    } else {
//                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 165 310 brak\r\n";
//                    }
//
//                    if(protocol.is_eq_ch_kitchen_term_working()){
//                        if(protocol.is_eq_kitchen_term_working()){
//                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 140 340 sprawna\r\n";
//                        } else {
//                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 140 340 niesprawna\r\n";
//                        }
//                    } else {
//                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 140 340 brak\r\n";
//                    }

                    if(protocol.is_eq_ch_others()){
                        if(protocol.get_eq_other().length()<35){
                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 260" + protocol.get_eq_other() + "\r\n";
                        } else {
                            if(protocol.get_eq_other().length()<70){
                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 260" + protocol.get_eq_other().substring(0, 35)+"-" + "\r\n";
                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 280" +"-"+protocol.get_eq_other().substring(35, protocol.get_eq_other().length()) + "\r\n";
                            } else {
                                if(protocol.get_eq_other().length()<105){
                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 260" + protocol.get_eq_other().substring(0, 35)+"-" + "\r\n";
                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 280" +"-"+protocol.get_eq_other().substring(35, 70)+"-" + "\r\n";
                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 300" +"-"+protocol.get_eq_other().substring(70, protocol.get_eq_other().length()) + "\r\n";
                                } else {
                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 260" + protocol.get_eq_other().substring(0, 35)+"-" + "\r\n";
                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 280" +"-"+protocol.get_eq_other().substring(35, 70)+"-" + "\r\n";
                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 300" +"-"+protocol.get_eq_other().substring(70, 105) + "\r\n";
                                }
                            }
                        }
                    } else {
                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 60 240 brak\r\n";
                    }

                    if(protocol.is_kitchen_enabled()){
                        double airflowClosed;
//                        double airflowMicro;
                        double airflowOpen;
                        if(protocol.get_kitchen_grid_dimension_round()==0){
                            airflowClosed = protocol.get_kitchen_grid_dimension_x()*protocol.get_kitchen_grid_dimension_y()*0.36*protocol.get_kitchen_airflow_windows_closed();
//                            airflowMicro = protocol.get_kitchen_grid_dimension_x()*protocol.get_kitchen_grid_dimension_y()*0.36*protocol.get_kitchen_airflow_microventilation();
                            airflowOpen = protocol.get_kitchen_grid_dimension_x()*protocol.get_kitchen_grid_dimension_y()*0.36*protocol.get_kitchen_airflow_windows_open();
                        } else {
                            airflowClosed = ((protocol.get_kitchen_grid_dimension_round()/2)*(protocol.get_kitchen_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_kitchen_airflow_windows_closed();
//                            airflowMicro = ((protocol.get_kitchen_grid_dimension_round()/2)*(protocol.get_kitchen_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_kitchen_airflow_microventilation();
                            airflowOpen = ((protocol.get_kitchen_grid_dimension_round()/2)*(protocol.get_kitchen_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_kitchen_airflow_windows_open();
                        }
                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 205 390 " + Double.toString(round(airflowClosed,2 )) + "\r\n"
                                + "TEXT CALIBRI8.CPF 0 205 420 " + Double.toString(round(airflowOpen, 2)) + "\r\n";
//                                + "TEXT CALIBRI8.CPF 0 205 580 " + Double.toString(round(airflowMicro, 2)) + "\r\n";
                    } else {
                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 205 390 - \r\n"
                                + "TEXT CALIBRI8.CPF 0 205 420 - \r\n";
//                                + "TEXT CALIBRI8.CPF 0 205 580 - \r\n";
                    }

                    if(protocol.is_bath_enabled()){
                        double airflowClosed;
//                        double airflowMicro;
                        double airflowOpen;
                        if(protocol.get_bath_grid_dimension_round()==0){
                            airflowClosed = protocol.get_bath_grid_dimension_x()*protocol.get_bath_grid_dimension_y()*0.36*protocol.get_bath_airflow_windows_closed();
//                            airflowMicro = protocol.get_bath_grid_dimension_x()*protocol.get_bath_grid_dimension_y()*0.36*protocol.get_bath_airflow_microventilation();
                            airflowOpen = protocol.get_bath_grid_dimension_x()*protocol.get_bath_grid_dimension_y()*0.36*protocol.get_bath_airflow_windows_open();
                        } else {
                            airflowClosed = ((protocol.get_bath_grid_dimension_round()/2)*(protocol.get_bath_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_bath_airflow_windows_closed();
//                            airflowMicro = ((protocol.get_bath_grid_dimension_round()/2)*(protocol.get_bath_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_bath_airflow_microventilation();
                            airflowOpen = ((protocol.get_bath_grid_dimension_round()/2)*(protocol.get_bath_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_bath_airflow_windows_open();
                        }
                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 205 480 " + Double.toString(round(airflowClosed, 2)) + "\r\n"
                                + "TEXT CALIBRI8.CPF 0 205 510 " + Double.toString(round(airflowOpen, 2)) + "\r\n";
//                                + "TEXT CALIBRI8.CPF 0 205 700 " + Double.toString(round(airflowMicro, 2)) + "\r\n";
                    } else {
                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 205 480 - \r\n"
                                + "TEXT CALIBRI8.CPF 0 205 510 - \r\n";
//                                + "TEXT CALIBRI8.CPF 0 205 700 - \r\n";
                    }

                    if(protocol.is_toilet_enabled()){
                        double airflowClosed;
//                        double airflowMicro;
                        double airflowOpen;
                        if(protocol.get_toilet_grid_dimension_round()==0){
                            airflowClosed = protocol.get_toilet_grid_dimension_x()*protocol.get_toilet_grid_dimension_y()*0.36*protocol.get_toilet_airflow_windows_closed();
//                            airflowMicro = protocol.get_toilet_grid_dimension_x()*protocol.get_toilet_grid_dimension_y()*0.36*protocol.get_toilet_airflow_microventilation();
                            airflowOpen = protocol.get_toilet_grid_dimension_x()*protocol.get_toilet_grid_dimension_y()*0.36*protocol.get_toilet_airflow_windows_open();
                        } else {
                            airflowClosed = ((protocol.get_toilet_grid_dimension_round()/2)*(protocol.get_toilet_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_toilet_airflow_windows_closed();
//                            airflowMicro = ((protocol.get_toilet_grid_dimension_round()/2)*(protocol.get_toilet_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_toilet_airflow_microventilation();
                            airflowOpen = ((protocol.get_toilet_grid_dimension_round()/2)*(protocol.get_toilet_grid_dimension_round()/2)*Math.PI)*0.36*protocol.get_toilet_airflow_windows_open();
                        }
                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 205 570 " + Double.toString(round(airflowClosed, 2)) + "\r\n"
                                + "TEXT CALIBRI8.CPF 0 205 600 " + Double.toString(round(airflowOpen, 2)) + "\r\n";
//                                + "TEXT CALIBRI8.CPF 0 205 820 " + Double.toString(round(airflowMicro, 2)) + "\r\n";
                    } else {
                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 205 570 - \r\n"
                                + "TEXT CALIBRI8.CPF 0 205 600 - \r\n";
//                                + "TEXT CALIBRI8.CPF 0 205 820 - \r\n";
                    }

                    if(protocol.is_flue_enabled()){
                        double airflowClosed = protocol.get_flue_airflow_windows_closed()*70.3;
//                        double airflowMicro = protocol.get_flue_airflow_microventilation()*70.3;
                        double airflowOpen = protocol.get_flue_airflow_windows_open()*70.3;
                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 205 660 " + Double.toString(round(airflowClosed, 2)) + "\r\n"
                                + "TEXT CALIBRI8.CPF 0 205 690 " + Double.toString(round(airflowOpen, 2)) + "\r\n";
//                                + "TEXT CALIBRI8.CPF 0 205 940 " + Double.toString(round(airflowMicro, 2)) + "\r\n";
                    } else {
                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 205 660 - \r\n"
                                + "TEXT CALIBRI8.CPF 0 205 690 - \r\n";
//                                + "TEXT CALIBRI8.CPF 0 205 940 - \r\n";
                    }

//                    String kitchenComments = "";
//                    if(protocol.is_kitchen_cleaned())
//                        kitchenComments = kitchenComments + "przeczyszczono, ";
//                    if(protocol.is_kitchen_hood())
//                        kitchenComments = kitchenComments + "okap elektryczny, ";
//                    if(protocol.is_kitchen_vent())
//                        kitchenComments = kitchenComments + "wentylator, ";
//                    if(protocol.is_kitchen_inaccessible())
//                        kitchenComments = kitchenComments + "zabudowa, brak dostepu, ";
//                    if(protocol.is_kitchen_steady())
//                        kitchenComments = kitchenComments + "kratka stala, ";
//                    if(protocol.is_kitchen_not_cleaned())
//                        kitchenComments = kitchenComments + "nie przeczyszczono, ";
//                    if(protocol.is_kitchen_others())
//                        kitchenComments = kitchenComments + protocol.get_kitchen_others_comments();
//
//                    if(kitchenComments.isEmpty()){
//                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 990 brak\r\n";
//                    } else {
//                        if(kitchenComments.length()<45){
//                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 990" + kitchenComments + "\r\n";
//                        } else {
//                            if(kitchenComments.length()<90){
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 990" + kitchenComments.substring(0, 45)+"-" + "\r\n";
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1010" +"-"+kitchenComments.substring(45, kitchenComments.length()) + "\r\n";
//                            } else {
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 990" + kitchenComments.substring(0, 45)+"-" + "\r\n";
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1010" + "-"+kitchenComments.substring(45, 90)+"-" + "\r\n";
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1030" + "-"+kitchenComments.substring(90, kitchenComments.length()) + "\r\n";
//                            }
//                        }
//                    }

//                    String toiletComments = "";
//                    if(protocol.is_toilet_cleaned())
//                        toiletComments = toiletComments + "przeczyszczono, ";
//                    if(protocol.is_toilet_vent())
//                        toiletComments = toiletComments + "wentylator, ";
//                    if(protocol.is_toilet_inaccessible())
//                        toiletComments = toiletComments + "zabudowa, brak dostepu, ";
//                    if(protocol.is_toilet_steady())
//                        toiletComments = toiletComments + "kratka stala, ";
//                    if(protocol.is_toilet_not_cleaned())
//                        toiletComments = toiletComments + "nie przeczyszczono, ";
//                    if(protocol.is_toilet_others())
//                        toiletComments = toiletComments + protocol.get_toilet_others_comments();
//
//                    if(toiletComments.isEmpty()){
//                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1080 brak\r\n";
//                    } else {
//                        if(toiletComments.length()<45){
//                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1080" + toiletComments + "\r\n";
//                        } else {
//                            if(toiletComments.length()<90){
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1080" + toiletComments.substring(0, 45)+"-" + "\r\n";
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1100" +"-"+toiletComments.substring(45, toiletComments.length()) + "\r\n";
//                            } else {
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1080" + toiletComments.substring(0, 45)+"-" + "\r\n";
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1100" + "-"+toiletComments.substring(45, 90)+"-" + "\r\n";
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1120" + "-"+toiletComments.substring(90, toiletComments.length()) + "\r\n";
//                            }
//                        }
//                    }

//                    String bathComments = "";
//                    if(protocol.is_bath_cleaned())
//                        bathComments = bathComments + "przeczyszczono, ";
//                    if(protocol.is_bath_vent())
//                        bathComments = bathComments + "wentylator, ";
//                    if(protocol.is_bath_inaccessible())
//                        bathComments = bathComments + "zabudowa, brak dostepu, ";
//                    if(protocol.is_bath_steady())
//                        bathComments = bathComments + "kratka stala, ";
//                    if(protocol.is_bath_not_cleaned())
//                        bathComments = bathComments + "nie przeczyszczono, ";
//                    if(protocol.is_bath_others())
//                        bathComments = bathComments + protocol.get_bath_others_comments();
//
//                    if(bathComments.isEmpty()){
//                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1170 brak\r\n";
//                    } else {
//                        if(bathComments.length()<45){
//                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1170" + bathComments + "\r\n";
//                        } else {
//                            if(bathComments.length()<90){
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1170" + bathComments.substring(0, 45)+"-" + "\r\n";
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1190" +"-"+bathComments.substring(45, bathComments.length()) + "\r\n";
//                            } else {
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1170" + bathComments.substring(0, 45)+"-" + "\r\n";
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1190" + "-"+bathComments.substring(45, 90)+"-" + "\r\n";
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1210" + "-"+bathComments.substring(90, bathComments.length()) + "\r\n";
//                            }
//                        }
//                    }


//                    String flueComments = "";
//                    if(protocol.is_flue_cleaned())
//                        flueComments = flueComments + "przeczyszczono, ";
//                    if(protocol.is_flue_boiler())
//                        flueComments = flueComments + "bojler/podgrzewacz przep., ";
//                    if(protocol.is_flue_inaccessible())
//                        flueComments = flueComments + "zabudowa, brak dostepu, ";
//                    if(protocol.is_flue_rigid())
//                        flueComments = flueComments + "sztywna rura, ";
//                    if(protocol.is_flue_alufol())
//                        flueComments = flueComments + "alufol, ";
//                    if(protocol.is_flue_not_cleaned())
//                        flueComments = flueComments + "nie przeczyszczono, ";
//                    if(protocol.is_flue_others())
//                        flueComments = flueComments + protocol.get_flue_others_comments();
//
//                    if(flueComments.isEmpty()){
//                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1260 brak\r\n";
//                    } else {
//                        if(flueComments.length()<45){
//                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1260" + flueComments + "\r\n";
//                        } else {
//                            if(flueComments.length()<90){
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1260" + flueComments.substring(0, 45)+"-" + "\r\n";
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1280" +"-"+flueComments.substring(45, flueComments.length()) + "\r\n";
//                            } else {
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1260" + flueComments.substring(0, 45)+"-" + "\r\n";
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1280" + "-"+flueComments.substring(45, 90)+"-" + "\r\n";
//                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 1300" + "-"+flueComments.substring(90, flueComments.length()) + "\r\n";
//                            }
//                        }
//                    }

                    if(protocol.get_comments().isEmpty()){
                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 740 brak\r\n";
                    } else {
                        if(protocol.get_comments().length()<35){
                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 740" + protocol.get_comments() + "\r\n";
                        } else {
                            if(protocol.get_comments().length()<70){
                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 740" + protocol.get_comments().substring(0, 35)+"-" + "\r\n";
                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 760" +"-"+protocol.get_comments().substring(35, protocol.get_comments().length()) + "\r\n";
                            } else {
                                if(protocol.get_comments().length()<105){
                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 740" + protocol.get_comments().substring(0, 35)+"-" + "\r\n";
                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 760" + "-"+protocol.get_comments().substring(35, 70)+"-" + "\r\n";
                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 780" + "-"+protocol.get_comments().substring(70, protocol.get_comments().length()) + "\r\n";
                                } else {
                                    if(protocol.get_comments().length()<140){
                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 740" + protocol.get_comments().substring(0, 35)+"-" + "\r\n";
                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 760" + "-"+protocol.get_comments().substring(35, 70)+"-" + "\r\n";
                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 780" + "-"+protocol.get_comments().substring(70, 105)+"-" + "\r\n";
                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 800" + "-"+protocol.get_comments().substring(105, protocol.get_comments().length()) + "\r\n";
                                    } else {
                                        if(protocol.get_comments().length()<175){
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 740" + protocol.get_comments().substring(0, 35)+"-" + "\r\n";
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 760" + "-"+protocol.get_comments().substring(35, 70)+"-" + "\r\n";
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 780" + "-"+protocol.get_comments().substring(70, 105)+"-" + "\r\n";
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 800" + "-"+protocol.get_comments().substring(105, 140)+"-" + "\r\n";
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 820" + "-"+protocol.get_comments().substring(140, protocol.get_comments().length()) + "\r\n";
                                        } else {
                                            if(protocol.get_comments().length()<210){
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 740" + protocol.get_comments().substring(0, 35)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 760" + "-"+protocol.get_comments().substring(35, 70)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 780" + "-"+protocol.get_comments().substring(70, 105)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 800" + "-"+protocol.get_comments().substring(105, 140)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 820" + "-"+protocol.get_comments().substring(140, 175)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 840" + "-"+protocol.get_comments().substring(175, protocol.get_comments().length()) + "\r\n";
                                            } else {
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 740" + protocol.get_comments().substring(0, 35)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 760" + "-"+protocol.get_comments().substring(35, 70)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 780" + "-"+protocol.get_comments().substring(70, 105)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 800" + "-"+protocol.get_comments().substring(105, 140)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 820" + "-"+protocol.get_comments().substring(140, 175)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 840" + "-"+protocol.get_comments().substring(175, 210) + "\r\n";
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
//                      TEST TEST TEST
                    if(protocol.get_comments_for_user().isEmpty()){
                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 890 brak\r\n";
                    } else {
                        if(protocol.get_comments_for_user().length()<35){
                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 890" + protocol.get_comments_for_user() + "\r\n";
                        } else {
                            if(protocol.get_comments_for_user().length()<70){
                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 890" + protocol.get_comments_for_user().substring(0, 35)+"-" + "\r\n";
                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 910" +"-"+protocol.get_comments_for_user().substring(35, protocol.get_comments_for_user().length()) + "\r\n";
                            } else {
                                if(protocol.get_comments_for_user().length()<105){
                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 890" + protocol.get_comments_for_user().substring(0, 35)+"-" + "\r\n";
                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 910" + "-"+protocol.get_comments_for_user().substring(35, 70)+"-" + "\r\n";
                                    cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 930" + "-"+protocol.get_comments_for_user().substring(70, protocol.get_comments_for_user().length()) + "\r\n";
                                } else {
                                    if(protocol.get_comments_for_user().length()<140){
                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 890" + protocol.get_comments_for_user().substring(0, 35)+"-" + "\r\n";
                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 910" + "-"+protocol.get_comments_for_user().substring(35, 70)+"-" + "\r\n";
                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 930" + "-"+protocol.get_comments_for_user().substring(70, 105)+"-" + "\r\n";
                                        cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 950" + "-"+protocol.get_comments_for_user().substring(105, protocol.get_comments_for_user().length()) + "\r\n";
                                    } else {
                                        if(protocol.get_comments_for_user().length()<175){
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 890" + protocol.get_comments_for_user().substring(0, 35)+"-" + "\r\n";
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 910" + "-"+protocol.get_comments_for_user().substring(35, 70)+"-" + "\r\n";
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 930" + "-"+protocol.get_comments_for_user().substring(70, 105)+"-" + "\r\n";
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 950" + "-"+protocol.get_comments_for_user().substring(105, 140)+"-" + "\r\n";
                                            cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 970" + "-"+protocol.get_comments_for_user().substring(140, protocol.get_comments_for_user().length()) + "\r\n";
                                        } else {
                                            if(protocol.get_comments_for_user().length()<210){
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 890" + protocol.get_comments_for_user().substring(0, 35)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 910" + "-"+protocol.get_comments_for_user().substring(35, 70)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 930" + "-"+protocol.get_comments_for_user().substring(70, 105)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 950" + "-"+protocol.get_comments_for_user().substring(105, 140)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 970" + "-"+protocol.get_comments_for_user().substring(140, 175)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 990" + "-"+protocol.get_comments_for_user().substring(175, protocol.get_comments_for_user().length()) + "\r\n";
                                            } else {
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 890" + protocol.get_comments_for_user().substring(0, 35)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 910" + "-"+protocol.get_comments_for_user().substring(35, 70)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 930" + "-"+protocol.get_comments_for_user().substring(70, 105)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 950" + "-"+protocol.get_comments_for_user().substring(105, 140)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 970" + "-"+protocol.get_comments_for_user().substring(140, 175)+"-" + "\r\n";
                                                cpclData = cpclData + "TEXT CALIBRI8.CPF 0 10 990" + "-"+protocol.get_comments_for_user().substring(175, 210) + "\r\n";
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    cpclData = cpclData + "FORM\r\n" + "PRINT\r\n";

                    // Send the data to printer as a byte array.
//                  use Cp1250 encoding for polish characters
                    thePrinterConn.write(decodeText(cpclData, "CP1250"));
//                    thePrinterConn.write(cpclData.getBytes());

                    Thread.sleep(500);

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
}
