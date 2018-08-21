package aspace.trya.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import aspace.trya.models.routing.RouteOptions;

public class RoutingOptionsResponse {

    @JsonProperty("res_info")
    private ResponseInfo responseInfo;

    @JsonProperty("res_content")
    private RouteOptions routeOptions;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
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
                "RoutingOptionsResponse{" +
                        "res_info = '" + responseInfo + '\'' +
                        ",res_content = '" + routeOptions + '\'' +
                        "}";
    }
}