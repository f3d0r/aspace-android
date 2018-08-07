package aspace.trya.misc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LocationUtils {

    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;

    private MapboxMap mapboxMap;
    private android.location.LocationManager locationManager;

    private Activity activity;
    private Context context;

    private Timer currentTimer;
    private boolean timerRunning;
    private int currentDelayMilli;

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                locationManager.removeUpdates(locationListener);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    public LocationUtils(Object systemService, MapboxMap mapboxMap, Activity activity, Context context) {
        locationManager = (LocationManager) systemService;
        this.mapboxMap = mapboxMap;
        this.activity = activity;
        this.context = context;
    }

    public void startCurrentLocationMapMover(int delayMilli) {
        currentTimer = new Timer();
        currentDelayMilli = delayMilli;
        currentTimer.schedule(new UpdateMapTask(), 0, currentDelayMilli);
        timerRunning = true;
    }

    public void stopCurrentLocationMapMover() {
        if (timerRunning) {
            currentTimer.cancel();
            currentTimer = null;
            timerRunning = false;
        }
    }

    public boolean isCurrentLocationTracked() {
        return timerRunning;
    }

    private void getCurrentLocation(Activity activity, Context context) {
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!(isGPSEnabled || isNetworkEnabled))
            Toast.makeText(context, "Location not available", Toast.LENGTH_LONG).show();
        else {
            if (isNetworkEnabled) {
                Dexter.withActivity(activity)
                        .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                        .withListener(new MultiplePermissionsListener() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                        currentDelayMilli, LOCATION_UPDATE_MIN_DISTANCE, locationListener);
                                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                Location location;
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                        currentDelayMilli, LOCATION_UPDATE_MIN_DISTANCE, locationListener);
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                zoomToLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                            }
                        }).check();
            }
        }
    }

    public void zoomToLatLng(LatLng latLng) {
        CameraPosition position = new CameraPosition.Builder()
                .target(latLng)
                .zoom(17) // Sets the zoom
                .build(); // Creates a CameraPosition from the builder

        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 1000);
    }

    class UpdateMapTask extends TimerTask {
        @Override
        public void run() {
            getCurrentLocation(activity, context);
        }
    }
}
