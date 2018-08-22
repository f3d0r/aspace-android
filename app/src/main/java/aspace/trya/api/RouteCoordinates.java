package aspace.trya.api;

import aspace.trya.models.routing.LngLat;

public class RouteCoordinates {
    private LngLat origin;
    private LngLat destination;

    public RouteCoordinates(LngLat origin, LngLat destination) {
        this.origin = origin;
        this.destination = destination;
    }

    @Override
    public String toString() {
        return origin.getLng() + origin.getLat() + ";" + destination.getLng() + destination.getLat();
    }
}

