package com.chan.android_lab8;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    List<Map<String, Object>> ContactsList = new ArrayList<>();
    private birthdayDB ContactsDBHelper;
    private ListView ContactsListView;
    private SimpleAdapter ContactsAdapter;
    private AlertDialog.Builder delete_alertdialog;
    private AlertDialog.Builder changeitem_alertdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContactsDBHelper = new birthdayDB(MainActivity.this);
        Cursor AllContacts = ContactsDBHelper.queryAll();
        fillContactsList(AllContacts);

        delete_alertdialog = new AlertDialog.Builder(this);
        delete_alertdialog.setTitle("是否删除?");

        LayoutInflater factor = LayoutInflater.from(MainActivity.this);
        View view_in = factor.inflate(R.layout.dialog_layout, null);
        changeitem_alertdialog = new AlertDialog.Builder(this);
        changeitem_alertdialog.setTitle("(≧∇≦)ﾉ恭喜发财").setView(view_in);

        ContactsListView = findViewById(R.id.list);
        ContactsAdapter = new SimpleAdapter(this, ContactsList,R.layout.item_layout,
                new String[]{"name","birth", "gift"}, new int[]{R.id.name_tag,R.id.birth_tag,R.id.gift_tag});
        ContactsListView.setAdapter(ContactsAdapter);
        ContactsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                delete_alertdialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int id = (int)ContactsList.get(which).get("id");
                        ContactsDBHelper.delete(id);
                        ContactsList.remove(which);
                        ContactsAdapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                return true;
            }
        });
        ContactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeitem_alertdialog.show();
                changeitem_alertdialog.setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("放弃修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
        });
    }

    void fillContactsList(Cursor cursor){

        if(cursor.moveToFirst()){
            do{
                Map<String, Object> temp = new LinkedHashMap<>();
                temp.put("id", cursor.getInt(cursor.getColumnIndex("id")));
                temp.put("name", cursor.getString(cursor.getColumnIndex("name")));
                temp.put("birth", cursor.getString(cursor.getColumnIndex("birth")));
                temp.put("gift", cursor.getString(cursor.getColumnIndex("gift")));
                ContactsList.add(temp);
            }while (cursor.moveToNext());
        }
    }
}
