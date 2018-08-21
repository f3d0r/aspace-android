package aspace.trya.models.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Leg implements Serializable {

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
    private List<Step> steps;

    public Annotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return
                "Leg{" +
                        "annotation = '" + annotation + '\'' +
                        ",summary = '" + summary + '\'' +
                        ",duration = '" + duration + '\'' +
                        ",distance = '" + distance + '\'' +
                        ",weight = '" + weight + '\'' +
                        ",steps = '" + steps + '\'' +
                        "}";
    }
}