package aspace.trya.misc;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import timber.log.Timber;

public class LocationMonitoringService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String ACTION_LOCATION_BROADCAST = LocationMonitoringService.class.getName() + "LocationBroadcast";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";
    private static final String TAG = LocationMonitoringService.class.getSimpleName();
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest = new LocationRequest();
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            } else if (locationResult.getLocations().size() > 0) {
                sendMessageToUI(String.valueOf(locationResult.getLocations().get(0).getLatitude()), String.valueOf(locationResult.getLocations().get(0).getLongitude()));
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);


        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default

        mLocationRequest.setPriority(priority);
        mLocationClient.connect();

        //Make it stick to the notification panel so it is less prone to get cancelled by the Operating System.
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     * LOCATION CALLBACKS
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "== Error On onConnected() Permission not granted");
            //Permission not granted by user so cancel the further execution.

            return;
        }
        LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                sendMessageToUI(location.getLatitude() + "", location.getLongitude() + "");
            }
        });
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
    }

    private void sendMessageToUI(String lat, String lng) {

        Log.d(TAG, "Sending info...");

        Timber.w("SENDING LAT: " + lat);
        Timber.w("SENDING LON: " + lng);
        Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
        intent.putExtra(EXTRA_LATITUDE, lat);
        intent.putExtra(EXTRA_LONGITUDE, lng);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Failed to connect to Google API");

    }
}