package com.example.freatnor.play_services_lab;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "MapsActivity";
    //int constant for permissions request
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 26;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setupGoogleApi();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void setupGoogleApi() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Add a marker in Sydney and move the camera
        if(mLastLocation != null) {
            LatLng userLoc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            Log.d(TAG, "onRequestPermissionsResult: " + userLoc.latitude + ", " + userLoc.longitude);
            mMap.addMarker(new MarkerOptions().position(userLoc).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLoc));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do something like set a global boolean?
//                    if(mMap != null) {
//                        if(mLastLocation != null) {
//                            LatLng userLoc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//                            Log.d(TAG, "onRequestPermissionsResult: " + userLoc.latitude + ", " + userLoc.longitude);
//                            mMap.addMarker(new MarkerOptions().position(userLoc).title("Marker in Sydney"));
//                            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLoc));
//                        }
//                    }

                }
                else{
                    //do nothing I guess
                }
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            Log.d(TAG, "onConnected: location object - " + mLastLocation.toString());
            if(mLastLocation != null) {
                LatLng userLoc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                Log.d(TAG, "onRequestPermissionsResult: " + userLoc.latitude + ", " + userLoc.longitude);
                mMap.addMarker(new MarkerOptions().position(userLoc).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLoc));
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
