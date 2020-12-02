package com.example.application.delivery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.application.MainActivity;
import com.example.application.Properties;
import com.example.application.R;
import com.example.application.SignUpActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SharedViewModel extends AndroidViewModel {

    private RequestQueue requestqueue;

    private MutableLiveData<JSONObject> result = new MutableLiveData<>();
    private MutableLiveData<JSONObject> response = new MutableLiveData<>();
    CompletableFuture<Void> locationFuture;

    // Create a Thread pool
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS,
            new LinkedBlockingDeque(100000), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    public SharedViewModel(@NonNull Application application) {
        super(application);

        SharedPreferences sharedPreferences2 = getApplication().getSharedPreferences(Properties.STORAGE, Context.MODE_PRIVATE);
        String employeeInfo = sharedPreferences2.getString(Properties.EMPLOYEE_INFO, null);

        this.requestqueue = Volley.newRequestQueue(getApplication());
        try {
            JSONObject result = new JSONObject(employeeInfo);
            this.result.setValue(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateDeliveryManLocation() {

        locationFuture = CompletableFuture.runAsync(new Runnable() {
            @SuppressLint("MissingPermission")
            @Override
            public void run() {

                LocationManager locationManager = (LocationManager) SharedViewModel.this.getApplication().getSystemService(Context.LOCATION_SERVICE);


                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                while (true) {

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("latitude", location.getLatitude() + "");
                    map.put("longitude", location.getLongitude() + "");

                    JSONObject object = new JSONObject(map);

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Properties.URL + "updateDeliveryManLocation", object, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            updateDeliveryManLocationResponse(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error);
                            Log.e("update_delivery_man_location---error", error.toString());
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> map = new HashMap<String, String>();

                            // get session from storage
                            SharedPreferences sharedPreferences2 = getApplication().getSharedPreferences(Properties.STORAGE, Context.MODE_PRIVATE);
                            Long time = sharedPreferences2.getLong(Properties.USER_LOGIN_TIME, 0);
                            String session = sharedPreferences2.getString(Properties.USER_SESS, "");
                            // set session for http request
                            if (session != null && time != 0) {
                                if ((System.currentTimeMillis() - time) < 86400000) {
                                    map.put("cookie", session);
                                    map.put("type", "staff");
                                }
                            }
                            return map;

                        }
                    };
                    requestqueue.add(request);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, threadPoolExecutor);


    }

    public void updateDeliveryManLocationResponse(final JSONObject result) {

        try {
            if ("404".equals(result.getString("code"))) {
                Log.e("404","update location error, ple contact Admin" );
//                Toast.makeText(getApplication(), "update location error, ple contact Admin", Toast.LENGTH_SHORT).show();

            }
            System.out.println(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void stopLocationFuture(){
//        System.out.println(locationFuture);
//        if(locationFuture!=null) {
            locationFuture.cancel(false);
//        }
    }

    // http request for delivery man info update
    public void updateInfo(String firstName, String lastName, String phoneNumber, String email) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("employeeId", "");
        map.put("firstName", firstName);
        map.put("lastName", lastName);
        map.put("phone", phoneNumber);
        map.put("email", email);
        JSONObject object = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Properties.URL + "update_delivery_info", object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //  processed results
                submitResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                Log.e("updateInfo---error", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<String, String>();

                // get session from storage
                SharedPreferences sharedPreferences2 = getApplication().getSharedPreferences(Properties.STORAGE, Context.MODE_PRIVATE);
                Long time = sharedPreferences2.getLong(Properties.USER_LOGIN_TIME, 0);

                String session = sharedPreferences2.getString(Properties.USER_SESS, "");

//                set session for http request
                if (session != null && time != 0) {
                    if ((System.currentTimeMillis() - time) < 86400000) {
                        map.put("cookie", session);
                        map.put("type", "staff");
                    }
                }
                return map;

            }
        };

        requestqueue.add(request);
    }




    public JSONObject getRes() {
        return this.result.getValue();
    }


    public void setRequestqueue(RequestQueue requestqueue) {
        this.requestqueue = requestqueue;
    }

    //  processed results from UpdateInfo()
    public void submitResponse(final JSONObject result) {

        try {
            if ("0".equals(result.getString("code"))) {
                JSONObject data = result.getJSONObject("data");
                this.setRes(data);
            } else {
                this.setResponse(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setRes(JSONObject res) {
        this.result.setValue(res);
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


    // updating order state after delivery man pick up the order
    public void takeOrder(int orderId) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("orderId", String.valueOf(orderId));

        JSONObject object = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Properties.URL + "takeOrder", object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                Log.e("takeOrder---error", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<String, String>();

                // get session from storage
                SharedPreferences sharedPreferences2 = getApplication().getSharedPreferences(Properties.STORAGE, Context.MODE_PRIVATE);
                Long time = sharedPreferences2.getLong(Properties.USER_LOGIN_TIME, 0);

                String session = sharedPreferences2.getString(Properties.USER_SESS, "");

//                set session for http request
                if (session != null && time != 0) {
                    if ((System.currentTimeMillis() - time) < 86400000) {
                        map.put("cookie", session);
                        map.put("type", "staff");
                    }
                }
                return map;

            }
        };

        requestqueue.add(request);




    }

    public void orderFinished(int orderId) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("orderId", String.valueOf(orderId));

        JSONObject object = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Properties.URL + "orderFinished", object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                Log.e("orderFinished---error", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<String, String>();

                // get session from storage
                SharedPreferences sharedPreferences2 = getApplication().getSharedPreferences(Properties.STORAGE, Context.MODE_PRIVATE);
                Long time = sharedPreferences2.getLong(Properties.USER_LOGIN_TIME, 0);

                String session = sharedPreferences2.getString(Properties.USER_SESS, "");
//                set session for http request
                if (session != null && time != 0) {
                    if ((System.currentTimeMillis() - time) < 86400000) {
                        map.put("cookie", session);
                        map.put("type", "staff");
                    }
                }
                return map;

            }
        };

        requestqueue.add(request);
    }
}
