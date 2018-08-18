package aspace.trya.models.geojson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Feature implements Serializable {

    @JsonProperty("place_name")
    private String placeName;

    @JsonProperty("place_type")
    private List<String> placeType;

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
    private int relevance;

    @JsonProperty("properties")
    private Properties properties;

    @JsonProperty("bbox")
    private List<Double> bbox;

    private void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceName() {
        return placeName;
    }

    private void setPlaceType(List<String> placeType) {
        this.placeType = placeType;
    }

    public List<String> getPlaceType() {
        return placeType;
    }

    public void setCenter(List<Double> center) {
        this.center = center;
    }

    public List<Double> getCenter() {
        return center;
    }

    public void setContext(List<Context> context) {
        this.context = context;
    }

    public List<Context> getContext() {
        return context;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    public int getRelevance() {
        return relevance;
    }

    public void setRelevance(int relevance) {
        this.relevance = relevance;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }

    private List<Double> getBbox() {
        return bbox;
    }

    public void setBbox(List<Double> bbox) {
        this.bbox = bbox;
    }

    @Override
    public String toString() {
        return
                "Feature{" +
                        "place_name = '" + placeName + '\'' +
                        ",place_type = '" + placeType + '\'' +
                        ",center = '" + center + '\'' +
                        ",context = '" + context + '\'' +
                        ",geometry = '" + geometry + '\'' +
                        ",id = '" + id + '\'' +
                        ",text = '" + text + '\'' +
                        ",type = '" + type + '\'' +
                        ",relevance = '" + relevance + '\'' +
                        ",properties = '" + properties + '\'' +
                        ",bbox = '" + bbox + '\'' +
                        "}";
    }

    public boolean hasBbox() {
        return getBbox() != null && getBbox().size() != 0;
    }

    public LatLngBounds getLatLngBounds() {
        if (getBbox() == null || getBbox().size() == 0) {
            return null;
        } else {
            return new LatLngBounds.Builder()
                    .include(new LatLng(getBbox().get(1), getBbox().get(0)))
                    .include(new LatLng(getBbox().get(3), getBbox().get(2)))
                    .build();
        }
    }

    public Feature() {
    }

    public Feature(LatLng latlng, boolean currentLocation) {
        if (currentLocation) {
            this.geometry = new Geometry();
            List<Double> coordinates = new ArrayList<>();
            coordinates.add(latlng.getLongitude());
            coordinates.add(latlng.getLatitude());
            geometry.setCoordinates(coordinates);
            this.setPlaceName("Current Location");
            List<String> placeType = new ArrayList<>();
            placeType.add("place");
            this.setPlaceType(placeType);
        }
    }

    public String getPlaceNameLine1() {
        return getPlaceName().substring(0, getPlaceName().indexOf(", "));
    }

    public String getPlaceNameLine2() {
        return getPlaceName().substring(getPlaceName().indexOf(", ") + 2);
    }

}