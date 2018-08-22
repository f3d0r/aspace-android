package aspace.trya.misc;

import aspace.trya.models.routing.LngLat;

public class MapboxDirectionsOptions {
    private LngLat origin;
    private LngLat destination;
    private String profile;

    public MapboxDirectionsOptions(LngLat origin, LngLat destination, String profile) {
        this.origin = origin;
        this.destination = destination;
        this.profile = profile;
    }

    public LngLat getOrigin() {
        return origin;
    }

    public void setOrigin(LngLat origin) {
        this.origin = origin;
    }

    public LngLat getDestination() {
        return destination;
    }

    public void setDestination(LngLat destination) {
        this.destination = destination;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "MapboxDirectionsOptions{" +
                "origin=" + origin +
                ", destination=" + destination +
                ", profile='" + profile + '\'' +
                '}';
    }
}
