package aspace.trya.models.routing_options;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mapbox.mapboxsdk.geometry.LatLng;
import java.io.Serializable;

public class RouteLoc implements Serializable {

    @JsonProperty("lng")
    private double lng;

    @JsonProperty("lat")
    private double lat;

    @JsonProperty("meta")
    private LocMetaData locMetaData;

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

    public LocMetaData getLocMetaData() {
        return locMetaData;
    }

    public void setLocMetaData(LocMetaData locMetaData) {
        this.locMetaData = locMetaData;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }

    @Override
    public String toString() {
        return
            "Dest{" +
                "lng = '" + lng + '\'' +
                ",lat = '" + lat + '\'' +
                ",locMetaData = '" + locMetaData + '\'' +
                "}";
    }
}