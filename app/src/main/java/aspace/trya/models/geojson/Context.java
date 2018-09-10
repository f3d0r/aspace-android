package aspace.trya.models.geojson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Context {

    @JsonProperty("id")
    private String id;

    @JsonProperty("text")
    private String text;

    @JsonProperty("wikidata")
    private String wikidata;

    @JsonProperty("short_code")
    private String shortCode;

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

    public String getWikidata() {
        return wikidata;
    }

    public void setWikidata(String wikidata) {
        this.wikidata = wikidata;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    @Override
    public String toString() {
        return
            "Context{" +
                "id = '" + id + '\'' +
                ",text = '" + text + '\'' +
                ",wikidata = '" + wikidata + '\'' +
                ",short_code = '" + shortCode + '\'' +
                "}";
    }
}