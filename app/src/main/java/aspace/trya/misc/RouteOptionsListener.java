package aspace.trya.misc;

import android.support.v7.widget.CardView;

import com.mapbox.api.directions.v5.models.RouteOptions;

public interface RouteOptionsListener {
    void routeOptionsOriginSelectorClicked(CardView cvRouteOptions);

    void routeOptionsDestinationSelectorClicked(CardView cvRouteOptions);

    void routeOptionsParkBikeSelectorClicked(RouteOptions routeOptions);

    void routeOptionsParkWalkSelectorClicked(RouteOptions routeOptions);

    void routeOptionsParkDirectSelectorClicked(RouteOptions routeOptions);
}
