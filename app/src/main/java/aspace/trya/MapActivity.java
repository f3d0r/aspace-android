package aspace.trya;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import aspace.trya.adapters.ArrayRecyclerAdapter;
import aspace.trya.api.AspaceRoutingService;
import aspace.trya.api.MapboxService;
import aspace.trya.api.RetrofitLatLng;
import aspace.trya.api.RetrofitServiceGenerator;
import aspace.trya.controllers.RouteOptionsMapController;
import aspace.trya.controllers.RoutePreviewBottomSheetController;
import aspace.trya.fragments.RouteOptionsPreviewFragment;
import aspace.trya.listeners.RouteOptionsListener;
import aspace.trya.listeners.RoutePreviewSwipeListener;
import aspace.trya.misc.APIURLs;
import aspace.trya.misc.ApplicationState;
import aspace.trya.misc.CurrentLocationUpdates;
import aspace.trya.misc.KeyboardUtils;
import aspace.trya.misc.LocationMonitoringService;
import aspace.trya.misc.MapUtils;
import aspace.trya.models.MapConstraintsResponse;
import aspace.trya.models.RouteOptionsResponse;
import aspace.trya.models.geojson.Feature;
import aspace.trya.models.geojson.GeoJSON;
import aspace.trya.search.SearchResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.android.gestures.StandardScaleGestureDetector;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mypopsy.widget.FloatingSearchView;
import com.steelkiwi.library.SlidingSquareLoaderView;
import io.intercom.android.sdk.Intercom;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MapActivity extends AppCompatActivity implements RouteOptionsListener,
    RoutePreviewSwipeListener, OnMapReadyCallback, View.OnClickListener,
    ActionMenuView.OnMenuItemClickListener {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private boolean mAlreadyStartedService = false;

    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.floating_search_view)
    FloatingSearchView floatingSearchView;
    @BindView(R.id.current_location_button)
    FloatingActionButton btCurrentLocation;
    Menu menuSearchView;
    @BindView(R.id.loading_square_view)
    SlidingSquareLoaderView loadingSquareView;

