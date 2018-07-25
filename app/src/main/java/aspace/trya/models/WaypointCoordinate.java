package aspace.trya.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import aspace.trya.BodyObjects.LngLat;

public class WaypointCoordinate {

    @JsonProperty("Coord")
    private String coord;

    @JsonProperty("Optimal_spot_id")
    private int optimalSpotId;

    @JsonProperty("Price")
    private double price;

    @JsonProperty("Time")
    private double time;

    public String getCoord() {
        return coord;
    }

    public void setCoord(String coord) {
        this.coord = coord;
    }

    public int getOptimalSpotId() {
        return optimalSpotId;
    }

    public void setOptimalSpotId(int optimalSpotId) {
        this.optimalSpotId = optimalSpotId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public LngLat getLngLat() {
        return new LngLat(coord);
    }

    @Override
    public String toString() {
        return
                "ResContentItem{" +
                        "coord = '" + coord + '\'' +
                        ",optimal_spot_id = '" + optimalSpotId + '\'' +
                        ",price = '" + price + '\'' +
                        ",time = '" + time + '\'' +
                        "}";
    }
}