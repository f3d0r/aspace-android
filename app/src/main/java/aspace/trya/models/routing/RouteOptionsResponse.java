package aspace.trya.models.routing;

import com.fasterxml.jackson.annotation.JsonProperty;

import aspace.trya.models.ResponseInfo;

public class RouteOptionsResponse {

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
                "RouteOptionsResponse{" +
                        "res_info = '" + responseInfo + '\'' +
                        ",res_content = '" + routeOptions + '\'' +
                        "}";
    }
}