package com.wruzjan.ihg.utils.model;

public class Protocol {

    private int _id;
    private int _address_id;
    private int _worker_id;
    private String _worker_name;
//    used for tracking changes
    private int _version;
    private float _temp_outside;
    private float _temp_inside;
    private float _co2;
    private String _created;

    private boolean _kitchen_enabled;
    private float _kitchen_grid_dimension_x;
    private float _kitchen_grid_dimension_y;

    private double _kitchen_grid_dimension_round;
    private float _kitchen_airflow_windows_closed;
    private float _kitchen_airflow_microventilation;
    private String _kitchen_comments;

    private boolean _bathroom_enabled;
    private float _bathroom_grid_dimension_x;
    private float _bathroom_grid_dimension_y;
    private double _bathroom_grid_dimension_round;
    private float _bathroom_airflow_windows_closed;
    private float _bathroom_airflow_microventilation;
    private String _bathroom_comments;

    private boolean _toilet_enabled;
    private float _toilet_grid_dimension_x;
    private float _toilet_grid_dimension_y;
    private double _toilet_grid_dimension_round;
    private float _toilet_airflow_windows_closed;
    private float _toilet_airflow_microventilation;
    private String _toilet_comments;

    private boolean _flue_enabled;
    private float _flue_airflow_windows_closed;
    private float _flue_airflow_microventilation;
    private String _flue_comments;

    private boolean _gas_fittings_present;
    private boolean _gas_cooker_present;
    private boolean _bathroom_bake_present;

    private boolean _gas_fittings_working;
    private boolean _gas_cooker_working;
    private boolean _bathroom_bake_working;
    private String _gas_fittings_comments;
    private String _equipment_comments;
    private String _comments_for_user;
    private String _comments_for_manager;

    private String companyAddress;
    private String protocolType;
    private int ventCount;

    public Protocol(){
    }

    public Protocol(float _co2, int _version, float _temp_outside, float _temp_inside, boolean _kitchen_enabled, float _kitchen_grid_dimension_x, float _kitchen_grid_dimension_y, float _kitchen_airflow_windows_closed, float _kitchen_airflow_microventilation, String _kitchen_comments, boolean _bathroom_enabled, float _bathroom_grid_dimension_x, float _bathroom_grid_dimension_y, float _bathroom_airflow_windows_closed, float _bathroom_airflow_microventilation, String _bathroom_comments, boolean _toilet_enabled, float _toilet_grid_dimension_x, float _toilet_grid_dimension_y, float _toilet_airflow_windows_closed, float _toilet_airflow_microventilation, String _toilet_comments, boolean _flue_enabled, float _flue_airflow_windows_closed, float _flue_airflow_microventilation, String _flue_comments, boolean _gas_fittings_working, boolean _gas_cooker_working, boolean _bathroom_bake_working, String _gas_fittings_comments, String _equipment_comments, String _comments_for_user, String _comments_for_manager) {
        this._co2 = _co2;
        this._version = _version;
        this._temp_outside = _temp_outside;
        this._temp_inside = _temp_inside;
        this._kitchen_enabled = _kitchen_enabled;
        this._kitchen_grid_dimension_x = _kitchen_grid_dimension_x;
        this._kitchen_grid_dimension_y = _kitchen_grid_dimension_y;
        this._kitchen_airflow_windows_closed = _kitchen_airflow_windows_closed;
        this._kitchen_airflow_microventilation = _kitchen_airflow_microventilation;
        this._kitchen_comments = _kitchen_comments;
        this._bathroom_enabled = _bathroom_enabled;
        this._bathroom_grid_dimension_x = _bathroom_grid_dimension_x;
        this._bathroom_grid_dimension_y = _bathroom_grid_dimension_y;
        this._bathroom_airflow_windows_closed = _bathroom_airflow_windows_closed;
        this._bathroom_airflow_microventilation = _bathroom_airflow_microventilation;
        this._bathroom_comments = _bathroom_comments;
        this._toilet_enabled = _toilet_enabled;
        this._toilet_grid_dimension_x = _toilet_grid_dimension_x;
        this._toilet_grid_dimension_y = _toilet_grid_dimension_y;
        this._toilet_airflow_windows_closed = _toilet_airflow_windows_closed;
        this._toilet_airflow_microventilation = _toilet_airflow_microventilation;
        this._toilet_comments = _toilet_comments;
        this._flue_enabled = _flue_enabled;
        this._flue_airflow_windows_closed = _flue_airflow_windows_closed;
        this._flue_airflow_microventilation = _flue_airflow_microventilation;
        this._flue_comments = _flue_comments;
        this._gas_fittings_working = _gas_fittings_working;
        this._gas_cooker_working = _gas_cooker_working;
        this._bathroom_bake_working = _bathroom_bake_working;
        this._gas_fittings_comments = _gas_fittings_comments;
        this._equipment_comments = _equipment_comments;
        this._comments_for_user = _comments_for_user;
        this._comments_for_manager = _comments_for_manager;
    }

