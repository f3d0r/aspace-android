package aspace.trya.models.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class PrimaryContent implements Serializable {

    @JsonProperty("components")
    private List<Component> components;

    @JsonProperty("modifier")
    private String modifier;

    @JsonProperty("text")
    private String text;

    @JsonProperty("type")
    private String type;

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

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
                "PrimaryContent{" +
                        "components = '" + components + '\'' +
                        ",modifier = '" + modifier + '\'' +
                        ",text = '" + text + '\'' +
                        ",type = '" + type + '\'' +
                        "}";
    }
}