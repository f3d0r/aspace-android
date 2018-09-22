package aspace.trya;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;
import aspace.trya.api.AspaceMainService;
import aspace.trya.api.RetrofitServiceGenerator;
import aspace.trya.misc.APIURLs;
import aspace.trya.misc.ApplicationState;
import aspace.trya.misc.LocationMonitoringService;
import aspace.trya.models.CodeResponse;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class SplashScreen extends Activity {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    // location last updated time
    private String mLastUpdateTime;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;

    private boolean mAlreadyStartedService = false;

    private boolean locationReceived;

    private ApplicationState applicationState;

    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationState = new ApplicationState(SplashScreen.this);

        initLocation();

        Dexter.withActivity(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse response) {
                    mRequestingLocationUpdates = true;
                    startLocationUpdates();
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse response) {
                    if (response.isPermanentlyDenied()) {
                        // open device settings when the permission is
                        // denied permanently
                        openSettings();
                    }
                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permission,
                    PermissionToken token) {
                    token.continuePermissionRequest();
                }
            }).check();

        setContentView(R.layout.activity_splash);

        loadLibraries();
    }

    private void initLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();

            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    public void loadLibraries() {
        Timber.plant(new Timber.DebugTree());
    }

    public void checkUserLoggedIn(String latitude, String longitude) {
        String loggedInAccessCode = applicationState.getAccessCode();
        applicationState.setLoadLocation(latitude, longitude);
        if (loggedInAccessCode == null) {
            getLocationAndSend(LoginActivity.class);
        } else {
            Call<CodeResponse> call = RetrofitServiceGenerator
                .createService(AspaceMainService.class,
                    APIURLs.ASPACE_MAIN_PROD_URL)
                .checkAuth(loggedInAccessCode, applicationState.getDeviceId());
            call.enqueue(new Callback<CodeResponse>() {
                @Override
                public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                    int responseCode = response.body().getResInfo().getCode();
                    if (responseCode == 40) {
                        getLocationAndSend(MapActivity.class);
                    } else {
                        getLocationAndSend(LoginActivity.class);
                    }
                }

                @Override
                public void onFailure(Call<CodeResponse> call, Throwable t) {
                    applicationState.logout();
                    getLocationAndSend(LoginActivity.class);
                }
            });
        }
    }

    public void getLocationAndSend(Class throughClass) {
        Intent intent = new Intent(SplashScreen.this, throughClass);
        ActivityOptions options = ActivityOptions
            .makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(intent, options.toBundle());
        stopService(new Intent(this, LocationMonitoringService.class));
        mAlreadyStartedService = false;
        finish();
    }

    private void startLocationUpdates() {
        mSettingsClient
            .checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                @SuppressLint("MissingPermission")
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    Log.i("TAG", "All location settings are satisfied.");

                    Toast.makeText(getApplicationContext(), "Started location updates!",
                        Toast.LENGTH_SHORT).show();

                    //noinspection MissingPermission
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                        mLocationCallback, Looper.myLooper());
                }
            })
            .addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.i("TAG",
                                "Location settings are not satisfied. Attempting to upgrade " +
                                    "location settings ");
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                ResolvableApiException rae = (ResolvableApiException) e;
                                rae.startResolutionForResult(SplashScreen.this,
                                    REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sie) {
                                Log.i("TAG", "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage =
                                "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Log.e("TAG", errorMessage);

                            Toast.makeText(SplashScreen.this, errorMessage, Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
            .removeLocationUpdates(mLocationCallback)
            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Location updates stopped!",
                        Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e("TAG", "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e("TAG", "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
            BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }
}
