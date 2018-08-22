package aspace.trya.models.routing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

import aspace.trya.models.DirectionsResponse;

public class RouteOptions implements Serializable {

    @JsonProperty("park_direct")
    private List<ParkDirectOption> parkDirectOptions;

    @JsonProperty("origin")
    private LngLat origin;

    @JsonProperty("bbox")
    private Bbox bbox;

    @JsonProperty("destination")
    private LngLat destination;

    @JsonProperty("park_bike")
    private List<ParkBikeOption> parkBikeOptions;

    @JsonProperty("park_walk")
    private List<ParkWalkOption> parkWalkOptions;

    public List<ParkDirectOption> getParkDirectOptions() {
        return parkDirectOptions;
    }

    public void setParkDirectOptions(List<ParkDirectOption> parkDirectOptions) {
        this.parkDirectOptions = parkDirectOptions;
    }

    public void setOrigin(LngLat origin) {
        this.origin = origin;
    }

    public LngLat getOrigin() {
        return origin;
    }

    public Bbox getBbox() {
        return bbox;
    }

    public void setBbox(Bbox bbox) {
        this.bbox = bbox;
    }

    public void setDestination(LngLat destination) {
        this.destination = destination;
    }

    public LngLat getDestination() {
        return destination;
    }

    public List<ParkBikeOption> getParkBikeOptions() {
        return parkBikeOptions;
    }

    public void setParkBikeOptions(List<ParkBikeOption> parkBikeOptions) {
        this.parkBikeOptions = parkBikeOptions;
    }

    public List<ParkWalkOption> getParkWalkOptions() {
        return parkWalkOptions;
    }

    public void setParkWalkOptions(List<ParkWalkOption> parkWalkOptions) {
        this.parkWalkOptions = parkWalkOptions;
    }

    @Override
    public String toString() {
        return
                "RouteOptions{" +
                        "park_direct = '" + parkDirectOptions + '\'' +
                        ",origin = '" + origin + '\'' +
                        ",bbox = '" + bbox + '\'' +
                        ",destination = '" + destination + '\'' +
                        ",park_bike = '" + parkBikeOptions + '\'' +
                        ",park_walk = '" + parkWalkOptions + '\'' +
                        "}";
    }
}