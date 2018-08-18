package aspace.trya.models.geojson;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import aspace.trya.search.SearchResult;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoJSON {

    @JsonProperty("features")
    private List<Feature> features;

    @JsonProperty("query")
    private List<String> query;

    @JsonProperty("attribution")
    private String attribution;

    @JsonProperty("type")
    private String type;

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    private List<Feature> getFeatures() {
        return features;
    }

    public void setQuery(List<String> query) {
        this.query = query;
    }

    public List<String> getQuery() {
        return query;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public String getAttribution() {
        return attribution;
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
                "GeoJSON{" +
                        "features = '" + features + '\'' +
                        ",query = '" + query + '\'' +
                        ",attribution = '" + attribution + '\'' +
                        ",type = '" + type + '\'' +
                        "}";
    }

    public ArrayList<SearchResult> getSearchResults(Context context) {
        ArrayList<SearchResult> searchResults = new ArrayList<>();
        for (Feature currentFeature : getFeatures()) {
            searchResults.add(new SearchResult(currentFeature, context));
        }
        return searchResults;
    }
}