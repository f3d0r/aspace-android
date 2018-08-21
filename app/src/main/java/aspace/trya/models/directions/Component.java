package aspace.trya.models.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Component implements Serializable {

    @JsonProperty("text")
    private String text;

    @JsonProperty("type")
    private String type;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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