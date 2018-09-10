package aspace.trya.models.geojson;

import android.content.Context;
import aspace.trya.search.SearchResult;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

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

    private List<Feature> getFeatures() {
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

    public ArrayList<SearchResult> getSearchResults(Context context) {
        ArrayList<SearchResult> searchResults = new ArrayList<>();
        for (Feature currentFeature : getFeatures()) {
            searchResults.add(new SearchResult(currentFeature, context));
        }
        return searchResults;
    }
}