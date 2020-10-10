package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.application.delivery.DeliveryManActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity{

    EditText userName, password;
    Button sign_in, sign_up;
    RequestQueue requestqueue;
    FragmentManager fm;
    boolean member;
    SharedPreferences pref;
    static boolean count = true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName =(EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);

        sign_in =(Button)findViewById(R.id.sign_in);
        sign_up =(Button)findViewById(R.id.sign_up);
        requestqueue = Volley.newRequestQueue(this);

        Intent intent = new Intent(MainActivity.this, DeliveryManActivity.class);
        startActivity(intent);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(userName.getText().toString(), password.getText().toString());
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public void signIn(final String userName, final String password ){
        boolean flag = false;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userName", userName);
        map.put("passwords", password);
        JSONObject object = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, (Properties.URL+"login"), object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                signInResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(111);
                System.out.println(error);
            }
        }){


            @Override
            public Map<String, String> getHeaders()  {

                Map<String, String> map = new HashMap<String, String>();

                SharedPreferences sharedPreferences2  = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
                String name = sharedPreferences2.getString("loginUser", null);
                Long time = sharedPreferences2.getLong("loginUserTime",0);
                if(name!=null && time==0 ) {
                    if((System.currentTimeMillis()-time)>86400000)
                        map.put("cookie", name);
                }
                return map;

            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                String cookie = (String)headers.get("Set-Cookie");

                if(cookie!=null) {
                    setCookie(cookie);
                }

                return super.parseNetworkResponse(response);
            }

        };

        requestqueue.add(request);
    }

    private void signInResponse(final JSONObject result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if("0".equals(result.getString("code"))){
                        Toast.makeText(getApplicationContext(),"Sign in success", Toast.LENGTH_LONG).show();
                        System.out.println(result.toString());

                        // 2-> employee   1 -> customer
                        if("1".equals(result.get("type"))){
//                            System.out.println("111");
                        }else if("2".equals(result.get("type"))){
//                            System.out.println("222");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private  void setCookie(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                    SharedPreferences sharedPreferences = getSharedPreferences(
                            "loginUser", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("loginUser");
                    editor.remove("loginUserTime");
                    editor.putString("loginUser", result);
                    editor.putLong("loginUserTime",System.currentTimeMillis());
                    editor.commit();

            }
        });
    }

}
