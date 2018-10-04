package aspace.trya.models;

import aspace.trya.models.routing_options.RouteOptions;
import aspace.trya.models.routing_options.RouteSegment;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RouteOptionsResponse implements Serializable {

    @JsonProperty("res_info")
    private ResponseInfo resInfo;

    @JsonProperty("res_content")
    private RouteOptions routeOptions;

    public ResponseInfo getResInfo() {
        return resInfo;
    }

    public void setResInfo(ResponseInfo resInfo) {
        this.resInfo = resInfo;
    }

    public RouteOptions getRouteOptions() {
        return routeOptions;
    }

    public void setRouteOptions(RouteOptions routeOptions) {
        this.routeOptions = routeOptions;
    }

    @Override
    public String toString() {
        return
            "RouteOptionsResponse{" +
                "res_info = '" + resInfo + '\'' +
                ",res_content = '" + routeOptions + '\'' +
                "}";
    }

    public LatLngBounds getLatLngBounds(int routeOptionsIndex) {
        List<LatLng> latLngs = new ArrayList<>();
        List<RouteSegment> defaultRouteSegments = routeOptions.getRoutes().get(routeOptionsIndex);
        for (RouteSegment currentRouteSegment : defaultRouteSegments) {
            latLngs.addAll(
                currentRouteSegment.getDirections().getRoutes().get(0).getGeometry().getLatLngs());
            latLngs.add(currentRouteSegment.getOrigin().getLatLng());
            latLngs.add(currentRouteSegment.getDest().getLatLng());
        }
        return new LatLngBounds.Builder().includes(latLngs).build();
    }
}