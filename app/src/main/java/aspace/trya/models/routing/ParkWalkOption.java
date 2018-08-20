package aspace.trya.models.routing;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class ParkWalkOption implements Serializable {

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
                "ParkWalkOption{" +
                        "bbox = '" + bbox + '\'' +
                        ",segments = '" + segments + '\'' +
                        "}";
    }
}