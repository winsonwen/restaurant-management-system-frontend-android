package com.example.application.user.UI.userAccount;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.application.Properties;
import com.example.application.R;
import com.example.application.user.UI.Interface.userFragmentInterface;

import org.json.JSONException;
import org.json.JSONObject;

public class userFragment extends Fragment {

    private View view;
    TextView firstname;
    TextView lastname;
    TextView email;
    TextView phoneNumber;
    Button update;

    public userFragment(){

    }

    public void onAttach(Context context){
        super.onAttach(context);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        firstname = view.findViewById(R.id.FirstName);
        lastname = view.findViewById(R.id.LastName);
        email = view.findViewById(R.id.Email);
        phoneNumber =  view.findViewById(R.id.PhoneNumber);
        update =  view.findViewById(R.id.update);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Properties.STORAGE, Context.MODE_PRIVATE);
        final String memberInfo = sharedPreferences.getString(Properties.MEMBER_INFO, null);

        try{
            JSONObject result = new JSONObject(memberInfo);
            firstname.setText(result.getString("firstName"));
            lastname.setText(result.getString("lastName"));
            email.setText(result.getString("email"));
            phoneNumber.setText(result.getString("phoneNumber"));
        }catch(JSONException e){
            e.printStackTrace();
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    JSONObject updated = new JSONObject(memberInfo);
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Properties.STORAGE, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Properties.MEMBER_INFO, memberInfo);
                    editor.apply();

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
}



