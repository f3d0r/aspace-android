package aspace.trya.models.osrm_directions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Directions implements Serializable {

    @JsonProperty("routes")
    private List<Route> routes;

    @JsonProperty("code")
    private String code;

    @JsonProperty("waypoints")
    private List<Waypoint> waypoints;

    @JsonProperty("name")
    private String name;

    @JsonProperty("statusCode")
    private String statusCode;

    @JsonProperty("message")
    private String message;

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