package aspace.trya;

import android.content.Intent;

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

        Intent mainActivityIntent = new Intent(SplashScreen.this, MainActivity.class);
        if (userLoggedIn()) {
//            send user to to home page/map
        } else {

//            send user to login/sign-up page
        }
        startActivity(mainActivityIntent);
        finish();
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
}
