package aspace.trya.models.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class BannerInstructions implements Serializable {

    @JsonProperty("secondary")
    private Object secondary;

    @JsonProperty("distanceAlongGeometry")
    private double distanceAlongGeometry;

    @JsonProperty("primary")
    private PrimaryContent primaryContent;

    @JsonProperty("sub")
    private SubContent subContent;

    public Object getSecondary() {
        return secondary;
    }

    public void setSecondary(Object secondary) {
        this.secondary = secondary;
    }

    public void setDistanceAlongGeometry(double distanceAlongGeometry) {
        this.distanceAlongGeometry = distanceAlongGeometry;
    }

    public double getDistanceAlongGeometry() {
        return distanceAlongGeometry;
    }

    public void setPrimaryContent(PrimaryContent primaryContent) {
        this.primaryContent = primaryContent;
    }

    public PrimaryContent getPrimaryContent() {
        return primaryContent;
    }

    public SubContent getSubContent() {
        return subContent;
    }

    public void setSubContent(SubContent subContent) {
        this.subContent = subContent;
    }

    @Override
    public String toString() {
        return
                "BannerInstructions{" +
                        "secondary = '" + secondary + '\'' +
                        ",distanceAlongGeometry = '" + distanceAlongGeometry + '\'' +
                        ",primaryContent = '" + primaryContent + '\'' +
                        "}";
    }
}