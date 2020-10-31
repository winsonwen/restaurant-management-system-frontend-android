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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.application.delivery.DeliveryManActivity;
import com.example.application.user.UserActivity;

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

//        Intent intent = new Intent(MainActivity.this, DeliveryManActivity.class);
//        startActivity(intent);

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

//                SharedPreferences sharedPreferences2  = getSharedPreferences(Properties.USER_SESS, Context.MODE_PRIVATE);
//                String name = sharedPreferences2.getString(Properties.USER_SESS, null);
//                Long time = sharedPreferences2.getLong(Properties.USER_LOGIN_TIME,0);
//                if(name!=null && time==0 ) {
//                    if((System.currentTimeMillis()-time)<86400000)
//                        map.put("cookie", name);
//                }
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
                    System.out.println(result);
                    String code = result.getString("code");
                    if("0".equals(code)){
                        Toast.makeText(getApplicationContext(),"Sign in success", Toast.LENGTH_LONG).show();

                        // 2-> employee
                        if("2".equals(result.get("type"))){

                            System.out.println("Employee Signed in");
                            JSONObject memberInfo = result.getJSONObject(Properties.EMPLOYEE_INFO);
                            System.out.println(memberInfo.toString());

                            SharedPreferences sharedPreferences = getSharedPreferences(
                                    Properties.STORAGE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove(Properties.EMPLOYEE_INFO);
                            editor.putString(Properties.EMPLOYEE_INFO,memberInfo.toString());
                            editor.commit();
                            

                            Intent intent = new Intent(MainActivity.this, DeliveryManActivity.class);
                            startActivity(intent);
                        //  1 -> customer
                        }else if("1".equals(result.get("type"))){

                            System.out.println("customer signed in");
                            JSONObject memberInfo = result.getJSONObject(Properties.USER_INFO);
                            System.out.println(memberInfo.toString());

                            SharedPreferences sharedPreferences = getSharedPreferences(
                                    Properties.STORAGE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove(Properties.USER_INFO);
                            editor.putString(Properties.USER_INFO, memberInfo.toString());
                            editor.commit();

                            Intent intent = new Intent(MainActivity.this, UserActivity.class);
                            startActivity(intent);

                        }
                    }else if("402".equals(code)){
                        TextView textView = (TextView) findViewById(R.id.textView13);
                        textView.setText(result.getString("msg"));

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
                            Properties.STORAGE, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(Properties.USER_SESS);
                    editor.remove(Properties.USER_LOGIN_TIME);
                    editor.putString(Properties.USER_SESS, result);
                    editor.putLong(Properties.USER_LOGIN_TIME,System.currentTimeMillis());
                    editor.commit();

            }
        });
    }

}
