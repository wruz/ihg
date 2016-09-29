package com.wruzjan.ihg.utils.model;

import java.util.Date;

public class ProtocolPaderewskiego {

    private int _id;
    private int _address_id;
    private int _worker_id;
    private String _worker_name;
    private String _created;

    private int _version;
    private String _telephone;
    private double _temp_outside;
    private double _temp_inside;
    private double _wind_speed;
    private String _wind_direction;
    private double _pressure;
    private int _windows_all;
    private int _windows_micro;
    private int _windows_vent;
    private int _windows_no_micro;

    private double _co2;
    private String _comments;

    private boolean _eq_ch_gas_meter_working;
    private boolean _eq_ch_stove_working;
    private boolean _eq_ch_bake_working;
    private boolean _eq_ch_combi_oven_working;
    private boolean _eq_ch_kitchen_term_working;
    private boolean _eq_ch_others;
    private boolean _eq_gas_meter_working;
    private boolean _eq_stove_working;
    private boolean _eq_bake_working;
    private boolean _eq_combi_oven_working;
    private boolean _eq_kitchen_term_working;
    private String _eq_other;

    private boolean _kitchen_enabled;
    private boolean _kitchen_cleaned;
    private boolean _kitchen_hood;
    private boolean _kitchen_vent;
    private boolean _kitchen_inaccessible;
    private boolean _kitchen_steady;
    private boolean _kitchen_not_cleaned;
    private boolean _kitchen_others;
    private String _kitchen_others_comments;
    private double _kitchen_grid_dimension_x;
    private double _kitchen_grid_dimension_y;
    private double _kitchen_grid_dimension_round;
    private double _kitchen_airflow_windows_closed;
    private double _kitchen_airflow_windows_open;
    private double _kitchen_airflow_microventilation;

    private boolean _toilet_enabled;
    private boolean _toilet_cleaned;
    private boolean _toilet_vent;
    private boolean _toilet_inaccessible;
    private boolean _toilet_steady;
    private boolean _toilet_not_cleaned;
    private boolean _toilet_others;
    private String _toilet_others_comments;
    private double _toilet_grid_dimension_x;
    private double _toilet_grid_dimension_y;
    private double _toilet_grid_dimension_round;
    private double _toilet_airflow_windows_closed;
    private double _toilet_airflow_windows_open;
    private double _toilet_airflow_microventilation;

    private boolean _bath_enabled;
    private boolean _bath_cleaned;
    private boolean _bath_vent;
    private boolean _bath_inaccessible;
    private boolean _bath_steady;
    private boolean _bath_not_cleaned;
    private boolean _bath_others;
    private String _bath_others_comments;
    private double _bath_grid_dimension_x;
    private double _bath_grid_dimension_y;
    private double _bath_grid_dimension_round;
    private double _bath_airflow_windows_closed;
    private double _bath_airflow_windows_open;
    private double _bath_airflow_microventilation;

    private boolean _flue_enabled;
    private boolean _flue_cleaned;
    private boolean _flue_boiler;
    private boolean _flue_inaccessible;
    private boolean _flue_rigid;
    private boolean _flue_alufol;
    private boolean _flue_not_cleaned;
    private boolean _flue_others;
    private String _flue_others_comments;
    private double _flue_airflow_windows_closed;
    private double _flue_airflow_windows_open;
    private double _flue_airflow_microventilation;

    private String _comments_for_user;

    public ProtocolPaderewskiego(){
    }

    public String get_created() {
        return _created;
    }

    public void set_created(String created) {
        this._created = created;
    }

    public boolean is_eq_ch_gas_meter_working() {
        return _eq_ch_gas_meter_working;
    }

    public void set_eq_ch_gas_meter_working(boolean _eq_ch_gas_meter_working) {
        this._eq_ch_gas_meter_working = _eq_ch_gas_meter_working;
    }

    public boolean is_eq_ch_stove_working() {
        return _eq_ch_stove_working;
    }

    public void set_eq_ch_stove_working(boolean _eq_ch_stove_working) {
        this._eq_ch_stove_working = _eq_ch_stove_working;
    }

    public boolean is_eq_ch_bake_working() {
        return _eq_ch_bake_working;
    }

    public void set_eq_ch_bake_working(boolean _eq_ch_bake_working) {
        this._eq_ch_bake_working = _eq_ch_bake_working;
    }

    public boolean is_eq_ch_combi_oven_working() {
        return _eq_ch_combi_oven_working;
    }

