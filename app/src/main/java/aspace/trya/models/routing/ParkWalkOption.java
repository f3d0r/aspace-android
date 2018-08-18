package aspace.trya.models.routing;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ParkWalkOption {

    @JsonProperty("drive_park")
    private RouteSegment driveParkSegment;

    @JsonProperty("park_walk")
    private RouteSegment parkWalkSegment;

    public RouteSegment getDriveParkSegment() {
        return driveParkSegment;
    }

    public void setDriveParkSegment(RouteSegment driveParkSegment) {
        this.driveParkSegment = driveParkSegment;
    }

    public RouteSegment getParkWalkSegment() {
        return parkWalkSegment;
    }

    public void setParkWalkSegment(RouteSegment parkWalkSegment) {
        this.parkWalkSegment = parkWalkSegment;
    }

    @Override
    public String toString() {
        return
                "ParkWalkOption{" +
                        "drive_park = '" + driveParkSegment + '\'' +
                        ",park_walk = '" + parkWalkSegment + '\'' +
                        "}";
    }
}