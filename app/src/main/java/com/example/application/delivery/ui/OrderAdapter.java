package com.example.application.delivery.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.application.Properties;
import com.example.application.R;
import com.example.application.delivery.SharedViewModel;
import com.example.application.delivery.ui.entity.OrderEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderAdapter extends BaseAdapter {


    Context context;
    ArrayList<OrderEntity> orderEntities;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    Activity activity;
    SharedViewModel sharedViewModel;
    RequestQueue requestqueue;


    public OrderAdapter(Context context, Activity activity, ArrayList orderEntities, SharedViewModel sharedViewModel, RequestQueue requestQueue) {
        this.context = context;
        this.orderEntities = orderEntities;
        this.activity = activity;
        this.sharedViewModel = sharedViewModel;
        this.requestqueue = requestQueue;
    }

    @Override
    public int getCount() {
        return orderEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return orderEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //the component in delivery_fragment_order_item
        final TextView orderIdTextView, oderStatusTextView, customerName, customerAddress, customerPhone, orderList;
        final Button deliveryButton;

        if (!(convertView instanceof LinearLayout)) {
            convertView = LayoutInflater.from(context).inflate(R.layout.delivery_fragment_order_item, parent, false);
        }

        orderIdTextView = convertView.findViewById(R.id.order_id);
        oderStatusTextView = convertView.findViewById(R.id.delivery_order_status);
        deliveryButton = convertView.findViewById(R.id.delivery_button);
        customerName = convertView.findViewById(R.id.delivery_customer_name);
        customerAddress = convertView.findViewById(R.id.delivery_customer_address);
        customerPhone = convertView.findViewById(R.id.delivery_customer_phone);
        orderList = convertView.findViewById(R.id.delivery_order_item);


        customerName.setText(((OrderEntity) getItem(position)).getCustomerEntity().getFirstName() + " " + ((OrderEntity) getItem(position)).getCustomerEntity().getLastName());
        customerAddress.setText(((OrderEntity) getItem(position)).getCustomerEntity().getStressName());
        customerPhone.setText(((OrderEntity) getItem(position)).getCustomerEntity().getPhone());
        orderList.setText("$" + ((OrderEntity) getItem(position)).getOrderTotal().toString() + "\n" + ((OrderEntity) getItem(position)).getFoodItems());
        orderIdTextView.setText(String.valueOf(((OrderEntity) getItem(position)).getOrderId()));

        int orderStatus = ((OrderEntity) getItem(position)).getOrderStatus();

        // 0-created, 1-paid, 2-completed, 3-cancel, 4-refund
        if (orderStatus == 0) {
            oderStatusTextView.setText("Order created");
            deliveryButton.setVisibility(View.GONE);
            customerName.setVisibility(View.GONE);
            customerAddress.setVisibility(View.GONE);
            customerPhone.setVisibility(View.GONE);
            orderList.setVisibility(View.GONE);

        } else if (orderStatus == 1) {
            oderStatusTextView.setText("Wait for Delivery");
            deliveryButton.setText("Delivery");
            deliveryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    // changing the display status
                    deliveryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sharedViewModel.stopLocationFuture();
                            sharedViewModel.orderFinished(((OrderEntity) getItem(position)).getOrderId());
                            oderStatusTextView.setText("Delivered");
                            deliveryButton.setVisibility(View.GONE);
                            customerName.setVisibility(View.GONE);
                            customerAddress.setVisibility(View.GONE);
                            customerPhone.setVisibility(View.GONE);
                            orderList.setVisibility(View.GONE);
                        }
                    });
                    oderStatusTextView.setText("Delivering");
                    deliveryButton.setText("Finished");


                    getCustomerLocation(((OrderEntity) getItem(position)).getCustomerEntity().getStressName(), position);

                }
            });


        } else if (orderStatus == 2) {
            oderStatusTextView.setText("Delivered");
            deliveryButton.setVisibility(View.GONE);
            customerName.setVisibility(View.GONE);
            customerAddress.setVisibility(View.GONE);
            customerPhone.setVisibility(View.GONE);
            orderList.setVisibility(View.GONE);


        } else if (orderStatus == 3 || orderStatus == 4) {
            oderStatusTextView.setText("Order canceled");
            deliveryButton.setVisibility(View.GONE);
            customerName.setVisibility(View.GONE);
            customerAddress.setVisibility(View.GONE);
            customerPhone.setVisibility(View.GONE);
            orderList.setVisibility(View.GONE);
        } else if (orderStatus == 5) {
            oderStatusTextView.setText("Delivering");
            deliveryButton.setText("Finished");
            sharedViewModel.updateDeliveryManLocation();

            deliveryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedViewModel.stopLocationFuture();
                    sharedViewModel.orderFinished(((OrderEntity) getItem(position)).getOrderId());
                    oderStatusTextView.setText("Delivered");
                    deliveryButton.setVisibility(View.GONE);
                    customerName.setVisibility(View.GONE);
                    customerAddress.setVisibility(View.GONE);
                    customerPhone.setVisibility(View.GONE);
                    orderList.setVisibility(View.GONE);

                }
            });

        }

        return convertView;
    }


    private void getCustomerLocation(String stress, final int p) {

        stress = stress.replace(" ", "+");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://maps.googleapis.com/maps/api/geocode/json?address=" + stress + "&key=AIzaSyBoeAY3hXgYwpt-JpijXNXCz9pBR7oznDI", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String s = response.toString();
                submitResponse(s, p);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                Log.e("orderFinished---error", error.toString());
            }
        }) {
        };

        requestqueue.add(request);
    }

    public void submitResponse(String s, final int position) {
        // finding Latitude and Longitude for the destination
        Pattern tsPattern = Pattern.compile("\"lat\":[-\\d.]+");
        Matcher tsMatcher = tsPattern.matcher(s);
        String lat = null;
        if (tsMatcher.find()) {
            lat = tsMatcher.group();
            lat = lat.substring(6);
        } else {
            Log.e("lat error", "lat not found ");
        }

        tsPattern = Pattern.compile("\"lng\":[-\\d.]+");
        tsMatcher = tsPattern.matcher(s);
        String lng = null;
        if (tsMatcher.find()) {
            lng = tsMatcher.group();
            lng = lng.substring(6);

        } else {
            Log.e("lng error", "lng not found ");
        }


        //Permission verification
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            LocationProvider lProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);
            sharedViewModel.updateDeliveryManLocation();
            sharedViewModel.takeOrder(((OrderEntity) getItem(position)).getOrderId());

            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            // transfer to google maps
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ditu.google.com/maps?f=d&source=s_d&saddr="
                    + location.getLatitude()
                    + ","
                    + location.getLongitude()
                    + "&daddr="
                    + lat
                    + ","
                    + lng + "&hl=en"));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            i.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

            context.startActivity(i);
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            String[] strings = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(activity, strings, LOCATION_PERMISSION_REQUEST_CODE);

        }




    }

}
