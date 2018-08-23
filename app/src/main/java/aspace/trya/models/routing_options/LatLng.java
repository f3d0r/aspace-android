package aspace.trya.models.routing_options;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class LatLng implements Serializable {

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
                "LatLng{" +
                        "lng = '" + lng + '\'' +
                        ",lat = '" + lat + '\'' +
                        "}";
    }

    public com.mapbox.mapboxsdk.geometry.LatLng getMapBoxLatLng() {
        return new com.mapbox.mapboxsdk.geometry.LatLng(lat, lng);
    }
}