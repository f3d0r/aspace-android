package aspace.trya.models.routing;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import aspace.trya.misc.MapboxDirectionsOptions;

public class ParkBikeOption implements Serializable {

    @JsonProperty("bbox")
    private Bbox bbox;

    @JsonProperty("segments")
    private List<RouteSegment> segments;

    public Bbox getBbox() {
        return bbox;
    }

    public void setBbox(Bbox bbox) {
        this.bbox = bbox;
    }

    public List<RouteSegment> getSegments() {
        return segments;
    }

    public void setSegments(List<RouteSegment> segments) {
        this.segments = segments;
    }

    @Override
    public String toString() {
        return
                "ParkBikeOption{" +
                        "bbox = '" + bbox + '\'' +
                        ",segments = '" + segments + '\'' +
                        "}";
    }

    public List<MapboxDirectionsOptions> getDirectionsOptions() {
        List<MapboxDirectionsOptions> directionsOptions = new ArrayList<>();
        for (RouteSegment currentSegment : segments) {
            directionsOptions.add(new MapboxDirectionsOptions(currentSegment.getStart(), currentSegment.getEnd(), currentSegment.getProfile()));
        }
        return directionsOptions;
    }
}