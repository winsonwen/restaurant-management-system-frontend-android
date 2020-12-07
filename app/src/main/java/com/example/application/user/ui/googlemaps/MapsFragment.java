package com.example.application.user.ui.googlemaps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.application.Properties;
import com.example.application.R;
import com.example.application.user.userSharedViewModel;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MapsFragment extends Fragment implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener{

    private boolean permissionDenied = false;
    private static final int Location_Permission_Request_Code =1;
    private GoogleMap map;
    private LatLng location, deliveryLocation, newLocation;
    private String latitude, longitude;
    private boolean delivery = true;
    private RequestQueue requestqueue;
    MarkerOptions place1, place2;

    private void enableMyLocation() {
        System.out.println(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION));
        System.out.println(PackageManager.PERMISSION_GRANTED);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            System.out.println("11111111");
            if (map != null) {
                System.out.println("333333");
                map.setMyLocationEnabled(true);
                map.setOnMyLocationButtonClickListener(this);
                map.setOnMyLocationClickListener(this);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            String[] strings = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(getActivity(), strings, Location_Permission_Request_Code);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.user_fragment_maps, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestqueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Properties.STORAGE, Context.MODE_PRIVATE);
        final String id = sharedPreferences.getString(Properties.Order_ID, null);
        if(id != null) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask(){
                @Override
                public void run() {
                    updateDeliveryLocation(Integer.parseInt(id));
                    deliveryLocation = newLocation;
                    System.out.println(deliveryLocation);
                }
            };

            if(delivery == false){
                timer.cancel();
            }else{
                timer.schedule(task, 1000);
            }
        }else{
            System.out.println("There is a connection error between the server, no location data received");
        }

    }

    private void updateDeliveryLocation(int parseInt) {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        LocationProvider lProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            final HashMap<String, Integer> map = new HashMap<String, Integer>();
            map.put("orderId", parseInt);
            final JSONObject object = new JSONObject(map);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Properties.URL + "getDeliveryLocation", object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response);
                    try {
                        if ("404".equals(response.getString("code"))) {
                            Log.e("404", "update location error, ple contact Admin");
                        } else {
                            JSONObject newObject = response.getJSONObject("locationInfo");
                            System.out.println(newObject);
                            System.out.println("test latitude value ");
                            latitude = newObject.get("latitude").toString();
                            System.out.println(latitude + "  try new thing   " );
                            longitude = newObject.get("longitude").toString();
                            System.out.println(latitude + "  try new thing   " + longitude);
                            setLocation(latitude, longitude);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> map = new HashMap<String, String>();
                    // get session from storage
                    SharedPreferences sharedPreferences2 = getContext().getSharedPreferences(Properties.STORAGE, Context.MODE_PRIVATE);
                    Long time = sharedPreferences2.getLong(Properties.USER_LOGIN_TIME, 0);
                    String session = sharedPreferences2.getString(Properties.USER_SESS, "");
                    // set session for http request
                    if (session != null && time != 0) {
                        if ((System.currentTimeMillis() - time) < 86400000) {
                            map.put("cookie", session);
                        }
                    }
                    return map;
                }
            };
            requestqueue.add(request);
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            String[] strings = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(getActivity(), strings, Location_Permission_Request_Code);
        }
    }

    @SuppressLint("MissingPermission")
    public void setLocation(String latitude, String longitude){
        enableMyLocation();
        newLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        LatLng test = new LatLng(37.00, -120.08400000000002);
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        LocationProvider lProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);
        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location locations=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        String locaLatitude = "" + locations.getLatitude();
        String locaLongitude = "" + locations.getLongitude();
        System.out.println("test out the delivery :" +newLocation);
        System.out.println(locaLatitude + " try new thing and do new thing " + locaLongitude);
        drawRoute(newLocation, test);

    }

    public void drawRoute(LatLng deliveryLocation, LatLng location){

        //map.addMarker(new MarkerOptions().position(location).title("user location"));
        place1 = new MarkerOptions().position(deliveryLocation).title("delivery");
        place2 = new MarkerOptions().position(location).title("user");
        OnMapReadyCallback callback = new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.addMarker(place1);
                googleMap.addMarker(place2);
            }
        };
        System.out.println("show map");
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        LocationProvider lProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location locations=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            location = new LatLng(locations.getLatitude(), locations.getLongitude());
            String loca = "location" + "("  + locations.getLatitude() + ", " + locations.getLongitude()+")";
            Toast.makeText( getContext(), loca, Toast.LENGTH_SHORT).show();
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            String[] strings = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(getActivity(), strings, Location_Permission_Request_Code);
        }
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        System.out.println(" onMyLocationClick");
        Toast.makeText(getActivity(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }


}