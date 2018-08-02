package aspace.trya.geojson;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Properties {

    @JsonProperty("wikidata")
    private String wikidata;

    public String getWikidata() {
        return wikidata;
    }

    public void setWikidata(String wikidata) {
        this.wikidata = wikidata;
    }

    @Override
    public String toString() {
        return
                "Properties{" +
                        "wikidata = '" + wikidata + '\'' +
                        "}";
    }
}