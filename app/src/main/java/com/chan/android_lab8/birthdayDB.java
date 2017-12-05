package com.chan.android_lab8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 61915 on 17/12/05.
 */

public class birthdayDB extends SQLiteOpenHelper{
    private static final String DB_NAME = "Contacts.db";
    private static final String TABLE_NAME = "Contacts";
    private static final int DB_VERSION = 1;

    public birthdayDB(Context  c){
        super(c, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table " + TABLE_NAME
                +"(_id integer primary key, "
                +"name text, "
                +"birth text, "
                +"gift text);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: upgrade database
    }

    public void insert(String name, String birth, String gift){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("birth", birth);
        values.put("gift", gift);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void update(Integer id,String name, String birth, String gift){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        String  whereClause = "id = ?";
        String[] whereArgs = {id.toString()};
        values.put("name", name);
        values.put("birth", birth);
        values.put("gift", gift);
        db.update(TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    public void delete(Integer id){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "id = ?";
        String [] whereArgs = {id.toString()};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    public Cursor query(Integer id){
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = "id = ?";
        String [] whereArgs = {id.toString()};
        Cursor cursor = db.query(TABLE_NAME, new String[]{"name", "birth", "gift"}, whereClause, whereArgs, null, null, null);
        db.close();
        return cursor;
    }

    public Cursor queryAll(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{"name", "birth", "gift"}, null, null, null, null, null);
        db.close();
        return cursor;
    }
}
