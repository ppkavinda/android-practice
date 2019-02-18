package com.example.myapplication;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private final String TAG = "MapActivity";

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng phone = new LatLng(5.978772, 80.430600);
        final String phones = "5.978772, 80.430600"; // weligama

        final LatLng middle = new LatLng(6.023606, 80.496578);
        final String middles = "6.023606, 80.496578"; // thelijjawila

        LatLng home = new LatLng(6.023761,80.509728);
        final String homes = "6.023761,80.509728"; // home

        final DirectionsResult[] results = new DirectionsResult[1];

        Runnable r = new Runnable() {

            @Override
            public void run() {
                try {
                    DateTime now = new DateTime();
                    DirectionsResult result = DirectionsApi.newRequest(getGeoContext())
                            .mode(TravelMode.DRIVING)
                            .origin(phones)
                            .destination(homes)
                            .waypoints(middles)
                            .departureTime(now)
                            .await();
                    results[0] = result;
                    Gson gson = new Gson();
                    Log.d(TAG, gson.toJson(result));

                } catch (ApiException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(r);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, String.valueOf(results[0].routes.length));

        final List<Marker> markers = addMarkersToMap(results[0], mMap);

        addPolyline(results[0], mMap);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker marker : markers) {
                    builder.include(marker.getPosition());
                }
                LatLngBounds bounds = builder.build();

                int padding = 20; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.animateCamera(cu);
            }
        });
    }

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3)
                .setApiKey(getString(R.string.directionsApiKey))
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }

    private List<Marker> addMarkersToMap(DirectionsResult results, GoogleMap mMap) {
        List<Marker> markers = new ArrayList<>();

        markers.add(mMap.addMarker(new MarkerOptions().position(
                new LatLng(results.routes[0].legs[0].startLocation.lat, results.routes[0].legs[0].startLocation.lng))
                .title(results.routes[0].legs[0].startAddress)
        ));
        for (DirectionsLeg leg : results.routes[0].legs) {
            markers.add(mMap.addMarker(new MarkerOptions().position(
                    new LatLng(leg.endLocation.lat, leg.endLocation.lng))
                    .title(leg.startAddress).snippet(getEndLocationTitle(leg))));
        }
        return markers;
    }

    private String getEndLocationTitle(DirectionsLeg leg) {
        return "Time :" + leg.duration.humanReadable + " Distance :" + leg.distance.humanReadable;
    }

    private void addPolyline(DirectionsResult result, GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(result.routes[0].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
    }
}
