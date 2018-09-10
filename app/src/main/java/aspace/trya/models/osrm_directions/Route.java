package aspace.trya.models.osrm_directions;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Route {

    @JsonProperty("duration")
    private double duration;

    @JsonProperty("distance")
    private int distance;

    @JsonProperty("legs")
    private List<Leg> legs;

    @JsonProperty("weight_name")
    private String weightName;

    @JsonProperty("weight")
    private double weight;

    @JsonProperty("geometry")
    private Geometry geometry;

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    public String getWeightName() {
        return weightName;
    }

    public void setWeightName(String weightName) {
        this.weightName = weightName;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @Override
    public String toString() {
        return
            "Route{" +
                "duration = '" + duration + '\'' +
                ",distance = '" + distance + '\'' +
                ",legs = '" + legs + '\'' +
                ",weight_name = '" + weightName + '\'' +
                ",weight = '" + weight + '\'' +
                ",geometry = '" + geometry + '\'' +
                "}";
    }
}