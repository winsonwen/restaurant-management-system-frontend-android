package com.example.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class signUpActivity extends Activity {
    RequestQueue requestqueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final EditText username, password, comfirmPassword,firstName, lastName, phoneNumber,email;
        final Button submit;
        username = (EditText)findViewById(R.id.Username);
        password = (EditText)findViewById(R.id.Password);
        comfirmPassword = (EditText)findViewById(R.id.Password2);
        firstName = (EditText)findViewById(R.id.Firstname);
        lastName = (EditText)findViewById(R.id.Lastname);
        phoneNumber = (EditText)findViewById(R.id.Phone);
        email = (EditText)findViewById(R.id.Email);
        submit = (Button)findViewById(R.id.Submit);
        requestqueue = Volley.newRequestQueue(this);


        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(comfirmPassword.getText().toString()) == true) {
                    submit(username.getText().toString(), password.getText().toString(), firstName.getText().toString(), lastName.getText().toString(), phoneNumber.getText().toString(), email.getText().toString());

                }else{
                    Toast.makeText(getApplicationContext(),"Sign up failed, passwords does not match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void submit(final String username, final String password, final String firstName, final String lastName, final String phoneNumber, final String Email){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userName", username);
        map.put("passwords", password);
        map.put("firstName", firstName);
        map.put("lastName", lastName);
        map.put("phoneNumber", phoneNumber);
        map.put("email", Email);
        JSONObject object = new JSONObject(map);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Properties.URL + "signUp", object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                submitResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        requestqueue.add(request);
    }

    private void submitResponse(final JSONObject result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if("0".equals(result.getString("code"))){
                        Toast.makeText(getApplicationContext(),"Sign up success", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(signUpActivity.this, MainActivity.class);
                        startActivity(intent);

                    }else {
                        JSONObject obj = result.getJSONObject("data");

                        Map<String,String> map = new HashMap<>();
                        Iterator<String> iterator = obj.keys();
                        while(iterator.hasNext())
                        {
                            String key = (String)iterator.next();
                            String value = (String) obj.get(key);
                            map.put(key, value);
                        }
                        String msg;
                        System.out.println(map);
                        msg = map.get("firstName");
                        if(msg!=null){
                            TextView textView = (TextView)findViewById(R.id.firstRes);
                            textView.setText(msg);

                        }

                        msg = map.get("lastName");
                        if(msg!=null){
                            TextView textView = (TextView)findViewById(R.id.lastRes);
                            textView.setText(msg);

                        }
                        msg = map.get("phoneNumber");
                        if(msg!=null){
                            TextView textView = (TextView)findViewById(R.id.phoneRes);
                            textView.setText(msg);

                        }
                        msg = map.get("userName");
                        if(msg!=null){
                            TextView textView = (TextView)findViewById(R.id.userRes);
                            textView.setText(msg);

                        }
                        msg = map.get("email");
                        if(msg!=null){
                            TextView textView = (TextView)findViewById(R.id.emailRes);
                            textView.setText(msg);
                        }
                        msg = map.get("passwords");
                        if(msg!=null){
                            TextView textView = (TextView)findViewById(R.id.passRes);
                            textView.setText(msg);
                        }
                    }
                } catch (JSONException e) {
                        e.printStackTrace();
                }




            }
        });
    }
}
