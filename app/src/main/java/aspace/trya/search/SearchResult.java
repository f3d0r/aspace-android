package aspace.trya.search;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import aspace.trya.R;
import aspace.trya.models.geojson.Feature;

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
        switch (placeType) {
            case "country":
                locationType = ContextCompat.getDrawable(context, R.drawable.ic_lt_flag);
                break;
            case "region":
            case "postcode":
            case "district":
                locationType = ContextCompat.getDrawable(context, R.drawable.ic_lt_region);
                break;
            case "locality":
            case "neighborhood":
                locationType = ContextCompat.getDrawable(context, R.drawable.ic_lt_neighborhood);
                break;
            case "place":
            case "poi":
            case "poi.landmark":
                locationType = ContextCompat.getDrawable(context, R.drawable.ic_lt_location);
                break;
            case "address":
                locationType = ContextCompat.getDrawable(context, R.drawable.ic_lt_home);
                break;
            default:
                locationType = ContextCompat.getDrawable(context, R.drawable.ic_lt_location);
                break;
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