    public Protocol(int _id, int _address_id, int _worker_id, float _co2, int _version, float _temp_outside, float _temp_inside, boolean _kitchen_enabled, float _kitchen_grid_dimension_x, float _kitchen_grid_dimension_y, float _kitchen_airflow_windows_closed, float _kitchen_airflow_microventilation, String _kitchen_comments, boolean _bathroom_enabled, float _bathroom_grid_dimension_x, float _bathroom_grid_dimension_y, float _bathroom_airflow_windows_closed, float _bathroom_airflow_microventilation, String _bathroom_comments, boolean _toilet_enabled, float _toilet_grid_dimension_x, float _toilet_grid_dimension_y, float _toilet_airflow_windows_closed, float _toilet_airflow_microventilation, String _toilet_comments, boolean _flue_enabled, float _flue_airflow_windows_closed, float _flue_airflow_microventilation, String _flue_comments, boolean _gas_fittings_working, boolean _gas_cooker_working, boolean _bathroom_bake_working, String _gas_fittings_comments, String _equipment_comments, String _comments_for_user, String _comments_for_manager) {
        this._id = _id;
        this._address_id = _address_id;
        this._worker_id = _worker_id;
        this._co2 = _co2;
        this._version = _version;
        this._temp_outside = _temp_outside;
        this._temp_inside = _temp_inside;
        this._kitchen_enabled = _kitchen_enabled;
        this._kitchen_grid_dimension_x = _kitchen_grid_dimension_x;
        this._kitchen_grid_dimension_y = _kitchen_grid_dimension_y;
        this._kitchen_airflow_windows_closed = _kitchen_airflow_windows_closed;
        this._kitchen_airflow_microventilation = _kitchen_airflow_microventilation;
        this._kitchen_comments = _kitchen_comments;
        this._bathroom_enabled = _bathroom_enabled;
        this._bathroom_grid_dimension_x = _bathroom_grid_dimension_x;
        this._bathroom_grid_dimension_y = _bathroom_grid_dimension_y;
        this._bathroom_airflow_windows_closed = _bathroom_airflow_windows_closed;
        this._bathroom_airflow_microventilation = _bathroom_airflow_microventilation;
        this._bathroom_comments = _bathroom_comments;
        this._toilet_enabled = _toilet_enabled;
        this._toilet_grid_dimension_x = _toilet_grid_dimension_x;
        this._toilet_grid_dimension_y = _toilet_grid_dimension_y;
        this._toilet_airflow_windows_closed = _toilet_airflow_windows_closed;
        this._toilet_airflow_microventilation = _toilet_airflow_microventilation;
        this._toilet_comments = _toilet_comments;
        this._flue_enabled = _flue_enabled;
        this._flue_airflow_windows_closed = _flue_airflow_windows_closed;
        this._flue_airflow_microventilation = _flue_airflow_microventilation;
        this._flue_comments = _flue_comments;
        this._gas_fittings_working = _gas_fittings_working;
        this._gas_cooker_working = _gas_cooker_working;
        this._bathroom_bake_working = _bathroom_bake_working;
        this._gas_fittings_comments = _gas_fittings_comments;
        this._equipment_comments = _equipment_comments;
        this._comments_for_user = _comments_for_user;
        this._comments_for_manager = _comments_for_manager;
    }

    public boolean is_gas_fittings_present() {
        return _gas_fittings_present;
    }

    public String get_created() {
        return _created;
    }

    public void set_created(String created) {
        this._created = created;
    }

    public void set_gas_fittings_present(boolean _gas_fittings_present) {
        this._gas_fittings_present = _gas_fittings_present;
    }

    public boolean is_gas_cooker_present() {
        return _gas_cooker_present;
    }

    public void set_gas_cooker_present(boolean _gas_cooker_present) {
        this._gas_cooker_present = _gas_cooker_present;
    }

