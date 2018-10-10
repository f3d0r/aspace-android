package aspace.trya;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.view.View.OnClickListener;
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
import aspace.trya.misc.MapUtils;
import aspace.trya.models.RouteOptionsResponse;
import aspace.trya.models.geojson.Feature;
import aspace.trya.models.geojson.GeoJSON;
import aspace.trya.search.SearchResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.clans.fab.FloatingActionMenu;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.android.gestures.StandardScaleGestureDetector;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMap.OnMapLongClickListener;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mypopsy.widget.FloatingSearchView;
import com.mypopsy.widget.FloatingSearchView.OnIconClickListener;
import com.steelkiwi.library.SlidingSquareLoaderView;
import io.intercom.android.sdk.Intercom;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.Timer;
import java.util.TimerTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MapActivity extends AppCompatActivity implements RouteOptionsListener,
    RoutePreviewSwipeListener, OnMapReadyCallback, View.OnClickListener,
    ActionMenuView.OnMenuItemClickListener {

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

        new java.util.Timer().schedule(
            new java.util.TimerTask() {
                @Override
                public void run() {
                    lastCurrentLocation = currentLocationUpdates.getLatLng();
                    routeOrigin = new Feature(lastCurrentLocation, true);
                    if (locationTracked && mapboxMap != null) {
                        mapUtils.zoomToLatLng(lastCurrentLocation, 250);
                    }
                }
            },
            2000
        );

        fabNavMenu.setIconAnimated(false);

        fabNavBikeDest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                firstRouteCompleted = true;
                fabNavMenu.close(true);
                getRoute(routeOptionsMapController.getBikeLoc().getPoint(), "Your Location",
                    routeOptionsMapController.getAbsDest().getPoint(), "Your Destination",
                    new aspace.trya.misc.Callback() {
                        @Override
                        public void execute() {
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
                                NavigationLauncher.startNavigation(MapActivity.this, options);
                            } catch (Exception e) {
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=com.limebike")));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                                        "https://play.google.com/store/apps/details?id=com.limebike&hl=en_US")));
                                }
                            }
                        }
                    });
            }
        });

        fabNavWalkDest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                firstRouteCompleted = true;
                fabNavMenu.close(true);
                getRoute(
                    routeOptionsMapController.getParkLoc()
                        .getPoint(), "Your Origin",
                    routeOptionsMapController.getAbsDest()
                        .getPoint(), "Your Destination",
                    new aspace.trya.misc.Callback() {
                        @Override
                        public void execute() {
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
                        }
                    });
            }
        });

        fabNavPark.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                firstRouteCompleted = true;
                fabNavMenu.close(true);
                getRoute(routeOptionsMapController.getAbsOrigin()
                        .getPoint(), "Your Location",
                    routeOptionsMapController.getParkLoc()
                        .getPoint(),
                    routeOptionsMapController.getParkLoc()
                        .getLocMetaData().getName(),
                    new aspace.trya.misc.Callback() {
                        @Override
                        public void execute() {
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
                        }
                    });
            }
        });

        floatingSearchView.setOnSearchFocusChangedListener(focused ->

        {
            floatingSearchView.getMenu().getItem(0)
                .setVisible(floatingSearchView.getText().toString().trim().length() != 0);
            if (cvZoomInWarningVisible) {
//                cvZoomInWarning.setAlpha(mapboxMap.getCameraPosition().zoom < 15 ? 1 : 0);
            }
            btCurrentLocation.setVisibility(focused ? View.INVISIBLE : View.VISIBLE);
        });

        floatingSearchView.setOnIconClickListener(new OnIconClickListener() {
            @Override
            public void onNavigationClick() {
                Intercom.client().displayMessenger();
            }
        });

        Mapbox.getInstance(

            getApplicationContext(), getString(R.string.mapbox_access_token));

        mapView =

            findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        floatingSearchView.setAdapter(mAdapter = new

            SearchAdapter());
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
                                if (floatingSearchView.getText().toString().trim().length() == 0) {
                                    mAdapter.clear();
                                    floatingSearchView.getMenu().getItem(0).setVisible(false);
                                } else {
                                    floatingSearchView.getMenu().getItem(0).setVisible(true);
                                }
                                String searchString =
                                    s.toString().replace(' ', '+').trim() + ".json";
                                Call<GeoJSON> call = RetrofitServiceGenerator
                                    .createService(
                                        MapboxService.class,
                                        APIURLs.MAPBOX_URL)
                                    .getSearchSuggestions(searchString, new RetrofitLatLng(mapboxMap
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
                                                    .getSearchResults(getApplicationContext()));
                                                mAdapter.setNotifyOnChange(true);
                                                mAdapter.notifyDataSetChanged();
                                            } catch (NullPointerException ignored) {
                                            }
                                        }

                                        @Override
                                        public void onFailure(
                                            @NonNull Call<GeoJSON> call,
                                            Throwable t) {
                                            Timber.w("THERE'S AN ERROR: ");
                                            Timber.w(t);
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

        mapboxMap.addOnMapLongClickListener(new OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng point) {
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
                        }

                        @Override
                        public void onFailure(Call<GeocodingResponse> call, Throwable t) {

                        }
                    });
                }
            }
        });
        mapboxMap.addOnMapClickListener(point -> btCurrentLocation.setVisibility(View.VISIBLE));
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
                    Timber.d("Error: " + throwable.getMessage());
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
                new aspace.trya.misc.Callback() {
                    @Override
                    public void execute() {
                        btCurrentLocation.setVisibility(View.VISIBLE);
                        floatingSearchView.requestFocus();
                        floatingSearchView.setActivated(true);
                    }

                });
            fabNavMenu.setVisibility(View.INVISIBLE);
            getSupportFragmentManager().beginTransaction().
                remove(getSupportFragmentManager().findFragmentById(R.id.top_summary_view_fragment))
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
                                        new aspace.trya.misc.Callback() {
                                            @Override
                                            public void execute() {
                                                btCurrentLocation.setVisibility(View.VISIBLE);
                                            }
                                        });
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                        "An error occured, please try again..",
                                        Toast.LENGTH_LONG).show();
                                    floatingSearchView.setVisibility(View.VISIBLE);
                                    loadingSquareView.hide();
                                    toggleSearchViewVisible(true,
                                        new aspace.trya.misc.Callback() {
                                            @Override
                                            public void execute() {
                                                btCurrentLocation.setVisibility(View.VISIBLE);
                                            }
                                        });
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

}