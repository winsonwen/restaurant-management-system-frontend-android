package com.example.application.delivery.ui.orderList;

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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.application.R;
import com.example.application.delivery.ui.OrderAdapter;
import com.example.application.delivery.ui.entity.CustomerEntity;
import com.example.application.delivery.ui.entity.OrderEntity;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    //    private HomeViewModel homeViewModel;
    private Button button;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private ListView listView;
    ArrayList<OrderEntity> orderEntities = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        // Google maps
        //nav transfer
        // NavController controller= Navigation.findNavController(container);
        //  controller.navigate(R.id.nav_googlemaps);

        CustomerEntity customerEntity = new CustomerEntity("Joe", "Jack", "2671111111","1801 N Broad St, Philadelphia, PA 19122");

        OrderEntity orderEntity0 = new OrderEntity(1,"Food List", 1,100.00,customerEntity,0);
        OrderEntity orderEntity1 = new OrderEntity(2,"Food List", 1,100.00,customerEntity,1);
        OrderEntity orderEntity2 = new OrderEntity(3,"Food List", 1,100.00,customerEntity,2);
        OrderEntity orderEntity3 = new OrderEntity(4,"Food List", 1,100.00,customerEntity,3);
        OrderEntity orderEntity4 = new OrderEntity(5,"Food List", 1,100.00,customerEntity,4);

        orderEntities.add(orderEntity0);
        orderEntities.add(orderEntity1);
        orderEntities.add(orderEntity2);
        orderEntities.add(orderEntity3);
        orderEntities.add(orderEntity4);


        View root = inflater.inflate(R.layout.delivery_fragment_order, container, false);

        button = (Button) root.findViewById(R.id.button2);


        listView = (ListView)root.findViewById(R.id.oder_list_view);
        listView.setAdapter(new OrderAdapter(getContext(), getActivity(), orderEntities));

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
                    customerName.setText(item.getCustomerEntity().getFirstName() + " " + item.getCustomerEntity().getLastName());
                    customerName.setVisibility(View.VISIBLE);
                    customerAddress.setVisibility(View.VISIBLE);
                    customerPhone.setVisibility(View.VISIBLE);
                    orderList.setVisibility(View.VISIBLE);

                    customerAddress.setText(item.getCustomerEntity().getStressName());
                    customerPhone.setText(item.getCustomerEntity().getPhone());
                    orderList.setText("$" + item.getOrderTotal().toString() + "\n" + item.getFoodItems());
                }else if(visibility==View.VISIBLE){
                    customerName.setVisibility(View.GONE);
                    customerAddress.setVisibility(View.GONE);
                    customerPhone.setVisibility(View.GONE);
                    orderList.setVisibility(View.GONE);
                }

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Jump to google map and Start navigation from current location to temple university
                LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                LocationProvider lProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    String loca = "location" + "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
                    Toast.makeText(getContext(), loca, Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ditu.google.com/maps?f=d&source=s_d&saddr="
                            + location.getLatitude()
                            + ","
                            + location.getLongitude()
                            + "&daddr="
                            + 39.978768
                            + ","
                            + -75.154054 + "&hl=en"));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    i.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(i);

                } else {
                    // Permission to access the location is missing. Show rationale and request permission
                    String[] strings = {Manifest.permission.ACCESS_FINE_LOCATION};
                    ActivityCompat.requestPermissions(getActivity(), strings, LOCATION_PERMISSION_REQUEST_CODE);

                }
            }
        });


//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel.class);
//        View root = inflater.inflate(R.layout.delivery_fragment_order, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }


}