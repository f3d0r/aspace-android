package aspace.trya.search;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import aspace.trya.R;
import aspace.trya.geojson.Feature;

public class SearchResult {
    private Feature feature;
    private Drawable locationType;
    private String mainAddress;
    private String cityState;
    private Drawable goButton;

    public SearchResult(Feature feature, Context context) {
        this.feature = feature;
        this.mainAddress = feature.getPlaceNameLine1();
        this.cityState = feature.getPlaceNameLine2();
        this.goButton = ContextCompat.getDrawable(context, R.drawable.ic_arrow_forward);
        String placeType = feature.getPlaceType().get(feature.getPlaceType().size() - 1);
        if (placeType.equals("country")) {
            locationType = ContextCompat.getDrawable(context, R.drawable.ic_lt_flag);
        } else if (placeType.equals("region") || placeType.equals("postcode") || placeType.equals("district")) {
            locationType = ContextCompat.getDrawable(context, R.drawable.ic_lt_region);
        } else if (placeType.equals("locality") || placeType.equals("neighborhood")) {
            locationType = ContextCompat.getDrawable(context, R.drawable.ic_lt_neighborhood);
        } else if (placeType.equals("place") || placeType.equals("poi") || placeType.equals("poi.landmark")) {
            locationType = ContextCompat.getDrawable(context, R.drawable.ic_lt_location);
        } else if (placeType.equals("address")) {
            locationType = ContextCompat.getDrawable(context, R.drawable.ic_lt_home);
        } else {
            locationType = ContextCompat.getDrawable(context, R.drawable.ic_lt_location);
        }
    }

    public Feature getFeature() {
        return feature;
    }

    public Drawable getLocationType() {
        return locationType;
    }

    public String getMainAddress() {
        return mainAddress;
    }

    public String getCityState() {
        return cityState;
    }

    public Drawable getGoButton() {
        return goButton;
    }
}