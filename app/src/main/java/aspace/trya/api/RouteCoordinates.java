package aspace.trya.api;

import aspace.trya.models.routing_options.LatLng;

public class RouteCoordinates {
    private LatLng origin;
    private LatLng destination;

    public RouteCoordinates(LatLng origin, LatLng destination) {
        this.origin = origin;
        this.destination = destination;
    }

    @Override
    public String toString() {
        return origin.getLng() + origin.getLat() + ";" + destination.getLng() + destination.getLat();
    }
}

