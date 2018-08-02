package aspace.trya;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;

import aspace.trya.api.MapboxService;
import aspace.trya.api.MapboxServiceGenerator;
import aspace.trya.geojson.GeoJSON;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.floating_search_view)
    FloatingSearchView floatingSearchView;
    @BindView(R.id.bottom_sheet)
    LinearLayout layoutBottomSheet;

    @BindView(R.id.bike_route_cost)
    TextView tvBikeRouteCost;
    @BindView(R.id.bike_route_time)
    TextView tvBikeRouteTime;
    @BindView(R.id.drive_route_cost)
    TextView tvDriveRouteCost;
    @BindView(R.id.drive_route_time)
    TextView tvDriveRouteTime;
    @BindView(R.id.walk_route_cost)
    TextView tvWalkRouteCost;
    @BindView(R.id.walk_route_time)
    TextView tvRouteWalkTime;

    @BindView(R.id.bike_route)
    RelativeLayout rlBikeRoute;
    @BindView(R.id.drive_route)
    RelativeLayout rlDriveRoute;
    @BindView(R.id.walk_route)
    RelativeLayout rlWalkRoute;

    @BindView(R.id.start_route_button)
    Button btStartRoute;
    BottomSheetBehavior sheetBehavior;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private LocationLayerPlugin locationPlugin;
    private LocationEngine locationEngine;
    private Location originLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ButterKnife.bind(this);

        rlBikeRoute.setOnClickListener(this);
        rlDriveRoute.setOnClickListener(this);
        rlWalkRoute.setOnClickListener(this);

        Mapbox.getInstance(getApplicationContext(), getString(R.string.mapbox_access_token));

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setPeekHeight(0);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        floatingSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {
            Call<GeoJSON> call = MapboxServiceGenerator.createService(MapboxService.class).getSearchSuggestions(newQuery + ".json", "-74.70850,40.78375", getString(R.string.mapbox_access_token));
            call.enqueue(new Callback<GeoJSON>() {
                @Override
                public void onResponse(@NonNull Call<GeoJSON> call, @NonNull Response<GeoJSON> response) {
                    try {
                        assert response.body() != null;
                        floatingSearchView.swapSuggestions(response.body().getFeatures());
                    } catch (NullPointerException ignored) {
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GeoJSON> call, Throwable t) {
                }
            });
        });

        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                LatLng toPosition = new LatLng(Double.parseDouble(searchSuggestion.getBody().split("\n")[1].split(",")[1]), Double.parseDouble(searchSuggestion.getBody().split("\n")[1].split(",")[0]));
                CameraPosition position = new CameraPosition.Builder()
                        .target(toPosition)
                        .zoom(16) // Sets the zoom to level 10
                        .build(); // Builds the CameraPosition object from the builder

                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 2000);
                summaryViewTransition();
            }

            @Override
            public void onSearchAction(String currentQuery) {
            }
        });

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        resetRouteSelection();
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        layoutBottomSheet.setOnClickListener(v -> sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        locationPlugin = new LocationLayerPlugin(mapView, this.mapboxMap);
        locationPlugin.setRenderMode(RenderMode.COMPASS);
        locationPlugin.setCameraMode(CameraMode.TRACKING);
//        UiSettings settings = new UiSettings()
    }

    public void summaryViewTransition() {
        floatingSearchView.clearQuery();
        floatingSearchView.setSearchFocused(false);

        floatingSearchView.animate()
                .translationY(-500.0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        floatingSearchView.setVisibility(View.INVISIBLE);
                    }
                });
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        sheetBehavior.setPeekHeight(150);
    }

    @Override
    public void onClick(View v) {
        resetRouteSelection();
        switch (v.getId()) {
            case R.id.bike_route:
                rlBikeRoute.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                btStartRoute.setText("Start Bike Transfer Route");
                btStartRoute.setVisibility(View.VISIBLE);
                break;
            case R.id.drive_route:
                rlDriveRoute.setBackground(getResources().getDrawable(R.drawable.rounded_dialog_selected));
                btStartRoute.setText("Start Direct Route");

                btStartRoute.setVisibility(View.VISIBLE);
                break;
            case R.id.walk_route:
                rlWalkRoute.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                btStartRoute.setText("Start Walk Transfer Route");
                btStartRoute.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    public void resetRouteSelection() {
        btStartRoute.setVisibility(View.GONE);
        rlWalkRoute.setBackground(getResources().getDrawable(R.drawable.rounded_dialog));
        rlBikeRoute.setBackground(getResources().getDrawable(R.drawable.rounded_dialog));
        rlDriveRoute.setBackground(getResources().getDrawable(R.drawable.rounded_dialog));
    }
}