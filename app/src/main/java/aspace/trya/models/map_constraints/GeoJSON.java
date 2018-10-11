package aspace.trya.models.map_constraints;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class GeoJSON {

    @JsonProperty("features")
    private List<Feature> features;

    @JsonProperty("type")
    private String type;

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
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
            "GeoJSON{" +
                "features = '" + features + '\'' +
                ",type = '" + type + '\'' +
                "}";
    }
}