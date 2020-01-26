package com.wruzjan.ihg.utils.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.wruzjan.ihg.utils.db.ApplicationOpenHelper;
import com.wruzjan.ihg.utils.model.Address;

import androidx.annotation.Nullable;


public class AddressDataSource {

    private SQLiteDatabase database;
    private ApplicationOpenHelper applicationHelper;
    private String[] allColumns = {
            ApplicationOpenHelper.COLUMN_ID,
            ApplicationOpenHelper.COLUMN_NAME,
            ApplicationOpenHelper.COLUMN_STREET,
            ApplicationOpenHelper.COLUMN_BUILDING,
            ApplicationOpenHelper.COLUMN_FLAT,
            ApplicationOpenHelper.COLUMN_CITY,
            ApplicationOpenHelper.COLUMN_DISTRICT};

    public AddressDataSource(Context context) {
        applicationHelper = new ApplicationOpenHelper(context);
    }

    public void open() throws SQLException {
        database = applicationHelper.getWritableDatabase();
//        applicationHelper.onCreate(database);
    }

    public void close() {
        applicationHelper.close();
    }

    public Address insertAddress(Address address) {

        ContentValues values = new ContentValues();
        values.put(applicationHelper.COLUMN_NAME, address.getName());
        values.put(applicationHelper.COLUMN_STREET, address.getStreet());
        values.put(applicationHelper.COLUMN_BUILDING, address.getBuilding());
        values.put(applicationHelper.COLUMN_FLAT, address.getFlat());
        values.put(applicationHelper.COLUMN_DISTRICT, address.getDistrinct());
        values.put(applicationHelper.COLUMN_CITY, address.getCity());

        long insertId = database.insert(ApplicationOpenHelper.TABLE_ADDRESSES, null,
                values);

        Cursor cursor = database.query(ApplicationOpenHelper.TABLE_ADDRESSES,
                allColumns, ApplicationOpenHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Address newAddress = cursorToAddress(cursor);
        cursor.close();
        return newAddress;
    }

    public void deleteAddress(Address address) {
        long id = address.getId();
        database.delete(ApplicationOpenHelper.TABLE_ADDRESSES, ApplicationOpenHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAllAddresses() {
        database.delete(ApplicationOpenHelper.TABLE_ADDRESSES, null, null);
    }

    @Nullable
    public Address getAddressById(int id) {
        Cursor cursor = null;
        try {
            cursor = database.query(ApplicationOpenHelper.TABLE_ADDRESSES,
                    allColumns, "_id = ?", new String[]{Integer.toString(id)}, null, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursorToAddress(cursor);
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

    public List<Address> getAllAddresses() {
        List<Address> addresses = new ArrayList<Address>();

        Cursor cursor = database.query(ApplicationOpenHelper.TABLE_ADDRESSES,
                allColumns, null, null, null, null, applicationHelper.COLUMN_STREET);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Address address = cursorToAddress(cursor);
            addresses.add(address);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return addresses;
    }

    private Address cursorToAddress(Cursor cursor) {
        Address address = new Address();
        address.setId(Integer.parseInt(cursor.getString(0)));
        address.setName(cursor.getString(1));
        address.setStreet(cursor.getString(2));
        address.setBuilding(cursor.getString(3));
        address.setFlat(cursor.getString(4));
        address.setCity(cursor.getString(5));
        address.setDistrict(cursor.getString(6));
        return address;
    }
}
