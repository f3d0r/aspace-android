package aspace.trya.models.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class BannerInstructions implements Serializable {

    @JsonProperty("secondaryContent")
    private SecondaryContent secondaryContent;

    @JsonProperty("distanceAlongGeometry")
    private double distanceAlongGeometry;

    @JsonProperty("primaryContent")
    private PrimaryContent primaryContent;

    public SecondaryContent getSecondaryContent() {
        return secondaryContent;
    }

    public void setSecondaryContent(SecondaryContent secondaryContent) {
        this.secondaryContent = secondaryContent;
    }

    public double getDistanceAlongGeometry() {
        return distanceAlongGeometry;
    }

    public void setDistanceAlongGeometry(double distanceAlongGeometry) {
        this.distanceAlongGeometry = distanceAlongGeometry;
    }

    public PrimaryContent getPrimaryContent() {
        return primaryContent;
    }

    public void setPrimaryContent(PrimaryContent primaryContent) {
        this.primaryContent = primaryContent;
    }

    @Override
    public String toString() {
        return
                "BannerInstructions{" +
                        "secondaryContent = '" + secondaryContent + '\'' +
                        ",distanceAlongGeometry = '" + distanceAlongGeometry + '\'' +
                        ",primaryContent = '" + primaryContent + '\'' +
                        "}";
    }
}