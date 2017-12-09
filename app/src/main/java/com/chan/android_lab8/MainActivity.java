package com.chan.android_lab8;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
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
    private TextView name_dialog;
    private EditText birth_dialog;
    private EditText gift_dialog;
    private TextView phone_dialog;
    private Button addBtn;
    public static int ADD_ITEM = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContactsDBHelper = new birthdayDB(MainActivity.this);
        ContactsList = ContactsDBHelper.queryAll();

        delete_alertdialog = new AlertDialog.Builder(this);
        delete_alertdialog.setTitle("是否删除?");

        changeitem_alertdialog = new AlertDialog.Builder(this);
        changeitem_alertdialog.setTitle("(≧∇≦)ﾉ恭喜发财");

        ContactsListView = findViewById(R.id.list);
        ContactsAdapter = new SimpleAdapter(this, ContactsList,R.layout.item_layout,
                new String[]{"name","birth", "gift"}, new int[]{R.id.name_tag,R.id.birth_tag,R.id.gift_tag});
        ContactsListView.setAdapter(ContactsAdapter);
        ContactsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                delete_alertdialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int id_temp = (int)ContactsList.get(pos).get("id");
                        ContactsDBHelper.delete(id_temp);
                        ContactsList.remove(pos);
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
                final int pos = position;
                final int id_temp = (int)ContactsList.get(position).get("id");
                Map<String, Object> curr_item = ContactsDBHelper.query(id_temp);
                final String curr_name = (String)curr_item.get("name");

                LayoutInflater factor = LayoutInflater.from(MainActivity.this);
                View view_in = factor.inflate(R.layout.dialog_layout, null);
                name_dialog = view_in.findViewById(R.id.d_nameText);
                birth_dialog = view_in.findViewById(R.id.d_edit2);
                gift_dialog = view_in.findViewById(R.id.d_edit3);
                phone_dialog = view_in.findViewById(R.id.phoneNoText);
                name_dialog.setText(curr_name);
                phone_dialog.setText(getPhoneNo(curr_name));
                changeitem_alertdialog.setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String curr_birth = birth_dialog.getText().toString();
                        String curr_gift = gift_dialog.getText().toString();
                        ContactsDBHelper.update(id_temp, curr_name, curr_birth, curr_gift);
                        Map<String, Object> temp = ContactsList.get(pos);
                        temp.put("birth", curr_birth);
                        temp.put("gift", curr_gift);
                        ContactsList.set(pos, temp);
                        ContactsAdapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("放弃修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                changeitem_alertdialog.setView(view_in).show();
            }
        });

        addBtn = findViewById(R.id.add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivityForResult(intent, ADD_ITEM);
            }
        });
    }//end onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Map<String, Object> new_item = new HashMap<>();
        new_item.put("id", data.getIntExtra("id", -1));
        new_item.put("name", data.getStringExtra("name"));
        new_item.put("birth", data.getStringExtra("birth"));
        new_item.put("gift", data.getStringExtra("gift"));
        ContactsList.add(new_item);
        ContactsAdapter.notifyDataSetChanged();
    }

    private String getPhoneNo(String name){
        String number = "";
        int isHas = 0;
        // reference: http://blog.csdn.net/zach_zhou/article/details/51595226
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null, ContactsContract.PhoneLookup.DISPLAY_NAME+"=?",new String[]{name},null);
        //has_phone_number:http://uule.iteye.com/blog/1709227
        //has_phone_number是否是1决定data表中有无电话记录
        if(cursor.moveToFirst()){
            isHas = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
        }
        if(isHas == 1){
            int ContactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactID,
                    null, null);
            while(phone.moveToNext()) {
                number += phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + " ";
            }
        }else{
            number = "无";
        }
        return number;
    }
}
