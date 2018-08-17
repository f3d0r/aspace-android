package aspace.trya.misc;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;

public class MapUtils {

    private MapboxMap mapboxMap;

    public MapUtils(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
    }

    public void zoomToLatLng(LatLng latLng, int animMilli) {
        CameraPosition position = new CameraPosition.Builder()
                .target(latLng)
                .zoom(17) // Sets the zoom
                .build(); // Creates a CameraPosition from the builder

        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), animMilli);
    }

    public void zoomToBbox(LatLngBounds latLngBounds, int animMilli) {
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100), animMilli);
    }
}
