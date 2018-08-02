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

import io.realm.Realm;
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
        checkPermissions();
//        First activity intent is for debugging
//        Intent mainActivityIntent = new Intent(SplashScreen.this, MainActivity.class);
//        if (userLoggedIn()) {
////            send user to to home page/map
//        } else {
//
////            send user to login/sign-up page
//        }
    }

    public void loadLibraries() {
        Timber.plant(new Timber.DebugTree());
//        Realm.init(this);
//
//        byte[] key = new byte[64];
//        new SecureRandom().nextBytes(key);
//
//        RealmConfiguration config = new RealmConfiguration.Builder()
//                .name("aspace.realm")
//                .encryptionKey(key)
//                .schemaVersion(42)
//                .build();
//
//        realm = Realm.getInstance(config);
    }

    public boolean userLoggedIn() {
        return false;
//        RealmQuery<AccessCode> query = realm.where(AccessCode.class);
//        RealmResults<AccessCode> accessCodes = query.findAll();
//        return !accessCodes.isEmpty();
    }

    public void checkPermissions() {
        boolean permissionsExist = true;
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent mainActivityIntent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(mainActivityIntent);
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
