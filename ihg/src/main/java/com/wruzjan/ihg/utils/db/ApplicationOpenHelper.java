package com.wruzjan.ihg.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ApplicationOpenHelper extends SQLiteOpenHelper {

    //columns
    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "ighDB.db";
    public static final String TABLE_ADDRESSES = "addresses";
    public static final String TABLE_PROTOCOL_PADEREWSKIEGO = "protocols_paderewskiego";
    public static final String TABLE_PROTOCOL_NEW_PADEREWSKIEGO = "protocols_new_paderewskiego";
    public static final String TABLE_PROTOCOL_SIEMIANOWICE = "protocols_siemianowice";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ADDRESS_ID = "address_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_STREET = "street";
    public static final String COLUMN_BUILDING = "builidng";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_FLAT = "flat";
    public static final String COLUMN_DISTRICT = "district";
    public static final String COLUMN_WORKER_NAME = "worker_name";
    public static final String COLUMN_PHONE_NR = "phone_nr";
    public static final String COLUMN_TEMP_OUTSIDE = "temp_out";
    public static final String COLUMN_TEMP_INSIDE = "temp_in";
    public static final String COLUMN_WIND_SPEED = "wind_speed";
    public static final String COLUMN_WIND_DIRECTION = "wind_direction";
    public static final String COLUMN_PRESSURE = "pressure";
    public static final String COLUMN_WINDOWS_ALL = "windows_all";
    public static final String COLUMN_WINDOWS_MICRO = "windows_micro";
    public static final String COLUMN_WINDOWS_VENT = "windows_vent";
    public static final String COLUMN_WINDOWS_NO_MICRO = "windows_no_micro";
    public static final String COLUMN_CO = "co";
    public static final String COLUMN_COMMENTS = "comments";
    public static final String COLUMN_EQ_CH_GAS_METER_WORKING = "eq_ch_gas_meter_working";
    public static final String COLUMN_EQ_CH_STOVE_WORKING = "eq_ch_stove_working";
    public static final String COLUMN_EQ_CH_BAKE_WORKING = "eq_ch_bake_working";
    public static final String COLUMN_EQ_CH_COMBI_OVEN_WORKING = "eq_ch_combi_oven_working";
    public static final String COLUMN_EQ_CH_KITCHEN_TERM_WORKING = "eq_ch_kitchen_term_working";
    public static final String COLUMN_EQ_CH_OTHERS = "eq_ch_others";
    public static final String COLUMN_EQ_GAS_METER_WORKING = "eq_gas_meter_working";
    public static final String COLUMN_EQ_STOVE_WORKING = "eq_stove_working";
    public static final String COLUMN_EQ_BAKE_WORKING = "eq_bake_working";
    public static final String COLUMN_EQ_COMBI_OVEN_WORKING = "eq_combi_oven_working";
    public static final String COLUMN_EQ_KITCHEN_TERM_WORKING = "eq_kitchen_term_working";
    public static final String COLUMN_EQ_OTHERS = "eq_others";
    public static final String COLUMN_KITCHEN_ENABLED = "kitchen_enabled";
    public static final String COLUMN_KITCHEN_CLEANED = "kitchen_cleaned";
    public static final String COLUMN_KITCHEN_COMMENTS = "kitchen_comments";
    public static final String COLUMN_KITCHEN_HOOD = "kitchen_hood";
    public static final String COLUMN_KITCHEN_VENT = "kitchen_vent";
    public static final String COLUMN_KITCHEN_INACCESSIBLE = "kitchen_inaccessible";
    public static final String COLUMN_KITCHEN_STEADY = "kitchen_steady";
    public static final String COLUMN_KITCHEN_NOT_CLEANED = "kitchen_not_cleaned";
    public static final String COLUMN_KITCHEN_OTHERS = "kitchen_others";
    public static final String COLUMN_KITCHEN_OTHERS_COMMENTS = "kitchen_others_comments";
    public static final String COLUMN_KITCHEN_GRID_DIMENSION_X = "kitchen_grid_dimension_x";
    public static final String COLUMN_KITCHEN_GRID_DIMENSION_Y = "kitchen_grid_dimension_y";
    public static final String COLUMN_KITCHEN_GRID_DIMENSION_ROUND = "kitchen_grid_dimension_round";
    public static final String COLUMN_KITCHEN_AIRFLOW_WINDOWS_CLOSED = "kitchen_airflow_windows_closed";
    public static final String COLUMN_KITCHEN_AIRFLOW_WINDOWS_OPEN = "kitchen_airflow_windows_open";
    public static final String COLUMN_KITCHEN_AIRFLOW_WINDOWS_MICRO = "kitchen_airflow_microventilation";
    public static final String COLUMN_TOILET_ENABLED = "toilet_enabled";
    public static final String COLUMN_TOILET_CLEANED = "toilet_cleaned";
    public static final String COLUMN_TOILET_COMMENTS = "toilet_comments";
    public static final String COLUMN_TOILET_VENT = "toilet_vent";
    public static final String COLUMN_TOILET_INACCESSIBLE = "toilet_inaccessible";
    public static final String COLUMN_TOILET_STEADY = "toilet_steady";
    public static final String COLUMN_TOILET_NOT_CLEANED = "toilet_not_cleaned";
    public static final String COLUMN_TOILET_OTHERS = "toilet_others";
    public static final String COLUMN_TOILET_OTHERS_COMMENTS = "toilet_others_comments";
    public static final String COLUMN_TOILET_GRID_DIMENSION_X = "toilet_grid_dimension_x";
    public static final String COLUMN_TOILET_GRID_DIMENSION_Y = "toilet_grid_dimension_y";
    public static final String COLUMN_TOILET_GRID_DIMENSION_ROUND = "toilet_grid_dimension_round";
    public static final String COLUMN_TOILET_AIRFLOW_WINDOWS_CLOSED = "toilet_airflow_windows_closed";
    public static final String COLUMN_TOILET_AIRFLOW_WINDOWS_OPEN = "toilet_airflow_windows_open";
    public static final String COLUMN_TOILET_AIRFLOW_WINDOWS_MICRO = "toilet_airflow_microventilation";
    public static final String COLUMN_BATH_ENABLED = "bath_enabled";
    public static final String COLUMN_BATH_CLEANED = "bath_cleaned";
    public static final String COLUMN_BATH_COMMENTS = "bath_comments";
    public static final String COLUMN_BATH_VENT = "bath_vent";
    public static final String COLUMN_BATH_INACCESSIBLE = "bath_inaccessible";
    public static final String COLUMN_BATH_STEADY = "bath_steady";
    public static final String COLUMN_BATH_NOT_CLEANED = "bath_not_cleaned";
    public static final String COLUMN_BATH_OTHERS = "bath_others";
    public static final String COLUMN_BATH_OTHERS_COMMENTS = "bath_others_comments";
    public static final String COLUMN_BATH_GRID_DIMENSION_X = "bath_grid_dimension_x";
    public static final String COLUMN_BATH_GRID_DIMENSION_Y = "bath_grid_dimension_y";
    public static final String COLUMN_BATH_GRID_DIMENSION_ROUND = "bath_grid_dimension_round";
    public static final String COLUMN_BATH_AIRFLOW_WINDOWS_CLOSED = "bath_airflow_windows_closed";
    public static final String COLUMN_BATH_AIRFLOW_WINDOWS_OPEN = "bath_airflow_windows_open";
    public static final String COLUMN_BATH_AIRFLOW_WINDOWS_MICRO = "bath_airflow_microventilation";
    public static final String COLUMN_FLUE_ENABLED = "flue_enabled";
    public static final String COLUMN_FLUE_CLEANED = "flue_cleaned";
    public static final String COLUMN_FLUE_COMMENTS = "flue_comments";
    public static final String COLUMN_FLUE_BOILER = "flue_boiler";
    public static final String COLUMN_FLUE_INACCESSIBLE = "flue_inaccessible";
    public static final String COLUMN_FLUE_RIGID = "flue_rigid";
    public static final String COLUMN_FLUE_ALUFOL = "flue_alufol";
    public static final String COLUMN_FLUE_NOT_CLEANED = "flue_not_cleaned";
    public static final String COLUMN_FLUE_OTHERS = "flue_others";
    public static final String COLUMN_FLUE_OTHERS_COMMENTS = "flue_others_comments";
    public static final String COLUMN_FLUE_AIRFLOW_WINDOWS_CLOSED = "flue_airflow_windows_closed";
    public static final String COLUMN_FLUE_AIRFLOW_WINDOWS_OPEN = "flue_airflow_windows_open";
    public static final String COLUMN_FLUE_AIRFLOW_WINDOWS_MICRO = "flue_airflow_microventilation";
    public static final String COLUMN_COMMENTS_FOR_USER = "comments_for_user";
    public static final String COLUMN_COMMENTS_FOR_MANAGER = "comments_for_manager";
    public static final String COLUMN_GAS_FITTINGS_PRESENT = "gas_fittings_present";
    public static final String COLUMN_GAS_FITTINGS_WORKING = "gas_fittings_working";
    public static final String COLUMN_GAS_COOKER_PRESENT = "gas_cooker_present";
    public static final String COLUMN_GAS_COOKER_WORKING = "gas_cooker_working";
    public static final String COLUMN_BATHROOM_BAKE_PRESENT = "bathroom_bake_present";
    public static final String COLUMN_BATHROOM_BAKE_WORKING = "bathroom_bake_working";
    public static final String COLUMN_EQUIPMENT_COMMENTS = "equipment_comments";
    public static final String COLUMN_COMPANY_ADDRESS = "company_address";
    public static final String COLUMN_PROTOCOL_TYPE = "protocol_type";

    public static final String COLUMN_CREATED = "created";

    public ApplicationOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_ADDRESSES_TABLE = "CREATE TABLE " +
                TABLE_ADDRESSES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT NOT NULL," +
                COLUMN_STREET + " TEXT NOT NULL," +
                COLUMN_BUILDING + " TEXT NOT NULL," +
                COLUMN_CITY + " TEXT NOT NULL," +
                COLUMN_FLAT + " TEXT," +
                COLUMN_DISTRICT + " TEXT" +
                ")";

        String CREATE_PROTOCOL_PADEREWSKIEGO_TABLE = "CREATE TABLE " +
                TABLE_PROTOCOL_PADEREWSKIEGO + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ADDRESS_ID + " INTEGER NOT NULL, " +
                COLUMN_WORKER_NAME + " TEXT NOT NULL," +
                COLUMN_PHONE_NR + " TEXT," +
                COLUMN_TEMP_OUTSIDE + " TEXT," +
                COLUMN_TEMP_INSIDE + " TEXT," +
                COLUMN_WIND_SPEED + " TEXT," +
                COLUMN_WIND_DIRECTION + " TEXT," +
                COLUMN_PRESSURE + " TEXT," +
                COLUMN_WINDOWS_ALL + " TEXT," +
                COLUMN_WINDOWS_MICRO + " TEXT," +
                COLUMN_WINDOWS_VENT + " TEXT," +
                COLUMN_WINDOWS_NO_MICRO + " TEXT," +
                COLUMN_CO + " TEXT," +
                COLUMN_COMMENTS + " TEXT," +
                COLUMN_EQ_CH_GAS_METER_WORKING + " INTEGER, " +
                COLUMN_EQ_CH_STOVE_WORKING + " INTEGER, " +
                COLUMN_EQ_CH_BAKE_WORKING + " INTEGER, " +
                COLUMN_EQ_CH_COMBI_OVEN_WORKING + " INTEGER, " +
                COLUMN_EQ_CH_KITCHEN_TERM_WORKING + " INTEGER, " +
                COLUMN_EQ_CH_OTHERS + " INTEGER, " +
                COLUMN_EQ_GAS_METER_WORKING + " INTEGER, " +
                COLUMN_EQ_STOVE_WORKING + " INTEGER, " +
                COLUMN_EQ_BAKE_WORKING + " INTEGER, " +
                COLUMN_EQ_COMBI_OVEN_WORKING + " INTEGER, " +
                COLUMN_EQ_KITCHEN_TERM_WORKING + " INTEGER, " +
                COLUMN_EQ_OTHERS + " TEXT," +
                COLUMN_KITCHEN_ENABLED + " INTEGER, " +
                COLUMN_KITCHEN_CLEANED + " INTEGER, " +
                COLUMN_KITCHEN_HOOD + " INTEGER, " +
                COLUMN_KITCHEN_VENT + " INTEGER, " +
                COLUMN_KITCHEN_INACCESSIBLE + " INTEGER, " +
                COLUMN_KITCHEN_STEADY + " INTEGER, " +
                COLUMN_KITCHEN_NOT_CLEANED + " INTEGER, " +
                COLUMN_KITCHEN_OTHERS + " INTEGER, " +
                COLUMN_KITCHEN_OTHERS_COMMENTS + " TEXT," +
                COLUMN_KITCHEN_GRID_DIMENSION_X + " DOUBLE," +
                COLUMN_KITCHEN_GRID_DIMENSION_Y + " DOUBLE," +
                COLUMN_KITCHEN_GRID_DIMENSION_ROUND + " DOUBLE," +
                COLUMN_KITCHEN_AIRFLOW_WINDOWS_CLOSED + " DOUBLE," +
                COLUMN_KITCHEN_AIRFLOW_WINDOWS_OPEN + " DOUBLE," +
                COLUMN_KITCHEN_AIRFLOW_WINDOWS_MICRO + " DOUBLE," +
                COLUMN_TOILET_ENABLED + " INTEGER, " +
                COLUMN_TOILET_CLEANED + " INTEGER, " +
                COLUMN_TOILET_VENT + " INTEGER, " +
                COLUMN_TOILET_INACCESSIBLE + " INTEGER, " +
                COLUMN_TOILET_STEADY + " INTEGER, " +
                COLUMN_TOILET_NOT_CLEANED + " INTEGER, " +
                COLUMN_TOILET_OTHERS + " INTEGER, " +
                COLUMN_TOILET_OTHERS_COMMENTS + " TEXT," +
                COLUMN_TOILET_GRID_DIMENSION_X + " DOUBLE," +
                COLUMN_TOILET_GRID_DIMENSION_Y + " DOUBLE," +
                COLUMN_TOILET_GRID_DIMENSION_ROUND + " DOUBLE," +
                COLUMN_TOILET_AIRFLOW_WINDOWS_CLOSED + " DOUBLE," +
                COLUMN_TOILET_AIRFLOW_WINDOWS_OPEN + " DOUBLE," +
                COLUMN_TOILET_AIRFLOW_WINDOWS_MICRO + " DOUBLE," +
                COLUMN_BATH_ENABLED + " INTEGER, " +
                COLUMN_BATH_CLEANED + " INTEGER, " +
                COLUMN_BATH_VENT + " INTEGER, " +
                COLUMN_BATH_INACCESSIBLE + " INTEGER, " +
                COLUMN_BATH_STEADY + " INTEGER, " +
                COLUMN_BATH_NOT_CLEANED + " INTEGER, " +
                COLUMN_BATH_OTHERS + " INTEGER, " +
                COLUMN_BATH_OTHERS_COMMENTS + " TEXT," +
                COLUMN_BATH_GRID_DIMENSION_X + " DOUBLE," +
                COLUMN_BATH_GRID_DIMENSION_Y + " DOUBLE," +
                COLUMN_BATH_GRID_DIMENSION_ROUND + " DOUBLE," +
                COLUMN_BATH_AIRFLOW_WINDOWS_CLOSED + " DOUBLE," +
                COLUMN_BATH_AIRFLOW_WINDOWS_OPEN + " DOUBLE," +
                COLUMN_BATH_AIRFLOW_WINDOWS_MICRO + " DOUBLE," +
                COLUMN_FLUE_ENABLED + " INTEGER, " +
                COLUMN_FLUE_CLEANED + " INTEGER, " +
                COLUMN_FLUE_BOILER + " INTEGER, " +
                COLUMN_FLUE_INACCESSIBLE + " INTEGER, " +
                COLUMN_FLUE_RIGID + " INTEGER, " +
                COLUMN_FLUE_ALUFOL + " INTEGER, " +
                COLUMN_FLUE_NOT_CLEANED + " INTEGER, " +
                COLUMN_FLUE_OTHERS + " INTEGER, " +
                COLUMN_FLUE_OTHERS_COMMENTS + " TEXT," +
                COLUMN_FLUE_AIRFLOW_WINDOWS_CLOSED + " DOUBLE," +
                COLUMN_FLUE_AIRFLOW_WINDOWS_OPEN + " DOUBLE," +
                COLUMN_FLUE_AIRFLOW_WINDOWS_MICRO + " DOUBLE," +
                COLUMN_COMMENTS_FOR_USER + " TEXT," +
                COLUMN_CREATED + " DATE default CURRENT_DATE," +
                "FOREIGN KEY ("+COLUMN_ADDRESS_ID+") REFERENCES "+TABLE_ADDRESSES+" ("+COLUMN_ID+"))";

        String CREATE_PROTOCOL_SIEMIANOWICE_TABLE = "CREATE TABLE " +
                TABLE_PROTOCOL_SIEMIANOWICE + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ADDRESS_ID + " INTEGER NOT NULL, " +
                COLUMN_WORKER_NAME + " TEXT NOT NULL," +
                COLUMN_TEMP_OUTSIDE + " TEXT," +
                COLUMN_TEMP_INSIDE + " TEXT," +
                COLUMN_CO + " TEXT," +
                COLUMN_KITCHEN_ENABLED + " INTEGER, " +
                COLUMN_KITCHEN_GRID_DIMENSION_X + " FLOAT," +
                COLUMN_KITCHEN_GRID_DIMENSION_Y + " FLOAT," +
                COLUMN_KITCHEN_GRID_DIMENSION_ROUND + " DOUBLE," +
                COLUMN_KITCHEN_AIRFLOW_WINDOWS_CLOSED + " FLOAT," +
                COLUMN_KITCHEN_AIRFLOW_WINDOWS_MICRO + " FLOAT," +
                COLUMN_KITCHEN_COMMENTS + " TEXT," +
                COLUMN_BATH_ENABLED + " INTEGER, " +
                COLUMN_BATH_GRID_DIMENSION_X + " FLOAT," +
                COLUMN_BATH_GRID_DIMENSION_Y + " FLOAT," +
                COLUMN_BATH_GRID_DIMENSION_ROUND + " DOUBLE," +
                COLUMN_BATH_AIRFLOW_WINDOWS_CLOSED + " FLOAT," +
                COLUMN_BATH_AIRFLOW_WINDOWS_MICRO + " FLOAT," +
                COLUMN_BATH_COMMENTS + " TEXT," +
                COLUMN_TOILET_ENABLED + " INTEGER, " +
                COLUMN_TOILET_GRID_DIMENSION_X + " FLOAT," +
                COLUMN_TOILET_GRID_DIMENSION_Y + " FLOAT," +
                COLUMN_TOILET_GRID_DIMENSION_ROUND + " DOUBLE," +
                COLUMN_TOILET_AIRFLOW_WINDOWS_CLOSED + " FLOAT," +
                COLUMN_TOILET_AIRFLOW_WINDOWS_MICRO + " FLOAT," +
                COLUMN_TOILET_COMMENTS + " TEXT," +
                COLUMN_FLUE_ENABLED + " INTEGER, " +
                COLUMN_FLUE_AIRFLOW_WINDOWS_CLOSED + " FLOAT," +
                COLUMN_FLUE_AIRFLOW_WINDOWS_MICRO + " FLOAT," +
                COLUMN_FLUE_COMMENTS + " TEXT," +
                COLUMN_GAS_FITTINGS_PRESENT + " INTEGER, " +
                COLUMN_GAS_FITTINGS_WORKING + " INTEGER, " +
                COLUMN_GAS_COOKER_PRESENT + " INTEGER, " +
                COLUMN_GAS_COOKER_WORKING + " INTEGER, " +
                COLUMN_BATHROOM_BAKE_PRESENT + " INTEGER, " +
                COLUMN_BATHROOM_BAKE_WORKING + " INTEGER, " +
                COLUMN_EQUIPMENT_COMMENTS + " TEXT," +
                COLUMN_COMMENTS_FOR_USER + " TEXT," +
                COLUMN_COMMENTS_FOR_MANAGER + " TEXT," +
                COLUMN_CREATED + " DATE default CURRENT_DATE," +
                COLUMN_COMPANY_ADDRESS + " TEXT," +
                COLUMN_PROTOCOL_TYPE + " TEXT," +
                "FOREIGN KEY ("+COLUMN_ADDRESS_ID+") REFERENCES "+TABLE_ADDRESSES+" ("+COLUMN_ID+"))";

        String CREATE_PROTOCOL_NEW_PADEREWSKIEGO_TABLE = "CREATE TABLE " +
                TABLE_PROTOCOL_NEW_PADEREWSKIEGO + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ADDRESS_ID + " INTEGER NOT NULL, " +
                COLUMN_WORKER_NAME + " TEXT NOT NULL," +
                COLUMN_TEMP_OUTSIDE + " TEXT," +
                COLUMN_TEMP_INSIDE + " TEXT," +
                COLUMN_CO + " TEXT," +
                COLUMN_KITCHEN_ENABLED + " INTEGER, " +
                COLUMN_KITCHEN_GRID_DIMENSION_X + " FLOAT," +
                COLUMN_KITCHEN_GRID_DIMENSION_Y + " FLOAT," +
                COLUMN_KITCHEN_GRID_DIMENSION_ROUND + " DOUBLE," +
                COLUMN_KITCHEN_AIRFLOW_WINDOWS_CLOSED + " FLOAT," +
                COLUMN_KITCHEN_AIRFLOW_WINDOWS_MICRO + " FLOAT," +
                COLUMN_KITCHEN_COMMENTS + " TEXT," +
                COLUMN_BATH_ENABLED + " INTEGER, " +
                COLUMN_BATH_GRID_DIMENSION_X + " FLOAT," +
                COLUMN_BATH_GRID_DIMENSION_Y + " FLOAT," +
                COLUMN_BATH_GRID_DIMENSION_ROUND + " DOUBLE," +
                COLUMN_BATH_AIRFLOW_WINDOWS_CLOSED + " FLOAT," +
                COLUMN_BATH_AIRFLOW_WINDOWS_MICRO + " FLOAT," +
                COLUMN_BATH_COMMENTS + " TEXT," +
                COLUMN_TOILET_ENABLED + " INTEGER, " +
                COLUMN_TOILET_GRID_DIMENSION_X + " FLOAT," +
                COLUMN_TOILET_GRID_DIMENSION_Y + " FLOAT," +
                COLUMN_TOILET_GRID_DIMENSION_ROUND + " DOUBLE," +
                COLUMN_TOILET_AIRFLOW_WINDOWS_CLOSED + " FLOAT," +
                COLUMN_TOILET_AIRFLOW_WINDOWS_MICRO + " FLOAT," +
                COLUMN_TOILET_COMMENTS + " TEXT," +
                COLUMN_FLUE_ENABLED + " INTEGER, " +
                COLUMN_FLUE_AIRFLOW_WINDOWS_CLOSED + " FLOAT," +
                COLUMN_FLUE_AIRFLOW_WINDOWS_MICRO + " FLOAT," +
                COLUMN_FLUE_COMMENTS + " TEXT," +
                COLUMN_GAS_FITTINGS_PRESENT + " INTEGER, " +
                COLUMN_GAS_FITTINGS_WORKING + " INTEGER, " +
                COLUMN_GAS_COOKER_PRESENT + " INTEGER, " +
                COLUMN_GAS_COOKER_WORKING + " INTEGER, " +
                COLUMN_BATHROOM_BAKE_PRESENT + " INTEGER, " +
                COLUMN_BATHROOM_BAKE_WORKING + " INTEGER, " +
                COLUMN_EQUIPMENT_COMMENTS + " TEXT," +
                COLUMN_COMMENTS_FOR_USER + " TEXT," +
                COLUMN_COMMENTS_FOR_MANAGER + " TEXT," +
                COLUMN_CREATED + " DATE default CURRENT_DATE," +
                COLUMN_PHONE_NR + " TEXT," +
                COLUMN_KITCHEN_CLEANED + " TEXT," +
                COLUMN_BATH_CLEANED + " TEXT," +
                COLUMN_TOILET_CLEANED + " TEXT," +
                COLUMN_FLUE_CLEANED + " TEXT," +
                COLUMN_COMPANY_ADDRESS + " TEXT," +
                COLUMN_PROTOCOL_TYPE + " TEXT," +
                "FOREIGN KEY ("+COLUMN_ADDRESS_ID+") REFERENCES "+TABLE_ADDRESSES+" ("+COLUMN_ID+"))";

        sqLiteDatabase.execSQL(CREATE_ADDRESSES_TABLE);
        sqLiteDatabase.execSQL(CREATE_PROTOCOL_PADEREWSKIEGO_TABLE);
        sqLiteDatabase.execSQL(CREATE_PROTOCOL_NEW_PADEREWSKIEGO_TABLE);
        sqLiteDatabase.execSQL(CREATE_PROTOCOL_SIEMIANOWICE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d("IHG_DEBUG",
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion);

        if (oldVersion < 5) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PROTOCOL_NEW_PADEREWSKIEGO);
            onUpgradeTo5(sqLiteDatabase);
        }
        if (oldVersion < 6) {
            onUpgradeTo6(sqLiteDatabase);
        }
        if (oldVersion < 7) {
            onUpgradeTo7(sqLiteDatabase);
        }
    }

    private void onUpgradeTo5(SQLiteDatabase sqLiteDatabase) {

        String CREATE_PROTOCOL_NEW_PADEREWSKIEGO_TABLE = "CREATE TABLE " +
                TABLE_PROTOCOL_NEW_PADEREWSKIEGO + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ADDRESS_ID + " INTEGER NOT NULL, " +
                COLUMN_WORKER_NAME + " TEXT NOT NULL," +
                COLUMN_TEMP_OUTSIDE + " TEXT," +
                COLUMN_TEMP_INSIDE + " TEXT," +
                COLUMN_CO + " TEXT," +
                COLUMN_KITCHEN_ENABLED + " INTEGER, " +
                COLUMN_KITCHEN_GRID_DIMENSION_X + " FLOAT," +
                COLUMN_KITCHEN_GRID_DIMENSION_Y + " FLOAT," +
                COLUMN_KITCHEN_GRID_DIMENSION_ROUND + " DOUBLE," +
                COLUMN_KITCHEN_AIRFLOW_WINDOWS_CLOSED + " FLOAT," +
                COLUMN_KITCHEN_AIRFLOW_WINDOWS_MICRO + " FLOAT," +
                COLUMN_KITCHEN_COMMENTS + " TEXT," +
                COLUMN_BATH_ENABLED + " INTEGER, " +
                COLUMN_BATH_GRID_DIMENSION_X + " FLOAT," +
                COLUMN_BATH_GRID_DIMENSION_Y + " FLOAT," +
                COLUMN_BATH_GRID_DIMENSION_ROUND + " DOUBLE," +
                COLUMN_BATH_AIRFLOW_WINDOWS_CLOSED + " FLOAT," +
                COLUMN_BATH_AIRFLOW_WINDOWS_MICRO + " FLOAT," +
                COLUMN_BATH_COMMENTS + " TEXT," +
                COLUMN_TOILET_ENABLED + " INTEGER, " +
                COLUMN_TOILET_GRID_DIMENSION_X + " FLOAT," +
                COLUMN_TOILET_GRID_DIMENSION_Y + " FLOAT," +
                COLUMN_TOILET_GRID_DIMENSION_ROUND + " DOUBLE," +
                COLUMN_TOILET_AIRFLOW_WINDOWS_CLOSED + " FLOAT," +
                COLUMN_TOILET_AIRFLOW_WINDOWS_MICRO + " FLOAT," +
                COLUMN_TOILET_COMMENTS + " TEXT," +
                COLUMN_FLUE_ENABLED + " INTEGER, " +
                COLUMN_FLUE_AIRFLOW_WINDOWS_CLOSED + " FLOAT," +
                COLUMN_FLUE_AIRFLOW_WINDOWS_MICRO + " FLOAT," +
                COLUMN_FLUE_COMMENTS + " TEXT," +
                COLUMN_GAS_FITTINGS_PRESENT + " INTEGER, " +
                COLUMN_GAS_FITTINGS_WORKING + " INTEGER, " +
                COLUMN_GAS_COOKER_PRESENT + " INTEGER, " +
                COLUMN_GAS_COOKER_WORKING + " INTEGER, " +
                COLUMN_BATHROOM_BAKE_PRESENT + " INTEGER, " +
                COLUMN_BATHROOM_BAKE_WORKING + " INTEGER, " +
                COLUMN_EQUIPMENT_COMMENTS + " TEXT," +
                COLUMN_COMMENTS_FOR_USER + " TEXT," +
                COLUMN_COMMENTS_FOR_MANAGER + " TEXT," +
                COLUMN_CREATED + " DATE default CURRENT_DATE," +
                COLUMN_PHONE_NR + " TEXT," +
                COLUMN_KITCHEN_CLEANED + " TEXT," +
                COLUMN_BATH_CLEANED + " TEXT," +
                COLUMN_TOILET_CLEANED + " TEXT," +
                COLUMN_FLUE_CLEANED + " TEXT," +
                "FOREIGN KEY ("+COLUMN_ADDRESS_ID+") REFERENCES "+TABLE_ADDRESSES+" ("+COLUMN_ID+"))";

        sqLiteDatabase.execSQL(CREATE_PROTOCOL_NEW_PADEREWSKIEGO_TABLE);
    }


    private void onUpgradeTo6(SQLiteDatabase sqLiteDatabase) {
        String addCompanyAddressColumnSiemanowice = "ALTER TABLE " + TABLE_PROTOCOL_SIEMIANOWICE + " ADD " + COLUMN_COMPANY_ADDRESS + " TEXT;";
        sqLiteDatabase.execSQL(addCompanyAddressColumnSiemanowice);

        String addCompanyAddressColumnNewPaderewskiego = "ALTER TABLE " + TABLE_PROTOCOL_NEW_PADEREWSKIEGO + " ADD " + COLUMN_COMPANY_ADDRESS + " TEXT;";
        sqLiteDatabase.execSQL(addCompanyAddressColumnNewPaderewskiego);
    }

    private void onUpgradeTo7(SQLiteDatabase sqLiteDatabase) {
        String addProtocolTypeColumnSiemanowice = "ALTER TABLE " + TABLE_PROTOCOL_SIEMIANOWICE + " ADD " + COLUMN_PROTOCOL_TYPE + " TEXT;";
        sqLiteDatabase.execSQL(addProtocolTypeColumnSiemanowice);

        String addProtocolTypeColumnNewPaderewskiego = "ALTER TABLE " + TABLE_PROTOCOL_NEW_PADEREWSKIEGO + " ADD " + COLUMN_PROTOCOL_TYPE + " TEXT;";
        sqLiteDatabase.execSQL(addProtocolTypeColumnNewPaderewskiego);
    }
}
