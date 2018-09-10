package aspace.trya.models.osrm_directions;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Step {

    @JsonProperty("duration")
    private double duration;

    @JsonProperty("mode")
    private String mode;

    @JsonProperty("distance")
    private double distance;

    @JsonProperty("instruction")
    private String instruction;

    @JsonProperty("name")
    private String name;

    @JsonProperty("weight")
    private double weight;

    @JsonProperty("geometry")
    private Geometry geometry;

    @JsonProperty("driving_side")
    private String drivingSide;

    @JsonProperty("intersections")
    private List<Intersection> intersections;

    @JsonProperty("maneuver")
    private Maneuver maneuver;

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
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

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
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
                "duration = '" + duration + '\'' +
                ",mode = '" + mode + '\'' +
                ",distance = '" + distance + '\'' +
                ",instruction = '" + instruction + '\'' +
                ",name = '" + name + '\'' +
                ",weight = '" + weight + '\'' +
                ",geometry = '" + geometry + '\'' +
                ",driving_side = '" + drivingSide + '\'' +
                ",intersections = '" + intersections + '\'' +
                ",maneuver = '" + maneuver + '\'' +
                "}";
    }
}