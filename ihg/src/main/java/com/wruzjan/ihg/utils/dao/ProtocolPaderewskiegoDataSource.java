package com.wruzjan.ihg.utils.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wruzjan.ihg.utils.Utils;
import com.wruzjan.ihg.utils.db.ApplicationOpenHelper;
import com.wruzjan.ihg.utils.model.ProtocolPaderewskiego;

import java.util.ArrayList;
import java.util.List;

public class ProtocolPaderewskiegoDataSource {

    private SQLiteDatabase database;
    private ApplicationOpenHelper applicationHelper;
    private String[] allColumns = {
            ApplicationOpenHelper.COLUMN_ID,
            ApplicationOpenHelper.COLUMN_ADDRESS_ID,
            ApplicationOpenHelper.COLUMN_WORKER_NAME,
            ApplicationOpenHelper.COLUMN_PHONE_NR,
            ApplicationOpenHelper.COLUMN_TEMP_OUTSIDE,
            ApplicationOpenHelper.COLUMN_TEMP_INSIDE,
            ApplicationOpenHelper.COLUMN_WIND_SPEED,
            ApplicationOpenHelper.COLUMN_WIND_DIRECTION,
            ApplicationOpenHelper.COLUMN_PRESSURE,
            ApplicationOpenHelper.COLUMN_WINDOWS_ALL,
            ApplicationOpenHelper.COLUMN_WINDOWS_MICRO,
            ApplicationOpenHelper.COLUMN_WINDOWS_VENT,
            ApplicationOpenHelper.COLUMN_WINDOWS_NO_MICRO,
            ApplicationOpenHelper.COLUMN_CO,
            ApplicationOpenHelper.COLUMN_COMMENTS,
            ApplicationOpenHelper.COLUMN_EQ_CH_GAS_METER_WORKING,
            ApplicationOpenHelper.COLUMN_EQ_CH_STOVE_WORKING,
            ApplicationOpenHelper.COLUMN_EQ_CH_BAKE_WORKING,
            ApplicationOpenHelper.COLUMN_EQ_CH_COMBI_OVEN_WORKING,
            ApplicationOpenHelper.COLUMN_EQ_CH_KITCHEN_TERM_WORKING,
            ApplicationOpenHelper.COLUMN_EQ_CH_OTHERS,
            ApplicationOpenHelper.COLUMN_EQ_GAS_METER_WORKING,
            ApplicationOpenHelper.COLUMN_EQ_STOVE_WORKING,
            ApplicationOpenHelper.COLUMN_EQ_BAKE_WORKING,
            ApplicationOpenHelper.COLUMN_EQ_COMBI_OVEN_WORKING,
            ApplicationOpenHelper.COLUMN_EQ_KITCHEN_TERM_WORKING,
            ApplicationOpenHelper.COLUMN_EQ_OTHERS,
            ApplicationOpenHelper.COLUMN_KITCHEN_ENABLED,
            ApplicationOpenHelper.COLUMN_KITCHEN_CLEANED,
            ApplicationOpenHelper.COLUMN_KITCHEN_HOOD,
            ApplicationOpenHelper.COLUMN_KITCHEN_VENT,
            ApplicationOpenHelper.COLUMN_KITCHEN_INACCESSIBLE,
            ApplicationOpenHelper.COLUMN_KITCHEN_STEADY,
            ApplicationOpenHelper.COLUMN_KITCHEN_NOT_CLEANED,
            ApplicationOpenHelper.COLUMN_KITCHEN_OTHERS,
            ApplicationOpenHelper.COLUMN_KITCHEN_OTHERS_COMMENTS,
            ApplicationOpenHelper.COLUMN_KITCHEN_GRID_DIMENSION_X,
            ApplicationOpenHelper.COLUMN_KITCHEN_GRID_DIMENSION_Y,
            ApplicationOpenHelper.COLUMN_KITCHEN_GRID_DIMENSION_ROUND,
            ApplicationOpenHelper.COLUMN_KITCHEN_AIRFLOW_WINDOWS_CLOSED,
            ApplicationOpenHelper.COLUMN_KITCHEN_AIRFLOW_WINDOWS_OPEN,
            ApplicationOpenHelper.COLUMN_KITCHEN_AIRFLOW_WINDOWS_MICRO,
            ApplicationOpenHelper.COLUMN_TOILET_ENABLED,
            ApplicationOpenHelper.COLUMN_TOILET_CLEANED,
            ApplicationOpenHelper.COLUMN_TOILET_VENT,
            ApplicationOpenHelper.COLUMN_TOILET_INACCESSIBLE,
            ApplicationOpenHelper.COLUMN_TOILET_STEADY,
            ApplicationOpenHelper.COLUMN_TOILET_NOT_CLEANED,
            ApplicationOpenHelper.COLUMN_TOILET_OTHERS,
            ApplicationOpenHelper.COLUMN_TOILET_OTHERS_COMMENTS,
            ApplicationOpenHelper.COLUMN_TOILET_GRID_DIMENSION_X,
            ApplicationOpenHelper.COLUMN_TOILET_GRID_DIMENSION_Y,
            ApplicationOpenHelper.COLUMN_TOILET_GRID_DIMENSION_ROUND,
            ApplicationOpenHelper.COLUMN_TOILET_AIRFLOW_WINDOWS_CLOSED,
            ApplicationOpenHelper.COLUMN_TOILET_AIRFLOW_WINDOWS_OPEN,
            ApplicationOpenHelper.COLUMN_TOILET_AIRFLOW_WINDOWS_MICRO,
            ApplicationOpenHelper.COLUMN_BATH_ENABLED,
            ApplicationOpenHelper.COLUMN_BATH_CLEANED,
            ApplicationOpenHelper.COLUMN_BATH_VENT,
            ApplicationOpenHelper.COLUMN_BATH_INACCESSIBLE,
            ApplicationOpenHelper.COLUMN_BATH_STEADY,
            ApplicationOpenHelper.COLUMN_BATH_NOT_CLEANED,
            ApplicationOpenHelper.COLUMN_BATH_OTHERS,
            ApplicationOpenHelper.COLUMN_BATH_OTHERS_COMMENTS,
            ApplicationOpenHelper.COLUMN_BATH_GRID_DIMENSION_X,
            ApplicationOpenHelper.COLUMN_BATH_GRID_DIMENSION_Y,
            ApplicationOpenHelper.COLUMN_BATH_GRID_DIMENSION_ROUND,
            ApplicationOpenHelper.COLUMN_BATH_AIRFLOW_WINDOWS_CLOSED,
            ApplicationOpenHelper.COLUMN_BATH_AIRFLOW_WINDOWS_OPEN,
            ApplicationOpenHelper.COLUMN_BATH_AIRFLOW_WINDOWS_MICRO,
            ApplicationOpenHelper.COLUMN_FLUE_ENABLED,
            ApplicationOpenHelper.COLUMN_FLUE_CLEANED,
            ApplicationOpenHelper.COLUMN_FLUE_BOILER,
            ApplicationOpenHelper.COLUMN_FLUE_INACCESSIBLE,
            ApplicationOpenHelper.COLUMN_FLUE_RIGID,
            ApplicationOpenHelper.COLUMN_FLUE_ALUFOL,
            ApplicationOpenHelper.COLUMN_FLUE_NOT_CLEANED,
            ApplicationOpenHelper.COLUMN_FLUE_OTHERS,
            ApplicationOpenHelper.COLUMN_FLUE_OTHERS_COMMENTS,
            ApplicationOpenHelper.COLUMN_FLUE_AIRFLOW_WINDOWS_CLOSED,
            ApplicationOpenHelper.COLUMN_FLUE_AIRFLOW_WINDOWS_OPEN,
            ApplicationOpenHelper.COLUMN_FLUE_AIRFLOW_WINDOWS_MICRO,
            ApplicationOpenHelper.COLUMN_COMMENTS_FOR_USER,
            ApplicationOpenHelper.COLUMN_CREATED};

