package com.example.application.delivery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.application.MainActivity;
import com.example.application.Properties;
import com.example.application.R;
import com.example.application.delivery.ui.logout.LogOutFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.Permission;
import java.util.HashMap;
import java.util.Map;

public class DeliveryManActivity extends AppCompatActivity  implements LogOutFragment.LogOutListener {

    private AppBarConfiguration mAppBarConfiguration;
    Button logoutb;
    RequestQueue requestqueue;
    TextView textView5;
    TextView textView;
    private SharedViewModel sharedViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_activity_delivery_man);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        requestqueue = Volley.newRequestQueue(this);

        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel.class);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_logout, R.id.nav_googlemaps)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // settings
        getMenuInflater().inflate(R.menu.delivery_man, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        textView = findViewById(R.id.textView);
        textView5 = findViewById(R.id.textView5);

        JSONObject result = sharedViewModel.getRes();
        try {
            textView.setText(result.getString("email"));
            textView5.setText( result.getString("firstName") +" "+result.getString("lastName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void LogOutInteraction() {
        this.logOutRequest();

        Intent intent = new Intent(DeliveryManActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void logOutRequest(){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, (Properties.URL+"logout"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.v("DeliveryMan","Logout successfully");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("DeliveryMan","logout error" );
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
                editor.remove(Properties.USER_SESS).remove(Properties.USER_LOGIN_TIME).remove(Properties.EMPLOYEE_INFO).commit();

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


}