package aspace.trya.models.osrm_directions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import java.util.ArrayList;
import java.util.List;

public class Geometry {

    @JsonProperty("coordinates")
    private List<List<Double>> coordinates;

    @JsonProperty("type")
    private String type;

    public List<List<Double>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<Double>> coordinates) {
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Point> getPoints() {
        List<Point> points = new ArrayList<>();
        for (List<Double> currentCoordinates : coordinates) {
            points.add(Point.fromLngLat(currentCoordinates.get(0), currentCoordinates.get(1)));
        }
        return points;
    }

    public List<LatLng> getLatLngs() {
        List<LatLng> latLngs = new ArrayList<>();
        for (List<Double> currentCoordinates : coordinates) {
            latLngs.add(new LatLng(currentCoordinates.get(1), currentCoordinates.get(0)));
        }
        return latLngs;
    }

    @Override
    public String toString() {
        return
            "Geometry{" +
                "coordinates = '" + coordinates + '\'' +
                ",type = '" + type + '\'' +
                "}";
    }
}