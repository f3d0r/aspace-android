package aspace.trya.models;

import java.io.Serializable;

public class ParkingSpot implements Serializable {

    private double lng;
    private double distance;
    private int spotId;
    private double drivingTime;
    private double lat;
    private String occupied;
    private int blockId;
    private double parkingPrice;

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getSpotId() {
        return spotId;
    }

    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }

    public double getDrivingTime() {
        return drivingTime;
    }

    public void setDrivingTime(double drivingTime) {
        this.drivingTime = drivingTime;
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
                ",distance = '" + distance + '\'' +
                ",spot_id = '" + spotId + '\'' +
                ",driving_time = '" + drivingTime + '\'' +
                ",lat = '" + lat + '\'' +
                ",occupied = '" + occupied + '\'' +
                ",block_id = '" + blockId + '\'' +
                ",parking_price = '" + parkingPrice + '\'' +
                "}";
    }
}
