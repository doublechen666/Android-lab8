package com.chan.android_lab8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

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

    public int insert(String name, String birth, String gift){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("birth", birth);
        values.put("gift", gift);
        db.insert(TABLE_NAME, null, values);
        Cursor cursor = db.rawQuery("select last_insert_rowid() from " + TABLE_NAME,null);
        int new_id = -1;
        if(cursor.moveToFirst()){
            new_id = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return new_id;
    }

    public void update(Integer id, String name, String birth, String gift){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        String  whereClause = "_id = ?";
        String[] whereArgs = {id.toString()};
        values.put("name", name);
        values.put("birth", birth);
        values.put("gift", gift);
        db.update(TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    public void delete(Integer id){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "_id = ?";
        String [] whereArgs = {id.toString()};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    public boolean isNameExist(String name){
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = "name = ?";
        String[] whereArgs = {name};
        Cursor cursor = db.query(TABLE_NAME, new String[]{"name"}, whereClause, whereArgs, null, null, null);
        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }

    public Map<String, Object> query(Integer id){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "_id = ?";
        String [] whereArgs = {id.toString()};
        Cursor cursor = db.query(TABLE_NAME, new String[]{"_id", "name", "birth", "gift"}, whereClause, whereArgs, null, null, null);
        ArrayList<Map<String, Object>> resultList = generateList(cursor);
        Map<String, Object> result = resultList.get(0);
        //db.close then the cursor is not usable
        cursor.close();
        db.close();
        return result;
    }

    public ArrayList<Map<String, Object>> queryAll(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<Map<String, Object>> resultList = generateList(cursor);
        cursor.close();
        db.close();
        return resultList;
    }

    private ArrayList<Map<String, Object>> generateList(Cursor cursor){
        ArrayList<Map<String, Object>> resultList = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                Map<String, Object> temp = new LinkedHashMap<>();
                temp.put("id", cursor.getInt(cursor.getColumnIndex("_id")));
                temp.put("name", cursor.getString(cursor.getColumnIndex("name")));
                temp.put("birth", cursor.getString(cursor.getColumnIndex("birth")));
                temp.put("gift", cursor.getString(cursor.getColumnIndex("gift")));
                resultList.add(temp);
            }while(cursor.moveToNext());
        }
        return resultList;
    }
}//end class birthdayDB
