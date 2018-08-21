package aspace.trya.models.directions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Route implements Serializable {

    @JsonProperty("duration")
    private double duration;

    @JsonProperty("distance")
    private double distance;

    @JsonProperty("voiceLocale")
    private String voiceLocale;

    @JsonProperty("legs")
    private List<Leg> legs;

    @JsonProperty("weight_name")
    private String weightName;

    @JsonProperty("weight")
    private double weight;

    @JsonProperty("geometry")
    private String geometry;

    @JsonIgnore
    private String routeType;

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getVoiceLocale() {
        return voiceLocale;
    }

    public void setVoiceLocale(String voiceLocale) {
        this.voiceLocale = voiceLocale;
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

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    @Override
    public String toString() {
        return
                "Route{" +
                        "duration = '" + duration + '\'' +
                        ",distance = '" + distance + '\'' +
                        ",voiceLocale = '" + voiceLocale + '\'' +
                        ",legs = '" + legs + '\'' +
                        ",weight_name = '" + weightName + '\'' +
                        ",weight = '" + weight + '\'' +
                        ",geometry = '" + geometry + '\'' +
                        "}";
    }
}