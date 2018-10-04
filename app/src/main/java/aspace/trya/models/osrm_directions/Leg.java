package aspace.trya.models.osrm_directions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Leg implements Serializable {

    @JsonProperty("annotation")
    private Annotation annotation;

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("duration")
    private double duration;

    @JsonProperty("distance")
    private int distance;

    @JsonProperty("weight")
    private double weight;

    @JsonProperty("steps")
    private List<Step> steps;

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

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
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