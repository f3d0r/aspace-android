package aspace.trya.models.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class RouteLeg implements Serializable {

    @JsonProperty("annotation")
    private Annotation annotation;

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("duration")
    private double duration;

    @JsonProperty("distance")
    private double distance;

    @JsonProperty("weight")
    private double weight;

    @JsonProperty("steps")
    private List<RouteStep> steps;

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getDuration() {
        return duration;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public List<RouteStep> getSteps() {
        return steps;
    }

    public void setSteps(List<RouteStep> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return
                "RouteLeg{" +
                        "annotation = '" + annotation + '\'' +
                        ",summary = '" + summary + '\'' +
                        ",duration = '" + duration + '\'' +
                        ",distance = '" + distance + '\'' +
                        ",weight = '" + weight + '\'' +
                        ",steps = '" + steps + '\'' +
                        "}";
    }
}