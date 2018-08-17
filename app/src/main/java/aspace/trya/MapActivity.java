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
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mypopsy.widget.FloatingSearchView;

import java.util.Timer;
import java.util.TimerTask;

import aspace.trya.adapter.ArrayRecyclerAdapter;
import aspace.trya.api.MapboxService;
import aspace.trya.api.MapboxServiceGenerator;
import aspace.trya.api.RetrofitLatLng;
import aspace.trya.fragments.RouteOptionsPreviewFragment;
import aspace.trya.geojson.Feature;
import aspace.trya.geojson.GeoJSON;
import aspace.trya.misc.ApplicationState;
import aspace.trya.misc.KeyboardUtils;
import aspace.trya.misc.LocationMonitoringService;
import aspace.trya.misc.MapUtils;
import aspace.trya.misc.RouteOptionsListener;
import aspace.trya.search.SearchResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements RouteOptionsListener, OnMapReadyCallback, View.OnClickListener, ActionMenuView.OnMenuItemClickListener, View.OnFocusChangeListener {
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.floating_search_view)
    FloatingSearchView floatingSearchView;
    @BindView(R.id.bottom_sheet)
    LinearLayout layoutBottomSheet;
    @BindView(R.id.current_location_button)
    FloatingActionButton btCurrentLocation;
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

    Menu menuSearchView;

    @BindView(R.id.start_route_button)
    Button btStartRoute;

    BottomSheetBehavior sheetBehavior;
    private MapboxMap mapboxMap;

    private SearchAdapter mAdapter;

    private LatLng beginLocation;

    private ApplicationState applicationState;

    private boolean locationTracked;

    private LatLng lastCurrentLocation;

    private MapUtils mapUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        applicationState = new ApplicationState(MapActivity.this);
        beginLocation = applicationState.getLoadLocation();

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String latitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
                        String longitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);
                        if (latitude != null && longitude != null) {
                            lastCurrentLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                            if (locationTracked && mapboxMap != null) {
                                mapUtils.zoomToLatLng(lastCurrentLocation, 250);
                            }
                        }
                    }
                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );

        ButterKnife.bind(this);

        rlBikeRoute.setOnClickListener(this);
        rlDriveRoute.setOnClickListener(this);
        rlWalkRoute.setOnClickListener(this);

        Mapbox.getInstance(getApplicationContext(), getString(R.string.mapbox_access_token));

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setPeekHeight(0);

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        floatingSearchView.setAdapter(mAdapter = new SearchAdapter());
        floatingSearchView.setOnSearchListener(text -> floatingSearchView.setActivated(false));

        floatingSearchView.addTextChangedListener(new TextWatcher() {
            private final long DELAY = 500; // milliseconds
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
                                    floatingSearchView.getMenu().getItem(0).setVisible(floatingSearchView.getText().toString().trim().length() != 0);
                                    if (floatingSearchView.getText().length() == 0) {
                                        mAdapter.clear();

                                    }
                                    String searchString = s.toString().replace(' ', '+').trim() + ".json";
                                    Call<GeoJSON> call = MapboxServiceGenerator.createService(MapboxService.class).getSearchSuggestions(searchString, new RetrofitLatLng(mapboxMap.getCameraPosition().target.getLatitude(), mapboxMap.getCameraPosition().target.getLongitude()), "us", getString(R.string.mapbox_access_token), 10);
                                    call.enqueue(new Callback<GeoJSON>() {
                                        @Override
                                        public void onResponse(@NonNull Call<GeoJSON> call, @NonNull Response<GeoJSON> response) {
                                            try {
                                                assert response.body() != null;
                                                mAdapter.setNotifyOnChange(false);
                                                mAdapter.clear();
                                                mAdapter.addAll(response.body().getSearchResults(getApplicationContext()));
                                                mAdapter.setNotifyOnChange(true);
                                                mAdapter.notifyDataSetChanged();
                                            } catch (NullPointerException ignored) {
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<GeoJSON> call, Throwable t) {
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
        mapUtils = new MapUtils(mapboxMap);
        CameraPosition position = new CameraPosition.Builder()
                .target(beginLocation)
                .zoom(14)
                .build();
        mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
        mapboxMap.getUiSettings().setRotateGesturesEnabled(false);
        btCurrentLocation.setOnClickListener(v -> {
            locationTracked = !locationTracked;
            btCurrentLocation.setImageResource(locationTracked ? R.drawable.ic_current_location_enabled : R.drawable.ic_current_location_disabled);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchview_menu, menuSearchView);
        return true;
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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        floatingSearchView.getMenu().getItem(0).setVisible(floatingSearchView.getText().toString().trim().length() != 0);
        switch (v.getId()) {
            case R.id.floating_search_view:
                btCurrentLocation.setVisibility(View.INVISIBLE);
            case R.id.map_view:
                btCurrentLocation.setVisibility(View.VISIBLE);
            default:
                btCurrentLocation.setVisibility(View.VISIBLE);
        }
    }

    public void toggleSearchViewVisible(boolean visible, aspace.trya.misc.Callback callback) {
        floatingSearchView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        floatingSearchView.animate()
                .translationYBy(visible ? 200.0f : -200.0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (callback != null) {
                            callback.execute();
                        }
                    }
                });
    }

    @Override
    public void routeOptionsOriginSelectorClicked() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.top_summary_view_fragment);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            floatingSearchView.setHint("Origin:");
            toggleSearchViewVisible(true, () -> {

            });
        }
    }

    @Override
    public void routeOptionsDestinationSelectorClicked() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.top_summary_view_fragment);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            floatingSearchView.setHint("Destination:");
            toggleSearchViewVisible(true, () -> {

            });
        }
    }

    private class SearchAdapter extends ArrayRecyclerAdapter<SearchResult, MapActivity.SuggestionViewHolder> {
        private LayoutInflater inflater;

        SearchAdapter() {
            setHasStableIds(true);
        }

        @Override
        public MapActivity.SuggestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (inflater == null) inflater = LayoutInflater.from(parent.getContext());
            return new MapActivity.SuggestionViewHolder(inflater.inflate(R.layout.item_suggestion, parent, false));
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
            itemView.findViewById(R.id.address_layout).setOnClickListener(v -> {
                Feature clickedFeature = mAdapter.getItem(getAdapterPosition()).getFeature();
                KeyboardUtils.hideSoftKeyboard(getCurrentFocus(), getSystemService(INPUT_METHOD_SERVICE));

                floatingSearchView.setActivated(false);
                toggleSearchViewVisible(false, () -> {
                    floatingSearchView.setText(clickedFeature.getPlaceName());
                    mAdapter.clear();
                    floatingSearchView.setVisibility(View.INVISIBLE);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    ft.replace(R.id.top_summary_view_fragment, new RouteOptionsPreviewFragment());
                    ft.commit();
                });
                if (clickedFeature.hasBbox()) {
                    mapUtils.zoomToBbox(clickedFeature.getLatLngBounds(), 4000);
                } else {
                    mapUtils.zoomToLatLng(clickedFeature.getGeometry().getLatLng(), 3000);
                }
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