package aspace.trya.models.osrm_directions;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Directions {

    @JsonProperty("routes")
    private List<Route> routes;

    @JsonProperty("code")
    private String code;

    @JsonProperty("waypoints")
    private List<Waypoint> waypoints;

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

    @Override
    public String toString() {
        return
            "Directions{" +
                "routes = '" + routes + '\'' +
                ",code = '" + code + '\'' +
                ",waypoints = '" + waypoints + '\'' +
                "}";
    }
}