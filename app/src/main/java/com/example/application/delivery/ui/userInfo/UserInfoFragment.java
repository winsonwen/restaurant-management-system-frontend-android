package com.example.application.delivery.ui.userInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.application.Properties;
import com.example.application.R;
import com.example.application.delivery.SharedViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserInfoFragment extends Fragment {

    private UserInfoViewModel galleryViewModel;

    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    Button button;
    RequestQueue requestqueue;
    private SharedViewModel sharedViewModel;
    private boolean verify = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(UserInfoViewModel.class);
        View root = inflater.inflate(R.layout.delivery_fragment_user_info, container, false);

        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);


        textView = root.findViewById(R.id.editTextTextPersonName);
        textView2 = root.findViewById(R.id.editTextTextPersonName2);
        textView3 = root.findViewById(R.id.editTextTextPersonName3);
        textView4 = root.findViewById(R.id.editTextTextPersonName4);
        button = root.findViewById(R.id.button);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Properties.STORAGE, Context.MODE_PRIVATE);
        String employeeInfo = sharedPreferences.getString(Properties.EMPLOYEE_INFO, null);
        try {
            JSONObject result = sharedViewModel.getRes(); // String 转 JSONObject
            textView.setText(result.getString("firstName"));
            textView2.setText(result.getString("lastName"));
            textView3.setText(result.getString("email"));
            textView4.setText(result.getString("phone"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedViewModel.UpdateInfo(textView.getText().toString(), textView2.getText().toString(), textView4.getText().toString(), textView3.getText().toString());
            }
        });

        sharedViewModel.getResult().observe(this, new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject result) {
                try {
                    textView.setText(result.getString("firstName"));
                    textView2.setText(result.getString("lastName"));
                    textView3.setText(result.getString("email"));
                    textView4.setText(result.getString("phone"));
                    if(verify==true) {
                        Toast.makeText(getContext(), "Updated successfully!!", Toast.LENGTH_SHORT).show();
                    }else  {
                        verify=true;
                    }
                    TextView textView;

                    textView = (TextView) getActivity().findViewById(R.id.textView10);
                    textView = (TextView) getActivity().findViewById(R.id.textView6);
                    textView.setText("");
                    textView = (TextView) getActivity().findViewById(R.id.textView12);
                    textView.setText("");
                    textView = (TextView) getActivity().findViewById(R.id.textView11);
                    textView.setText("");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        sharedViewModel.getResponse().observe(this, new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject result) {

                try {

                    if (101 == result.getInt("code")) {
                        JSONObject data = result.getJSONObject("data");
                        Map<String, String> map = new HashMap<>();
                        Iterator<String> iterator = data.keys();
                        while (iterator.hasNext()) {
                            String key = (String) iterator.next();
                            String value = (String) data.get(key);
                            map.put(key, value);
                        }
                        String msg;

                        msg = map.get("firstName");

                        TextView textView;
                        if (msg != null) {
                            textView = (TextView) getActivity().findViewById(R.id.textView10);
                            textView.setText(msg);
                        }
                        msg = map.get("lastName");
                        if (msg != null) {
                            textView = (TextView) getActivity().findViewById(R.id.textView6);
                            textView.setText(msg);

                        }
                        msg = map.get("email");
                        if (msg != null) {
                            textView = (TextView) getActivity().findViewById(R.id.textView12);
                            textView.setText(msg);
                        }
                        msg = map.get("phone");
                        if (msg != null) {
                            textView = (TextView) getActivity().findViewById(R.id.textView11);
                            textView.setText(msg);
                        }


                    } else {
                        String data = result.getString("data");
                        if (data != null) {
                            TextView textView = (TextView) getActivity().findViewById(R.id.textView10);
                            textView.setText(data);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        return root;
    }
}