package aspace.trya.models.routing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import aspace.trya.models.DirectionsResponse;

public class RouteSegment implements Serializable {

    @JsonProperty("pretty_name")
    private String prettyName;

    @JsonProperty("name")
    private String name;

    @JsonProperty("start")
    private LngLat start;

    @JsonProperty("end")
    private LngLat end;

    @JsonIgnore
    private DirectionsResponse directions;

    public DirectionsResponse getDirections() {
        return directions;
    }

    public void setDirections(DirectionsResponse directions) {
        this.directions = directions;
    }

    public String getPrettyName() {
        return prettyName;
    }

    public void setPrettyName(String prettyName) {
        this.prettyName = prettyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStart(LngLat start) {
        this.start = start;
    }

    public LngLat getStart() {
        return start;
    }

    public void setEnd(LngLat end) {
        this.end = end;
    }

    public LngLat getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return
                "RouteSegment{" +
                        "pretty_name = '" + prettyName + '\'' +
                        ",name = '" + name + '\'' +
                        ",start = '" + start + '\'' +
                        ",end = '" + end + '\'' +
                        "}";
    }

    public String getProfile() {
        if (name.equals("drive_park")) {
            return "mapbox/driving-traffic";
        } else if (name.equals("walk_bike")) {
            return "mapbox/walking";
        } else if (name.equals("bike_dest")) {
            return "mapbox/cycling";
        } else if (name.equals("walk_dest")) {
            return "mapbox/walking";
        } else {
            return "mapbox/driving-traffic";
        }
    }
}