package aspace.trya.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import aspace.trya.models.directions.Route;
import aspace.trya.models.directions.Waypoint;

public class DirectionsResponse {

    @JsonProperty("routes")
    private List<Route> routes;

    @JsonProperty("code")
    private String code;

    @JsonProperty("waypoints")
    private List<Waypoint> waypoints;

    @JsonProperty("uuid")
    private String uuid;

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return
                "DirectionsResponse{" +
                        "routes = '" + routes + '\'' +
                        ",code = '" + code + '\'' +
                        ",waypoints = '" + waypoints + '\'' +
                        ",uuid = '" + uuid + '\'' +
                        "}";
    }
}