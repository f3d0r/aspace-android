package aspace.trya.api;

import com.mapbox.mapboxsdk.geometry.LatLng;

class RouteCoordinates {
    private LatLng origin;
    private LatLng destination;

    public RouteCoordinates(LatLng origin, LatLng destination) {
        this.origin = origin;
        this.destination = destination;
    }

    @Override
    public String toString() {
        return origin.getLongitude() + origin.getLatitude() + ";" + destination.getLongitude() + destination.getLatitude();
    }
}

