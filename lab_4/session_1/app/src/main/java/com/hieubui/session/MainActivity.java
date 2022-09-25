package com.hieubui.session;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private GoogleMap googleMap;

    private final List<LatLng> route = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) mapFragment.getMapAsync(googleMap -> this.googleMap = googleMap);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermissions(new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION}, 80);
    }

    @Override
    public void onRequestPermissionsResult(
        int requestCode,
        @NonNull String[] permissions,
        @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isGranted = Arrays.stream(grantResults).allMatch(result -> result == PackageManager.PERMISSION_GRANTED);

        if (requestCode == 80 && isGranted) {
            requestUpdateLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestUpdateLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getAllProviders();

        if (providers == null || providers.isEmpty()) {
            return;
        }

        long minTime = TimeUnit.SECONDS.toMillis(5);
        int minDistance = 1;

        providers.forEach(provider -> locationManager.requestLocationUpdates(provider, minTime, minDistance, this));
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("MainActivity", location.toString());
        if (googleMap == null) {
            return;
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        route.add(latLng);
        googleMap.clear();
        googleMap.addPolyline(new PolylineOptions().addAll(route));
        googleMap.addMarker(new MarkerOptions().position(latLng).title("Me"));
    }

    @Override
    protected void onStop() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationManager.removeUpdates(this);
        super.onStop();
    }
}