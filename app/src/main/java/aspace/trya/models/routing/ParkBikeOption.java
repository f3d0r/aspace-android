package aspace.trya.models.routing;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ParkBikeOption {

    @JsonProperty("drive_park")
    private RouteSegment driveParkSegment;

    @JsonProperty("bike_walk")
    private RouteSegment bikeWalkSegment;

    @JsonProperty("park_bike")
    private RouteSegment parkBikeSegment;

    public RouteSegment getDriveParkSegment() {
        return driveParkSegment;
    }

    public void setDriveParkSegment(RouteSegment driveParkSegment) {
        this.driveParkSegment = driveParkSegment;
    }

    public RouteSegment getBikeWalkSegment() {
        return bikeWalkSegment;
    }

    public void setBikeWalkSegment(RouteSegment bikeWalkSegment) {
        this.bikeWalkSegment = bikeWalkSegment;
    }

    public RouteSegment getParkBikeSegment() {
        return parkBikeSegment;
    }

    public void setParkBikeSegment(RouteSegment parkBikeSegment) {
        this.parkBikeSegment = parkBikeSegment;
    }

    @Override
    public String toString() {
        return
                "ParkBikeOption{" +
                        "drive_park = '" + driveParkSegment + '\'' +
                        ",bike_walk = '" + bikeWalkSegment + '\'' +
                        ",park_bike = '" + parkBikeSegment + '\'' +
                        "}";
    }
}