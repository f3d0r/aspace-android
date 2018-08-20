package aspace.trya.misc;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

public class GeojsonUtils {
    public static List<LatLng> featureCollectionToLatLng(FeatureCollection featureCollection) {
        List<LatLng> latLngs = new ArrayList<>();
        for (Feature currentFeature : featureCollection.features()) {
            latLngs.add(new LatLng(currentFeature.geometry().bbox().northeast().latitude(), currentFeature.geometry().bbox().northeast().longitude()));
        }
        return latLngs;
    }
}
