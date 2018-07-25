package aspace.trya.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ParkingSpot {

    @JsonProperty("lng")
    private double lng;

    @JsonProperty("spot_id")
    private int spotId;

    @JsonProperty("lat")
    private double lat;

    @JsonProperty("occupied")
    private String occupied;

    @JsonProperty("block_id")
    private int blockId;

    @JsonProperty("parking_price")
    private double parkingPrice;

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getSpotId() {
        return spotId;
    }

    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getOccupied() {
        return occupied;
    }

    public void setOccupied(String occupied) {
        this.occupied = occupied;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public double getParkingPrice() {
        return parkingPrice;
    }

    public void setParkingPrice(double parkingPrice) {
        this.parkingPrice = parkingPrice;
    }

    @Override
    public String toString() {
        return
                "ParkingSpot{" +
                        "lng = '" + lng + '\'' +
                        ",spot_id = '" + spotId + '\'' +
                        ",lat = '" + lat + '\'' +
                        ",occupied = '" + occupied + '\'' +
                        ",block_id = '" + blockId + '\'' +
                        ",parking_price = '" + parkingPrice + '\'' +
                        "}";
    }
}