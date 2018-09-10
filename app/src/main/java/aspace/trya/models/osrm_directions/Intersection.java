package aspace.trya.models.osrm_directions;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Intersection {

    @JsonProperty("entry")
    private List<Boolean> entry;

    @JsonProperty("bearings")
    private List<Integer> bearings;

    @JsonProperty("in")
    private int in;

    @JsonProperty("location")
    private List<Double> location;

    public List<Boolean> getEntry() {
        return entry;
    }

    public void setEntry(List<Boolean> entry) {
        this.entry = entry;
    }

    public List<Integer> getBearings() {
        return bearings;
    }

    public void setBearings(List<Integer> bearings) {
        this.bearings = bearings;
    }

    public int getIn() {
        return in;
    }

    public void setIn(int in) {
        this.in = in;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
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