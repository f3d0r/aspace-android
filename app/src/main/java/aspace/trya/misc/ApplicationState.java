package aspace.trya.misc;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.mapbox.mapboxsdk.geometry.LatLng;

import aspace.trya.models.AccessCode;

public final class ApplicationState {
    private static final String LOGIN_PHONE_NUMBER = "79c0477f-3913-4be6-8b27-359b360eb8c7";
    private static final String DEVICE_ID = "26d2fd06-94d7-4f44-a611-9ad8eca6ce83";
    private static final String ACCESS_CODE = "4f2d8c57-5062-4864-b53c-7fcf6836797f";
    private static final String LOAD_LOC_LAT = "837bf747-04d7-45f7-be25-71ae5c055fd9";
    private static final String LOAD_LOC_LNG = "dd79f8ee-0730-4a51-b46f-7dbb7565dd49";

    private SharedPreferences prefs;

    public ApplicationState(Activity activity) {
        prefs = activity.getSharedPreferences(
                "aspace.trya", Context.MODE_PRIVATE);
    }

    public void logout() {
        prefs.edit().remove(LOGIN_PHONE_NUMBER).apply();
        prefs.edit().remove(DEVICE_ID).apply();
        prefs.edit().remove(ACCESS_CODE).apply();
    }

    public void login(String phoneNumber, AccessCode accessCode, String deviceId) {
        logout();
        prefs.edit().putString(LOGIN_PHONE_NUMBER, phoneNumber).apply();
        prefs.edit().putString(DEVICE_ID, deviceId).apply();
        prefs.edit().putString(ACCESS_CODE, accessCode.getAccessCode()).apply();
    }

    public String getAccessCode() {
        return prefs.getString(ACCESS_CODE, null);
    }

    public String getDeviceId() {
        return prefs.getString(DEVICE_ID, null);
    }

    public void setLoadLocation(String lat, String lng) {
        prefs.edit().putString(LOAD_LOC_LAT, lat).apply();
        prefs.edit().putString(LOAD_LOC_LNG, lng).apply();
    }

    public LatLng getLoadLocation() {
        double lat = Double.parseDouble(prefs.getString(LOAD_LOC_LAT, "0"));
        double lng = Double.parseDouble(prefs.getString(LOAD_LOC_LNG, "0"));
        return new LatLng(lat, lng);
    }
}
