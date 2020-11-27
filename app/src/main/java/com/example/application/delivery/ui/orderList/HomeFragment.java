package com.example.application.delivery.ui.orderList;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.application.Properties;
import com.example.application.R;
import com.example.application.delivery.SharedViewModel;
import com.example.application.delivery.ui.OrderAdapter;
import com.example.application.delivery.ui.entity.CustomerEntity;
import com.example.application.delivery.ui.entity.OrderEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    //    private HomeViewModel homeViewModel;
    private Button button;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private ListView listView;
    ArrayList<OrderEntity> orderEntities = new ArrayList<>();
    SharedViewModel sharedViewModel;
    RequestQueue requestQueue;
    LayoutInflater inflater;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        // Google maps
        //nav transfer
        // NavController controller= Navigation.findNavController(container);
        //  controller.navigate(R.id.nav_googlemaps);

        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel .class);

        CustomerEntity customerEntity = new CustomerEntity("Joe", "Jack", "2671111111","18i01 N Broad St, Philadelphia, PA 19122");

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

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        getOrderInfo();
        this.inflater = inflater;

        this.root = inflater.inflate(R.layout.delivery_fragment_order, container, false);

//        button = (Button) root.findViewById(R.id.button2);




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

    private void getOrderInfo() {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Properties.URL + "getOrderInfo", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //  processed results
                getOrderInfoResult(response);
//                submitResponse(response);
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
                SharedPreferences sharedPreferences2 = getContext().getSharedPreferences(Properties.STORAGE, Context.MODE_PRIVATE);
                Long time = sharedPreferences2.getLong(Properties.USER_LOGIN_TIME, 0);

                String session = sharedPreferences2.getString(Properties.USER_SESS, "");

//                set session for http request
                if (session != null && time != 0) {
                    if ((System.currentTimeMillis() - time) < 86400000) {
                        map.put("cookie", session);
                        map.put("type","staff");
                    }
                }
                return map;

            }
        };

        requestQueue.add(request);
    }

    public void getOrderInfoResult(final JSONObject result){
        try {
            orderEntities = new ArrayList<>();
            if("0".equals(result.getString("code"))){
                JSONArray orders = result.getJSONArray("orderEntities");
                for (int i=0; i<orders.length(); i++){
                    JSONObject jsonObject = orders.getJSONObject(i);
                    JSONObject customerEntity1 = jsonObject.getJSONObject("customerEntity");
                    CustomerEntity customerEntity = new CustomerEntity(customerEntity1.getString("firstName"),customerEntity1.getString("lastName"),customerEntity1.getString("phone"),customerEntity1.getString("stressName"));
                    OrderEntity orderEntity = new OrderEntity(jsonObject.getInt("orderId"),jsonObject.getString("foodItems"),jsonObject.getInt("quantity"),jsonObject.getDouble("orderTotal"), customerEntity,jsonObject.getInt("orderStatus"));

                    System.out.println(orderEntity.toString());
                    orderEntities.add(orderEntity);

                }

                listView = (ListView)root.findViewById(R.id.oder_list_view);
                listView.setAdapter(new OrderAdapter(getContext(), getActivity(), orderEntities, sharedViewModel));

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

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}