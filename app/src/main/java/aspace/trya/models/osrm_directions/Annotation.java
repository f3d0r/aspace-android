package aspace.trya.models.osrm_directions;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Annotation {

    @JsonProperty("duration")
    private List<Double> duration;

    @JsonProperty("metadata")
    private Metadata metadata;

    @JsonProperty("nodes")
    private List<Long> nodes;

    @JsonProperty("distance")
    private List<Double> distance;

    @JsonProperty("datasources")
    private List<Integer> datasources;

    @JsonProperty("weight")
    private List<Double> weight;

    @JsonProperty("speed")
    private List<Double> speed;

    public List<Double> getDuration() {
        return duration;
    }

    public void setDuration(List<Double> duration) {
        this.duration = duration;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<Long> getNodes() {
        return nodes;
    }

    public void setNodes(List<Long> nodes) {
        this.nodes = nodes;
    }

    public List<Double> getDistance() {
        return distance;
    }

    public void setDistance(List<Double> distance) {
        this.distance = distance;
    }

    public List<Integer> getDatasources() {
        return datasources;
    }

    public void setDatasources(List<Integer> datasources) {
        this.datasources = datasources;
    }

    public List<Double> getWeight() {
        return weight;
    }

    public void setWeight(List<Double> weight) {
        this.weight = weight;
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
                ",metadata = '" + metadata + '\'' +
                ",nodes = '" + nodes + '\'' +
                ",distance = '" + distance + '\'' +
                ",datasources = '" + datasources + '\'' +
                ",weight = '" + weight + '\'' +
                ",speed = '" + speed + '\'' +
                "}";
    }
}