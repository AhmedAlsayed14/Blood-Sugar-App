package com.y2m.bloodsugartwo;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Mohamed Antar on 10/19/2016.
 */
 public class SugerDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "sugerDB";

    private static final String TABLE_NAME= "suger_table";
    private static final String KEY_ID = "id";
    private static final String KEY_VALUE= "value";
    private static final String KEY_TIME= "time";
    private static final String KEY_TYPE= "type";


    SharedPreferences prefs;
    public SugerDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SUGER_TABLE =
                "CREATE TABLE " + TABLE_NAME+ "(" +
                        KEY_ID+ " INTEGER PRIMARY KEY," +
                        KEY_VALUE+ " INTEGER," +
                        KEY_TIME+ " INTEGER," +
                        KEY_TYPE+ " INTEGER" + ")";

        db.execSQL(CREATE_SUGER_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public long addNewRow(Item sugerItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VALUE, sugerItem.getValue());
        values.put(KEY_TIME, sugerItem.getTimeInSeconds());
        values.put(KEY_TYPE, sugerItem.getType());
        long i=db.insert(TABLE_NAME, null, values);
        Log.d("Insert","I = " +i+" "+sugerItem.getType());
        db.close();
        return i;
    }
    public Item getRow(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{
                KEY_VALUE,KEY_TIME,KEY_TYPE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Item item = new Item(
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getInt(3));
            cursor.close();
            db.close();
            return item;
        }
        else
            return null;
    }
    public ArrayList<Item> getAllRows() {
        ArrayList<Item> DataList = new ArrayList<Item>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Item item;
        if (cursor.moveToFirst())
        {
            do
            {
                item = new Item(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),cursor.getInt(3));
                DataList.add(item);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return DataList;
    }
    public ArrayList<Item> getAllRowsInRange(int From, int To) {
        ArrayList<Item> DataList = new ArrayList<Item>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME
                + " Where " + KEY_TIME + " BETWEEN "+ String.valueOf(From) + " AND " + String.valueOf(To);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Item item;
        if (cursor.moveToFirst())
        {
            do
            {
                item = new Item(cursor.getInt(1),cursor.getInt(2),cursor.getInt(3));
                DataList.add(item);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return DataList;
    }
    public int getRowCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count=cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
    public int updateRow(Item sugerItem) {
        Log.d("//////////  =","update : "+sugerItem.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, sugerItem.getId());
        values.put(KEY_VALUE, sugerItem.getValue());
        values.put(KEY_TYPE, sugerItem.getType());
        values.put(KEY_TIME, sugerItem.getTimeInSeconds());
        int count=db.update(TABLE_NAME, values, KEY_ID + " = ? ",
                new String[]{String.valueOf(sugerItem.getId())});
        db.close();
        return count;
    }
    public int deleteRow(int id) {
        Log.d("//////////  =","Delete : "+id);

        SQLiteDatabase db = this.getWritableDatabase();
        int count=db.delete(TABLE_NAME, KEY_ID + " = ? ",
                new String[]{String.valueOf(id)});
        db.close();
        return count;
    }
}