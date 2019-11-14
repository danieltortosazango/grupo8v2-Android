package com.example.navigationdrawtest;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.navigationdrawtest.ui.home.HomeFragment;
import com.google.common.collect.Maps;

public class Localizacion implements LocationListener {

    MainActivity mainActivity;
    TextView tvMensaje;

    public double latitude;
    public double longitude;

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onLocationChanged(Location location) {

                //latitude = location.getLatitude();
                //longitude = location.getLongitude();
        MapsActivity.setLatitude(location.getLatitude());
        MapsActivity.setLongitude(location.getLongitude());


    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        tvMensaje.setText("GPS Activado");
    }

    @Override
    public void onProviderDisabled(String provider) {
        tvMensaje.setText("GPS Desactivado");
    }
}