    public void set_eq_ch_combi_oven_working(boolean _eq_ch_combi_oven_working) {
        this._eq_ch_combi_oven_working = _eq_ch_combi_oven_working;
    }

    public boolean is_eq_ch_kitchen_term_working() {
        return _eq_ch_kitchen_term_working;
    }

    public void set_eq_ch_kitchen_term_working(boolean _eq_ch_kitchen_term_working) {
        this._eq_ch_kitchen_term_working = _eq_ch_kitchen_term_working;
    }

    public boolean is_eq_ch_others() {
        return _eq_ch_others;
    }

    public void set_eq_ch_others(boolean _eq_ch_others) {
        this._eq_ch_others = _eq_ch_others;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_address_id() {
        return _address_id;
    }

    public void set_address_id(int _address_id) {
        this._address_id = _address_id;
    }

    public int get_worker_id() {
        return _worker_id;
    }

    public void set_worker_id(int _worker_id) {
        this._worker_id = _worker_id;
    }

    public String get_worker_name() {
        return _worker_name;
    }

    public void set_worker_name(String _worker_name) {
        this._worker_name = _worker_name;
    }

    public int get_version() {
        return _version;
    }

    public void set_version(int _version) {
        this._version = _version;
    }

    public String get_telephone() {
        return _telephone;
    }

    public void set_telephone(String _telephone) {
        this._telephone = _telephone;
    }

    public double get_temp_outside() {
        return _temp_outside;
    }

    public void set_temp_outside(double _temp_outside) {
        this._temp_outside = _temp_outside;
    }

    public double get_temp_inside() {
        return _temp_inside;
    }

    public void set_temp_inside(double _temp_inside) {
        this._temp_inside = _temp_inside;
    }

    public double get_wind_speed() {
        return _wind_speed;
    }

    public void set_wind_speed(double _wind_speed) {
        this._wind_speed = _wind_speed;
    }

    public String get_wind_direction() {
        return _wind_direction;
    }

    public void set_wind_direction(String _wind_direction) {
        this._wind_direction = _wind_direction;
    }

    public double get_pressure() {
        return _pressure;
    }

    public void set_pressure(double _pressure) {
        this._pressure = _pressure;
    }

    public int get_windows_all() {
        return _windows_all;
    }

    public void set_windows_all(int _windows_all) {
        this._windows_all = _windows_all;
    }

    public int get_windows_micro() {
        return _windows_micro;
    }

    public void set_windows_micro(int _windows_micro) {
        this._windows_micro = _windows_micro;
    }

    public int get_windows_vent() {
        return _windows_vent;
    }

    public void set_windows_vent(int _windows_vent) {
        this._windows_vent = _windows_vent;
    }

    public int get_windows_no_micro() {
        return _windows_no_micro;
    }

    public void set_windows_no_micro(int _windows_no_micro) {
        this._windows_no_micro = _windows_no_micro;
    }

    public double get_co2() {
        return _co2;
    }

    public void set_co2(double _co2) {
        this._co2 = _co2;
    }

    public String get_comments() {
        return _comments;
    }

    public void set_comments(String _comments) {
        this._comments = _comments;
    }

    public boolean is_eq_gas_meter_working() {
        return _eq_gas_meter_working;
    }

    public void set_eq_gas_meter_working(boolean _eq_gas_meter_working) {
        this._eq_gas_meter_working = _eq_gas_meter_working;
    }

    public boolean is_eq_stove_working() {
        return _eq_stove_working;
    }

    public void set_eq_stove_working(boolean _eq_stove_working) {
        this._eq_stove_working = _eq_stove_working;
    }

    public boolean is_eq_bake_working() {
        return _eq_bake_working;
    }

    public void set_eq_bake_working(boolean _eq_bake_working) {
        this._eq_bake_working = _eq_bake_working;
    }

    public boolean is_eq_combi_oven_working() {
        return _eq_combi_oven_working;
    }

    public void set_eq_combi_oven_working(boolean _eq_combi_oven_working) {
        this._eq_combi_oven_working = _eq_combi_oven_working;
    }

    public boolean is_eq_kitchen_term_working() {
        return _eq_kitchen_term_working;
    }

    public void set_eq_kitchen_term_working(boolean _eq_kitchen_term_working) {
        this._eq_kitchen_term_working = _eq_kitchen_term_working;
    }

    public String get_eq_other() {
        return _eq_other;
    }

    public void set_eq_other(String _eq_other) {
        this._eq_other = _eq_other;
    }

    public boolean is_kitchen_enabled() {
        return _kitchen_enabled;
    }

    public void set_kitchen_enabled(boolean _kitchen_enabled) {
        this._kitchen_enabled = _kitchen_enabled;
    }

    public boolean is_kitchen_cleaned() {
        return _kitchen_cleaned;
    }

    public void set_kitchen_cleaned(boolean _kitchen_cleaned) {
        this._kitchen_cleaned = _kitchen_cleaned;
    }

    public boolean is_kitchen_hood() {
        return _kitchen_hood;
    }

    public void set_kitchen_hood(boolean _kitchen_hood) {
        this._kitchen_hood = _kitchen_hood;
    }

    public boolean is_kitchen_vent() {
        return _kitchen_vent;
    }

    public void set_kitchen_vent(boolean _kitchen_vent) {
        this._kitchen_vent = _kitchen_vent;
    }

    public boolean is_kitchen_inaccessible() {
        return _kitchen_inaccessible;
    }

    public void set_kitchen_inaccessible(boolean _kitchen_inaccessible) {
        this._kitchen_inaccessible = _kitchen_inaccessible;
    }

    public boolean is_kitchen_steady() {
        return _kitchen_steady;
    }

    public void set_kitchen_steady(boolean _kitchen_steady) {
        this._kitchen_steady = _kitchen_steady;
    }

    public boolean is_kitchen_not_cleaned() {
        return _kitchen_not_cleaned;
    }

    public void set_kitchen_not_cleaned(boolean _kitchen_not_cleaned) {
        this._kitchen_not_cleaned = _kitchen_not_cleaned;
    }

    public boolean is_kitchen_others() {
        return _kitchen_others;
    }

    public void set_kitchen_others(boolean _kitchen_others) {
        this._kitchen_others = _kitchen_others;
    }

    public String get_kitchen_others_comments() {
        return _kitchen_others_comments;
    }

    public void set_kitchen_others_comments(String _kitchen_others_comments) {
        this._kitchen_others_comments = _kitchen_others_comments;
    }

    public double get_kitchen_grid_dimension_x() {
        return _kitchen_grid_dimension_x;
    }

    public void set_kitchen_grid_dimension_x(double _kitchen_grid_dimension_x) {
        this._kitchen_grid_dimension_x = _kitchen_grid_dimension_x;
    }

    public double get_kitchen_grid_dimension_y() {
        return _kitchen_grid_dimension_y;
    }

    public void set_kitchen_grid_dimension_y(double _kitchen_grid_dimension_y) {
        this._kitchen_grid_dimension_y = _kitchen_grid_dimension_y;
    }

    public double get_kitchen_grid_dimension_round() {
        return _kitchen_grid_dimension_round;
    }

    public void set_kitchen_grid_dimension_round(double _kitchen_grid_dimension_round) {
        this._kitchen_grid_dimension_round = _kitchen_grid_dimension_round;
    }

    public double get_kitchen_airflow_windows_closed() {
        return _kitchen_airflow_windows_closed;
    }

    public void set_kitchen_airflow_windows_closed(double _kitchen_airflow_windows_closed) {
        this._kitchen_airflow_windows_closed = _kitchen_airflow_windows_closed;
    }

    public double get_kitchen_airflow_windows_open() {
        return _kitchen_airflow_windows_open;
    }

    public void set_kitchen_airflow_windows_open(double _kitchen_airflow_windows_open) {
        this._kitchen_airflow_windows_open = _kitchen_airflow_windows_open;
    }

    public double get_kitchen_airflow_microventilation() {
        return _kitchen_airflow_microventilation;
    }

    public void set_kitchen_airflow_microventilation(double _kitchen_airflow_microventilation) {
        this._kitchen_airflow_microventilation = _kitchen_airflow_microventilation;
    }

    public boolean is_toilet_enabled() {
        return _toilet_enabled;
    }

    public void set_toilet_enabled(boolean _toilet_enabled) {
        this._toilet_enabled = _toilet_enabled;
    }

    public boolean is_toilet_cleaned() {
        return _toilet_cleaned;
    }

    public void set_toilet_cleaned(boolean _toilet_cleaned) {
        this._toilet_cleaned = _toilet_cleaned;
    }

    public boolean is_toilet_vent() {
        return _toilet_vent;
    }

    public void set_toilet_vent(boolean _toilet_vent) {
        this._toilet_vent = _toilet_vent;
    }

    public boolean is_toilet_inaccessible() {
        return _toilet_inaccessible;
    }

    public void set_toilet_inaccessible(boolean _toilet_inaccessible) {
        this._toilet_inaccessible = _toilet_inaccessible;
    }

    public boolean is_toilet_steady() {
        return _toilet_steady;
    }

    public void set_toilet_steady(boolean _toilet_steady) {
        this._toilet_steady = _toilet_steady;
    }

    public boolean is_toilet_not_cleaned() {
        return _toilet_not_cleaned;
    }

    public void set_toilet_not_cleaned(boolean _toilet_not_cleaned) {
        this._toilet_not_cleaned = _toilet_not_cleaned;
    }

    public boolean is_toilet_others() {
        return _toilet_others;
    }

    public void set_toilet_others(boolean _toilet_others) {
        this._toilet_others = _toilet_others;
    }

    public String get_toilet_others_comments() {
        return _toilet_others_comments;
    }

    public void set_toilet_others_comments(String _toilet_others_comments) {
        this._toilet_others_comments = _toilet_others_comments;
    }

    public double get_toilet_grid_dimension_x() {
        return _toilet_grid_dimension_x;
    }

    public void set_toilet_grid_dimension_x(double _toilet_grid_dimension_x) {
        this._toilet_grid_dimension_x = _toilet_grid_dimension_x;
    }

    public double get_toilet_grid_dimension_y() {
        return _toilet_grid_dimension_y;
    }

    public void set_toilet_grid_dimension_y(double _toilet_grid_dimension_y) {
        this._toilet_grid_dimension_y = _toilet_grid_dimension_y;
    }

    public double get_toilet_grid_dimension_round() {
        return _toilet_grid_dimension_round;
    }

    public void set_toilet_grid_dimension_round(double _toilet_grid_dimension_round) {
        this._toilet_grid_dimension_round = _toilet_grid_dimension_round;
    }

    public double get_toilet_airflow_windows_closed() {
        return _toilet_airflow_windows_closed;
    }

    public void set_toilet_airflow_windows_closed(double _toilet_airflow_windows_closed) {
        this._toilet_airflow_windows_closed = _toilet_airflow_windows_closed;
    }

    public double get_toilet_airflow_windows_open() {
        return _toilet_airflow_windows_open;
    }

    public void set_toilet_airflow_windows_open(double _toilet_airflow_windows_open) {
        this._toilet_airflow_windows_open = _toilet_airflow_windows_open;
    }

    public double get_toilet_airflow_microventilation() {
        return _toilet_airflow_microventilation;
    }

    public void set_toilet_airflow_microventilation(double _toilet_airflow_microventilation) {
        this._toilet_airflow_microventilation = _toilet_airflow_microventilation;
    }

    public boolean is_bath_enabled() {
        return _bath_enabled;
    }

    public void set_bath_enabled(boolean _bath_enabled) {
        this._bath_enabled = _bath_enabled;
    }

    public boolean is_bath_cleaned() {
        return _bath_cleaned;
    }

    public void set_bath_cleaned(boolean _bath_cleaned) {
        this._bath_cleaned = _bath_cleaned;
    }

    public boolean is_bath_vent() {
        return _bath_vent;
    }

    public void set_bath_vent(boolean _bath_vent) {
        this._bath_vent = _bath_vent;
    }

    public boolean is_bath_inaccessible() {
        return _bath_inaccessible;
    }

    public void set_bath_inaccessible(boolean _bath_inaccessible) {
        this._bath_inaccessible = _bath_inaccessible;
    }

    public boolean is_bath_steady() {
        return _bath_steady;
    }

    public void set_bath_steady(boolean _bath_steady) {
        this._bath_steady = _bath_steady;
    }

    public boolean is_bath_not_cleaned() {
        return _bath_not_cleaned;
    }

    public void set_bath_not_cleaned(boolean _bath_not_cleaned) {
        this._bath_not_cleaned = _bath_not_cleaned;
    }

    public boolean is_bath_others() {
        return _bath_others;
    }

    public void set_bath_others(boolean _bath_others) {
        this._bath_others = _bath_others;
    }

    public String get_bath_others_comments() {
        return _bath_others_comments;
    }

    public void set_bath_others_comments(String _bath_others_comments) {
        this._bath_others_comments = _bath_others_comments;
    }

    public double get_bath_grid_dimension_x() {
        return _bath_grid_dimension_x;
    }

    public void set_bath_grid_dimension_x(double _bath_grid_dimension_x) {
        this._bath_grid_dimension_x = _bath_grid_dimension_x;
    }

    public double get_bath_grid_dimension_y() {
        return _bath_grid_dimension_y;
    }

    public void set_bath_grid_dimension_y(double _bath_grid_dimension_y) {
        this._bath_grid_dimension_y = _bath_grid_dimension_y;
    }

    public double get_bath_grid_dimension_round() {
        return _bath_grid_dimension_round;
    }

    public void set_bath_grid_dimension_round(double _bath_grid_dimension_round) {
        this._bath_grid_dimension_round = _bath_grid_dimension_round;
    }

    public double get_bath_airflow_windows_closed() {
        return _bath_airflow_windows_closed;
    }

    public void set_bath_airflow_windows_closed(double _bath_airflow_windows_closed) {
        this._bath_airflow_windows_closed = _bath_airflow_windows_closed;
    }

    public double get_bath_airflow_windows_open() {
        return _bath_airflow_windows_open;
    }

    public void set_bath_airflow_windows_open(double _bath_airflow_windows_open) {
        this._bath_airflow_windows_open = _bath_airflow_windows_open;
    }

    public double get_bath_airflow_microventilation() {
        return _bath_airflow_microventilation;
    }

    public void set_bath_airflow_microventilation(double _bath_airflow_microventilation) {
        this._bath_airflow_microventilation = _bath_airflow_microventilation;
    }

    public boolean is_flue_boiler() {
        return _flue_boiler;
    }

    public void set_flue_boiler(boolean _flue_boiler) {
        this._flue_boiler = _flue_boiler;
    }

    public boolean is_flue_rigid() {
        return _flue_rigid;
    }

    public void set_flue_rigid(boolean _flue_rigid) {
        this._flue_rigid = _flue_rigid;
    }

    public boolean is_flue_enabled() {
        return _flue_enabled;
    }

    public void set_flue_enabled(boolean _flue_enabled) {
        this._flue_enabled = _flue_enabled;
    }

    public boolean is_flue_cleaned() {
        return _flue_cleaned;
    }

    public void set_flue_cleaned(boolean _flue_cleaned) {
        this._flue_cleaned = _flue_cleaned;
    }

    public boolean is_flue_inaccessible() {
        return _flue_inaccessible;
    }

    public void set_flue_inaccessible(boolean _flue_inaccessible) {
        this._flue_inaccessible = _flue_inaccessible;
    }

    public boolean is_flue_alufol() {
        return _flue_alufol;
    }

    public void set_flue_alufol(boolean _flue_alufol) {
        this._flue_alufol = _flue_alufol;
    }

    public boolean is_flue_not_cleaned() {
        return _flue_not_cleaned;
    }

    public void set_flue_not_cleaned(boolean _flue_not_cleaned) {
        this._flue_not_cleaned = _flue_not_cleaned;
    }

    public boolean is_flue_others() {
        return _flue_others;
    }

    public void set_flue_others(boolean _flue_others) {
        this._flue_others = _flue_others;
    }

    public String get_flue_others_comments() {
        return _flue_others_comments;
    }

    public void set_flue_others_comments(String _flue_others_comments) {
        this._flue_others_comments = _flue_others_comments;
    }

    public double get_flue_airflow_windows_closed() {
        return _flue_airflow_windows_closed;
    }

    public void set_flue_airflow_windows_closed(double _flue_airflow_windows_closed) {
        this._flue_airflow_windows_closed = _flue_airflow_windows_closed;
    }

    public double get_flue_airflow_windows_open() {
        return _flue_airflow_windows_open;
    }

    public void set_flue_airflow_windows_open(double _flue_airflow_windows_open) {
        this._flue_airflow_windows_open = _flue_airflow_windows_open;
    }

    public double get_flue_airflow_microventilation() {
        return _flue_airflow_microventilation;
    }

    public void set_flue_airflow_microventilation(double _flue_airflow_microventilation) {
        this._flue_airflow_microventilation = _flue_airflow_microventilation;
    }

    public String get_comments_for_user() {
        return _comments_for_user;
    }

    public void set_comments_for_user(String _comments_for_user) {
        this._comments_for_user = _comments_for_user;
    }

    //    Prepare record for listView
    @Override
    public String toString() {
        String readableAddress = this._created;
        return readableAddress;
    }

}