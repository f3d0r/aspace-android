package aspace.trya.controllers;

import aspace.trya.misc.MapUtils;
import aspace.trya.models.RouteOptionsResponse;
import aspace.trya.models.routing_options.RouteLoc;
import java.util.Arrays;

public class RouteOptionsMapController {

    private boolean[] currentViewOptions = {true, false, false};
    private int currentView = -1;
    private RouteOptionsResponse[] routeOptionsResponses;
    private MapUtils mapUtils;

    private RouteLoc absOrigin;
    private RouteLoc currentPark;
    private RouteLoc absDest;

    private RouteLoc currentBike;

    public RouteOptionsMapController(RouteOptionsResponse[] routeOptionsResponses,
        MapUtils mapUtils) {
        this.routeOptionsResponses = routeOptionsResponses;
        this.mapUtils = mapUtils;
        mapUtils.drawRoutes(routeOptionsResponses[0], 0);

        absOrigin = routeOptionsResponses[0].getAbsOrigin();
        absDest = routeOptionsResponses[0].getAbsDest();

        currentBike =
            routeOptionsResponses[0].getRouteOptions().getRoutes().get(0).get(1).getDest();
    }

    public void parkBikeOptionPressed() {
        currentPark = routeOptionsResponses[0].getParkLoc();
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
        currentPark = routeOptionsResponses[1].getParkLoc();
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
        currentPark = routeOptionsResponses[2].getParkLoc();
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

    public RouteLoc getAbsOrigin() {
        return absOrigin;
    }

    public RouteLoc getAbsDest() {
        return absDest;
    }

    public RouteLoc getParkLoc() {
        return currentPark;
    }

    public RouteLoc getBikeLoc() {
        return currentBike;
    }

//    public void firstRouteCompleted() {
//        if (getEnabledRouteIndex() == 0) {
//            currentOrigin = Point.fromLngLat(
//                routeOptionsResponses[0].getRouteOptions().getRoutes().get(0).get(2).getOrigin()
//                    .getLng(),
//                routeOptionsResponses[0].getRouteOptions().getRoutes().get(0).get(2).getOrigin()
//                    .getLat());
//            currentDest = Point.fromLngLat(
//                routeOptionsResponses[0].getRouteOptions().getRoutes().get(0).get(2).getDest()
//                    .getLng(),
//                routeOptionsResponses[0].getRouteOptions().getRoutes().get(0).get(2).getDest()
//                    .getLat());
//        } else {
//            currentOrigin = Point.fromLngLat(
//                routeOptionsResponses[getEnabledRouteIndex()].getRouteOptions().getRoutes().get(0).get(1).getOrigin()
//                    .getLng(),
//                routeOptionsResponses[getEnabledRouteIndex()].getRouteOptions().getRoutes().get(0).get(1).getOrigin()
//                    .getLat());
//            currentDest = Point.fromLngLat(
//                routeOptionsResponses[getEnabledRouteIndex()].getRouteOptions().getRoutes().get(0).get(1).getDest().getLng(),
//                routeOptionsResponses[getEnabledRouteIndex()].getRouteOptions().getRoutes().get(0).get(1).getDest()
//                    .getLat());
//        }
//    }

    public int getEnabledRouteIndex() {
        for (int i = 0; i < currentViewOptions.length; i++) {
            if (currentViewOptions[i]) {
                return i;
            }
        }
        return -1;
    }
}
