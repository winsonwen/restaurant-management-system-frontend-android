package com.example.application.delivery.ui.userInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.application.Properties;
import com.example.application.R;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfoFragment extends Fragment {

    private UserInfoViewModel galleryViewModel;

    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    Button button;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(UserInfoViewModel.class);
        View root = inflater.inflate(R.layout.delivery_fragment_user_info, container, false);


        textView = root.findViewById(R.id.editTextTextPersonName);
        textView2 = root.findViewById(R.id.editTextTextPersonName2);
        textView3 = root.findViewById(R.id.editTextTextPersonName3);
        textView4 = root.findViewById(R.id.editTextTextPersonName4);
        button = root.findViewById(R.id.button);


        SharedPreferences sharedPreferences  = getActivity().getSharedPreferences(Properties.STORAGE, Context.MODE_PRIVATE);
        String employeeInfo = sharedPreferences.getString(Properties.EMPLOYEE_INFO, null);
        try {
            JSONObject result = new JSONObject(employeeInfo); // String 转 JSONObject
            textView.setText(result.getString("firstName"));
            textView2.setText(result.getString("lastName"));
            textView3.setText(result.getString("email"));
            textView4.setText(result.getString("phoneNumber"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("success");
            }
        });


        return root;
    }
}