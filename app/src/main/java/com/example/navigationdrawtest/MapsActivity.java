package com.example.navigationdrawtest;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    static public double latitude = 0.0;
    static public double longitude = 0.0;

    static public void setLatitude(double latitude) {
        MapsActivity.latitude = latitude;
    }
    static public void setLongitude(double longitude) {
        MapsActivity.longitude = longitude;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if(latitude !=0.0 & longitude !=0.0) {

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


            // Add a marker in Sydney and move the camera
            //LatLng sydney = new LatLng(-34, 151);
            LatLng ubicacion = new LatLng(latitude, longitude);
            LatLng UPV = new LatLng(38.99643, -0.165367 );
            mMap.addMarker(new MarkerOptions().position(ubicacion).title("Mi ubicación"));
            mMap.addMarker(new MarkerOptions().position(UPV).title("Primera base") .icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 16));

    }
}
