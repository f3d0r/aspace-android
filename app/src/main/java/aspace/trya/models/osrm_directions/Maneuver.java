package aspace.trya.models.osrm_directions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Maneuver implements Serializable {

    @JsonProperty("modifier")
    private String modifier;

    @JsonProperty("bearing_after")
    private int bearingAfter;

    @JsonProperty("location")
    private List<Double> location;

    @JsonProperty("bearing_before")
    private int bearingBefore;

    @JsonProperty("type")
    private String type;

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public void setBearingAfter(int bearingAfter) {
        this.bearingAfter = bearingAfter;
    }

    public int getBearingAfter() {
        return bearingAfter;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public List<Double> getLocation() {
        return location;
    }

    public int getBearingBefore() {
        return bearingBefore;
    }

    public void setBearingBefore(int bearingBefore) {
        this.bearingBefore = bearingBefore;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return
            "Maneuver{" +
                "modifier = '" + modifier + '\'' +
                ",bearing_after = '" + bearingAfter + '\'' +
                ",location = '" + location + '\'' +
                ",bearing_before = '" + bearingBefore + '\'' +
                ",type = '" + type + '\'' +
                "}";
    }
}