//    @BindView(R.id.zoom_in_warning_cardview)
//    CardView cvZoomInWarning;

    @BindView(R.id.route_summary_fragment)
    LinearLayout fragmentRouteSummary;

    @BindView(R.id.fab_nav_menu)
    FloatingActionMenu fabNavMenu;
    @BindView(R.id.fab_nav_park)
    com.github.clans.fab.FloatingActionButton fabNavPark;
    @BindView(R.id.fab_nav_bike_dest)
    com.github.clans.fab.FloatingActionButton fabNavBikeDest;
    @BindView(R.id.fab_nav_walk_dest)
    com.github.clans.fab.FloatingActionButton fabNavWalkDest;

    private MapboxMap mapboxMap;

    private SearchAdapter mAdapter;

    private ApplicationState applicationState;

    private boolean locationTracked;

    private LatLng lastCurrentLocation;

    private MapUtils mapUtils;

    private boolean firstRouteCompleted = false;

    //For Route Display (Defaults)
    private Feature routeOrigin;

    //Route Options Fragment Controller for Map
    private RouteOptionsMapController routeOptionsMapController;

    private boolean cvZoomInWarningVisible;

    private BottomSheetBehavior fgRouteSummaryBehavior;

    boolean displayingRoute = false;
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;

    private boolean viewingRoute = false;

    private CurrentLocationUpdates currentLocationUpdates;

    private RouteOptionsPreviewFragment routeOptionsPreviewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ButterKnife.bind(this);

        applicationState = new ApplicationState(MapActivity.this);
        lastCurrentLocation = applicationState.getLoadLocation();
        fgRouteSummaryBehavior = BottomSheetBehavior.from(fragmentRouteSummary);

        CurrentLocationUpdates currentLocationUpdates = new CurrentLocationUpdates(this);
        startStep1();

        LocalBroadcastManager.getInstance(this).registerReceiver(
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String latitude = intent
                        .getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
                    String longitude = intent
                        .getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);
                    Timber.d("LONG : " + longitude + ", LAT : " + latitude);
                    if (latitude != null && longitude != null) {
                        lastCurrentLocation = new LatLng(Double.parseDouble(latitude),
                            Double.parseDouble(longitude));
                        if (locationTracked && mapboxMap != null) {
                            mapUtils.zoomToLatLng(lastCurrentLocation, 250);
                        }
                    }
                }
            }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );

        fabNavMenu.setIconAnimated(false);

        fabNavBikeDest.setOnClickListener(view -> {
            Intercom.client().logEvent("NAV_BIKE_STARTED");
            firstRouteCompleted = true;
            fabNavMenu.close(true);
            getRoute(routeOptionsMapController.getBikeLoc().getPoint(), "Your Location",
                routeOptionsMapController.getAbsDest().getPoint(), "Your Destination",
                () -> {
                    try {
                        Intent launchIntent = getPackageManager()
                            .getLaunchIntentForPackage("com.limebike");
                        startActivity(launchIntent);
                        boolean simulateRoute = false;
                        NavigationLauncherOptions options = NavigationLauncherOptions
                            .builder()
                            .directionsRoute(currentRoute)
                            .shouldSimulateRoute(simulateRoute)
                            .build();
                        NavigationLauncher
                            .startNavigation(MapActivity.this, options);
                    } catch (Exception e) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=com.limebike")));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                                "https://play.google.com/store/apps/details?id=com.limebike&hl=en_US")));
                        }
                    }
                });
        });

        fabNavWalkDest.setOnClickListener(view -> {
            Intercom.client().logEvent("NAV_WALK_STARTED");
            firstRouteCompleted = true;
            fabNavMenu.close(true);
            getRoute(
                routeOptionsMapController.getParkLoc()
                    .getPoint(), "Your Origin",
                routeOptionsMapController.getAbsDest()
                    .getPoint(), "Your Destination",
                () -> {
                    boolean simulateRoute = false;
                    NavigationLauncherOptions options = NavigationLauncherOptions
                        .builder()
                        .directionsRoute(currentRoute)
                        .shouldSimulateRoute(
                            simulateRoute)
                        .build();
                    NavigationLauncher
                        .startNavigation(
                            MapActivity.this,
                            options);
                });
        });

        fabNavPark.setOnClickListener(view -> {
            Intercom.client().logEvent("NAV_PARK_STARTED");
            firstRouteCompleted = true;
            fabNavMenu.close(true);
            getRoute(routeOptionsMapController.getAbsOrigin()
                    .getPoint(), "Your Location",
                routeOptionsMapController.getParkLoc()
                    .getPoint(),
                routeOptionsMapController.getParkLoc()
                    .getLocMetaData().getName(),
                () -> {
                    boolean simulateRoute = false;
                    NavigationLauncherOptions options = NavigationLauncherOptions
                        .builder()
                        .directionsRoute(currentRoute)
                        .shouldSimulateRoute(
                            simulateRoute)
                        .build();
                    NavigationLauncher
                        .startNavigation(MapActivity.this,
                            options);
                });
        });

        floatingSearchView.setOnSearchFocusChangedListener(focused ->

        {
            floatingSearchView.getMenu().getItem(0)
                .setVisible(floatingSearchView.getText().toString().trim().length() != 0);
//            if (cvZoomInWarningVisible) {
//                cvZoomInWarning.setAlpha(mapboxMap.getCameraPosition().zoom < 15 ? 1 : 0);
//            }
            btCurrentLocation.setVisibility(focused ? View.INVISIBLE : View.VISIBLE);
        });

        floatingSearchView.setOnIconClickListener(() -> Intercom.client().displayMessenger());

        Mapbox.getInstance(getApplicationContext(), getString(R.string.mapbox_access_token));

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        floatingSearchView.setAdapter(mAdapter = new SearchAdapter());
        floatingSearchView.setOnSearchListener(text -> floatingSearchView.setActivated(false));

        floatingSearchView.addTextChangedListener(new TextWatcher() {
            private final long DELAY = getResources()
                .getInteger(
                    R.integer.search_gecode_delay_milli); // milliseconds
            private Timer timer = new Timer();

            @Override
            public void beforeTextChanged(
                CharSequence s, int start, int count,
                int after) {
            }

            @Override
            public void onTextChanged(CharSequence s,
                int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(
                final Editable s) {
                timer.cancel();
                timer = new Timer();
                timer.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(() -> {
                                if (floatingSearchView.getText().toString().trim().length()
                                    == 0) {
                                    mAdapter.clear();
                                    floatingSearchView.getMenu().getItem(0)
                                        .setVisible(false);
                                } else {
                                    floatingSearchView.getMenu().getItem(0)
                                        .setVisible(true);
                                }
                                String searchString =
                                    s.toString().replace(' ', '+').trim() + ".json";
                                Call<GeoJSON> call = RetrofitServiceGenerator
                                    .createService(
                                        MapboxService.class,
                                        APIURLs.MAPBOX_URL)
                                    .getSearchSuggestions(searchString,
                                        new RetrofitLatLng(mapboxMap
                                            .getCameraPosition().target
                                            .getLatitude(),
                                            mapboxMap
                                                .getCameraPosition().target
                                                .getLongitude()),
                                        "us",
                                        getString(
                                            R.string.mapbox_access_token),
                                        10);
                                call.enqueue(
                                    new Callback<GeoJSON>() {
                                        @Override
                                        public void onResponse(
                                            @NonNull Call<GeoJSON> call,
                                            @NonNull Response<GeoJSON> response) {
                                            try {
                                                assert response.body() != null;
                                                mAdapter.setNotifyOnChange(false);
                                                mAdapter.clear();
                                                mAdapter.addAll(response.body()
                                                    .getSearchResults(
                                                        getApplicationContext()));
                                                mAdapter.setNotifyOnChange(true);
                                                mAdapter.notifyDataSetChanged();
                                            } catch (NullPointerException ignored) {
                                            }
                                        }

                                        @Override
                                        public void onFailure(
                                            @NonNull Call<GeoJSON> call,
                                            Throwable t) {
                                            Toast.makeText(getApplicationContext(),
                                                "Something went wrong... Please try again.",
                                                Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            });
                        }
                    },
                    DELAY
                );
            }
        });
        floatingSearchView.setOnMenuItemClickListener(this);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapUtils = new MapUtils(mapboxMap, this);
        CameraPosition position = new CameraPosition.Builder()
            .target(lastCurrentLocation)
            .zoom(14)
            .build();
        mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
        mapboxMap.getUiSettings().setRotateGesturesEnabled(false);
        btCurrentLocation.setOnClickListener(v -> {
            locationTracked = !locationTracked;
            btCurrentLocation
                .setImageResource(locationTracked ? R.drawable.ic_current_location_enabled
                    : R.drawable.ic_current_location_disabled);
            if (locationTracked) {
                mapUtils.zoomToLatLng(lastCurrentLocation, 250);
            }
        });

        mapboxMap.addOnMoveListener(new MapboxMap.OnMoveListener() {
            @Override
            public void onMoveBegin(@NonNull MoveGestureDetector detector) {
            }

            @Override
            public void onMove(@NonNull MoveGestureDetector detector) {
                btCurrentLocation.setImageResource(R.drawable.ic_current_location_disabled);
                locationTracked = false;
            }

            @Override
            public void onMoveEnd(@NonNull MoveGestureDetector detector) {
            }
        });

        mapboxMap.addOnScaleListener(new MapboxMap.OnScaleListener() {
            boolean beginStatus;

            @Override
            public void onScaleBegin(@NonNull StandardScaleGestureDetector detector) {
                beginStatus = mapboxMap.getCameraPosition().zoom < 15;
            }

            @Override
            public void onScale(@NonNull StandardScaleGestureDetector detector) {
                boolean warningVisible = mapboxMap.getCameraPosition().zoom < 15;
                if (beginStatus != warningVisible) {
                    beginStatus = warningVisible;
//                    cvZoomInWarning.animate().setDuration(200).alpha(warningVisible ? 1 : 0)
//                        .start();
                }
            }

            @Override
            public void onScaleEnd(@NonNull StandardScaleGestureDetector detector) {
            }
        });

        mapboxMap.addOnMapLongClickListener(point -> {
            if (!displayingRoute) {
                RetrofitLatLng latLng = new RetrofitLatLng(point.getLatitude(),
                    point.getLongitude());
                Point searchLocationPoint = Point
                    .fromLngLat(point.getLongitude(), point.getLatitude());

                MapboxGeocoding client = MapboxGeocoding.builder()
                    .accessToken(getString(R.string.mapbox_access_token))
                    .query(searchLocationPoint)
                    .geocodingTypes(GeocodingCriteria.TYPE_POI)
                    .mode(GeocodingCriteria.MODE_PLACES)
                    .build();

                client.enqueueCall(new Callback<GeocodingResponse>() {
                    @Override
                    public void onResponse(Call<GeocodingResponse> call,
                        Response<GeocodingResponse> response) {
                        try {
                            Point zoomCenter = response.body().features().get(0).center();
                            mapUtils.zoomToLatLng(new LatLng(zoomCenter.coordinates().get(1),
                                zoomCenter.coordinates().get(0)), 500);
                            if (response.body().features().get(0) != null
                                && response.body().features().get(0).properties() != null) {
                                String address = response.body().features().get(0).properties()
                                    .get("address").toString();
                                floatingSearchView
                                    .setText(address.substring(1, address.length() - 1));
                            } else {
                                floatingSearchView
                                    .setText(point.getLatitude() + ", " + point.getLongitude());
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),
                                "Nothing found here, please enter an address.",
                                Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),
                            "Something went wrong... Please try again.", Toast.LENGTH_SHORT)
                            .show();
                    }
                });
            }
        });
        mapboxMap.addOnMapClickListener(point -> btCurrentLocation.setVisibility(View.VISIBLE));

        List<LatLng> worldPolygon = new ArrayList<>();
        worldPolygon.add(new LatLng(-90, -180));
        worldPolygon.add(new LatLng(90, -180));
        worldPolygon.add(new LatLng(90, 180));
        worldPolygon.add(new LatLng(-90, 180));
        worldPolygon.add(new LatLng(-90, -180));

        RetrofitServiceGenerator
            .createService(AspaceRoutingService.class, APIURLs.ASPACE_ROUTING_PROD_URL)
            .getMapConstraints()
            .enqueue(
                new Callback<MapConstraintsResponse>() {
                    @Override
                    public void onResponse(Call<MapConstraintsResponse> call,
                        Response<MapConstraintsResponse> response) {
                        Timber.w("URL : %s", call.request().url());
                        List<LatLng> mainHole = response.body().getGeoJSON().getFeatures().get(0)
                            .getGeometry().getLatLngs();
                        List<List<LatLng>> allHoles = new ArrayList<>();
                        allHoles.add(mainHole);
                        Polygon polygon = mapboxMap.addPolygon(new PolygonOptions()
                            .addAll(worldPolygon)
                            .addAllHoles(allHoles)
                            .fillColor(Color.parseColor("#3bb2d0"))
                            .alpha(0.5f));
                    }

                    @Override
                    public void onFailure(Call<MapConstraintsResponse> call, Throwable t) {
                        Timber.w("URL : %s", call.request().url());
                        Timber.w("ERROR: ");
                        Timber.w(t);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchview_menu, menuSearchView);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                btCurrentLocation.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_search_menu_item:
                floatingSearchView.setText("");
                floatingSearchView.setHint("Where to?");
                floatingSearchView.setActivated(false);
                return true;
            case R.id.logout_menu_item:
                applicationState.logout();
                startActivity(new Intent(MapActivity.this, LoginActivity.class));
                finish();
            default:
                return false;
        }

    }

    public void toggleSearchViewVisible(boolean visible, aspace.trya.misc.Callback callback) {
        floatingSearchView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        floatingSearchView.animate()
            .translationYBy(visible ? 200.0f : -200.0f)
            .setDuration(150)
            .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (callback != null) {
                        callback.execute();
                    }
                }
            });
    }

    public void toggleRouteSummarySheetVisible(boolean visible,
        aspace.trya.misc.Callback callback) {
//        fgRouteSummaryBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void routeOptionsOriginSelectorClicked(CardView cvRouteOptions) {
        viewingRoute = false;
        Fragment fragment = getSupportFragmentManager()
            .findFragmentById(R.id.top_summary_view_fragment);
        if (fragment != null) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.exit_to_top);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.remove(fragment);
                    ft.commitAllowingStateLoss();
                    floatingSearchView.setHint("RouteLatLng?");
                    toggleSearchViewVisible(true, () -> {
                        floatingSearchView.requestFocus();
                        floatingSearchView.setActivated(true);
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            cvRouteOptions.startAnimation(animation);
        }

    }

    @Override
    public void routeOptionsDestinationSelectorClicked(CardView cvRouteOptions) {
        Fragment fragment = getSupportFragmentManager()
            .findFragmentById(R.id.top_summary_view_fragment);
        if (fragment != null) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.exit_to_top);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.remove(fragment);
                    ft.commitAllowingStateLoss();
                    floatingSearchView.setHint("Destination?");
                    toggleSearchViewVisible(true, () -> {
                        floatingSearchView.requestFocus();
                        floatingSearchView.setActivated(true);
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            cvRouteOptions.startAnimation(animation);
        }
    }

