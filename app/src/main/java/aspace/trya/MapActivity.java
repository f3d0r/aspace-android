package aspace.trya;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
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
import aspace.trya.misc.KeyboardUtils;
import aspace.trya.misc.LocationMonitoringService;
import aspace.trya.misc.MapUtils;
import aspace.trya.models.RouteOptionsResponse;
import aspace.trya.models.geojson.Feature;
import aspace.trya.models.geojson.GeoJSON;
import aspace.trya.search.SearchResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.android.gestures.StandardScaleGestureDetector;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mypopsy.widget.FloatingSearchView;
import com.steelkiwi.library.SlidingSquareLoaderView;
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

    @BindView(R.id.zoom_in_warning_cardview)
    CardView cvZoomInWarning;

    @BindView(R.id.route_summary_fragment)
    LinearLayout fragmentRouteSummary;
    @BindView(R.id.start_navigation_button)
    FloatingActionButton btStartNavigation;

    private MapboxMap mapboxMap;

    private SearchAdapter mAdapter;

    private LatLng beginLocation;

    private ApplicationState applicationState;

    private boolean locationTracked;

    private LatLng lastCurrentLocation;

    private MapUtils mapUtils;

    //For Route Display (Defaults)
    private Feature routeOrigin;

    //Route Options Fragment Controller for Map
    private RouteOptionsMapController routeOptionsMapController;

    private boolean cvZoomInWarningVisible;

    private BottomSheetBehavior fgRouteSummaryBehavior;

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        applicationState = new ApplicationState(MapActivity.this);
        beginLocation = applicationState.getLoadLocation();
        fgRouteSummaryBehavior = BottomSheetBehavior.from(fragmentRouteSummary);

        LocalBroadcastManager.getInstance(this).registerReceiver(
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String latitude = intent
                        .getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
                    String longitude = intent
                        .getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);
                    if (latitude != null && longitude != null) {
                        lastCurrentLocation = new LatLng(Double.parseDouble(latitude),
                            Double.parseDouble(longitude));
                        routeOrigin = new Feature(beginLocation, true);
                        if (locationTracked && mapboxMap != null) {
                            mapUtils.zoomToLatLng(lastCurrentLocation, 250);
                        }
                    }
                }
            }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );

        ButterKnife.bind(this);

        floatingSearchView.setOnSearchFocusChangedListener(focused -> {
            floatingSearchView.getMenu().getItem(0)
                .setVisible(floatingSearchView.getText().toString().trim().length() != 0);
            if (cvZoomInWarningVisible) {
                cvZoomInWarning.setAlpha(mapboxMap.getCameraPosition().zoom < 15 ? 1 : 0);
            }
            btCurrentLocation.setVisibility(focused ? View.INVISIBLE : View.VISIBLE);
        });

        Mapbox.getInstance(getApplicationContext(), getString(R.string.mapbox_access_token));

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        floatingSearchView.setAdapter(mAdapter = new SearchAdapter());
        floatingSearchView.setOnSearchListener(text -> floatingSearchView.setActivated(false));

        floatingSearchView.addTextChangedListener(new TextWatcher() {
            private final long DELAY = getResources()
                .getInteger(R.integer.search_gecode_delay_milli); // milliseconds
            private Timer timer = new Timer();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(final Editable s) {
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
                                    .createService(MapboxService.class,
                                        APIURLs.MAPBOX_URL)
                                    .getSearchSuggestions(searchString,
                                        new RetrofitLatLng(
                                            mapboxMap.getCameraPosition().target.getLatitude(),
                                            mapboxMap.getCameraPosition().target.getLongitude()),
                                        "us",
                                        getString(R.string.mapbox_access_token), 10);
                                call.enqueue(new Callback<GeoJSON>() {
                                    @Override
                                    public void onResponse(@NonNull Call<GeoJSON> call,
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
                                    public void onFailure(@NonNull Call<GeoJSON> call,
                                        Throwable t) {
                                        Timber.w("ERROR: ");
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

        btStartNavigation.setOnClickListener(v -> Timber.w("START NAVIGATION HERE!"));
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapUtils = new MapUtils(mapboxMap, this);
        CameraPosition position = new CameraPosition.Builder()
            .target(beginLocation)
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
                    cvZoomInWarning.animate().setDuration(200).alpha(warningVisible ? 1 : 0)
                        .start();
                }
            }

            @Override
            public void onScaleEnd(@NonNull StandardScaleGestureDetector detector) {
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
        fgRouteSummaryBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void routeOptionsOriginSelectorClicked(CardView cvRouteOptions) {
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

    public void toggleZoomInWarning(boolean visible) {
        cvZoomInWarningVisible = visible;
        if (visible) {
            cvZoomInWarning.animate().setDuration(200)
                .alpha(mapboxMap.getCameraPosition().zoom < 15 ? 1 : 0).start();
        } else {
            cvZoomInWarning.animate().setDuration(200).alpha(0).start();
        }
    }

    @Override
    public void routeOptionsParkBikeSelectorClicked(RouteOptionsResponse routeOptionsResponse,
        int optionSelected) {
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

    private class SearchAdapter extends ArrayRecyclerAdapter<SearchResult, SuggestionViewHolder> {

        private LayoutInflater inflater;

        SearchAdapter() {
            setHasStableIds(true);
        }

        @Override
        public MapActivity.SuggestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
                loadingSquareView.show();
                toggleZoomInWarning(false);

                Feature clickedFeature = mAdapter.getItem(getAdapterPosition()).getFeature();
                KeyboardUtils
                    .hideSoftKeyboard(getCurrentFocus(), getSystemService(INPUT_METHOD_SERVICE));

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
                        .createService(AspaceRoutingService.class, APIURLs.ASPACE_ROUTING_PROD_URL);
                    Observable<RouteOptionsResponse> driveBikeRouteOptions = aspaceRoutingService
                        .getDriveBikeRoute(lastCurrentLocation.getLatitude(),
                            lastCurrentLocation.getLongitude(), clickedFeature.getLatitude(),
                            clickedFeature.getLongitude());
                    Observable<RouteOptionsResponse> driveWalkRouteOptions = aspaceRoutingService
                        .getDriveWalkWaypoints(lastCurrentLocation.getLatitude(),
                            lastCurrentLocation.getLongitude(), clickedFeature.getLatitude(),
                            clickedFeature.getLongitude());
                    Observable<RouteOptionsResponse> driveDirectRouteOptions = aspaceRoutingService
                        .getDriveDirectRoute(lastCurrentLocation.getLatitude(),
                            lastCurrentLocation.getLongitude(), clickedFeature.getLatitude(),
                            clickedFeature.getLongitude());
                    Observable
                        .zip(driveBikeRouteOptions, driveWalkRouteOptions, driveDirectRouteOptions,
                            (routeOptionsResponse, routeOptionsResponse2, routeOptionsResponse3) -> new RouteOptionsResponse[]{
                                routeOptionsResponse, routeOptionsResponse2, routeOptionsResponse3})
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<RouteOptionsResponse[]>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(RouteOptionsResponse[] routeOptionsResponses) {
                                FragmentTransaction ft = getSupportFragmentManager()
                                    .beginTransaction();
                                ft.setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out);
                                RoutePreviewBottomSheetController routePreviewBottomSheetController = new RoutePreviewBottomSheetController(
                                    fgRouteSummaryBehavior);
                                RouteOptionsPreviewFragment fragment = RouteOptionsPreviewFragment
                                    .newInstance(routeOrigin, clickedFeature, routeOptionsResponses,
                                        0);

                                mapUtils
                                    .zoomToBbox(routeOptionsResponses[0].getLatLngBounds(0), 3000,
                                        true);
                                routeOptionsMapController = new RouteOptionsMapController(
                                    routeOptionsResponses,
                                    mapUtils);
                                ft.replace(R.id.top_summary_view_fragment, fragment);
                                ft.commit();
                                loadingSquareView.hide();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.w("ERROR");
                                Timber.w(e);
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
                });
//        RouteO
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