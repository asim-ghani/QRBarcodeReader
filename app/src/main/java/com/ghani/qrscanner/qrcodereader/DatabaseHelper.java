package com.ghani.qrscanner.qrcodereader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "history.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "history";
    private static final String ID_COL = "id";
    private static final String TYPE_COL = "type";
    private static final String TIMESTAMP_COL = "timestamp";
    private static final String VALUE_COL = "value";

    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TYPE_COL + " TEXT, " +
                TIMESTAMP_COL + " TEXT, " +
                VALUE_COL + " TEXT)";
        db.execSQL(query);
    }

    public void insertData(String type, String value) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TYPE_COL, type);
        values.put(TIMESTAMP_COL, getTimestamp());
        values.put(VALUE_COL, value);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<DataModel> getAllData() {
        List<DataModel> dataList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {ID_COL, TYPE_COL, TIMESTAMP_COL, VALUE_COL};
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(ID_COL));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(TYPE_COL));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(TIMESTAMP_COL));
                String value = cursor.getString(cursor.getColumnIndexOrThrow(VALUE_COL));

                dataList.add(new DataModel(id, type, timestamp, value));
            }
            cursor.close();
        }
        db.close();
        return dataList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy hh:mm a", Locale.getDefault());
        return sdf.format(new Date());
    }

    public void deleteData(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, ID_COL + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void clearTable(){

        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}
