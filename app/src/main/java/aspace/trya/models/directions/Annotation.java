package aspace.trya.models.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Annotation implements Serializable {

    @JsonProperty("duration")
    private List<Double> duration;

    @JsonProperty("distance")
    private List<Double> distance;

    @JsonProperty("congestion")
    private List<String> congestion;

    @JsonProperty("speed")
    private List<Double> speed;

    public List<Double> getDuration() {
        return duration;
    }

    public void setDuration(List<Double> duration) {
        this.duration = duration;
    }

    public List<Double> getDistance() {
        return distance;
    }

    public void setDistance(List<Double> distance) {
        this.distance = distance;
    }

    public List<String> getCongestion() {
        return congestion;
    }

    public void setCongestion(List<String> congestion) {
        this.congestion = congestion;
    }

    public List<Double> getSpeed() {
        return speed;
    }

    public void setSpeed(List<Double> speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return
                "Annotation{" +
                        "duration = '" + duration + '\'' +
                        ",distance = '" + distance + '\'' +
                        ",congestion = '" + congestion + '\'' +
                        ",speed = '" + speed + '\'' +
                        "}";
    }
}