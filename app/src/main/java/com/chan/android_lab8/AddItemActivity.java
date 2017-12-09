package com.chan.android_lab8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddItemActivity extends AppCompatActivity {

    private birthdayDB ContactsDBHelper;
    EditText nameEdit;
    EditText birthEdit;
    EditText giftEdit;
    Button confirmBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        nameEdit = findViewById(R.id.edit1);
        birthEdit = findViewById(R.id.edit2);
        giftEdit = findViewById(R.id.edit3);
        confirmBtn = findViewById(R.id.comfirm_btn);
        ContactsDBHelper = new birthdayDB(AddItemActivity.this);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = nameEdit.getText().toString();
                String birthStr = birthEdit.getText().toString();
                String giftStr = giftEdit.getText().toString();
                int new_id;
                if(ContactsDBHelper.isNameExist(nameStr)){
                    Toast.makeText(getApplication(), "名字重复啦，请检查", Toast.LENGTH_LONG).show();
                }else{
                    new_id = ContactsDBHelper.insert(nameStr, birthStr, giftStr);
                    Intent intent = new Intent(AddItemActivity.this, MainActivity.class);
                    intent.putExtra("id", new_id);
                    intent.putExtra("name", nameStr);
                    intent.putExtra("birth", birthStr);
                    intent.putExtra("gift", giftStr);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });
    }
}
