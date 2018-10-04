package aspace.trya.models.routing_options;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocMetaData implements Serializable {

    @JsonProperty("bikes_available")
    private int bikesAvailable;

    @JsonProperty("distance")
    private double distance;

    @JsonProperty("num")
    private String num;

    @JsonProperty("company")
    private String company;

    @JsonProperty("id")
    private String id;

    @JsonProperty("region")
    private String region;

    @JsonProperty("type")
    private String type;

    @JsonProperty("spot_id")
    private int spotId;

    @JsonProperty("driving_time")
    private double drivingTime;

    @JsonProperty("occupied")
    private String occupied;

    @JsonProperty("block_id")
    private int blockId;

    @JsonProperty("parking_price")
    private double parkingPrice;

    @JsonProperty("name")
    private String name;

    @JsonProperty("payment_process")
    private String paymentProcess;

    @JsonProperty("address")
    private String address;

    @JsonProperty("payment_types")
    private String paymentTypes;

    @JsonProperty("facilities")
    private String facilities;

    public int getBikesAvailable() {
        return bikesAvailable;
    }

    public void setBikesAvailable(int bikesAvailable) {
        this.bikesAvailable = bikesAvailable;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaymentProcess() {
        return paymentProcess;
    }

    public void setPaymentProcess(String paymentProcess) {
        this.paymentProcess = paymentProcess;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentTypes() {
        return paymentTypes;
    }

    public void setPaymentTypes(String paymentTypes) {
        this.paymentTypes = paymentTypes;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    @Override
    public String toString() {
        return "LocMetaData{" +
            "bikesAvailable=" + bikesAvailable +
            ", distance=" + distance +
            ", num='" + num + '\'' +
            ", company='" + company + '\'' +
            ", id='" + id + '\'' +
            ", region='" + region + '\'' +
            ", type='" + type + '\'' +
            ", spotId=" + spotId +
            ", drivingTime=" + drivingTime +
            ", occupied='" + occupied + '\'' +
            ", blockId=" + blockId +
            ", parkingPrice=" + parkingPrice +
            ", name='" + name + '\'' +
            ", paymentProcess='" + paymentProcess + '\'' +
            ", address='" + address + '\'' +
            ", paymentTypes='" + paymentTypes + '\'' +
            ", facilities='" + facilities + '\'' +
            '}';
    }
}