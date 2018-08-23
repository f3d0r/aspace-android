package aspace.trya.models.routing_options;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

import aspace.trya.models.DirectionsResponse;

public class RouteSegment implements Serializable {

    @JsonProperty("pretty_name")
    private String prettyName;

    @JsonProperty("directions")
    private List<DirectionsResponse> directions;

    @JsonProperty("origin")
    private LatLng origin;

    @JsonProperty("name")
    private String name;

    @JsonProperty("dest")
    private LatLng dest;

    public String getPrettyName() {
        return prettyName;
    }

    public void setPrettyName(String prettyName) {
        this.prettyName = prettyName;
    }

    public List<DirectionsResponse> getDirections() {
        return directions;
    }

    public void setDirections(List<DirectionsResponse> directions) {
        this.directions = directions;
    }

    public LatLng getOrigin() {
        return origin;
    }

    public void setOrigin(LatLng origin) {
        this.origin = origin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getDest() {
        return dest;
    }

    public void setDest(LatLng dest) {
        this.dest = dest;
    }

    @Override
    public String toString() {
        return
                "RouteSegment{" +
                        "pretty_name = '" + prettyName + '\'' +
                        ",directions = '" + directions + '\'' +
                        ",origin = '" + origin + '\'' +
                        ",name = '" + name + '\'' +
                        ",dest = '" + dest + '\'' +
                        "}";
    }
}