package aspace.trya.geojson;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GeoJSON {

    @JsonProperty("features")
    private List<Feature> features;

    @JsonProperty("query")
    private List<String> query;

    @JsonProperty("attribution")
    private String attribution;

    @JsonProperty("type")
    private String type;

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public List<String> getQuery() {
        return query;
    }

    public void setQuery(List<String> query) {
        this.query = query;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
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
                        ",query = '" + query + '\'' +
                        ",attribution = '" + attribution + '\'' +
                        ",type = '" + type + '\'' +
                        "}";
    }
}