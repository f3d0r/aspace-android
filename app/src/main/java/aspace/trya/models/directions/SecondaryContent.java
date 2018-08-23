package aspace.trya.models.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class SecondaryContent implements Serializable {

    @JsonProperty("components")
    private List<Component> components;

    @JsonProperty("modifier")
    private String modifier;

    @JsonProperty("text")
    private String text;

    @JsonProperty("type")
    private String type;

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModifier() {
        return modifier;
    }

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

    @Override
    public String toString() {
        return
                "SecondaryContent{" +
                        "components = '" + components + '\'' +
                        ",modifier = '" + modifier + '\'' +
                        ",text = '" + text + '\'' +
                        ",type = '" + type + '\'' +
                        "}";
    }
}