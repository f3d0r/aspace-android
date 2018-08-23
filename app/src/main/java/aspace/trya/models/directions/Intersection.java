package aspace.trya.models.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Intersection implements Serializable {

    @JsonProperty("entry")
    private List<Boolean> entry;

    @JsonProperty("bearings")
    private List<Integer> bearings;

    @JsonProperty("out")
    private int out;

    @JsonProperty("in")
    private int in;

    @JsonProperty("location")
    private List<Double> location;

    @JsonProperty("classes")
    private List<String> classes;

    @JsonProperty("lanes")
    private List<Lane> lanes;

    public void setEntry(List<Boolean> entry) {
        this.entry = entry;
    }

    public List<Boolean> getEntry() {
        return entry;
    }

    public void setBearings(List<Integer> bearings) {
        this.bearings = bearings;
    }

    public List<Integer> getBearings() {
        return bearings;
    }

    public void setIn(int in) {
        this.in = in;
    }

    public int getIn() {
        return in;
    }

    public int getOut() {
        return out;
    }

    public void setOut(int out) {
        this.out = out;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public List<Double> getLocation() {
        return location;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    public List<Lane> getLanes() {
        return lanes;
    }

    public void setLanes(List<Lane> lanes) {
        this.lanes = lanes;
    }

    @Override
    public String toString() {
        return
                "Intersection{" +
                        "entry = '" + entry + '\'' +
                        ",bearings = '" + bearings + '\'' +
                        ",in = '" + in + '\'' +
                        ",location = '" + location + '\'' +
                        "}";
    }
}