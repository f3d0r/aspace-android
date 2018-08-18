package aspace.trya.models.routing;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LngLat {

    @JsonProperty("lng")
    private double lng;

    @JsonProperty("lat")
    private double lat;

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return
                "End{" +
                        "lng = '" + lng + '\'' +
                        ",lat = '" + lat + '\'' +
                        "}";
    }
}