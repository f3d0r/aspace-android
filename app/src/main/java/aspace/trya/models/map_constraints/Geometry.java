package aspace.trya.models.map_constraints;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mapbox.mapboxsdk.geometry.LatLng;
import java.util.ArrayList;
import java.util.List;

public class Geometry {

    @JsonProperty("coordinates")
    private List<List<List<Double>>> coordinates;

    @JsonProperty("type")
    private String type;

    public List<List<List<Double>>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<List<Double>>> coordinates) {
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

    public List<LatLng> getLatLngs() {
        List<LatLng> latLngs = new ArrayList<>();
        for (List<Double> currentCoordinates : coordinates.get(0)) {
            latLngs.add(new LatLng(currentCoordinates.get(1), currentCoordinates.get(0)));
        }
        return latLngs;
    }
}