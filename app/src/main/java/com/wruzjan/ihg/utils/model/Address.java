package com.wruzjan.ihg.utils.model;

public class Address {

    private int _id;
    private String _name;
    private String _street;
    private String _building;
    private String _city;
    private String _flat;
    private String _district;

    public Address() {
    }

    public Address(String name, String street, String building, String flat, String district, String city) {
        this._name = name;
        this._street = street;
        this._building = building;
        if(flat.isEmpty())
            this._flat = "1";
        else
            this._flat = flat;
        this._district = district;
        this._city = city;
    }

    public Address(int id, String name, String street, String building, String flat, String district, String city) {
        this._id = id;
        this._name = name;
        this._street = street;
        this._building = building;
        if(flat.isEmpty())
            this._flat = "1";
        else
            this._flat = flat;
        this._district = district;
        this._city = city;
    }

    public void setId(int id) {
        this._id = id;
    }

    public int getId() {
        return this._id;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getName() {
        return this._name;
    }

    public void setStreet(String street) {
        this._street = street;
    }

    public String getStreet() {
        return this._street;
    }

    public void setBuilding(String building) {
        this._building = building;
    }

    public String getBuilding() {
        return this._building;
    }

    public void setFlat(String flat) {
        this._flat = flat;
    }

    public String getFlat() {
        return this._flat;
    }

    public void setDistrict(String district) {
        this._district = district;
    }

    public String getDistrinct() {
        return this._district;
    }

    public void setCity(String city) {
        this._city = city;
    }

    public String getCity() {
        return this._city;
    }

    //    Prepare record for listView
    @Override
    public String toString() {
        String readableAddress = this._name + " " + this._street + " " + this._building + "/" + this._flat + ", " + this._city;
        return readableAddress;
    }
}
