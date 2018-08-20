package aspace.trya.models.routing;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class LngLat implements Serializable {

    @JsonProperty("lng")
    private double lng;

    @JsonProperty("lat")
    private double lat;

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLng() {
        return lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    @Override
    public String toString() {
        return
                "Start{" +
                        "lng = '" + lng + '\'' +
                        ",lat = '" + lat + '\'' +
                        "}";
    }
}