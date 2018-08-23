package aspace.trya.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

import aspace.trya.models.directions.RouteLeg;

public class DirectionsResponse implements Serializable {

    @JsonProperty("duration")
    private double duration;

    @JsonProperty("distance")
    private double distance;

    @JsonProperty("voiceLocale")
    private String voiceLocale;

    @JsonProperty("legs")
    private List<RouteLeg> routeLegs;

    @JsonProperty("weight_name")
    private String weightName;

    @JsonProperty("weight")
    private double weight;

    @JsonProperty("geometry")
    private String geometry;

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

    public List<RouteLeg> getRouteLegs() {
        return routeLegs;
    }

    public void setRouteLegs(List<RouteLeg> routeLegs) {
        this.routeLegs = routeLegs;
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

    @Override
    public String toString() {
        return
                "DirectionsResponse{" +
                        "duration = '" + duration + '\'' +
                        ",distance = '" + distance + '\'' +
                        ",voiceLocale = '" + voiceLocale + '\'' +
                        ",routeLegs = '" + routeLegs + '\'' +
                        ",weight_name = '" + weightName + '\'' +
                        ",weight = '" + weight + '\'' +
                        ",geometry = '" + geometry + '\'' +
                        "}";
    }
}