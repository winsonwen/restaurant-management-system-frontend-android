package com.example.application.delivery.ui.googlemaps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.example.application.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static androidx.core.content.ContextCompat.getSystemService;

public class MapsFragment extends Fragment implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean permissionDenied = false;

    private GoogleMap map;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
//            LatLng sydney = new LatLng(151, 151);
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            map = googleMap;

            enableMyLocation();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.delivery_fragment_maps, container, false);
    }

    private void enableMyLocation() {

        System.out.println("2222222");
        System.out.println(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION));
        System.out.println(PackageManager.PERMISSION_GRANTED);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            System.out.println("11111111");
            if (map != null) {
                System.out.println("333333");
                map.setMyLocationEnabled(true);
                map.setOnMyLocationButtonClickListener(this);
                map.setOnMyLocationClickListener(this);


            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            String[] strings = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(getActivity(), strings, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {

        System.out.println(" onMyLocationButtonClick");
//        CameraPosition cameraPosition = map.getCameraPosition();
//        System.out.println(cameraPosition.toString());
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        LocationProvider lProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            String loca = "location" + "("  + location.getLatitude() + ", " + location.getLongitude()+")";
            Toast.makeText( getContext(), loca, Toast.LENGTH_SHORT).show();

//            Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse("http://ditu.google.com/maps?f=d&source=s_d&saddr="
//                    + location.getLatitude()
//                    + ","
//                    + location.getLongitude()
//                    + "&daddr="
//                    + 39.978768
//                    + ","
//                    +  -75.154054 + "&hl=en"));
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                    & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//            i.setClassName("com.google.android.apps.maps",
//                    "com.google.android.maps.MapsActivity");
//            startActivity(i);

        } else {
            // Permission to access the location is missing. Show rationale and request permission
            String[] strings = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(getActivity(), strings, LOCATION_PERMISSION_REQUEST_CODE);
        }




        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        System.out.println(" onMyLocationClick");
        Toast.makeText(getActivity(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }
}