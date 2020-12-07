package com.example.application.user;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.application.Properties;
import com.example.application.delivery.SharedViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class userSharedViewModel extends AndroidViewModel {

    private RequestQueue requestqueue,locationrequestqueue;

    private MutableLiveData<JSONObject> result = new MutableLiveData<>();
    private MutableLiveData<JSONObject> response = new MutableLiveData<>();

    public userSharedViewModel(@NonNull Application application) {
        super(application);

        SharedPreferences sharedPreferences2  = getApplication().getSharedPreferences(Properties.STORAGE, Context.MODE_PRIVATE);
        String employeeInfo = sharedPreferences2.getString(Properties.USER_INFO, null);

        this.requestqueue = Volley.newRequestQueue(getApplication());
        try {
            JSONObject result = new JSONObject(employeeInfo);
            this.result.setValue(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // http request for delivery info update
    public void UpdateInfo(String firstName, String lastName, String phoneNumber, String email){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("memberId", "");
        map.put("firstName", firstName);
        map.put("lastName", lastName);
        map.put("phone", phoneNumber);
        map.put("email", email);
        JSONObject object = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Properties.URL + "update_user_info", object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                submitResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> map = new HashMap<String, String>();

                // get session from storage
                SharedPreferences sharedPreferences2  = getApplication().getSharedPreferences(Properties.STORAGE, Context.MODE_PRIVATE);
                Long time = sharedPreferences2.getLong(Properties.USER_LOGIN_TIME,0);

                String name = sharedPreferences2.getString(Properties.USER_SESS, "");

//                set session for http request
                if(name!=null && time!=0 ) {
                    if((System.currentTimeMillis()-time)<86400000)
                        map.put("cookie", name);
                }
                return map;

            }
        };

        requestqueue.add(request);
    }

    public JSONObject getRes() {
        return this.result.getValue();
    }

    public void setRes(JSONObject res) {
        this.result.setValue(res);
    }

    public void setRequestqueue(RequestQueue requestqueue) {
        this.requestqueue = requestqueue;
    }


    public void submitResponse(final JSONObject result) {

        try {
            if("0".equals(result.getString("code"))){
                JSONObject data = result.getJSONObject("data");
                this.setRes(data);
            }else{
                this.setResponse(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setResponse(JSONObject res) {
        this.response.setValue(res);
    }

    public MutableLiveData<JSONObject> getResult() {
        return this.result;
    }

    public MutableLiveData<JSONObject> getResponse() {
        return this.response;
    }

}
