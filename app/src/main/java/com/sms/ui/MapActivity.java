package com.sms.ui;

/*
 * sms
 * Created by A.Kolchev  24.2.2016
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sms.App;
import com.sms.R;
import com.sms.data.model.Route;
import com.sms.data.model.TradePoint;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends BaseActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private Location mLastLocation;
    private List<Route> mRouts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fillRouteList();
    }

    public void fillRouteList() {
        mRouts.clear();
        mRouts.addAll(App.getInstance().getDBHelper().getRouts());
    }

    public void getLastKnownLocation() {
        if (checkSelfPermission()) {
            //Move camera to the location
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            String provider = locationManager.getBestProvider(new Criteria(), true);
            mLastLocation = locationManager.getLastKnownLocation(provider);
        }
    }

    public boolean checkSelfPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (checkSelfPermission()) {
            if (mMap != null) {
                // Access to the location has been granted to the app.
                mMap.setOnMyLocationButtonClickListener(this);
                mMap.setOnMarkerClickListener(this);
                mMap.setMyLocationEnabled(true);

                getLastKnownLocation();

                //Add markers to map
                LatLng point_latLng = null;
                for (Route route : mRouts) {
                    if (route.isDone()) {
                        TradePoint tradePoint = route.getTradePoint();
                        if (tradePoint != null) {
                            point_latLng = new LatLng(tradePoint.getLatitude(), tradePoint.getLongitude());
                            mMap.addMarker(new MarkerOptions()
                                    .position(point_latLng)
                                    .title(tradePoint.getDescription())
                                    .snippet(tradePoint.getAddress())
                                    .icon(BitmapDescriptorFactory.defaultMarker())).showInfoWindow();
                        }
                    }
                }

                if (point_latLng != null) {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(point_latLng, 15);
                    mMap.moveCamera(cameraUpdate);
                }

            }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        getLastKnownLocation();
        return false;
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        String markerLatitude = String.valueOf(marker.getPosition().latitude);
        String markerLongitude = String.valueOf(marker.getPosition().longitude);
        // open Directions
        if (mLastLocation != null) {
            String LocationLatitude = String.valueOf(mLastLocation.getLatitude());
            String LocationLongitude = String.valueOf(mLastLocation.getLongitude());
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=" + LocationLatitude + ", " + LocationLongitude + "&daddr=" + markerLatitude + ", " + markerLongitude + ""));
            startActivity(intent);
        }else {
            Toast.makeText(this, "Last location not set", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
