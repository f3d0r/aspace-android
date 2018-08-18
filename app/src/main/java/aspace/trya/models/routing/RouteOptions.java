package aspace.trya.models.routing;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RouteOptions {

    @JsonProperty("park_direct")
    private List<ParkDirectOption> parkDirect;

    @JsonProperty("origin")
    private LngLat origin;

    @JsonProperty("destination")
    private LngLat destination;

    @JsonProperty("park_bike")
    private List<ParkBikeOption> parkBike;

    @JsonProperty("park_walk")
    private List<ParkWalkOption> parkWalk;

    public List<ParkDirectOption> getParkDirect() {
        return parkDirect;
    }

    public void setParkDirect(List<ParkDirectOption> parkDirect) {
        this.parkDirect = parkDirect;
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

    public List<ParkBikeOption> getParkBike() {
        return parkBike;
    }

    public void setParkBike(List<ParkBikeOption> parkBike) {
        this.parkBike = parkBike;
    }

    public List<ParkWalkOption> getParkWalk() {
        return parkWalk;
    }

    public void setParkWalk(List<ParkWalkOption> parkWalk) {
        this.parkWalk = parkWalk;
    }

    @Override
    public String toString() {
        return
                "RouteOptions{" +
                        "park_direct = '" + parkDirect + '\'' +
                        ",origin = '" + origin + '\'' +
                        ",destination = '" + destination + '\'' +
                        ",park_bike = '" + parkBike + '\'' +
                        ",park_walk = '" + parkWalk + '\'' +
                        "}";
    }
}