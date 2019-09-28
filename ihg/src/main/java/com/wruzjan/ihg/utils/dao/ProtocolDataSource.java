package com.wruzjan.ihg.utils.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.wruzjan.ihg.utils.db.ApplicationOpenHelper;
import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.Protocol;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProtocolDataSource {

    private SQLiteDatabase database;
    private ApplicationOpenHelper applicationHelper;
    private String[] allColumns = {
            ApplicationOpenHelper.COLUMN_ID,
            ApplicationOpenHelper.COLUMN_ADDRESS_ID,
            ApplicationOpenHelper.COLUMN_WORKER_NAME,
            ApplicationOpenHelper.COLUMN_TEMP_OUTSIDE,
            ApplicationOpenHelper.COLUMN_TEMP_INSIDE,
            ApplicationOpenHelper.COLUMN_CO,
            ApplicationOpenHelper.COLUMN_KITCHEN_ENABLED,
            ApplicationOpenHelper.COLUMN_KITCHEN_GRID_DIMENSION_X,
            ApplicationOpenHelper.COLUMN_KITCHEN_GRID_DIMENSION_Y,
            ApplicationOpenHelper.COLUMN_KITCHEN_GRID_DIMENSION_ROUND,
            ApplicationOpenHelper.COLUMN_KITCHEN_AIRFLOW_WINDOWS_CLOSED,
            ApplicationOpenHelper.COLUMN_KITCHEN_AIRFLOW_WINDOWS_MICRO,
            ApplicationOpenHelper.COLUMN_KITCHEN_COMMENTS,
            ApplicationOpenHelper.COLUMN_BATH_ENABLED,
            ApplicationOpenHelper.COLUMN_BATH_GRID_DIMENSION_X,
            ApplicationOpenHelper.COLUMN_BATH_GRID_DIMENSION_Y,
            ApplicationOpenHelper.COLUMN_BATH_GRID_DIMENSION_ROUND,
            ApplicationOpenHelper.COLUMN_BATH_AIRFLOW_WINDOWS_CLOSED,
            ApplicationOpenHelper.COLUMN_BATH_AIRFLOW_WINDOWS_MICRO,
            ApplicationOpenHelper.COLUMN_BATH_COMMENTS,
            ApplicationOpenHelper.COLUMN_TOILET_ENABLED,
            ApplicationOpenHelper.COLUMN_TOILET_GRID_DIMENSION_X,
            ApplicationOpenHelper.COLUMN_TOILET_GRID_DIMENSION_Y,
            ApplicationOpenHelper.COLUMN_TOILET_GRID_DIMENSION_ROUND,
            ApplicationOpenHelper.COLUMN_TOILET_AIRFLOW_WINDOWS_CLOSED,
            ApplicationOpenHelper.COLUMN_TOILET_AIRFLOW_WINDOWS_MICRO,
            ApplicationOpenHelper.COLUMN_TOILET_COMMENTS,
            ApplicationOpenHelper.COLUMN_FLUE_ENABLED,
            ApplicationOpenHelper.COLUMN_FLUE_AIRFLOW_WINDOWS_CLOSED,
            ApplicationOpenHelper.COLUMN_FLUE_AIRFLOW_WINDOWS_MICRO,
            ApplicationOpenHelper.COLUMN_FLUE_COMMENTS,
            ApplicationOpenHelper.COLUMN_GAS_FITTINGS_PRESENT,
            ApplicationOpenHelper.COLUMN_GAS_FITTINGS_WORKING,
            ApplicationOpenHelper.COLUMN_GAS_COOKER_PRESENT,
            ApplicationOpenHelper.COLUMN_GAS_COOKER_WORKING,
            ApplicationOpenHelper.COLUMN_BATHROOM_BAKE_PRESENT,
            ApplicationOpenHelper.COLUMN_BATHROOM_BAKE_WORKING,
            ApplicationOpenHelper.COLUMN_EQUIPMENT_COMMENTS,
            ApplicationOpenHelper.COLUMN_COMMENTS_FOR_USER,
            ApplicationOpenHelper.COLUMN_COMMENTS_FOR_MANAGER,
            ApplicationOpenHelper.COLUMN_CREATED,
            ApplicationOpenHelper.COLUMN_COMPANY_ADDRESS};

    public ProtocolDataSource(Context context) {
        applicationHelper = new ApplicationOpenHelper(context);
    }

    public void open() throws SQLException {
        database = applicationHelper.getWritableDatabase();
    }

    public void close() {
        applicationHelper.close();
    }

    //TODO extract to utility for handling whole db
    public void deleteProtocols(Address address){
        database.delete(ApplicationOpenHelper.TABLE_PROTOCOL_PADEREWSKIEGO,
                ApplicationOpenHelper.COLUMN_ADDRESS_ID + " = " + address.getId(), null);
        database.delete(ApplicationOpenHelper.TABLE_PROTOCOL_SIEMIANOWICE,
                ApplicationOpenHelper.COLUMN_ADDRESS_ID + " = " + address.getId(), null);
        database.delete(ApplicationOpenHelper.TABLE_PROTOCOL_NEW_PADEREWSKIEGO,
                ApplicationOpenHelper.COLUMN_ADDRESS_ID + " = " + address.getId(), null);
    }

    //TODO extract to utility for handling whole db
    public void deleteAllProtocols(){
        database.delete(ApplicationOpenHelper.TABLE_PROTOCOL_PADEREWSKIEGO,
                null, null);
        database.delete(ApplicationOpenHelper.TABLE_PROTOCOL_NEW_PADEREWSKIEGO,
                null, null);
        database.delete(ApplicationOpenHelper.TABLE_PROTOCOL_SIEMIANOWICE,
                null, null);
    }

    public Protocol insertProtocolSiemianowice(Protocol protocol) {

        ContentValues values = new ContentValues();
        values.put(applicationHelper.COLUMN_ADDRESS_ID, protocol.get_address_id());
        values.put(applicationHelper.COLUMN_WORKER_NAME, protocol.get_worker_name());
        values.put(applicationHelper.COLUMN_TEMP_OUTSIDE, protocol.get_temp_outside());
        values.put(applicationHelper.COLUMN_TEMP_INSIDE, protocol.get_temp_inside());
        values.put(applicationHelper.COLUMN_CO, protocol.get_co2());
        if(protocol.is_kitchen_enabled()){
            values.put(applicationHelper.COLUMN_KITCHEN_ENABLED, 1);
        } else {
            values.put(applicationHelper.COLUMN_KITCHEN_ENABLED, 0);
        }
        values.put(applicationHelper.COLUMN_KITCHEN_GRID_DIMENSION_X, protocol.get_kitchen_grid_dimension_x());
        values.put(applicationHelper.COLUMN_KITCHEN_GRID_DIMENSION_Y, protocol.get_kitchen_grid_dimension_y());
        values.put(applicationHelper.COLUMN_KITCHEN_GRID_DIMENSION_ROUND, protocol.get_kitchen_grid_dimension_round());
        values.put(applicationHelper.COLUMN_KITCHEN_AIRFLOW_WINDOWS_CLOSED, protocol.get_kitchen_airflow_windows_closed());
        values.put(applicationHelper.COLUMN_KITCHEN_AIRFLOW_WINDOWS_MICRO, protocol.get_kitchen_airflow_microventilation());
        values.put(applicationHelper.COLUMN_KITCHEN_COMMENTS, protocol.get_kitchen_comments());
        if(protocol.is_bathroom_enabled()){
            values.put(applicationHelper.COLUMN_BATH_ENABLED, 1);
        } else {
            values.put(applicationHelper.COLUMN_BATH_ENABLED, 0);
        }
        values.put(applicationHelper.COLUMN_BATH_GRID_DIMENSION_X, protocol.get_bathroom_grid_dimension_x());
        values.put(applicationHelper.COLUMN_BATH_GRID_DIMENSION_Y, protocol.get_bathroom_grid_dimension_y());
        values.put(applicationHelper.COLUMN_BATH_GRID_DIMENSION_ROUND, protocol.get_bathroom_grid_dimension_round());
        values.put(applicationHelper.COLUMN_BATH_AIRFLOW_WINDOWS_CLOSED, protocol.get_bathroom_airflow_windows_closed());
        values.put(applicationHelper.COLUMN_BATH_AIRFLOW_WINDOWS_MICRO, protocol.get_bathroom_airflow_microventilation());
        values.put(applicationHelper.COLUMN_BATH_COMMENTS, protocol.get_bathroom_comments());
        if(protocol.is_toilet_enabled()){
            values.put(applicationHelper.COLUMN_TOILET_ENABLED, 1);
        } else {
            values.put(applicationHelper.COLUMN_TOILET_ENABLED, 0);
        }
        values.put(applicationHelper.COLUMN_TOILET_GRID_DIMENSION_X, protocol.get_toilet_grid_dimension_x());
        values.put(applicationHelper.COLUMN_TOILET_GRID_DIMENSION_Y, protocol.get_toilet_grid_dimension_y());
        values.put(applicationHelper.COLUMN_TOILET_GRID_DIMENSION_ROUND, protocol.get_toilet_grid_dimension_round());
        values.put(applicationHelper.COLUMN_TOILET_AIRFLOW_WINDOWS_CLOSED, protocol.get_toilet_airflow_windows_closed());
        values.put(applicationHelper.COLUMN_TOILET_AIRFLOW_WINDOWS_MICRO, protocol.get_toilet_airflow_microventilation());
        values.put(applicationHelper.COLUMN_TOILET_COMMENTS, protocol.get_toilet_comments());
        if(protocol.is_flue_enabled()){
            values.put(applicationHelper.COLUMN_FLUE_ENABLED, 1);
        } else {
            values.put(applicationHelper.COLUMN_FLUE_ENABLED, 0);
        }
        values.put(applicationHelper.COLUMN_FLUE_AIRFLOW_WINDOWS_CLOSED, protocol.get_flue_airflow_windows_closed());
        values.put(applicationHelper.COLUMN_FLUE_AIRFLOW_WINDOWS_MICRO, protocol.get_flue_airflow_microventilation());
        values.put(applicationHelper.COLUMN_FLUE_COMMENTS, protocol.get_flue_comments());
        if(protocol.is_gas_fittings_present()){
            values.put(applicationHelper.COLUMN_GAS_FITTINGS_PRESENT, 1);
        } else {
            values.put(applicationHelper.COLUMN_GAS_FITTINGS_PRESENT, 0);
        }
        if(protocol.is_gas_fittings_working()){
            values.put(applicationHelper.COLUMN_GAS_FITTINGS_WORKING, 1);
        } else {
            values.put(applicationHelper.COLUMN_GAS_FITTINGS_WORKING, 0);
        }
        if(protocol.is_gas_cooker_present()){
            values.put(applicationHelper.COLUMN_GAS_COOKER_PRESENT, 1);
        } else {
            values.put(applicationHelper.COLUMN_GAS_COOKER_PRESENT, 0);
        }
        if(protocol.is_gas_cooker_working()){
            values.put(applicationHelper.COLUMN_GAS_COOKER_WORKING, 1);
        } else {
            values.put(applicationHelper.COLUMN_GAS_COOKER_WORKING, 0);
        }
        if(protocol.is_bathroom_bake_present()){
            values.put(applicationHelper.COLUMN_BATHROOM_BAKE_PRESENT, 1);
        } else {
            values.put(applicationHelper.COLUMN_BATHROOM_BAKE_PRESENT, 0);
        }
        if(protocol.is_bathroom_bake_working()){
            values.put(applicationHelper.COLUMN_BATHROOM_BAKE_WORKING, 1);
        } else {
            values.put(applicationHelper.COLUMN_BATHROOM_BAKE_WORKING, 0);
        }
        values.put(applicationHelper.COLUMN_EQUIPMENT_COMMENTS, protocol.get_equipment_comments());
        values.put(applicationHelper.COLUMN_COMMENTS_FOR_USER, protocol.get_comments_for_user());
        values.put(applicationHelper.COLUMN_COMMENTS_FOR_MANAGER, protocol.get_comments_for_manager());
        values.put(applicationHelper.COLUMN_CREATED, protocol.get_created());
        values.put(ApplicationOpenHelper.COLUMN_COMPANY_ADDRESS, protocol.getCompanyAddress());

        long insertId = database.insert(ApplicationOpenHelper.TABLE_PROTOCOL_SIEMIANOWICE, null,
                values);

        Cursor cursor = database.query(ApplicationOpenHelper.TABLE_PROTOCOL_SIEMIANOWICE,
                allColumns, ApplicationOpenHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Protocol newProtocol = cursorToProtocolShort(cursor);
        cursor.close();
        return newProtocol;
    }

    @Nullable
    public Protocol getLatestProtocol() {
        Cursor cursor = null;
        try {
            cursor = database.query(ApplicationOpenHelper.TABLE_PROTOCOL_SIEMIANOWICE,
                    allColumns, null, null,  null, null,
                    ApplicationOpenHelper.COLUMN_ID + " DESC LIMIT 1");
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursorToProtocol(cursor);
            } else {
                return null;
            }
        } finally {
            // Make sure to close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Nullable
    public Protocol getProtocolBefore(int latestProtocolId) {
        Cursor cursor = null;
        try {
            cursor = database.query(ApplicationOpenHelper.TABLE_PROTOCOL_SIEMIANOWICE,
                    allColumns, ApplicationOpenHelper.COLUMN_ID + " < " + latestProtocolId, null,  null, null,
                    ApplicationOpenHelper.COLUMN_ID + " DESC LIMIT 1");
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursorToProtocolShort(cursor);
            } else {
                return null;
            }
        } finally {
            // Make sure to close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public List<Protocol> getSiemanowiceProtocolsByCreationDate(@NonNull String date) {
        Cursor cursor = null;
        try {
            List<Protocol> protocols = new ArrayList<>();
            cursor = database.query(ApplicationOpenHelper.TABLE_PROTOCOL_SIEMIANOWICE,
                    allColumns, ApplicationOpenHelper.COLUMN_CREATED + " LIKE '" + date + "%'", null, null, null,  ApplicationOpenHelper.COLUMN_CREATED + " asc");
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Protocol protocol = cursorToProtocol(cursor);
                protocols.add(protocol);
                cursor.moveToNext();
            }
            return protocols;
        } finally {
            // Make sure to close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public List<Protocol> getAllSiemianowiceProtocolsByAddressId(int addressId) {
        List<Protocol> protocols = new ArrayList<Protocol>();
        Cursor cursor = database.query(ApplicationOpenHelper.TABLE_PROTOCOL_SIEMIANOWICE,
                allColumns, applicationHelper.COLUMN_ADDRESS_ID + " = " + addressId, null, null, null,  applicationHelper.COLUMN_CREATED + " desc");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Protocol protocol = cursorToProtocolShort(cursor);
            protocols.add(protocol);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return protocols;
    }

    public Protocol getSiemianowiceProtocolsById(int protocolId) {
        Protocol protocol;
        Cursor cursor = database.query(ApplicationOpenHelper.TABLE_PROTOCOL_SIEMIANOWICE,
                allColumns, ApplicationOpenHelper.COLUMN_ID + " = " + protocolId, null,
                null, null, null);

        cursor.moveToFirst();
        protocol = cursorToProtocol(cursor);
        cursor.close();
        return protocol;
    }

    private Protocol cursorToProtocolShort(Cursor cursor) {
        Protocol protocol = new Protocol();
        protocol.set_id(cursor.getInt(0));
        protocol.set_address_id(Integer.parseInt(cursor.getString(1)));
        protocol.set_created(cursor.getString(40));
        return protocol;
    }

    private Protocol cursorToProtocol(Cursor cursor) {
        Protocol protocol = new Protocol();
        protocol.set_id(cursor.getInt(0));
        protocol.set_address_id(Integer.parseInt(cursor.getString(1)));
        protocol.set_worker_name(cursor.getString(2));
        protocol.set_temp_outside(Float.parseFloat(cursor.getString(3)));
        protocol.set_temp_inside(Float.parseFloat(cursor.getString(4)));
        protocol.set_co2(Float.parseFloat(cursor.getString(5)));
        if(cursor.getInt(6) == 1){
            protocol.set_kitchen_enabled(true);
            protocol.set_kitchen_grid_dimension_x(cursor.getFloat(7));
            protocol.set_kitchen_grid_dimension_y(cursor.getFloat(8));
            protocol.set_kitchen_grid_dimension_round(cursor.getDouble(9));
            protocol.set_kitchen_airflow_windows_closed(cursor.getFloat(10));
            protocol.set_kitchen_airflow_microventilation(cursor.getFloat(11));
        } else {
            protocol.set_kitchen_enabled(false);
        }
        protocol.set_kitchen_comments(cursor.getString(12));
        if(cursor.getInt(13) == 1){
            protocol.set_bathroom_enabled(true);
            protocol.set_bathroom_grid_dimension_x(cursor.getFloat(14));
            protocol.set_bathroom_grid_dimension_y(cursor.getFloat(15));
            protocol.set_bathroom_grid_dimension_round(cursor.getDouble(16));
            protocol.set_bathroom_airflow_windows_closed(cursor.getFloat(17));
            protocol.set_bathroom_airflow_microventilation(cursor.getFloat(18));
        } else {
            protocol.set_bathroom_enabled(false);
        }
        protocol.set_bathroom_comments(cursor.getString(19));
        if(cursor.getInt(20) == 1){
            protocol.set_toilet_enabled(true);
            protocol.set_toilet_grid_dimension_x(cursor.getFloat(21));
            protocol.set_toilet_grid_dimension_y(cursor.getFloat(22));
            protocol.set_toilet_grid_dimension_round(cursor.getDouble(23));
            protocol.set_toilet_airflow_windows_closed(cursor.getFloat(24));
            protocol.set_toilet_airflow_microventilation(cursor.getFloat(25));
        } else {
            protocol.set_toilet_enabled(false);
        }
        protocol.set_toilet_comments(cursor.getString(26));
        if(cursor.getInt(27) == 1){
            protocol.set_flue_enabled(true);
            protocol.set_flue_airflow_windows_closed(cursor.getFloat(28));
            protocol.set_flue_airflow_microventilation(cursor.getFloat(29));
        } else {
            protocol.set_flue_enabled(false);
        }
        protocol.set_flue_comments(cursor.getString(30));
        if(cursor.getInt(31) == 1){
            protocol.set_gas_fittings_present(true);
            if(cursor.getInt(32) == 1){
                protocol.set_gas_fittings_working(true);
            } else {
                protocol.set_gas_fittings_working(false);
            }
        } else {
            protocol.set_gas_fittings_present(false);
        }
        if(cursor.getInt(33) == 1){
            protocol.set_gas_cooker_present(true);
            if(cursor.getInt(34) == 1){
                protocol.set_gas_cooker_working(true);
            } else {
                protocol.set_gas_cooker_working(false);
            }
        } else {
            protocol.set_gas_cooker_present(false);
        }
        if(cursor.getInt(35) == 1){
            protocol.set_bathroom_bake_present(true);
            if(cursor.getInt(36) == 1){
                protocol.set_bathroom_bake_working(true);
            } else {
                protocol.set_bathroom_bake_working(false);
            }
        } else {
            protocol.set_bathroom_bake_present(false);
        }
        protocol.set_equipment_comments(cursor.getString(37));
        protocol.set_comments_for_user(cursor.getString(38));
        protocol.set_comments_for_manager(cursor.getString(39));
        protocol.set_created(cursor.getString(cursor.getColumnIndex(ApplicationOpenHelper.COLUMN_CREATED)));
        protocol.setCompanyAddress(cursor.getString(cursor.getColumnIndex(ApplicationOpenHelper.COLUMN_COMPANY_ADDRESS)));
        return protocol;
    }
}