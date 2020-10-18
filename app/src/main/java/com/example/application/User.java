package com.example.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class User extends AppCompatActivity {

    EditText editText;
    Button account, order, tracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        editText =(EditText) findViewById(R.id.username);
        account = (Button)findViewById(R.id.Account);
        order =  (Button)findViewById(R.id.order);
        tracking =  (Button)findViewById(R.id.tracking);
/*
        SharedPreferences sp = getSharedPreferences(Properties.STORAGE, Context.MODE_PRIVATE);
        String name = sp.getString(Properties.USER_SESS, null);
        String info = sp.getString(Properties.USER_INFO, null);

        editText.setText(info);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }


}
