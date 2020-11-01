package com.example.application.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

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
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity implements LogOutFragment.LogOutListener{
    private AppBarConfiguration mAppBarConfiguration;
    Button logoutb;
    RequestQueue requestqueue;
    TextView textView5;
    TextView textView;
    private userSharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_user);
        Toolbar toolbar = findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);
        requestqueue = Volley.newRequestQueue(this);


        sharedViewModel = ViewModelProviders.of(this).get(userSharedViewModel.class);
        FloatingActionButton fab = findViewById(R.id.fab_user);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout_user);
        NavigationView navigationView = findViewById(R.id.nav_view_user);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_user, R.id.nav_gallery_user, R.id.nav_logout_user, R.id.nav_googlemaps_user)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_user);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // settings
        getMenuInflater().inflate(R.menu.user, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_user);

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

        Intent intent = new Intent(UserActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void logOutRequest(){

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
                editor.remove(Properties.USER_SESS).remove(Properties.USER_LOGIN_TIME).remove(Properties.USER_INFO).commit();

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
