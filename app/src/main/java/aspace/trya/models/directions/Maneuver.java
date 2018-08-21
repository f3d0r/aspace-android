package aspace.trya.models.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Maneuver implements Serializable {

    @JsonProperty("instruction")
    private String instruction;

    @JsonProperty("bearing_after")
    private int bearingAfter;

    @JsonProperty("bearing_before")
    private int bearingBefore;

    @JsonProperty("location")
    private List<Double> location;

    @JsonProperty("type")
    private String type;

    @JsonProperty("modifier")
    private String modifier;

    @JsonProperty("exit")
    private int exit;

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public int getBearingAfter() {
        return bearingAfter;
    }

    public void setBearingAfter(int bearingAfter) {
        this.bearingAfter = bearingAfter;
    }

    public int getBearingBefore() {
        return bearingBefore;
    }

    public void setBearingBefore(int bearingBefore) {
        this.bearingBefore = bearingBefore;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public int getExit() {
        return exit;
    }

    public void setExit(int exit) {
        this.exit = exit;
    }

    @Override
    public String toString() {
        return
                "Maneuver{" +
                        "instruction = '" + instruction + '\'' +
                        ",bearing_after = '" + bearingAfter + '\'' +
                        ",bearing_before = '" + bearingBefore + '\'' +
                        ",location = '" + location + '\'' +
                        ",type = '" + type + '\'' +
                        ",modifier = '" + modifier + '\'' +
                        ",exit = '" + exit + '\'' +
                        "}";
    }
}