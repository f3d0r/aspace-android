package aspace.trya.misc;

import aspace.trya.models.AccessCode;
import aspace.trya.realm.AppUser;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public final class ApplicationState {
    public static final String LOGIN_PHONE_NUMBER = "79c0477f-3913-4be6-8b27-359b360eb8c7";
    public static final String DEVICE_ID = "26d2fd06-94d7-4f44-a611-9ad8eca6ce83";
    public static final String ONBOARD = "ee9ab574-7b66-43da-9517-5379651083b4";
    public static final String ACCESS_CODE = "4f2d8c57-5062-4864-b53c-7fcf6836797f";


    private static RealmConfiguration config = new RealmConfiguration.Builder()
            .schemaVersion(42)
            .build();


    public static void logout() {
        Realm realm = Realm.getInstance(config);
        final RealmResults<AppUser> user = realm.where(AppUser.class).findAll();
        realm.executeTransaction(realm1 -> user.deleteAllFromRealm());

    }

    public static void login(String phoneNumber, AccessCode accessCode, String deviceId) {
        Realm realm = Realm.getInstance(config);
        realm.beginTransaction();
        AppUser user = realm.createObject(AppUser.class);
        user.phoneNumber = phoneNumber;
        user.deviceId = deviceId;

        AccessCode realmAccessCode = realm.createObject(AccessCode.class);
        realmAccessCode.accessCode = accessCode.accessCode;
        realmAccessCode.expiry = accessCode.expiry;
        user.accessCode = realmAccessCode;

        realm.commitTransaction();
    }

    private RealmConfiguration getConfig() {
        return config;
    }
}