    public ProtocolPaderewskiegoDataSource(Context context) {
        applicationHelper = new ApplicationOpenHelper(context);
    }

    public void open() throws SQLException {
        database = applicationHelper.getWritableDatabase();
    }

    public void close() {
        applicationHelper.close();
    }

    public ProtocolPaderewskiego insertProtocolPaderewskiego(ProtocolPaderewskiego protocol) {

        Log.d(Utils.DEBUG_TAG, "ZAPISZ DO TABLE_PROTOCOL_PADEREWSKIEGO Z ADRESEM: " + protocol.get_address_id());

        ContentValues values = new ContentValues();
        values.put(applicationHelper.COLUMN_ADDRESS_ID, protocol.get_address_id());
        values.put(applicationHelper.COLUMN_WORKER_NAME, protocol.get_worker_name());
        values.put(applicationHelper.COLUMN_PHONE_NR, protocol.get_telephone());
        values.put(applicationHelper.COLUMN_TEMP_OUTSIDE, protocol.get_temp_outside());
        values.put(applicationHelper.COLUMN_TEMP_INSIDE, protocol.get_temp_inside());
        values.put(applicationHelper.COLUMN_WIND_SPEED, protocol.get_wind_speed());
        values.put(applicationHelper.COLUMN_WIND_DIRECTION, protocol.get_wind_direction());
        values.put(applicationHelper.COLUMN_PRESSURE, protocol.get_pressure());
        values.put(applicationHelper.COLUMN_WINDOWS_ALL, protocol.get_windows_all());
        values.put(applicationHelper.COLUMN_WINDOWS_MICRO, protocol.get_windows_micro());
        values.put(applicationHelper.COLUMN_WINDOWS_VENT, protocol.get_windows_vent());
        values.put(applicationHelper.COLUMN_WINDOWS_NO_MICRO, protocol.get_windows_no_micro());
        values.put(applicationHelper.COLUMN_CO, protocol.get_co2());
        values.put(applicationHelper.COLUMN_COMMENTS, protocol.get_comments());
        if(protocol.is_eq_ch_gas_meter_working()){
            values.put(applicationHelper.COLUMN_EQ_CH_GAS_METER_WORKING, 1);
        } else {
            values.put(applicationHelper.COLUMN_EQ_CH_GAS_METER_WORKING, 0);
        }
        if(protocol.is_eq_gas_meter_working()){
            values.put(applicationHelper.COLUMN_EQ_GAS_METER_WORKING, 1);
        } else {
            values.put(applicationHelper.COLUMN_EQ_GAS_METER_WORKING, 0);
        }
        if(protocol.is_eq_ch_stove_working()){
            values.put(applicationHelper.COLUMN_EQ_CH_STOVE_WORKING, 1);
        } else {
            values.put(applicationHelper.COLUMN_EQ_CH_STOVE_WORKING, 0);
        }
        if(protocol.is_eq_stove_working()){
            values.put(applicationHelper.COLUMN_EQ_STOVE_WORKING, 1);
        } else {
            values.put(applicationHelper.COLUMN_EQ_STOVE_WORKING, 0);
        }
        if(protocol.is_eq_ch_bake_working()){
            values.put(applicationHelper.COLUMN_EQ_CH_BAKE_WORKING, 1);
        } else {
            values.put(applicationHelper.COLUMN_EQ_CH_BAKE_WORKING, 0);
        }
        if(protocol.is_eq_bake_working()){
            values.put(applicationHelper.COLUMN_EQ_BAKE_WORKING, 1);
        } else {
            values.put(applicationHelper.COLUMN_EQ_BAKE_WORKING, 0);
        }
        if (protocol.is_eq_ch_combi_oven_working()){
            values.put(applicationHelper.COLUMN_EQ_CH_COMBI_OVEN_WORKING, 1);
        } else {
            values.put(applicationHelper.COLUMN_EQ_CH_COMBI_OVEN_WORKING, 0);
        }
        if (protocol.is_eq_combi_oven_working()){
            values.put(applicationHelper.COLUMN_EQ_COMBI_OVEN_WORKING, 1);
        } else {
            values.put(applicationHelper.COLUMN_EQ_COMBI_OVEN_WORKING, 0);
        }
        if(protocol.is_eq_ch_kitchen_term_working()){
            values.put(applicationHelper.COLUMN_EQ_CH_KITCHEN_TERM_WORKING, 1);
        } else {
            values.put(applicationHelper.COLUMN_EQ_CH_KITCHEN_TERM_WORKING, 0);
        }
        if(protocol.is_eq_kitchen_term_working()){
            values.put(applicationHelper.COLUMN_EQ_KITCHEN_TERM_WORKING, 1);
        } else {
            values.put(applicationHelper.COLUMN_EQ_KITCHEN_TERM_WORKING, 0);
        }
        if(protocol.is_eq_ch_others()){
            values.put(applicationHelper.COLUMN_EQ_CH_OTHERS, 1);
        } else {
            values.put(applicationHelper.COLUMN_EQ_CH_OTHERS, 0);
        }
        values.put(applicationHelper.COLUMN_EQ_OTHERS, protocol.get_eq_other());
        if(protocol.is_kitchen_enabled()){
            values.put(applicationHelper.COLUMN_KITCHEN_ENABLED, 1);
        } else {
            values.put(applicationHelper.COLUMN_KITCHEN_ENABLED, 0);
        }
        if(protocol.is_kitchen_cleaned()){
            values.put(applicationHelper.COLUMN_KITCHEN_CLEANED, 1);
        } else {
            values.put(applicationHelper.COLUMN_KITCHEN_CLEANED, 0);
        }
        if(protocol.is_kitchen_hood()){
            values.put(applicationHelper.COLUMN_KITCHEN_HOOD, 1);
        } else {
            values.put(applicationHelper.COLUMN_KITCHEN_HOOD, 0);
        }
        if(protocol.is_kitchen_vent()){
            values.put(applicationHelper.COLUMN_KITCHEN_VENT, 1);
        } else {
            values.put(applicationHelper.COLUMN_KITCHEN_VENT, 0);
        }
        if(protocol.is_kitchen_inaccessible()){
            values.put(applicationHelper.COLUMN_KITCHEN_INACCESSIBLE, 1);
        } else {
            values.put(applicationHelper.COLUMN_KITCHEN_INACCESSIBLE, 0);
        }
        if(protocol.is_kitchen_steady()){
            values.put(applicationHelper.COLUMN_KITCHEN_STEADY, 1);
        } else {
            values.put(applicationHelper.COLUMN_KITCHEN_STEADY, 0);
        }
        if(protocol.is_kitchen_not_cleaned()){
            values.put(applicationHelper.COLUMN_KITCHEN_NOT_CLEANED, 1);
        } else {
            values.put(applicationHelper.COLUMN_KITCHEN_NOT_CLEANED, 0);
        }
        if(protocol.is_kitchen_others()){
            values.put(applicationHelper.COLUMN_KITCHEN_OTHERS, 1);
        } else {
            values.put(applicationHelper.COLUMN_KITCHEN_OTHERS, 0);
        }
        values.put(applicationHelper.COLUMN_KITCHEN_OTHERS_COMMENTS, protocol.get_kitchen_others_comments());
        values.put(applicationHelper.COLUMN_KITCHEN_GRID_DIMENSION_X, protocol.get_kitchen_grid_dimension_x());
        values.put(applicationHelper.COLUMN_KITCHEN_GRID_DIMENSION_Y, protocol.get_kitchen_grid_dimension_y());
        values.put(applicationHelper.COLUMN_KITCHEN_GRID_DIMENSION_ROUND, protocol.get_kitchen_grid_dimension_round());
        values.put(applicationHelper.COLUMN_KITCHEN_AIRFLOW_WINDOWS_CLOSED, protocol.get_kitchen_airflow_windows_closed());
        values.put(applicationHelper.COLUMN_KITCHEN_AIRFLOW_WINDOWS_OPEN, protocol.get_kitchen_airflow_windows_open());
        values.put(applicationHelper.COLUMN_KITCHEN_AIRFLOW_WINDOWS_MICRO, protocol.get_kitchen_airflow_microventilation());
        if(protocol.is_toilet_enabled()){
            values.put(applicationHelper.COLUMN_TOILET_ENABLED, 1);
        } else {
            values.put(applicationHelper.COLUMN_TOILET_ENABLED, 0);
        }
        if(protocol.is_toilet_cleaned()){
            values.put(applicationHelper.COLUMN_TOILET_CLEANED, 1);
        } else {
            values.put(applicationHelper.COLUMN_TOILET_CLEANED, 0);
        }
        if(protocol.is_toilet_vent()){
            values.put(applicationHelper.COLUMN_TOILET_VENT, 1);
        } else {
            values.put(applicationHelper.COLUMN_TOILET_VENT, 0);
        }
        if(protocol.is_toilet_inaccessible()){
            values.put(applicationHelper.COLUMN_TOILET_INACCESSIBLE, 1);
        } else {
            values.put(applicationHelper.COLUMN_TOILET_INACCESSIBLE, 0);
        }
        if(protocol.is_toilet_steady()){
            values.put(applicationHelper.COLUMN_TOILET_STEADY, 1);
        } else {
            values.put(applicationHelper.COLUMN_TOILET_STEADY, 0);
        }
        if(protocol.is_toilet_not_cleaned()){
            values.put(applicationHelper.COLUMN_TOILET_NOT_CLEANED, 1);
        } else {
            values.put(applicationHelper.COLUMN_TOILET_NOT_CLEANED, 0);
        }
        if(protocol.is_toilet_others()){
            values.put(applicationHelper.COLUMN_TOILET_OTHERS, 1);
        } else {
            values.put(applicationHelper.COLUMN_TOILET_OTHERS, 0);
        }
        values.put(applicationHelper.COLUMN_TOILET_OTHERS_COMMENTS, protocol.get_toilet_others_comments());
        values.put(applicationHelper.COLUMN_TOILET_GRID_DIMENSION_X, protocol.get_toilet_grid_dimension_x());
        values.put(applicationHelper.COLUMN_TOILET_GRID_DIMENSION_Y, protocol.get_toilet_grid_dimension_y());
        values.put(applicationHelper.COLUMN_TOILET_GRID_DIMENSION_ROUND, protocol.get_toilet_grid_dimension_round());
        values.put(applicationHelper.COLUMN_TOILET_AIRFLOW_WINDOWS_CLOSED, protocol.get_toilet_airflow_windows_closed());
        values.put(applicationHelper.COLUMN_TOILET_AIRFLOW_WINDOWS_OPEN, protocol.get_toilet_airflow_windows_open());
        values.put(applicationHelper.COLUMN_TOILET_AIRFLOW_WINDOWS_MICRO, protocol.get_toilet_airflow_microventilation());
        if(protocol.is_bath_enabled()){
            values.put(applicationHelper.COLUMN_BATH_ENABLED, 1);
        } else {
            values.put(applicationHelper.COLUMN_BATH_ENABLED, 0);
        }
        if(protocol.is_bath_cleaned()){
            values.put(applicationHelper.COLUMN_BATH_CLEANED, 1);
        } else {
            values.put(applicationHelper.COLUMN_BATH_CLEANED, 0);
        }
        if(protocol.is_bath_vent()){
            values.put(applicationHelper.COLUMN_BATH_VENT, 1);
        } else {
            values.put(applicationHelper.COLUMN_BATH_VENT, 0);
        }
        if(protocol.is_bath_inaccessible()){
            values.put(applicationHelper.COLUMN_BATH_INACCESSIBLE, 1);
        } else {
            values.put(applicationHelper.COLUMN_BATH_INACCESSIBLE, 0);
        }
        if(protocol.is_bath_steady()){
            values.put(applicationHelper.COLUMN_BATH_STEADY, 1);
        } else {
            values.put(applicationHelper.COLUMN_BATH_STEADY, 0);
        }
        if(protocol.is_bath_not_cleaned()){
            values.put(applicationHelper.COLUMN_BATH_NOT_CLEANED, 1);
        } else {
            values.put(applicationHelper.COLUMN_BATH_NOT_CLEANED, 0);
        }
        if(protocol.is_bath_others()){
            values.put(applicationHelper.COLUMN_BATH_OTHERS, 1);
        } else {
            values.put(applicationHelper.COLUMN_BATH_OTHERS, 0);
        }
        values.put(applicationHelper.COLUMN_BATH_OTHERS_COMMENTS, protocol.get_bath_others_comments());
        values.put(applicationHelper.COLUMN_BATH_GRID_DIMENSION_X, protocol.get_bath_grid_dimension_x());
        values.put(applicationHelper.COLUMN_BATH_GRID_DIMENSION_Y, protocol.get_bath_grid_dimension_y());
        values.put(applicationHelper.COLUMN_BATH_GRID_DIMENSION_ROUND, protocol.get_bath_grid_dimension_round());
        values.put(applicationHelper.COLUMN_BATH_AIRFLOW_WINDOWS_CLOSED, protocol.get_bath_airflow_windows_closed());
        values.put(applicationHelper.COLUMN_BATH_AIRFLOW_WINDOWS_OPEN, protocol.get_bath_airflow_windows_open());
        values.put(applicationHelper.COLUMN_BATH_AIRFLOW_WINDOWS_MICRO, protocol.get_bath_airflow_microventilation());
        if(protocol.is_flue_enabled()){
            values.put(applicationHelper.COLUMN_FLUE_ENABLED, 1);
        } else {
            values.put(applicationHelper.COLUMN_FLUE_ENABLED, 0);
        }
        if(protocol.is_flue_cleaned()){
            values.put(applicationHelper.COLUMN_FLUE_CLEANED, 1);
        } else {
            values.put(applicationHelper.COLUMN_FLUE_CLEANED, 0);
        }
        if(protocol.is_flue_boiler()){
            values.put(applicationHelper.COLUMN_FLUE_BOILER, 1);
        } else {
            values.put(applicationHelper.COLUMN_FLUE_BOILER, 0);
        }
        if(protocol.is_flue_inaccessible()){
            values.put(applicationHelper.COLUMN_FLUE_INACCESSIBLE, 1);
        } else {
            values.put(applicationHelper.COLUMN_FLUE_INACCESSIBLE, 0);
        }
        if(protocol.is_flue_rigid()){
            values.put(applicationHelper.COLUMN_FLUE_RIGID, 1);
        } else {
            values.put(applicationHelper.COLUMN_FLUE_RIGID, 0);
        }
        if(protocol.is_flue_alufol()){
            values.put(applicationHelper.COLUMN_FLUE_ALUFOL, 1);
        } else {
            values.put(applicationHelper.COLUMN_FLUE_ALUFOL, 0);
        }
        if(protocol.is_flue_not_cleaned()){
            values.put(applicationHelper.COLUMN_FLUE_NOT_CLEANED, 1);
        } else {
            values.put(applicationHelper.COLUMN_FLUE_NOT_CLEANED, 0);
        }
        if(protocol.is_flue_others()){
            values.put(applicationHelper.COLUMN_FLUE_OTHERS, 1);
        } else {
            values.put(applicationHelper.COLUMN_FLUE_OTHERS, 0);
        }
        values.put(applicationHelper.COLUMN_FLUE_OTHERS_COMMENTS, protocol.get_flue_others_comments());
        values.put(applicationHelper.COLUMN_FLUE_AIRFLOW_WINDOWS_CLOSED, protocol.get_flue_airflow_windows_closed());
        values.put(applicationHelper.COLUMN_FLUE_AIRFLOW_WINDOWS_OPEN, protocol.get_flue_airflow_windows_open());
        values.put(applicationHelper.COLUMN_FLUE_AIRFLOW_WINDOWS_MICRO, protocol.get_flue_airflow_microventilation());
        values.put(applicationHelper.COLUMN_COMMENTS_FOR_USER, protocol.get_comments_for_user());
        values.put(applicationHelper.COLUMN_CREATED, protocol.get_created());

        long insertId = database.insert(ApplicationOpenHelper.TABLE_PROTOCOL_PADEREWSKIEGO, null,
                values);

        Cursor cursor = database.query(ApplicationOpenHelper.TABLE_PROTOCOL_PADEREWSKIEGO,
                allColumns, ApplicationOpenHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        ProtocolPaderewskiego newProtocol = cursorToProtocolShort(cursor);
        cursor.close();
        return newProtocol;
    }

    public List<ProtocolPaderewskiego> getAllPaderewskiegoProtocolsByAddressId(int addressId) {
        List<ProtocolPaderewskiego> protocols = new ArrayList<ProtocolPaderewskiego>();
        Cursor cursor = database.query(ApplicationOpenHelper.TABLE_PROTOCOL_PADEREWSKIEGO,
                allColumns, applicationHelper.COLUMN_ADDRESS_ID + " = " + addressId, null, null, null, applicationHelper.COLUMN_CREATED + " desc");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ProtocolPaderewskiego protocol = cursorToProtocolShort(cursor);
            protocols.add(protocol);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return protocols;
    }

    public ProtocolPaderewskiego getPaderewskiegoProtocolsById(int protocolId) {
        ProtocolPaderewskiego protocol;
        Cursor cursor = database.query(ApplicationOpenHelper.TABLE_PROTOCOL_PADEREWSKIEGO,
                allColumns, ApplicationOpenHelper.COLUMN_ID + " = " + protocolId, null,
                null, null, null);

        cursor.moveToFirst();
        protocol = cursorToProtocol(cursor);
        cursor.close();
        return protocol;
    }

    private ProtocolPaderewskiego cursorToProtocolShort(Cursor cursor) {
        ProtocolPaderewskiego protocol = new ProtocolPaderewskiego();
        protocol.set_id(cursor.getInt(0));
        protocol.set_address_id(Integer.parseInt(cursor.getString(1)));
        protocol.set_created(cursor.getString(83));
        return protocol;
    }

    private ProtocolPaderewskiego cursorToProtocol(Cursor cursor) {
        ProtocolPaderewskiego protocol = new ProtocolPaderewskiego();
        protocol.set_id(cursor.getInt(0));
        protocol.set_address_id(Integer.parseInt(cursor.getString(1)));
        protocol.set_worker_name(cursor.getString(2));
        protocol.set_telephone(cursor.getString(3));
        protocol.set_temp_outside(Double.parseDouble(cursor.getString(4)));
        protocol.set_temp_inside(Double.parseDouble(cursor.getString(5)));
        protocol.set_wind_speed(Double.parseDouble(cursor.getString(6)));
        protocol.set_wind_direction(cursor.getString(7));
        protocol.set_pressure(Double.parseDouble(cursor.getString(8)));
        protocol.set_windows_all(Integer.parseInt(cursor.getString(9)));
        protocol.set_windows_micro(Integer.parseInt(cursor.getString(10)));
        protocol.set_windows_vent(Integer.parseInt(cursor.getString(11)));
        protocol.set_windows_no_micro(Integer.parseInt(cursor.getString(12)));

        protocol.set_co2(Double.parseDouble(cursor.getString(13)));
        protocol.set_comments(cursor.getString(14));
        if(cursor.getInt(15) == 1){
            protocol.set_eq_ch_gas_meter_working(true);
        } else {
            protocol.set_eq_ch_gas_meter_working(false);
        }
        if(cursor.getInt(16) == 1){
            protocol.set_eq_ch_stove_working(true);
        } else {
            protocol.set_eq_ch_stove_working(false);
        }
        if(cursor.getInt(17) == 1){
            protocol.set_eq_ch_bake_working(true);
        } else {
            protocol.set_eq_ch_bake_working(false);
        }
        if(cursor.getInt(18) == 1){
            protocol.set_eq_ch_combi_oven_working(true);
        } else {
            protocol.set_eq_ch_combi_oven_working(false);
        }
        if(cursor.getInt(19) == 1){
            protocol.set_eq_ch_kitchen_term_working(true);
        } else {
            protocol.set_eq_ch_kitchen_term_working(false);
        }
        if(cursor.getInt(20) == 1){
            protocol.set_eq_ch_others(true);
        } else {
            protocol.set_eq_ch_others(false);
        }
        if(cursor.getInt(21) == 1){
            protocol.set_eq_gas_meter_working(true);
        } else {
            protocol.set_eq_gas_meter_working(false);
        }
        if(cursor.getInt(22) == 1){
            protocol.set_eq_stove_working(true);
        } else {
            protocol.set_eq_stove_working(false);
        }
        if(cursor.getInt(23) == 1){
            protocol.set_eq_bake_working(true);
        } else {
            protocol.set_eq_bake_working(true);
        }
        if(cursor.getInt(24) == 1){
            protocol.set_eq_combi_oven_working(true);
        } else {
            protocol.set_eq_combi_oven_working(false);
        }
        if(cursor.getInt(25) == 1){
            protocol.set_eq_kitchen_term_working(true);
        } else {
            protocol.set_eq_kitchen_term_working(false);
        }
        protocol.set_eq_other(cursor.getString(26));
        if(cursor.getInt(27) == 1){
            protocol.set_kitchen_enabled(true);
        } else {
            protocol.set_kitchen_enabled(false);
        }
        if(cursor.getInt(28) == 1){
            protocol.set_kitchen_cleaned(true);
        } else {
            protocol.set_kitchen_cleaned(false);
        }
        if(cursor.getInt(29) == 1){
            protocol.set_kitchen_hood(true);
        } else {
            protocol.set_kitchen_hood(false);
        }
        if(cursor.getInt(30) == 1){
            protocol.set_kitchen_vent(true);
        } else {
            protocol.set_kitchen_vent(false);
        }
        if(cursor.getInt(31) == 1){
            protocol.set_kitchen_inaccessible(true);
        } else {
            protocol.set_kitchen_inaccessible(false);
        }
        if(cursor.getInt(32) == 1){
            protocol.set_kitchen_steady(true);
        } else {
            protocol.set_kitchen_steady(false);
        }
        if(cursor.getInt(33) == 1){
            protocol.set_kitchen_not_cleaned(true);
        } else {
            protocol.set_kitchen_not_cleaned(false);
        }
        if(cursor.getInt(34) == 1){
            protocol.set_kitchen_others(true);
        } else {
            protocol.set_kitchen_others(false);
        }
        protocol.set_kitchen_others_comments(cursor.getString(35));
        protocol.set_kitchen_grid_dimension_x(cursor.getDouble(36));
        protocol.set_kitchen_grid_dimension_y(cursor.getDouble(37));
        protocol.set_kitchen_grid_dimension_round(cursor.getDouble(38));
        protocol.set_kitchen_airflow_windows_closed(cursor.getDouble(39));
        protocol.set_kitchen_airflow_windows_open(cursor.getDouble(40));
        protocol.set_kitchen_airflow_microventilation(cursor.getDouble(41));
        if(cursor.getInt(42) == 1){
            protocol.set_toilet_enabled(true);
        } else {
            protocol.set_toilet_enabled(false);
        }
        if(cursor.getInt(43) == 1){
            protocol.set_toilet_cleaned(true);
        } else {
            protocol.set_toilet_cleaned(false);
        }
        if(cursor.getInt(44) == 1){
            protocol.set_toilet_vent(true);
        } else {
            protocol.set_toilet_vent(false);
        }
        if(cursor.getInt(45) == 1){
            protocol.set_toilet_inaccessible(true);
        } else {
            protocol.set_toilet_inaccessible(false);
        }
        if(cursor.getInt(46) == 1){
            protocol.set_toilet_steady(true);
        } else {
            protocol.set_toilet_steady(false);
        }
        if(cursor.getInt(47) == 1){
            protocol.set_toilet_not_cleaned(true);
        } else {
            protocol.set_toilet_not_cleaned(false);
        }
        if(cursor.getInt(48) == 1){
            protocol.set_toilet_others(true);
        } else {
            protocol.set_toilet_others(false);
        }
        protocol.set_toilet_others_comments(cursor.getString(49));
        protocol.set_toilet_grid_dimension_x(cursor.getDouble(50));
        protocol.set_toilet_grid_dimension_y(cursor.getDouble(51));
        protocol.set_toilet_grid_dimension_round(cursor.getDouble(52));
        protocol.set_toilet_airflow_windows_closed(cursor.getDouble(53));
        protocol.set_toilet_airflow_windows_open(cursor.getDouble(54));
        protocol.set_toilet_airflow_microventilation(cursor.getDouble(55));
        if(cursor.getInt(56) == 1){
            protocol.set_bath_enabled(true);
        } else {
            protocol.set_bath_enabled(false);
        }
        if(cursor.getInt(57) == 1){
            protocol.set_bath_cleaned(true);
        } else {
            protocol.set_bath_cleaned(false);
        }
        if(cursor.getInt(58) == 1){
            protocol.set_bath_vent(true);
        } else {
            protocol.set_bath_vent(false);
        }
        if(cursor.getInt(59) == 1){
            protocol.set_bath_inaccessible(true);
        } else {
            protocol.set_bath_inaccessible(false);
        }
        if(cursor.getInt(60) == 1){
            protocol.set_bath_steady(true);
        } else {
            protocol.set_bath_steady(false);
        }
        if(cursor.getInt(61) == 1){
            protocol.set_bath_not_cleaned(true);
        } else {
            protocol.set_bath_not_cleaned(false);
        }
        if(cursor.getInt(62) == 1){
            protocol.set_bath_others(true);
        } else {
            protocol.set_bath_others(false);
        }
        protocol.set_bath_others_comments(cursor.getString(63));
        protocol.set_bath_grid_dimension_x(cursor.getDouble(64));
        protocol.set_bath_grid_dimension_y(cursor.getDouble(65));
        protocol.set_bath_grid_dimension_round(cursor.getDouble(66));
        protocol.set_bath_airflow_windows_closed(cursor.getDouble(67));
        protocol.set_bath_airflow_windows_open(cursor.getDouble(68));
        protocol.set_bath_airflow_microventilation(cursor.getDouble(69));
        if(cursor.getInt(70) == 1){
            protocol.set_flue_enabled(true);
        } else {
            protocol.set_flue_enabled(false);
        }
        if(cursor.getInt(71) == 1){
            protocol.set_flue_cleaned(true);
        } else {
            protocol.set_flue_cleaned(false);
        }
        if(cursor.getInt(72) == 1){
            protocol.set_flue_boiler(true);
        } else {
            protocol.set_flue_boiler(false);
        }
        if(cursor.getInt(73) == 1){
            protocol.set_flue_inaccessible(true);
        } else {
            protocol.set_flue_inaccessible(false);
        }
        if(cursor.getInt(74) == 1){
            protocol.set_flue_rigid(true);
        } else {
            protocol.set_flue_rigid(false);
        }
        if(cursor.getInt(75) == 1){
            protocol.set_flue_alufol(true);
        } else {
            protocol.set_flue_alufol(false);
        }
        if(cursor.getInt(76) == 1){
            protocol.set_flue_not_cleaned(true);
        } else {
            protocol.set_flue_not_cleaned(false);
        }
        if(cursor.getInt(77) == 1){
            protocol.set_flue_others(true);
        } else {
            protocol.set_flue_others(false);
        }
        protocol.set_flue_others_comments(cursor.getString(78));
        protocol.set_flue_airflow_windows_closed(cursor.getDouble(79));
        protocol.set_flue_airflow_windows_open(cursor.getDouble(80));
        protocol.set_flue_airflow_microventilation(cursor.getDouble(81));
        protocol.set_comments_for_user(cursor.getString(82));
        protocol.set_created(cursor.getString(83));
        return protocol;
    }
}