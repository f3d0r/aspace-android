package aspace.trya.controllers;

import aspace.trya.misc.MapUtils;
import aspace.trya.models.RouteOptionsResponse;
import com.mapbox.geojson.Point;
import java.util.Arrays;

public class RouteOptionsMapController {

    private boolean[] currentViewOptions = {true, false, false};
    private int currentView = -1;
    private RouteOptionsResponse[] routeOptionsResponses;
    private MapUtils mapUtils;

    private Point currentOrigin;
    private Point currentDest;

    public RouteOptionsMapController(RouteOptionsResponse[] routeOptionsResponses,
        MapUtils mapUtils) {
        this.routeOptionsResponses = routeOptionsResponses;
        this.mapUtils = mapUtils;
        mapUtils.drawRoutes(routeOptionsResponses[0], 0);
    }

    public void parkBikeOptionPressed() {
        currentOrigin = Point.fromLngLat(
            routeOptionsResponses[0].getRouteOptions().getRoutes().get(0).get(0).getOrigin()
                .getLng(),
            routeOptionsResponses[0].getRouteOptions().getRoutes().get(0).get(0).getOrigin()
                .getLat());
        currentDest = Point.fromLngLat(routeOptionsResponses[1].getRouteOptions().getRoutes().get(0)
            .get(routeOptionsResponses[1].getRouteOptions().getRoutes().get(0).size() - 1).getDest()
            .getLng(), routeOptionsResponses[1].getRouteOptions().getRoutes().get(0)
            .get(routeOptionsResponses[1].getRouteOptions().getRoutes().get(0).size() - 1).getDest()
            .getLat());
        if (currentViewOptions[0]) {
            currentView++;
            if (currentView + 1 > routeOptionsResponses[0].getRouteOptions().getRoutes().get(0)
                .size()) {
                currentView = -1;
            }
            if (currentView == -1) {
                mapUtils.zoomToBbox(routeOptionsResponses[0].getLatLngBounds(0), 2000, true);
            } else {
                mapUtils.zoomToBbox(
                    routeOptionsResponses[0].getRouteOptions().getRoutes().get(0).get(currentView)
                        .getLatLngBounds(),
                    2000, true);
            }
        } else {
            mapUtils.drawRoutes(routeOptionsResponses[0], 0);
            Arrays.fill(currentViewOptions, false);
            currentViewOptions[0] = true;
            currentView = -1;
            mapUtils.zoomToBbox(routeOptionsResponses[0].getLatLngBounds(0), 2000, true);
        }
    }

    public void parkWalkOptionPressed() {
        currentOrigin = Point.fromLngLat(
            routeOptionsResponses[1].getRouteOptions().getRoutes().get(0).get(0).getOrigin()
                .getLng(),
            routeOptionsResponses[1].getRouteOptions().getRoutes().get(0).get(0).getOrigin()
                .getLat());
        currentDest = Point.fromLngLat(
            routeOptionsResponses[1].getRouteOptions().getRoutes().get(0).get(1).getDest().getLng(),
            routeOptionsResponses[1].getRouteOptions().getRoutes().get(0).get(1).getDest()
                .getLat());
        if (currentViewOptions[1]) {
            currentView++;
            if (currentView + 1 > routeOptionsResponses[1].getRouteOptions().getRoutes().get(0)
                .size()) {
                currentView = -1;
            }
            if (currentView == -1) {
                mapUtils.zoomToBbox(routeOptionsResponses[1].getLatLngBounds(0), 2000, true);
            } else {
                mapUtils.zoomToBbox(
                    routeOptionsResponses[1].getRouteOptions().getRoutes().get(0).get(currentView)
                        .getLatLngBounds(),
                    2000, true);
            }
        } else {
            mapUtils.drawRoutes(routeOptionsResponses[1], 0);
            Arrays.fill(currentViewOptions, false);
            currentViewOptions[1] = true;
            currentView = -1;
            mapUtils.zoomToBbox(routeOptionsResponses[1].getLatLngBounds(0), 2000, true);
        }
    }

    public void parkDirectionOptionPressed() {
        currentOrigin = Point.fromLngLat(
            routeOptionsResponses[2].getRouteOptions().getRoutes().get(0).get(0).getOrigin()
                .getLng(),
            routeOptionsResponses[2].getRouteOptions().getRoutes().get(0).get(0).getOrigin()
                .getLat());
        currentDest = Point.fromLngLat(
            routeOptionsResponses[2].getRouteOptions().getRoutes().get(0).get(1).getDest().getLng(),
            routeOptionsResponses[2].getRouteOptions().getRoutes().get(0).get(1).getDest()
                .getLat());
        if (currentViewOptions[2]) {
            currentView++;
            if (currentView + 1 > routeOptionsResponses[2].getRouteOptions().getRoutes().get(0)
                .size()) {
                currentView = -1;
            }
            if (currentView == -1) {
                mapUtils.zoomToBbox(routeOptionsResponses[2].getLatLngBounds(0), 2000, true);
            } else {
                mapUtils.zoomToBbox(
                    routeOptionsResponses[2].getRouteOptions().getRoutes().get(0).get(currentView)
                        .getLatLngBounds(),
                    2000, true);
            }
        } else {
            mapUtils.drawRoutes(routeOptionsResponses[2], 0);
            Arrays.fill(currentViewOptions, false);
            currentViewOptions[2] = true;
            currentView = -1;
            mapUtils.zoomToBbox(routeOptionsResponses[2].getLatLngBounds(0), 2000, true);
        }
    }

    public Point getCurrentOrigin() {
        return currentOrigin;
    }

    public Point getCurrentDest() {
        return currentDest;
    }
}
