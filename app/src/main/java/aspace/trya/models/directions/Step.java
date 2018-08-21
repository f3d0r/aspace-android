package aspace.trya.models.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Step implements Serializable {

    @JsonProperty("mode")
    private String mode;

    @JsonProperty("duration")
    private double duration;

    @JsonProperty("distance")
    private double distance;

    @JsonProperty("bannerInstructions")
    private List<BannerInstructions> bannerInstructions;

    @JsonProperty("voiceInstructions")
    private List<VoiceInstructions> voiceInstructions;

    @JsonProperty("name")
    private String name;

    @JsonProperty("weight")
    private double weight;

    @JsonProperty("geometry")
    private String geometry;

    @JsonProperty("driving_side")
    private String drivingSide;

    @JsonProperty("intersections")
    private List<Intersection> intersections;

    @JsonProperty("maneuver")
    private Maneuver maneuver;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

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

    public List<BannerInstructions> getBannerInstructions() {
        return bannerInstructions;
    }

    public void setBannerInstructions(List<BannerInstructions> bannerInstructions) {
        this.bannerInstructions = bannerInstructions;
    }

    public List<VoiceInstructions> getVoiceInstructions() {
        return voiceInstructions;
    }

    public void setVoiceInstructions(List<VoiceInstructions> voiceInstructions) {
        this.voiceInstructions = voiceInstructions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDrivingSide() {
        return drivingSide;
    }

    public void setDrivingSide(String drivingSide) {
        this.drivingSide = drivingSide;
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public void setIntersections(List<Intersection> intersections) {
        this.intersections = intersections;
    }

    public Maneuver getManeuver() {
        return maneuver;
    }

    public void setManeuver(Maneuver maneuver) {
        this.maneuver = maneuver;
    }

    @Override
    public String toString() {
        return
                "Step{" +
                        "mode = '" + mode + '\'' +
                        ",duration = '" + duration + '\'' +
                        ",distance = '" + distance + '\'' +
                        ",bannerInstructions = '" + bannerInstructions + '\'' +
                        ",voiceInstructions = '" + voiceInstructions + '\'' +
                        ",name = '" + name + '\'' +
                        ",weight = '" + weight + '\'' +
                        ",geometry = '" + geometry + '\'' +
                        ",driving_side = '" + drivingSide + '\'' +
                        ",intersections = '" + intersections + '\'' +
                        ",maneuver = '" + maneuver + '\'' +
                        "}";
    }
}