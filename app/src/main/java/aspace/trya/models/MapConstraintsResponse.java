package aspace.trya.models;

import aspace.trya.models.map_constraints.GeoJSON;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MapConstraintsResponse {

    @JsonProperty("res_info")
    private ResponseInfo responseInfo;

    @JsonProperty("res_content")
    private GeoJSON geoJSON;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public GeoJSON getGeoJSON() {
        return geoJSON;
    }

    public void setGeoJSON(GeoJSON geoJSON) {
        this.geoJSON = geoJSON;
    }

    @Override
    public String toString() {
        return "MapConstraintsResponse{" +
            "responseInfo=" + responseInfo +
            ", geoJSON=" + geoJSON +
            '}';
    }
}