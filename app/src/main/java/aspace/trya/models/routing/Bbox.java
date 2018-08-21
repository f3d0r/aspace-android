package aspace.trya.models.routing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;

import java.io.Serializable;

public class Bbox implements Serializable {

    @JsonProperty("se")
    private LngLat se;

    @JsonProperty("nw")
    private LngLat nw;

    public LngLat getSe() {
        return se;
    }

    public void setSe(LngLat se) {
        this.se = se;
    }

    public LngLat getNw() {
        return nw;
    }

    public void setNw(LngLat nw) {
        this.nw = nw;
    }

    @Override
    public String toString() {
        return
                "Bbox{" +
                        "se = '" + se + '\'' +
                        ",nw = '" + nw + '\'' +
                        "}";
    }

    public LatLngBounds getLatLngBounds() {
        if (se == null || nw == null) {
            return null;
        } else {
            return new LatLngBounds.Builder()
                    .include(new LatLng(se.getLat(), se.getLng()))
                    .include(new LatLng(nw.getLat(), nw.getLng()))
                    .build();
        }
    }
}