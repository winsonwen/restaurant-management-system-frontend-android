package com.example.application.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.application.MainActivity;
import com.example.application.Properties;
import com.example.application.R;
import com.example.application.user.UI.Interface.userFragmentInterface;
import com.example.application.user.UI.userAccount.userFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity implements userFragmentInterface.LogOutListener {
    TextView editText;
    Button account, order, tracking, logout;
    RequestQueue requestqueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        editText = (TextView) findViewById(R.id.username);
        account = (Button) findViewById(R.id.Account);
        order = (Button) findViewById(R.id.order);
        tracking = (Button) findViewById(R.id.tracking);
        logout = (Button)findViewById(R.id.logout);

        SharedPreferences sp = getSharedPreferences(Properties.STORAGE, Context.MODE_PRIVATE);
        String info = sp.getString(Properties.MEMBER_INFO, null);
        try{
            JSONObject result = new JSONObject(info);
            editText.setText(result.getString("firstName") + ",  " + result.getString("lastName"));
        }catch (JSONException e){
            e.printStackTrace();
        }
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new userFragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.add(R.id.fragment, fragment, null).addToBackStack(null).commit();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOutInteraction();
            }
        });
    }

    public void logOutRequest() {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, (Properties.URL+"logout"), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.v("User","Logout successfully");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("User","logout error" );
                }
            }){
                @Override
                public Map<String, String> getHeaders()  {
                    Map<String, String> map = new HashMap<String, String>();

                    // get session from storage
                    SharedPreferences sharedPreferences2  = getSharedPreferences(Properties.STORAGE, Context.MODE_PRIVATE);
                    String name = sharedPreferences2.getString(Properties.USER_SESS, null);
                    Long time = sharedPreferences2.getLong(Properties.USER_LOGIN_TIME,0);

                    //clear cookie
                    SharedPreferences.Editor editor = sharedPreferences2.edit();
                    editor.remove(Properties.USER_SESS).remove(Properties.USER_LOGIN_TIME).remove(Properties.MEMBER_INFO).commit();

                    //set session for http request
                    if(name!=null && time!=0 ) {
                        if((System.currentTimeMillis()-time)<86400000)
                            map.put("cookie", name);
                    }
                    return map;

                }
            };
            requestqueue.add(request);

        }

    @Override
    public void LogOutInteraction() {
        this.logOutRequest();

        Intent intent = new Intent(UserActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