//    public void toggleZoomInWarning(boolean visible) {
//        cvZoomInWarningVisible = visible;
//        if (visible) {
//            cvZoomInWarning.animate().setDuration(200)
//                .alpha(mapboxMap.getCameraPosition().zoom < 15 ? 1 : 0).start();
//        } else {
//            cvZoomInWarning.animate().setDuration(200).alpha(0).start();
//        }
//    }

    @Override
    public void routeOptionsParkBikeSelectorClicked(RouteOptionsResponse routeOptionsResponse,
        int optionSelected) {
        fabNavMenu.close(true);
        fabNavPark.setEnabled(true);
        fabNavBikeDest.setEnabled(true);
        fabNavWalkDest.setEnabled(false);
//        List<DirectionsResponse> directions = new ArrayList<>();
//        for (RouteSegment currentSegment : routeOptionsResponse.getRouteOptions()
//            .get(optionSelected)) {
//            directions.add(currentSegment.getDirections().get(0));
//        }
        routeOptionsMapController.parkBikeOptionPressed();
    }

    @Override
    public void routeOptionsParkWalkSelectorClicked(RouteOptionsResponse routeOptionsResponse,
        int optionSelected) {
        fabNavMenu.close(true);
        fabNavPark.setEnabled(true);
        fabNavBikeDest.setEnabled(false);
        fabNavWalkDest.setEnabled(true);
//        List<DirectionsResponse> directions = new ArrayList<>();
//        for (RouteSegment currentSegment : routeOptionsResponse.getRouteOptions()
//            .get(optionSelected)) {
//            directions.add(currentSegment.getDirections().get(0));
//        }
        routeOptionsMapController.parkWalkOptionPressed();
    }

    @Override
    public void routeOptionsParkDirectSelectorClicked(RouteOptionsResponse routeOptionsResponse,
        int optionSelected) {
        fabNavMenu.close(true);
        fabNavPark.setEnabled(true);
        fabNavBikeDest.setEnabled(false);
        fabNavWalkDest.setEnabled(true);
//        List<DirectionsResponse> directions = new ArrayList<>();
//        for (RouteSegment currentSegment : routeOptionsResponse.getRouteOptions()
//            .get(optionSelected)) {
//            directions.add(currentSegment.getDirections().get(0));
//        }
        routeOptionsMapController.parkDirectionOptionPressed();
    }

    @Override
    public void routeSegmentSwipe(int toSegmentIndex) {
//        routeOptionsMapController
    }

    @Override
    public void startRouteButtonPressed() {

    }

    private void getRoute(Point origin, String originName, Point destination,
        String destinationName, aspace.trya.misc.Callback callback) {
        String[] waypointsNames = {originName, destinationName};
        NavigationRoute.builder(this)
            .accessToken(Mapbox.getAccessToken())
            .origin(origin)
            .destination(destination)
            .addWaypointNames(waypointsNames)
            .build()
            .getRoute(new Callback<DirectionsResponse>() {
                @Override
                public void onResponse(Call<DirectionsResponse> call,
                    Response<DirectionsResponse> response) {
                    // You can get the generic HTTP info about the response
                    if (response.body() == null) {
                        Timber
                            .d("No routes found, make sure you set the right user and access token.");
                        return;
                    } else if (response.body().routes().size() < 1) {
                        Toast.makeText(getApplicationContext(), "No routes were found.",
                            Toast.LENGTH_SHORT).show();
                        return;
                    }

                    currentRoute = response.body().routes().get(0);

                    // Draw the route on the map
//                    if (navigationMapRoute != null) {
//                        navigationMapRoute.removeRoute();
//                    } else {
//                        navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap,
//                            R.style.NavigationMapRoute);
//                    }
//                    navigationMapRoute.addRoute(currentRoute);
                    callback.execute();
                }

                @Override
                public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(),
                        "Something went wrong... Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private class SearchAdapter extends
        ArrayRecyclerAdapter<SearchResult, SuggestionViewHolder> {

        private LayoutInflater inflater;

        SearchAdapter() {
            setHasStableIds(true);
        }

        @Override
        public MapActivity.SuggestionViewHolder onCreateViewHolder(ViewGroup parent,
            int viewType) {
            if (inflater == null) {
                inflater = LayoutInflater.from(parent.getContext());
            }
            return new MapActivity.SuggestionViewHolder(
                inflater.inflate(R.layout.item_suggestion, parent, false));
        }

        @Override
        public void onBindViewHolder(MapActivity.SuggestionViewHolder holder, int position) {
            holder.bind(getItem(position));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (viewingRoute) {
            floatingSearchView.setVisibility(View.VISIBLE);
            loadingSquareView.hide();
            toggleSearchViewVisible(true,
                () -> {
                    btCurrentLocation.setVisibility(View.VISIBLE);
                    floatingSearchView.requestFocus();
                    floatingSearchView.setActivated(true);
                });
            fabNavMenu.setVisibility(View.INVISIBLE);
            getSupportFragmentManager().beginTransaction().
                remove(getSupportFragmentManager()
                    .findFragmentById(R.id.top_summary_view_fragment))
                .commit();
            displayingRoute = false;
            mapUtils.clearMap();
            mapUtils.zoomToLatLng(lastCurrentLocation, 500);
        } else {
            if (count == 0) {
                super.onBackPressed();
                //additional code
            } else {
                getFragmentManager().popBackStack();
            }
        }
    }

    private class SuggestionViewHolder extends RecyclerView.ViewHolder {

        ImageView locationType, goButton;
        TextView mainAddress, cityState;

        SuggestionViewHolder(final View itemView) {
            super(itemView);
            locationType = itemView.findViewById(R.id.location_type_iv);
            goButton = itemView.findViewById(R.id.go_iv);
            mainAddress = itemView.findViewById(R.id.main_address_tv);
            cityState = itemView.findViewById(R.id.city_state_tv);
            itemView.findViewById(R.id.address_layout).setOnClickListener((View v) -> {
                viewingRoute = true;
                loadingSquareView.show();
//                toggleZoomInWarning(false);

                Feature clickedFeature = mAdapter.getItem(getAdapterPosition()).getFeature();
                KeyboardUtils
                    .hideSoftKeyboard(getCurrentFocus(),
                        getSystemService(INPUT_METHOD_SERVICE));

                btCurrentLocation.setImageResource(R.drawable.ic_current_location_disabled);
                btCurrentLocation.setVisibility(View.GONE);
                locationTracked = false;

                floatingSearchView.setActivated(false);
                toggleSearchViewVisible(false, () -> {
                    floatingSearchView.setText(clickedFeature.getPlaceName());

                    mAdapter.clear();
                    floatingSearchView.setVisibility(View.INVISIBLE);
                    btCurrentLocation.setVisibility(View.INVISIBLE);

                    AspaceRoutingService aspaceRoutingService = RetrofitServiceGenerator
                        .createService(AspaceRoutingService.class,
                            APIURLs.ASPACE_ROUTING_PROD_URL);
                    Observable<RouteOptionsResponse> driveBikeRouteOptions = aspaceRoutingService
                        .getDriveBikeRoute(lastCurrentLocation.getLatitude(),
                            lastCurrentLocation.getLongitude(), clickedFeature.getLatitude(),
                            clickedFeature.getLongitude(), 0, applicationState.getAccessCode(),
                            applicationState.getDeviceId());
                    Observable<RouteOptionsResponse> driveWalkRouteOptions = aspaceRoutingService
                        .getDriveWalkWaypoints(lastCurrentLocation.getLatitude(),
                            lastCurrentLocation.getLongitude(), clickedFeature.getLatitude(),
                            clickedFeature.getLongitude(), 0, applicationState.getAccessCode(),
                            applicationState.getDeviceId());
                    Observable<RouteOptionsResponse> driveDirectRouteOptions = aspaceRoutingService
                        .getDriveDirectRoute(lastCurrentLocation.getLatitude(),
                            lastCurrentLocation.getLongitude(), clickedFeature.getLatitude(),
                            clickedFeature.getLongitude(), 0, applicationState.getAccessCode(),
                            applicationState.getDeviceId());
                    Call<RouteOptionsResponse> testReq = aspaceRoutingService
                        .testReq(lastCurrentLocation.getLatitude(),
                            lastCurrentLocation.getLongitude(), clickedFeature.getLatitude(),
                            clickedFeature.getLongitude(), 0, applicationState.getAccessCode(),
                            applicationState.getDeviceId());
                    Timber.d(testReq.request().url() + "");
                    Observable
                        .zip(driveBikeRouteOptions, driveWalkRouteOptions,
                            driveDirectRouteOptions,
                            (routeOptionsResponse, routeOptionsResponse2, routeOptionsResponse3) -> new RouteOptionsResponse[]{
                                routeOptionsResponse, routeOptionsResponse2,
                                routeOptionsResponse3})
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<RouteOptionsResponse[]>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(RouteOptionsResponse[] routeOptionsResponses) {
                                btCurrentLocation.setVisibility(View.INVISIBLE);
                                fabNavMenu.setVisibility(View.VISIBLE);
                                FragmentTransaction ft = getSupportFragmentManager()
                                    .beginTransaction();
                                ft.setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out);
                                RoutePreviewBottomSheetController routePreviewBottomSheetController = new RoutePreviewBottomSheetController(
                                    fgRouteSummaryBehavior);
                                RouteOptionsPreviewFragment fragment = RouteOptionsPreviewFragment
                                    .newInstance(routeOrigin, clickedFeature,
                                        routeOptionsResponses,
                                        0);

                                mapUtils
                                    .zoomToBbox(routeOptionsResponses[0].getLatLngBounds(0),
                                        3000,
                                        true);
                                routeOptionsMapController = new RouteOptionsMapController(
                                    routeOptionsResponses,
                                    mapUtils);
                                ft.replace(R.id.top_summary_view_fragment, fragment);
                                ft.commit();
                                loadingSquareView.hide();
                                displayingRoute = true;
                            }

                            @Override
                            public void onError(Throwable e) {
                                viewingRoute = false;
                                Timber.e("HERE ERROR!");
                                if (e.getMessage().equals("HTTP 403 Forbidden")) {
                                    Toast.makeText(getApplicationContext(),
                                        "Please enter a location within the bounds.",
                                        Toast.LENGTH_LONG).show();
                                    floatingSearchView.setVisibility(View.VISIBLE);
                                    loadingSquareView.hide();
                                    toggleSearchViewVisible(true,
                                        () -> btCurrentLocation.setVisibility(View.VISIBLE));
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                        "An error occured, please try again..",
                                        Toast.LENGTH_LONG).show();
                                    floatingSearchView.setVisibility(View.VISIBLE);
                                    loadingSquareView.hide();
                                    toggleSearchViewVisible(true,
                                        () -> btCurrentLocation.setVisibility(View.VISIBLE));
                                }
                                Timber.w(e);
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
                });
                toggleRouteSummarySheetVisible(true, () -> {

                });
            });
        }

        void bind(SearchResult result) {
            locationType.setImageDrawable(result.getLocationType());
            goButton.setImageDrawable(result.getGoButton());
            mainAddress.setText(result.getMainAddress());
            cityState.setText(result.getCityState());
        }
    }

    /**
     * Step 1: Check Google Play services
     */
    private void startStep1() {

        //Check whether this user has installed Google play service which is being used by Location updates.
        if (isGooglePlayServicesAvailable()) {

            //Passing null to indicate that it is executing for the first time.
            startStep2(null);

        } else {
            Toast.makeText(getApplicationContext(), "NO GOOGLE PLAY SERVICES", Toast.LENGTH_LONG)
                .show();
        }
    }

    /**
     * Step 2: Check & Prompt Internet connection
     */
    private Boolean startStep2(DialogInterface dialog) {
        ConnectivityManager connectivityManager
            = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            promptInternetConnect();
            return false;
        }

        if (dialog != null) {
            dialog.dismiss();
        }

        //Yes there is active internet connection. Next check Location is granted by user or not.

        if (checkPermissions()) { //Yes permissions are granted by the user. Go to the next step.
            startStep3();
        } else {  //No user has not granted the permissions yet. Request now.
            requestPermissions();
        }
        return true;
    }

    private void promptInternetConnect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
        builder.setTitle("Title: No internet");
        builder.setMessage("Message: No internet");

        String positiveText = "Refresh";
        builder.setPositiveButton(positiveText,
            (dialog, which) -> {
                //Block the Application Execution until user grants the permissions
                if (startStep2(dialog)) {
                    //Now make sure about location permission.
                    if (checkPermissions()) {
                        //Step 2: Start the Location Monitor Service
                        //Everything is there to start the service.
                        startStep3();
                    } else if (!checkPermissions()) {
                        requestPermissions();
                    }
                }
            });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //       Return the availability of GooglePlayServices
    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION);

        int permissionState2 = ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION);

        return permissionState1 == PackageManager.PERMISSION_GRANTED
            && permissionState2 == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermissions() {

        boolean shouldProvideRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        boolean shouldProvideRationale2 =
            ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (shouldProvideRationale || shouldProvideRationale2) {
            Timber.w("Displaying permission rationale to provide additional context.");
            showSnackbar("Requesting Permission",
                "OK?", view -> ActivityCompat.requestPermissions(MapActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE));
        } else {
            Timber.w("Requesting permission");
            ActivityCompat.requestPermissions(MapActivity.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextString The id for the string resource for the Snackbar text.
     * @param actionString The text of the action item.
     * @param listener The listener associated with the Snackbar action.
     */
    private void showSnackbar(String mainTextString, String actionString,
        View.OnClickListener listener) {
        Snackbar.make(
            findViewById(android.R.id.content),
            mainTextString,
            Snackbar.LENGTH_INDEFINITE)
            .setAction(actionString, listener).show();
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        Timber.w("onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If img_user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Timber.w("User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Timber.w("Permission granted, updates requested, starting location updates");
                startStep3();

            } else {
                showSnackbar("Permission was denied",
                    "Settings", view -> {
                        // Build intent that displays the App settings screen.
                        Intent intent = new Intent();
                        intent.setAction(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",
                            BuildConfig.APPLICATION_ID, null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    });
            }
        }
    }

    /**
     * Step 3: Start the Location Monitor Service
     */
    private void startStep3() {
        //And it will be keep running until you close the entire application from task manager.
        //This method will executed only once.
        if (!mAlreadyStartedService) {

            //Start location sharing service to app server.........
            Intent intent = new Intent(this, LocationMonitoringService.class);
            startService(intent);

            mAlreadyStartedService = true;
            //Ends................................................
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();
    }
}