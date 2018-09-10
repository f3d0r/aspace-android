package aspace.trya.models.geojson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Properties {

    @JsonProperty("address")
    private String address;

    @JsonProperty("landmark")
    private boolean landmark;

    @JsonProperty("category")
    private String category;

    @JsonProperty("tel")
    private String tel;

    @JsonProperty("wikidata")
    private String wikidata;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isLandmark() {
        return landmark;
    }

    public void setLandmark(boolean landmark) {
        this.landmark = landmark;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

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
                "address = '" + address + '\'' +
                ",landmark = '" + landmark + '\'' +
                ",category = '" + category + '\'' +
                ",tel = '" + tel + '\'' +
                ",wikidata = '" + wikidata + '\'' +
                "}";
    }
}