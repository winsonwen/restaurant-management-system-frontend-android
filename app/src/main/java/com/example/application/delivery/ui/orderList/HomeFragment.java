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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.application.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button button;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // TODO Google maps
        //nav transfer
//        NavController controller= Navigation.findNavController(container);
//        controller.navigate(R.id.nav_googlemaps);
        System.out.println("??????????!!!");
        View root = inflater.inflate(R.layout.delivery_fragment_home, container, false);

        button = (Button) root.findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
//        View root = inflater.inflate(R.layout.delivery_fragment_home, container, false);
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