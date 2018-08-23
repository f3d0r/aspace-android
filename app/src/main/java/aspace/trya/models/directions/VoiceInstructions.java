package aspace.trya.models.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class VoiceInstructions implements Serializable {

    @JsonProperty("ssmlAnnouncement")
    private String ssmlAnnouncement;

    @JsonProperty("distanceAlongGeometry")
    private double distanceAlongGeometry;

    @JsonProperty("announcement")
    private String announcement;

    public void setSsmlAnnouncement(String ssmlAnnouncement) {
        this.ssmlAnnouncement = ssmlAnnouncement;
    }

    public String getSsmlAnnouncement() {
        return ssmlAnnouncement;
    }

    public void setDistanceAlongGeometry(double distanceAlongGeometry) {
        this.distanceAlongGeometry = distanceAlongGeometry;
    }

    public double getDistanceAlongGeometry() {
        return distanceAlongGeometry;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public String getAnnouncement() {
        return announcement;
    }

    @Override
    public String toString() {
        return
                "VoiceInstructions{" +
                        "ssmlAnnouncement = '" + ssmlAnnouncement + '\'' +
                        ",distanceAlongGeometry = '" + distanceAlongGeometry + '\'' +
                        ",announcement = '" + announcement + '\'' +
                        "}";
    }
}