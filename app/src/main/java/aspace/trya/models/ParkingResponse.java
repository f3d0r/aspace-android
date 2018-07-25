package aspace.trya.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ParkingResponse {

    @JsonProperty("res_info")
    private ResponseInfo responseInfo;

    @JsonProperty("res_content")
    private List<ParkingSpot> parkingSpots;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<ParkingSpot> getParkingSpots() {
        return parkingSpots;
    }

    public void setParkingSpots(List<ParkingSpot> parkingSpots) {
        this.parkingSpots = parkingSpots;
    }

    @Override
    public String toString() {
        return
                "ParkingResponse{" +
                        "res_info = '" + responseInfo + '\'' +
                        ",res_content = '" + parkingSpots + '\'' +
                        "}";
    }
}