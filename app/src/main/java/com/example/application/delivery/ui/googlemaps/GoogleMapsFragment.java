package com.example.application.delivery.ui.googlemaps;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.application.R;

public class GoogleMapsFragment extends Fragment {

    private GoogleMapsViewModel mViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        mViewModel =
                ViewModelProviders.of(this).get(GoogleMapsViewModel.class);
        View root = inflater.inflate(R.layout.delivery_fragment_google_maps, container, false);
        final TextView textView = root.findViewById(R.id.text_google_maps);
        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

}