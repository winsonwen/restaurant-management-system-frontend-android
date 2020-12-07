package com.example.application.user.ui;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.application.R;
import com.example.application.user.ui.entity.OrderEntity;
import com.example.application.user.ui.googlemaps.MapsFragment;

import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;

public class OrderAdapter extends BaseAdapter {
    Context context;
    ArrayList<OrderEntity> orderEntities;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    Activity activity;
    ViewGroup container;

    public OrderAdapter (Context context, Activity activity, ViewGroup container, ArrayList orderEntities) {
        this.context = context;
        this.orderEntities = orderEntities;
        this.container = container;
        this.activity = activity;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView orderIdTextView, oderStatusTextView, customerName, customerAddress, customerPhone, orderList;
        Button deliveryButton, customerInfoButton;

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
        orderIdTextView.setText(String.valueOf(((OrderEntity) getItem(position)).getOrderId()));

        int orderStatus = ((OrderEntity) getItem(position)).getOrderStatus();

        // 0-created, 1-paid, 2-completed, 3-cancel, 4-refund
        if(orderStatus==0){
            oderStatusTextView.setText("Order created");
            deliveryButton.setVisibility(View.GONE);
            customerName.setVisibility(View.GONE);
            customerAddress.setVisibility(View.GONE);
            customerPhone.setVisibility(View.GONE);
            orderList.setVisibility(View.GONE);

        } else if(orderStatus==1){
            oderStatusTextView.setText("Wait for Delivery");
            deliveryButton.setText("Delivery");
            deliveryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startChecking();
                }
            });


            customerName.setText( ((OrderEntity) getItem(position)).getUserEntity().getFirstName() + " " + ((OrderEntity) getItem(position)).getUserEntity().getLastName() );

            customerAddress.setText( ((OrderEntity) getItem(position)).getUserEntity().getStressName());
            customerPhone.setText( ((OrderEntity) getItem(position)).getUserEntity().getPhone());
            orderList.setText( "$" + ((OrderEntity) getItem(position)).getOrderTotal().toString() + "\n" +  ((OrderEntity) getItem(position)).getFoodItems()   );

        }else if(orderStatus==2){
            oderStatusTextView.setText("Delivered");
            deliveryButton.setVisibility(View.GONE);
            customerName.setVisibility(View.GONE);
            customerAddress.setVisibility(View.GONE);
            customerPhone.setVisibility(View.GONE);
            orderList.setVisibility(View.GONE);
        }else if(orderStatus==3 || orderStatus==4 ){
            oderStatusTextView.setText("Order canceled");
            deliveryButton.setVisibility(View.GONE);
            customerName.setVisibility(View.GONE);
            customerAddress.setVisibility(View.GONE);
            customerPhone.setVisibility(View.GONE);
            orderList.setVisibility(View.GONE);
        }

        return convertView;
    }

    private void startChecking() {
        NavController controller = Navigation.findNavController(container);
        controller.navigate(R.id.nav_googlemaps);

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        LocationProvider lProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);



            String loca = "location" + "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
            Toast.makeText(context, loca, Toast.LENGTH_SHORT).show();

        } else {
            // Permission to access the location is missing. Show rationale and request permission
            String[] strings = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(activity, strings, LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

}
