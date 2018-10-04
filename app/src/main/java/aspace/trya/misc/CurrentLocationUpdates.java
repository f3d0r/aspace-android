package aspace.trya.misc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import aspace.trya.bodyobjects.LngLat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.mapbox.mapboxsdk.geometry.LatLng;
import java.util.concurrent.Executor;

public class CurrentLocationUpdates {

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationCallback mLocationCallback;
    private boolean mRequestingLocationUpdates;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;

    private boolean locationUpdated = false;

    public CurrentLocationUpdates(Context context) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mSettingsClient = LocationServices.getSettingsClient(context);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                if (locationResult.getLastLocation() != null) {
                    locationUpdated = true;
                    mCurrentLocation = locationResult.getLastLocation();
                }
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        startLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        mFusedLocationClient.getLastLocation().addOnSuccessListener(
            location -> {
                if (location != null) {
                    locationUpdated = true;
                    mCurrentLocation = location;
                }
            });
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
            .removeLocationUpdates(mLocationCallback)
            .addOnCompleteListener((Executor) this, task -> {
                // Location updates stopped!
            });
    }

    public Location getLocation() {
        return mCurrentLocation;
    }

    public LatLng getLatLng() {
        return new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
    }

    public LngLat getLngLat() {
        return new LngLat(mCurrentLocation.getLongitude() + "," + mCurrentLocation.getLatitude());
    }

    public boolean isLocationUpdated() {
        return locationUpdated;
    }

}
