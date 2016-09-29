package com.wruzjan.ihg.utils.model;

public class Worker {

    private int _id;
    private String _name;
    private String _surname;

    public Worker(){
    }

    public Worker(String name, String surname){
        this._name = name;
        this._surname = surname;
    }

    public Worker(int id, String name, String surname){
        this._id = id;
        this._name = name;
        this._surname = surname;
    }

    //    Prepare record for listView
    @Override
    public String toString() {
        String readableWorker = this._name + " " + this._surname;
        return readableWorker;
    }

    public void set_id(int id){
        this._id = id;
    }

    public int get_id(){
        return this._id;
    }

    public void set_name(String name){
        this._name = name;
    }

    public String get_name(){
        return this._name;
    }

    public void set_surname(String surname){
        this._surname = surname;
    }

    public String get_surname(){
        return this._surname;
    }
}