    public boolean is_bathroom_bake_present() {
        return _bathroom_bake_present;
    }

    public void set_bathroom_bake_present(boolean _bathroom_bake_present) {
        this._bathroom_bake_present = _bathroom_bake_present;
    }

    public double get_kitchen_grid_dimension_round() {
        return _kitchen_grid_dimension_round;
    }

    public void set_kitchen_grid_dimension_round(double _kitchen_grid_dimension_round) {
        this._kitchen_grid_dimension_round = _kitchen_grid_dimension_round;
    }

    public double get_bathroom_grid_dimension_round() {
        return _bathroom_grid_dimension_round;
    }

    public void set_bathroom_grid_dimension_round(double _bathroom_grid_dimension_round) {
        this._bathroom_grid_dimension_round = _bathroom_grid_dimension_round;
    }

    public double get_toilet_grid_dimension_round() {
        return _toilet_grid_dimension_round;
    }

    public void set_toilet_grid_dimension_round(double _toilet_grid_dimension_round) {
        this._toilet_grid_dimension_round = _toilet_grid_dimension_round;
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

    public float get_temp_outside() {
        return _temp_outside;
    }

    public void set_temp_outside(float _temp_outside) {
        this._temp_outside = _temp_outside;
    }

    public float get_temp_inside() {
        return _temp_inside;
    }

    public void set_temp_inside(float _temp_inside) {
        this._temp_inside = _temp_inside;
    }

    public float get_co2() {
        return _co2;
    }

    public void set_co2(float _co2) {
        this._co2 = _co2;
    }

    public boolean is_kitchen_enabled() {
        return _kitchen_enabled;
    }

    public void set_kitchen_enabled(boolean _kitchen_enabled) {
        this._kitchen_enabled = _kitchen_enabled;
    }

    public float get_kitchen_grid_dimension_x() {
        return _kitchen_grid_dimension_x;
    }

    public void set_kitchen_grid_dimension_x(float _kitchen_grid_dimension_x) {
        this._kitchen_grid_dimension_x = _kitchen_grid_dimension_x;
    }

    public float get_kitchen_grid_dimension_y() {
        return _kitchen_grid_dimension_y;
    }

    public void set_kitchen_grid_dimension_y(float _kitchen_grid_dimension_y) {
        this._kitchen_grid_dimension_y = _kitchen_grid_dimension_y;
    }

    public float get_kitchen_airflow_windows_closed() {
        return _kitchen_airflow_windows_closed;
    }

    public void set_kitchen_airflow_windows_closed(float _kitchen_airflow_windows_closed) {
        this._kitchen_airflow_windows_closed = _kitchen_airflow_windows_closed;
    }

    public float get_kitchen_airflow_microventilation() {
        return _kitchen_airflow_microventilation;
    }

    public void set_kitchen_airflow_microventilation(float _kitchen_airflow_microventilation) {
        this._kitchen_airflow_microventilation = _kitchen_airflow_microventilation;
    }

    public String get_kitchen_comments() {
        return _kitchen_comments;
    }

    public void set_kitchen_comments(String _kitchen_comments) {
        this._kitchen_comments = _kitchen_comments;
    }

    public boolean is_bathroom_enabled() {
        return _bathroom_enabled;
    }

    public void set_bathroom_enabled(boolean _bathroom_enabled) {
        this._bathroom_enabled = _bathroom_enabled;
    }

    public float get_bathroom_grid_dimension_x() {
        return _bathroom_grid_dimension_x;
    }

    public void set_bathroom_grid_dimension_x(float _bathroom_grid_dimension_x) {
        this._bathroom_grid_dimension_x = _bathroom_grid_dimension_x;
    }

    public float get_bathroom_grid_dimension_y() {
        return _bathroom_grid_dimension_y;
    }

    public void set_bathroom_grid_dimension_y(float _bathroom_grid_dimension_y) {
        this._bathroom_grid_dimension_y = _bathroom_grid_dimension_y;
    }

    public float get_bathroom_airflow_windows_closed() {
        return _bathroom_airflow_windows_closed;
    }

    public void set_bathroom_airflow_windows_closed(float _bathroom_airflow_windows_closed) {
        this._bathroom_airflow_windows_closed = _bathroom_airflow_windows_closed;
    }

    public float get_bathroom_airflow_microventilation() {
        return _bathroom_airflow_microventilation;
    }

    public void set_bathroom_airflow_microventilation(float _bathroom_airflow_microventilation) {
        this._bathroom_airflow_microventilation = _bathroom_airflow_microventilation;
    }

    public String get_bathroom_comments() {
        return _bathroom_comments;
    }

    public void set_bathroom_comments(String _bathroom_comments) {
        this._bathroom_comments = _bathroom_comments;
    }

    public boolean is_toilet_enabled() {
        return _toilet_enabled;
    }

    public void set_toilet_enabled(boolean _toilet_enabled) {
        this._toilet_enabled = _toilet_enabled;
    }

    public float get_toilet_grid_dimension_x() {
        return _toilet_grid_dimension_x;
    }

    public void set_toilet_grid_dimension_x(float _toilet_grid_dimension_x) {
        this._toilet_grid_dimension_x = _toilet_grid_dimension_x;
    }

    public float get_toilet_grid_dimension_y() {
        return _toilet_grid_dimension_y;
    }

    public void set_toilet_grid_dimension_y(float _toilet_grid_dimension_y) {
        this._toilet_grid_dimension_y = _toilet_grid_dimension_y;
    }

    public float get_toilet_airflow_windows_closed() {
        return _toilet_airflow_windows_closed;
    }

    public void set_toilet_airflow_windows_closed(float _toilet_airflow_windows_closed) {
        this._toilet_airflow_windows_closed = _toilet_airflow_windows_closed;
    }

    public float get_toilet_airflow_microventilation() {
        return _toilet_airflow_microventilation;
    }

    public void set_toilet_airflow_microventilation(float _toilet_airflow_microventilation) {
        this._toilet_airflow_microventilation = _toilet_airflow_microventilation;
    }

    public String get_toilet_comments() {
        return _toilet_comments;
    }

    public void set_toilet_comments(String _toilet_comments) {
        this._toilet_comments = _toilet_comments;
    }

    public boolean is_flue_enabled() {
        return _flue_enabled;
    }

    public void set_flue_enabled(boolean _flue_enabled) {
        this._flue_enabled = _flue_enabled;
    }

    public float get_flue_airflow_windows_closed() {
        return _flue_airflow_windows_closed;
    }

    public void set_flue_airflow_windows_closed(float _flue_airflow_windows_closed) {
        this._flue_airflow_windows_closed = _flue_airflow_windows_closed;
    }

    public float get_flue_airflow_microventilation() {
        return _flue_airflow_microventilation;
    }

    public void set_flue_airflow_microventilation(float _flue_airflow_microventilation) {
        this._flue_airflow_microventilation = _flue_airflow_microventilation;
    }

    public String get_flue_comments() {
        return _flue_comments;
    }

    public void set_flue_comments(String _flue_comments) {
        this._flue_comments = _flue_comments;
    }

    public boolean is_gas_fittings_working() {
        return _gas_fittings_working;
    }

    public void set_gas_fittings_working(boolean _gas_fittings_working) {
        this._gas_fittings_working = _gas_fittings_working;
    }

    public boolean is_gas_cooker_working() {
        return _gas_cooker_working;
    }

    public void set_gas_cooker_working(boolean _gas_cooker_working) {
        this._gas_cooker_working = _gas_cooker_working;
    }

    public boolean is_bathroom_bake_working() {
        return _bathroom_bake_working;
    }

    public void set_bathroom_bake_working(boolean _bathroom_bake_working) {
        this._bathroom_bake_working = _bathroom_bake_working;
    }

    public String get_gas_fittings_comments() {
        return _gas_fittings_comments;
    }

    public void set_gas_fittings_comments(String _gas_fittings_comments) {
        this._gas_fittings_comments = _gas_fittings_comments;
    }

    public String get_equipment_comments() {
        return _equipment_comments;
    }

    public void set_equipment_comments(String _equipment_comments) {
        this._equipment_comments = _equipment_comments;
    }

    public String get_comments_for_user() {
        return _comments_for_user;
    }

    public void set_comments_for_user(String _comments_for_user) {
        this._comments_for_user = _comments_for_user;
    }

    public String get_comments_for_manager() {
        return _comments_for_manager;
    }

    public void set_comments_for_manager(String _comments_for_manager) {
        this._comments_for_manager = _comments_for_manager;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public int getVentCount() {
        return ventCount;
    }

    public void setVentCount(int ventCount) {
        this.ventCount = ventCount;
    }

    //    Prepare record for listView
    @Override
    public String toString() {
        String readableAddress = this._created;
        return readableAddress;
    }

}
