package aspace.trya.models.routing_options;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

public class RouteOptions implements Serializable {

    @JsonProperty("routes")
    private List<List<RouteSegment>> routes;

    public List<List<RouteSegment>> getRoutes() {
        return routes;
    }

    public void setRoutes(List<List<RouteSegment>> routes) {
        this.routes = routes;
    }

    @Override
    public String toString() {
        return
            "RouteOptions{" +
                "routes = '" + routes + '\'' +
                "}";
    }
}