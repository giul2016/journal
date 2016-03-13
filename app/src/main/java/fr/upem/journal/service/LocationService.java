package fr.upem.journal.service;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;


public class LocationService implements LocationListener {
    // Paris by default
    private double latitude = 48.85;
    private double longitude = 2.56;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("DEBUG LOCATION", "onLocationChanged");
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
