package aspace.trya.models.map_constraints;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Feature {

    @JsonProperty("geometry")
    private Geometry geometry;

    @JsonProperty("type")
    private String type;

    @JsonProperty("properties")
    private Properties properties;

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return
            "Feature{" +
                "geometry = '" + geometry + '\'' +
                ",type = '" + type + '\'' +
                ",properties = '" + properties + '\'' +
                "}";
    }
}