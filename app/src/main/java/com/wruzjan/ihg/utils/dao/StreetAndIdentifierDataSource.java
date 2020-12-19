package com.wruzjan.ihg.utils.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.wruzjan.ihg.utils.db.ApplicationOpenHelper;
import com.wruzjan.ihg.utils.model.StreetAndIdentifier;

public class StreetAndIdentifierDataSource {

    private SQLiteDatabase database;
    private final ApplicationOpenHelper applicationHelper;
    private final String[] allColumns = {
            ApplicationOpenHelper.COLUMN_ID,
            ApplicationOpenHelper.COLUMN_STREET,
            ApplicationOpenHelper.COLUMN_STREET_IDENTIFIER
    };

    public StreetAndIdentifierDataSource(Context context) {
        applicationHelper = new ApplicationOpenHelper(context);
    }

    public void open() throws SQLException {
        database = applicationHelper.getWritableDatabase();
    }

    public StreetAndIdentifier getByStreetIdentifier(int streetIdentifier) {
        Cursor cursor = null;
        try {
            cursor = database.query(ApplicationOpenHelper.TABLE_STREET_AND_IDENTIFIER,
                    allColumns, "street_identifier = ?", new String[]{Integer.toString(streetIdentifier)}, null, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                return convertToModel(cursor);
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

    private StreetAndIdentifier convertToModel(Cursor cursor) {
        return new StreetAndIdentifier(
                cursor.getInt(cursor.getColumnIndex(ApplicationOpenHelper.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(ApplicationOpenHelper.COLUMN_STREET)),
                cursor.getInt(cursor.getColumnIndex(ApplicationOpenHelper.COLUMN_STREET_IDENTIFIER))
        );
    }

    public void close() {
        applicationHelper.close();
    }
}
