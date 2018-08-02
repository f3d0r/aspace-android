package aspace.trya.geojson;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Feature implements SearchSuggestion {

    @JsonProperty("place_name")
    private String placeName;

    @JsonProperty("place_type")
    private List<String> placeType;

    @JsonProperty("bbox")
    private List<Double> bbox;

    @JsonProperty("center")
    private List<Double> center;

    @JsonProperty("context")
    private List<Context> context;

    @JsonProperty("geometry")
    private Geometry geometry;

    @JsonProperty("id")
    private String id;

    @JsonProperty("text")
    private String text;

    @JsonProperty("type")
    private String type;

    @JsonProperty("relevance")
    private double relevance;

    @JsonProperty("properties")
    private Properties properties;

    @JsonProperty("matching_place_name")
    private String matchingPlaceName;

    @JsonProperty("matching_text")
    private String matchingText;

    @JsonProperty("address")
    private String address;

    private String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public List<String> getPlaceType() {
        return placeType;
    }

    public void setPlaceType(List<String> placeType) {
        this.placeType = placeType;
    }

    public List<Double> getBbox() {
        return bbox;
    }

    public void setBbox(List<Double> bbox) {
        this.bbox = bbox;
    }

    public List<Double> getCenter() {
        return center;
    }

    public void setCenter(List<Double> center) {
        this.center = center;
    }

    public List<Context> getContext() {
        return context;
    }

    public void setContext(List<Context> context) {
        this.context = context;
    }

    private Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getRelevance() {
        return relevance;
    }

    public void setRelevance(double relevance) {
        this.relevance = relevance;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getMatchingPlaceName() {
        return matchingPlaceName;
    }

    public void setMatchingPlaceName(String matchingPlaceName) {
        this.matchingPlaceName = matchingPlaceName;
    }

    public String getMatchingText() {
        return matchingText;
    }

    public void setMatchingText(String matchingText) {
        this.matchingText = matchingText;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return
                "Feature{" +
                        "place_name = '" + placeName + '\'' +
                        ",place_type = '" + placeType + '\'' +
                        ",bbox = '" + bbox + '\'' +
                        ",center = '" + center + '\'' +
                        ",context = '" + context + '\'' +
                        ",geometry = '" + geometry + '\'' +
                        ",id = '" + id + '\'' +
                        ",text = '" + text + '\'' +
                        ",type = '" + type + '\'' +
                        ",relevance = '" + relevance + '\'' +
                        ",properties = '" + properties + '\'' +
                        ",matching_place_name = '" + matchingPlaceName + '\'' +
                        ",matching_text = '" + matchingText + '\'' +
                        ",address = '" + address + '\'' +
                        "}";
    }

    @Override
    public String getBody() {
        return getPlaceName() + "\n" + getGeometry().getCoordinatesString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}