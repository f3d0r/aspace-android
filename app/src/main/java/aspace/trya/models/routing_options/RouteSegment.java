package aspace.trya.models.routing_options;

import aspace.trya.models.osrm_directions.Directions;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;

public class RouteSegment {

    @JsonProperty("pretty_name")
    private String prettyName;

    @JsonProperty("directions")
    private Directions directions;

    @JsonProperty("origin")
    private RouteLoc origin;

    @JsonProperty("name")
    private String name;

    @JsonProperty("dest")
    private RouteLoc dest;

    public void setPrettyName(String prettyName) {
        this.prettyName = prettyName;
    }

    public String getPrettyName() {
        return prettyName;
    }

    public Directions getDirections() {
        return directions;
    }

    public void setDirections(Directions directions) {
        this.directions = directions;
    }

    public RouteLoc getOrigin() {
        return origin;
    }

    public void setOrigin(RouteLoc origin) {
        this.origin = origin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public RouteLoc getDest() {
        return dest;
    }

    public void setDest(RouteLoc dest) {
        this.dest = dest;
    }

    public LatLngBounds getLatLngBounds() {
        return new LatLngBounds.Builder()
            .includes(getDirections().getRoutes().get(0).getGeometry().getLatLngs())
            .include(origin.getLatLng())
            .include(dest.getLatLng())
            .build();
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