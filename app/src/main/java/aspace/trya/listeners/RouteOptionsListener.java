package aspace.trya.listeners;

import android.support.v7.widget.CardView;
import aspace.trya.models.RouteOptionsResponse;


public interface RouteOptionsListener {

    void routeOptionsOriginSelectorClicked(CardView cvRouteOptions);

    void routeOptionsDestinationSelectorClicked(CardView cvRouteOptions);

    void routeOptionsParkBikeSelectorClicked(RouteOptionsResponse routeOptionsResponse,
        int optionSelected);

    void routeOptionsParkWalkSelectorClicked(RouteOptionsResponse routeOptionsResponse,
        int optionSelected);

    void routeOptionsParkDirectSelectorClicked(RouteOptionsResponse routeOptionsResponse,
        int optionSelected);
}
