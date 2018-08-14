package aspace.trya;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import aspace.trya.api.AspaceService;
import aspace.trya.api.AspaceServiceGenerator;
import aspace.trya.misc.ApplicationState;
import aspace.trya.misc.LocationMonitoringService;
import aspace.trya.models.CodeResponse;
import aspace.trya.realm.AppUser;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class SplashScreen extends Activity {

    private Realm realm;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private boolean mAlreadyStartedService = false;

    private boolean locationReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startStep1();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String latitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
                        String longitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);
                        if (latitude != null && longitude != null && !locationReceived) {
                            locationReceived = true;
                            checkUserLoggedIn(latitude, longitude);
                        }
                    }
                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );

        setContentView(R.layout.activity_splash);

        loadLibraries();
        initRealm();
    }

    public void loadLibraries() {
        Timber.plant(new Timber.DebugTree());
    }

    public void initRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(42)
                .build();
        realm = Realm.getInstance(config);
    }

    public void checkUserLoggedIn(String latitude, String longitude) {
        RealmQuery<AppUser> query = realm.where(AppUser.class);
        RealmResults<AppUser> users = query.findAll();
        if (users.size() == 0) {
            getLocationAndSend(latitude, longitude, MainActivity.class);
        } else {
            Call<CodeResponse> call = AspaceServiceGenerator.createService(AspaceService.class).checkAuth(users.get(0).accessCode.accessCode, users.get(0).deviceId);
            call.enqueue(new Callback<CodeResponse>() {
                @Override
                public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                    int responseCode = response.body().getResInfo().getCode();
                    if (responseCode == 40) {
                        getLocationAndSend(latitude, longitude, MapActivity.class);
                    } else {
                        getLocationAndSend(latitude, longitude, MainActivity.class);
                    }
                }

                @Override
                public void onFailure(Call<CodeResponse> call, Throwable t) {
                    ApplicationState.logout();
                    getLocationAndSend(latitude, longitude, MainActivity.class);
                }
            });
        }
    }

    public void getLocationAndSend(String latitude, String longitude, Class throughClass) {
        Intent nextIntent = new Intent(SplashScreen.this, throughClass);
        nextIntent.putExtra("currentLocLat", Double.valueOf(latitude));
        nextIntent.putExtra("currentLocLon", Double.valueOf(longitude));
        startActivity(nextIntent);
        stopService(new Intent(this, LocationMonitoringService.class));
        mAlreadyStartedService = false;
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();

        startStep1();
    }


    /**
     * Step 1: Check Google Play services
     */
    private void startStep1() {

        //Check whether this user has installed Google play service which is being used by Location updates.
        if (isGooglePlayServicesAvailable()) {

            //Passing null to indicate that it is executing for the first time.
            startStep2(null);

        } else {
            Toast.makeText(getApplicationContext(), "NO GOOGLE PLAY SERVICES", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Step 2: Check & Prompt Internet connection
     */
    private Boolean startStep2(DialogInterface dialog) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            promptInternetConnect();
            return false;
        }


        if (dialog != null) {
            dialog.dismiss();
        }

        //Yes there is active internet connection. Next check Location is granted by user or not.

        if (checkPermissions()) { //Yes permissions are granted by the user. Go to the next step.
            startStep3();
        } else {  //No user has not granted the permissions yet. Request now.
            requestPermissions();
        }
        return true;
    }

    /**
     * Show A Dialog with button to refresh the internet state.
     */
    private void promptInternetConnect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
        builder.setTitle("Title: No internet");
        builder.setMessage("Message: No internet");

        String positiveText = "Refresh";
        builder.setPositiveButton(positiveText,
                (dialog, which) -> {
                    //Block the Application Execution until user grants the permissions
                    if (startStep2(dialog)) {
                        //Now make sure about location permission.
                        if (checkPermissions()) {
                            //Step 2: Start the Location Monitor Service
                            //Everything is there to start the service.
                            startStep3();
                        } else if (!checkPermissions()) {
                            requestPermissions();
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Step 3: Start the Location Monitor Service
     */
    private void startStep3() {
        //And it will be keep running until you close the entire application from task manager.
        //This method will executed only once.
        if (!mAlreadyStartedService) {

            //Start location sharing service to app server.........
            Intent intent = new Intent(this, LocationMonitoringService.class);
            startService(intent);

            mAlreadyStartedService = true;
            //Ends................................................
        }
    }

    /**
     * Return the availability of GooglePlayServices
     */
    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }


    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        int permissionState2 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED;

    }

    /**
     * Start permissions requests.
     */
    private void requestPermissions() {

        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        boolean shouldProvideRationale2 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        if (shouldProvideRationale || shouldProvideRationale2) {
            Timber.w("Displaying permission rationale to provide additional context.");
            showSnackbar("Requesting Permission",
                    "OK?", view -> ActivityCompat.requestPermissions(SplashScreen.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_PERMISSIONS_REQUEST_CODE));
        } else {
            Timber.w("Requesting permission");
            ActivityCompat.requestPermissions(SplashScreen.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextString The id for the string resource for the Snackbar text.
     * @param actionString   The text of the action item.
     * @param listener       The listener associated with the Snackbar action.
     */
    private void showSnackbar(String mainTextString, String actionString,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                mainTextString,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(actionString, listener).show();
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Timber.w("onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If img_user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Timber.w("User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Timber.w("Permission granted, updates requested, starting location updates");
                startStep3();

            } else {
                showSnackbar("Permission was denied",
                        "Settings", view -> {
                            // Build intent that displays the App settings screen.
                            Intent intent = new Intent();
                            intent.setAction(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",
                                    BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        });
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, LocationMonitoringService.class));
        mAlreadyStartedService = false;
    }
}
