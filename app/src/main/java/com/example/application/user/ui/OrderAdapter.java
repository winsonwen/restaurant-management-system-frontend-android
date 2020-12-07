package com.example.application.user.ui;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.android.volley.RequestQueue;
import com.example.application.Properties;
import com.example.application.R;
import com.example.application.delivery.SharedViewModel;
import com.example.application.user.ui.entity.OrderEntity;
import com.example.application.user.ui.googlemaps.MapsFragment;
import com.example.application.user.userSharedViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;

public class OrderAdapter extends BaseAdapter {
    Context context;
    ArrayList<OrderEntity> orderEntities;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    Activity activity;
    ViewGroup container;
    userSharedViewModel sharedViewModel;
    RequestQueue requestQueue;

    public OrderAdapter (Context context, Activity activity, ViewGroup container, ArrayList orderEntities, userSharedViewModel sharedViewModel, RequestQueue requestQueue) {
        this.context = context;
        this.orderEntities = orderEntities;
        this.container = container;
        this.activity = activity;
        this.sharedViewModel = sharedViewModel;
        this.requestQueue = requestQueue;
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
        final TextView orderIdTextView, oderStatusTextView, customerName, customerAddress, customerPhone, orderList;
        final Button deliveryButton, customerInfoButton;

        if (!(convertView instanceof LinearLayout)) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_fragment_order_item, parent, false);
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
                    SharedPreferences sharedPreferences = context.getSharedPreferences(Properties.STORAGE, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(Properties.Order_ID);
                    editor.putString(Properties.Order_ID, orderIdTextView.getText().toString());
                    editor.commit();
                    oderStatusTextView.setText("Delivered");
                    deliveryButton.setVisibility(View.GONE);
                    customerName.setVisibility(View.GONE);
                    customerAddress.setVisibility(View.GONE);
                    customerPhone.setVisibility(View.GONE);
                    orderList.setVisibility(View.GONE);
                    startChecking();
                }
            });


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
        controller.navigate(R.id.nav_googlemaps_user);
    }
}

