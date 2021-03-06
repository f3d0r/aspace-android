package aspace.trya.misc;

import android.content.Context;
import aspace.trya.R;
import aspace.trya.models.RouteOptionsResponse;
import aspace.trya.models.routing_options.RouteSegment;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUtils {

    private MapboxMap mapboxMap;
    private Context context;

    private List<Layer> layers;
    private List<Source> sources;

    private HashMap<String, Marker> placedMarkers;

    public MapUtils(MapboxMap mapboxMap, Context context) {
        this.mapboxMap = mapboxMap;
        this.context = context;
        layers = new ArrayList<>();
        sources = new ArrayList<>();
        placedMarkers = new HashMap<>();
    }

    public void zoomToLatLng(LatLng latLng, int animMilli) {
        CameraPosition position = new CameraPosition.Builder()
            .target(latLng)
            .zoom(17) // Sets the zoom
            .build(); // Creates a CameraPosition from the builder

        mapboxMap.animateCamera(CameraUpdateFactory
            .newCameraPosition(position), animMilli);
    }

    public void zoomToBbox(LatLngBounds latLngBounds, int animMilli, boolean addSearchBarBounds) {
        mapboxMap
            .animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 150, 400, 150, 150),
                animMilli);
    }

    public void clearTopLayer() {
        if (!layers.isEmpty()) {
            mapboxMap.removeLayer(layers.get(layers.size() - 1));
            mapboxMap.removeSource(sources.get(sources.size() - 1));
            layers.remove(layers.size() - 1);
            sources.remove(sources.size() - 1);
        }
    }

    private void addLayer(Layer layer, Source source) {
        mapboxMap.addLayer(layer);
        layers.add(layer);
        mapboxMap.addSource(source);
        sources.add(source);
    }

    public void clearMap() {
        for (Layer layer : layers) {
            mapboxMap.removeLayer(layer);
        }
        for (Source source : sources) {
            mapboxMap.removeSource(source);
        }
        layers.clear();
        sources.clear();
        removeAllMarkers();
    }

    public void drawRoutes(RouteOptionsResponse routeOptionsResponse, int optionSelected) {
        clearMap();
        List<RouteSegment> routeSegments = routeOptionsResponse.getRouteOptions().getRoutes()
            .get(0);
        for (RouteSegment currentRouteSegment : routeSegments) {

            LineString lineString = LineString
                .fromLngLats(currentRouteSegment.getDirections().getRoutes().get(0).getGeometry()
                    .getPoints());
            FeatureCollection featureCollection = FeatureCollection
                .fromFeatures(new Feature[]{Feature.fromGeometry(lineString)});
            Source geoJsonSource = new GeoJsonSource(currentRouteSegment.getName() + "-source",
                featureCollection);
            LineLayer routeLayer = new LineLayer(currentRouteSegment.getName() + "-layer",
                currentRouteSegment.getName() + "-source");
            switch (currentRouteSegment.getName()) {
                case "drive_park": {
                    int lineColor = context.getResources().getColor(R.color.routeDriveColor);
                    routeLayer.setProperties(
                        PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                        PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                        PropertyFactory.lineWidth(4f),
                        PropertyFactory.lineColor(lineColor));
                    addMarker("abs_origin", currentRouteSegment.getOrigin().getLatLng(),
                        "Your Location", "");
                    addMarker("park_loc", currentRouteSegment.getDest().getLatLng(),
                        currentRouteSegment.getDest().getLocMetaData().getName() + " ($"
                            + (int) (currentRouteSegment.getDest().getLocMetaData()
                            .getParkingPrice()) + ")",
                        currentRouteSegment.getDest().getLocMetaData().getAddress());
                    break;
                }
                case "walk_bike": {
                    int lineColor = context.getResources().getColor(R.color.routeWalkColor);
                    routeLayer.setProperties(
                        PropertyFactory.lineDasharray(new Float[]{0.01f, 2f}),
                        PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                        PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                        PropertyFactory.lineWidth(4f),
                        PropertyFactory.lineColor(lineColor));
                    addMarker("bike_loc", currentRouteSegment.getDest().getLatLng(),
                        currentRouteSegment.getDest().getLocMetaData().getCompany(),
                        currentRouteSegment.getDest().getLocMetaData().getNum()
                    );
                    break;
                }
                case "bike_dest": {
                    int lineColor = context.getResources().getColor(R.color.routeBikeColor);
                    routeLayer.setProperties(
                        PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                        PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                        PropertyFactory.lineWidth(4f),
                        PropertyFactory.lineColor(lineColor));
                    addMarker("abs_dest", currentRouteSegment.getDest().getLatLng(),
                        "Your Destination", "");
                    break;
                }
                default: {
                    int lineColor = context.getResources().getColor(R.color.routeWalkColor);
                    routeLayer.setProperties(
                        PropertyFactory.lineDasharray(new Float[]{0.01f, 2f}),
                        PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                        PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                        PropertyFactory.lineWidth(4f),
                        PropertyFactory.lineColor(lineColor));
                    addMarker("abs_dest", currentRouteSegment.getDest().getLatLng(),
                        "Your Destination", "");
                    break;
                }
            }

            addLayer(routeLayer, geoJsonSource);
        }
    }

    private void addMarker(String keyName, LatLng position, String title, String description) {
        MarkerOptions marker = new MarkerOptions()
            .position(position)
            .title(title)
            .snippet(description);
        placedMarkers.put(keyName, mapboxMap.addMarker(marker));
    }

    private void removeAllMarkers() {
        for (Map.Entry<String, Marker> entry : placedMarkers.entrySet()) {
            mapboxMap.removeMarker(entry.getValue());
        }
        placedMarkers.clear();
    }

    private void removeMarkerWithKey(String key) {
        mapboxMap.removeMarker(placedMarkers.get(key));
    }
}
