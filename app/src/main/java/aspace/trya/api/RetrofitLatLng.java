package aspace.trya.api;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class RetrofitLatLng extends LatLng {

    public RetrofitLatLng(double lat, double lng) {
        super(lat, lng);
    }

    @Override
    public String toString() {
        return super.getLongitude() + "," + super.getLatitude();
    }
}
