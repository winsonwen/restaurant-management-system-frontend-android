package com.example.application.user.ui.orderList;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.application.R;
import com.example.application.user.ui.OrderAdapter;
import com.example.application.user.ui.entity.OrderEntity;
import com.example.application.user.ui.entity.UserEntity;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    //    private HomeViewModel homeViewModel;
    private Button button;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private ListView listView;
    ArrayList<OrderEntity> orderEntities = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        UserEntity userEntity = new UserEntity("Jack", "Lin", "2671111111","1801 N Broad St, Philadelphia, PA 19122");

        OrderEntity orderEntity0 = new OrderEntity(1,"Food List", 1,100.00,userEntity,0);
        OrderEntity orderEntity1 = new OrderEntity(2,"Food List", 1,100.00,userEntity,1);
        OrderEntity orderEntity2 = new OrderEntity(3,"Food List", 1,100.00,userEntity,2);
        OrderEntity orderEntity3 = new OrderEntity(4,"Food List", 1,100.00,userEntity,3);
        OrderEntity orderEntity4 = new OrderEntity(5,"Food List", 1,100.00,userEntity,4);

        orderEntities.add(orderEntity0);
        orderEntities.add(orderEntity1);
        orderEntities.add(orderEntity2);
        orderEntities.add(orderEntity3);
        orderEntities.add(orderEntity4);

        View root = inflater.inflate(R.layout.delivery_fragment_order, container, false);

        listView = (ListView)root.findViewById(R.id.oder_list_view);
        listView.setAdapter(new OrderAdapter(getContext(), getActivity(), container, orderEntities));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView  customerName, customerAddress, customerPhone, orderList;

                customerName = view.findViewById(R.id.delivery_customer_name);
                customerAddress = view.findViewById(R.id.delivery_customer_address);
                customerPhone = view.findViewById(R.id.delivery_customer_phone);
                orderList = view.findViewById(R.id.delivery_order_item);

                int visibility = customerName.getVisibility();
                if(visibility==View.GONE) {
                    OrderEntity item = (OrderEntity) listView.getAdapter().getItem(position);
                    customerName.setText(item.getUserEntity().getFirstName() + " " + item.getUserEntity().getLastName());
                    customerName.setVisibility(View.VISIBLE);
                    customerAddress.setVisibility(View.VISIBLE);
                    customerPhone.setVisibility(View.VISIBLE);
                    orderList.setVisibility(View.VISIBLE);

                    customerAddress.setText(item.getUserEntity().getStressName());
                    customerPhone.setText(item.getUserEntity().getPhone());
                    orderList.setText("$" + item.getOrderTotal().toString() + "\n" + item.getFoodItems());
                }else if(visibility==View.VISIBLE){
                    customerName.setVisibility(View.GONE);
                    customerAddress.setVisibility(View.GONE);
                    customerPhone.setVisibility(View.GONE);
                    orderList.setVisibility(View.GONE);
                }
            }
        });

        return root;
    }


}