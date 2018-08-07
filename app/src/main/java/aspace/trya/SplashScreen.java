package aspace.trya;

import android.Manifest;
import android.content.Intent;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import aspace.trya.realm.AppUser;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

public class SplashScreen extends AwesomeSplash {

    private Realm realm;

    @Override
    public void initSplash(ConfigSplash configSplash) {
        configSplash.setBackgroundColor(R.color.white);
        configSplash.setAnimCircularRevealDuration(0); //int ms

        configSplash.setLogoSplash(R.drawable.splash_logo); //or any other drawable
        configSplash.setAnimLogoSplashDuration(500); //int ms
    }

    @Override
    public void animationsFinished() {
        loadLibraries();
        initRealm();
        if (userLoggedIn()) {
            checkPermissions(MapActivity.class);
        } else {
            checkPermissions(MainActivity.class);
        }
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


    public boolean userLoggedIn() {
        return !realm.where(AppUser.class).findAll().isEmpty();
    }

    public void checkPermissions(Class throughClass) {
        boolean permissionsExist = true;
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent mainActivityIntent = new Intent(SplashScreen.this, throughClass);
                        startActivity(mainActivityIntent);
                        finish();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        finish();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                    }
                }).check();
    }
}
