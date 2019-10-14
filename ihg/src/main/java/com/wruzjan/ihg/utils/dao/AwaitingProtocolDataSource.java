package com.wruzjan.ihg.utils.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.wruzjan.ihg.utils.db.ApplicationOpenHelper;
import com.wruzjan.ihg.utils.model.AwaitingProtocol;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class AwaitingProtocolDataSource {

    private static final String[] ALL_COLUMNS = {
            ApplicationOpenHelper.COLUMN_ID,
            ApplicationOpenHelper.COLUMN_PROTOCOL_PDF_URL
    };

    private final ApplicationOpenHelper applicationHelper;

    private SQLiteDatabase database;

    public AwaitingProtocolDataSource(Context context) {
        applicationHelper = new ApplicationOpenHelper(context);
    }

    public void open() throws SQLException {
        database = applicationHelper.getWritableDatabase();
    }

    public void close() {
        applicationHelper.close();
    }

    public void addAwaitingProtocol(@NonNull AwaitingProtocol awaitingProtocol) {
        ContentValues values = new ContentValues();
        values.put(ApplicationOpenHelper.COLUMN_PROTOCOL_PDF_URL, awaitingProtocol.getProtocolPdfUrl());
        database.insert(ApplicationOpenHelper.TABLE_AWAITING_PROTOCOL, null, values);
    }

    public int getAwaitincProtocolCount() {
        Cursor cursor = null;
        try {
            cursor = database.query(
                    ApplicationOpenHelper.TABLE_AWAITING_PROTOCOL,
                    ALL_COLUMNS,
                    null, null, null, null, null);
            return cursor.getCount();
        } finally {
            // Make sure to close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public List<AwaitingProtocol> getAwaitingProtocols() {
        Cursor cursor = null;
        try {
            List<AwaitingProtocol> protocols = new ArrayList<>();
            cursor = database.query(
                    ApplicationOpenHelper.TABLE_AWAITING_PROTOCOL,
                    ALL_COLUMNS,
                    null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                AwaitingProtocol protocol = cursorToAwaitingProtocol(cursor);
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

    private AwaitingProtocol cursorToAwaitingProtocol(Cursor cursor) {
        return new AwaitingProtocol(
                cursor.getInt(cursor.getColumnIndex(ApplicationOpenHelper.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(ApplicationOpenHelper.COLUMN_PROTOCOL_PDF_URL))
        );
    }

    public void deleteAllAwaitingProtocols() {
        database.delete(ApplicationOpenHelper.TABLE_AWAITING_PROTOCOL, null, null);
    }
}
