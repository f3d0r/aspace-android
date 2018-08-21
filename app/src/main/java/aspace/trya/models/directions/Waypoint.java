package aspace.trya.models.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Waypoint implements Serializable {

    @JsonProperty("name")
    private String name;

    @JsonProperty("location")
    private List<Double> location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                "Waypoint{" +
                        "name = '" + name + '\'' +
                        ",location = '" + location + '\'' +
                        "}";
    }
}