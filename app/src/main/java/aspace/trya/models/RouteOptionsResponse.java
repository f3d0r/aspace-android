package aspace.trya.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import aspace.trya.models.routing_options.RouteSegment;

public class RouteOptionsResponse implements Serializable {

    @JsonProperty("res_info")
    private ResponseInfo responseInfo;

    @JsonProperty("res_content")
    private List<List<RouteSegment>> routeOptions;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<List<RouteSegment>> getRouteOptions() {
        return routeOptions;
    }

    public void setRouteOptions(List<List<RouteSegment>> routeOptions) {
        this.routeOptions = routeOptions;
    }

    @Override
    public String toString() {
        return
                "RouteOptionsResponse{" +
                        "res_info = '" + responseInfo + '\'' +
                        ",res_content = '" + routeOptions + '\'' +
                        "}";
    }

    public LatLngBounds getLatLngBounds(int routeOption) {
        List<com.mapbox.mapboxsdk.geometry.LatLng> latLngs = new ArrayList<>();
        for (RouteSegment currentRouteSegment : routeOptions.get(routeOption)) {
            latLngs.add(currentRouteSegment.getOrigin().getMapBoxLatLng());
            latLngs.add(currentRouteSegment.getDest().getMapBoxLatLng());
        }
        return new LatLngBounds.Builder().includes(latLngs).build();
    }
}