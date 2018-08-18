package aspace.trya.models.routing;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RouteSegment {

    @JsonProperty("start")
    private LngLat start;

    @JsonProperty("end")
    private LngLat end;

    public LngLat getStart() {
        return start;
    }

    public void setStart(LngLat start) {
        this.start = start;
    }

    public LngLat getEnd() {
        return end;
    }

    public void setEnd(LngLat end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return
                "RouteSegment{" +
                        "start = '" + start + '\'' +
                        ",end = '" + end + '\'' +
                        "}";
    }
}