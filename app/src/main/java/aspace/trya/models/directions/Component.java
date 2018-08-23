package aspace.trya.models.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;

public class Component implements Serializable {

    @JsonProperty("text")
    private String text;

    @JsonProperty("type")
    private String type;

    @JsonProperty("abbr")
    private String abbreviation;

    @JsonProperty("abbr_priority")
    private int abbreviationPriority;

    @JsonProperty("imageBaseURL")
    private String imageBaseURL;

    @JsonProperty("directions")
    private ArrayList<String> directions;

    @JsonProperty("active")
    private boolean isActive;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public int getAbbreviationPriority() {
        return abbreviationPriority;
    }

    public void setAbbreviationPriority(int abbreviationPriority) {
        this.abbreviationPriority = abbreviationPriority;
    }

    public String getImageBaseURL() {
        return imageBaseURL;
    }

    public void setImageBaseURL(String imageBaseURL) {
        this.imageBaseURL = imageBaseURL;
    }

    public ArrayList<String> getDirections() {
        return directions;
    }

    public void setDirections(ArrayList<String> directions) {
        this.directions = directions;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return
                "Component{" +
                        "text = '" + text + '\'' +
                        ",type = '" + type + '\'' +
                        "}";
    }
}