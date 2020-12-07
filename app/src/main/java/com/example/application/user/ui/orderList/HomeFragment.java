package com.example.application.user.ui.orderList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.application.user.ui.OrderAdapter;
import com.example.application.user.ui.entity.OrderEntity;
import com.example.application.user.ui.entity.UserEntity;
import com.example.application.user.userSharedViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    //    private HomeViewModel homeViewModel;
    private Button button;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private ListView listView;
    ArrayList<OrderEntity> orderEntities = new ArrayList<>();
    userSharedViewModel sharedViewModel;
    RequestQueue requestQueue;
    LayoutInflater inflater;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        sharedViewModel = ViewModelProviders.of(this).get(userSharedViewModel.class);
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Properties.URL + "getUserOrderInfo", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    orderEntities = new ArrayList<>();
                    if("0".equals(response.getString("code"))){
                        JSONArray orders = response.getJSONArray("orderEntities");
                        for (int i=0; i<orders.length(); i++){
                            JSONObject object = orders.getJSONObject(i);
                            OrderEntity orderEntity = new OrderEntity(object.getInt("orderId"),object.getString("foodItems"),object.getInt("quantity"),object.getDouble("orderTotal"),null,object.getInt("orderStatus"));
                            orderEntities.add(orderEntity);
                        }

                        listView = (ListView)root.findViewById(R.id.oder_list_view);
                        listView.setAdapter(new OrderAdapter(getContext(), getActivity(), container, orderEntities, sharedViewModel, requestQueue));
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                TextView customerName, customerAddress, customerPhone, orderList;

                                customerName = view.findViewById(R.id.delivery_customer_name);
                                orderList = view.findViewById(R.id.delivery_order_item);

                                int visibility = customerName.getVisibility();
                                if(visibility==View.GONE) {
                                    OrderEntity item = (OrderEntity) listView.getAdapter().getItem(position);
                                    orderList.setText("$" + item.getOrderTotal().toString() + "\n" + item.getFoodItems());
                                }else if(visibility==View.VISIBLE){
                                    orderList.setVisibility(View.GONE);
                                }

                            }
                        });
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }){
            public Map<String, String> getHeaders(){
                Map<String, String> map = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(Properties.STORAGE, Context.MODE_PRIVATE);
                Long time = sharedPreferences.getLong(Properties.USER_LOGIN_TIME, 0);

                String session = sharedPreferences.getString(Properties.USER_SESS, "");
                if(session != null && time != 0){
                    if((System.currentTimeMillis() - time) < 86400000){
                        map.put("cookie", session);
                        map.put("type", "user");
                    }
                }
                return map;
            }
        };
        requestQueue.add(request);

        this.inflater = inflater;

        this.root = inflater.inflate(R.layout.user_fragment_order, container, false);

        return root;
    }

}