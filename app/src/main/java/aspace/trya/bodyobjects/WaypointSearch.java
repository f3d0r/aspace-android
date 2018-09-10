package aspace.trya.bodyobjects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WaypointSearch {

    @JsonProperty("car_size")
    private double carSize;

    @JsonProperty("origin")
    private LngLat origin;

    @JsonProperty("dest")
    private LngLat dest;

    public double getCarSize() {
        return carSize;
    }

    public void setCarSize(double carSize) {
        this.carSize = carSize;
    }

    public LngLat getOrigin() {
        return origin;
    }

    public void setOrigin(LngLat origin) {
        this.origin = origin;
    }

    public LngLat getDest() {
        return dest;
    }

    public void setDest(LngLat dest) {
        this.dest = dest;
    }

    @Override
    public String toString() {
        return
            "WaypointSearch{" +
                "car_size = '" + carSize + '\'' +
                ",origin = '" + origin + '\'' +
                ",dest = '" + dest + '\'' +
                "}";
    }
}