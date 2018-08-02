package aspace.trya.geojson;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Geometry {

    @JsonProperty("coordinates")
    private List<Double> coordinates;

    @JsonProperty("type")
    private String type;

    @JsonProperty("interpolated")
    private boolean interpolated;

    @JsonProperty("omitted")
    private boolean omitted;

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

    public boolean isInterpolated() {
        return interpolated;
    }

    public void setInterpolated(boolean interpolated) {
        this.interpolated = interpolated;
    }

    public boolean isOmitted() {
        return omitted;
    }

    public void setOmitted(boolean omitted) {
        this.omitted = omitted;
    }

    public String getCoordinatesString() {
        return getCoordinates().get(0) + "," + getCoordinates().get(1);
    }

    @Override
    public String toString() {
        return
                "Geometry{" +
                        "coordinates = '" + coordinates + '\'' +
                        ",type = '" + type + '\'' +
                        ",interpolated = '" + interpolated + '\'' +
                        ",omitted = '" + omitted + '\'' +
                        "}";
    }
}