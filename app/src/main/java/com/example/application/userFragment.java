package com.example.application;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;

public class userFragment extends Fragment {
    private ListView view;

    public static userFragment newInstance(JSONObject object){
        userFragment fragment = new userFragment();
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//        view =inflater.inflate( , container, false);
        return null;
    }
}
