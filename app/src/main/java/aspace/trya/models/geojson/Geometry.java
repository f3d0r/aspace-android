package aspace.trya.models.geojson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mapbox.mapboxsdk.geometry.LatLng;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Geometry {

    @JsonProperty("coordinates")
    private List<Double> coordinates;

    @JsonProperty("type")
    private String type;

    private List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return
            "Geometry{" +
                "coordinates = '" + coordinates + '\'' +
                ",type = '" + type + '\'' +
                "}";
    }

    public LatLng getLatLng() {
        return new LatLng(getCoordinates().get(1), getCoordinates().get(0));
    }
}