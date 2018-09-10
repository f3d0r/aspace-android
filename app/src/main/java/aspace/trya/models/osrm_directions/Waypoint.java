package aspace.trya.models.osrm_directions;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Waypoint {

    @JsonProperty("hint")
    private String hint;

    @JsonProperty("name")
    private String name;

    @JsonProperty("location")
    private List<Double> location;

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

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
                "hint = '" + hint + '\'' +
                ",name = '" + name + '\'' +
                ",location = '" + location + '\'' +
                "}";
    }
}