package aspace.trya.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RoutingResponse {

    @JsonProperty("res_info")
    private ResponseInfo responseInfo;

    @JsonProperty("res_content")
    private List<WaypointCoordinate> waypoints;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<WaypointCoordinate> getResContent() {
        return waypoints;
    }

    public void setResContent(List<WaypointCoordinate> waypoints) {
        this.waypoints = waypoints;
    }

    @Override
    public String toString() {
        return "RoutingResponse{" +
                "responseInfo=" + responseInfo +
                ", waypoints=" + waypoints +
                '}';
    }
}