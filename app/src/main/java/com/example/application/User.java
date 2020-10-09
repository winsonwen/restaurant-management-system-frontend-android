package com.example.application;

import android.os.Bundle;
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


    }
}
