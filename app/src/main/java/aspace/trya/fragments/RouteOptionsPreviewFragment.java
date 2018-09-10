package aspace.trya.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import aspace.trya.R;
import aspace.trya.listeners.RouteOptionsListener;
import aspace.trya.misc.BundleIdentifiers;
import aspace.trya.models.RouteOptionsResponse;
import aspace.trya.models.geojson.Feature;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.Arrays;
import java.util.List;

public class RouteOptionsPreviewFragment extends Fragment {

    @BindView(R.id.start_location_ic)
    ImageView selectStartLocation;
    @BindView(R.id.end_location_tv)
    TextView tvEndLocation;

    @BindView(R.id.car_bike_selector)
    CardView carBikeSelector;
    @BindView(R.id.car_walk_selector)
    CardView carWalkSelector;
    @BindView(R.id.car_direct_selector)
    CardView cardDirectSelector;

    @BindView(R.id.route_options_card_view)
    CardView cvRouteOptions;

    @BindViews({R.id.first_option_car_iv, R.id.first_option_then1_iv, R.id.first_option_park_iv,
        R.id.first_option_then2_iv, R.id.first_option_bike_iv})
    List<ImageView> firstOptionImageViews;

    @BindViews({R.id.second_option_car_iv, R.id.second_option_then1_iv, R.id.second_option_park_iv,
        R.id.second_option_then2_iv, R.id.second_option_walk_iv})
    List<ImageView> secondOptionImageViews;

    @BindViews({R.id.third_option_car_iv, R.id.third_option_then_iv, R.id.third_option_park_iv})
    List<ImageView> thirdOptionImageViews;

    private RouteOptionsListener mListener;

    private boolean[] routeOptionsSelected = {false, false, false};

    private Feature origin;
    private Feature destination;

    private RouteOptionsResponse[] routeOptions;

    public static RouteOptionsPreviewFragment newInstance(Feature routeOrigin,
        Feature routeDestination, RouteOptionsResponse[] routeOptions,
        int preferredRouteOptionIndex) {
        RouteOptionsPreviewFragment fragment = new RouteOptionsPreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BundleIdentifiers.ORIGIN_LOCATION, routeOrigin);
        bundle.putSerializable(BundleIdentifiers.DESTINATION_LOCATION, routeDestination);
        bundle.putSerializable(BundleIdentifiers.ROUTE_OPTIONS, routeOptions);
        bundle.putInt(BundleIdentifiers.PREFERRED_ROUTE_OPTION_INDEX, preferredRouteOptionIndex);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RouteOptionsListener) {
            mListener = (RouteOptionsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void resetRouteSelectorColors() {
        carBikeSelector.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        carWalkSelector.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        cardDirectSelector.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        for (ImageView currentImageView : firstOptionImageViews) {
            currentImageView.setColorFilter(getResources().getColor(android.R.color.black));
        }
        for (ImageView currentImageView : secondOptionImageViews) {
            currentImageView.setColorFilter(getResources().getColor(android.R.color.black));
        }
        for (ImageView currentImageView : thirdOptionImageViews) {
            currentImageView.setColorFilter(getResources().getColor(android.R.color.black));
        }
    }

    @OnClick(R.id.start_location_ic)
    public void originClicked() {
        mListener.routeOptionsOriginSelectorClicked(cvRouteOptions);
    }

    @OnClick(R.id.end_location_tv)
    public void destinationClicked() {
        mListener.routeOptionsDestinationSelectorClicked(cvRouteOptions);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route_options_preview, parent, false);
        ButterKnife.bind(this, view);

        origin = (Feature) getArguments().getSerializable(BundleIdentifiers.ORIGIN_LOCATION);
        destination = (Feature) getArguments()
            .getSerializable(BundleIdentifiers.DESTINATION_LOCATION);
        tvEndLocation.setText(destination.getPlaceNameLine1());
        routeOptions = (RouteOptionsResponse[]) getArguments()
            .getSerializable(BundleIdentifiers.ROUTE_OPTIONS);
        routeOptionsSelected[getArguments()
            .getInt(BundleIdentifiers.PREFERRED_ROUTE_OPTION_INDEX)] = true;

        carBikeSelector.setOnClickListener(v -> {
            Arrays.fill(routeOptionsSelected, false);
            resetRouteSelectorColors();
            routeOptionsSelected[0] = true;
            for (ImageView currentImageView : firstOptionImageViews) {
                currentImageView.setColorFilter(getResources().getColor(android.R.color.white));
            }
            carBikeSelector
                .setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mListener.routeOptionsParkBikeSelectorClicked(routeOptions[0], 0);
        });

        carWalkSelector.setOnClickListener(v -> {
            Arrays.fill(routeOptionsSelected, false);
            resetRouteSelectorColors();
            routeOptionsSelected[1] = true;
            for (ImageView currentImageView : secondOptionImageViews) {
                currentImageView.setColorFilter(getResources().getColor(android.R.color.white));
            }
            carWalkSelector
                .setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mListener.routeOptionsParkWalkSelectorClicked(routeOptions[1], 0);
        });

        cardDirectSelector.setOnClickListener(v -> {
            Arrays.fill(routeOptionsSelected, false);
            resetRouteSelectorColors();
            routeOptionsSelected[2] = true;
            for (ImageView currentImageView : thirdOptionImageViews) {
                currentImageView.setColorFilter(getResources().getColor(android.R.color.white));
            }
            cardDirectSelector
                .setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mListener.routeOptionsParkDirectSelectorClicked(routeOptions[2], 0);
        });

        int preferredRouteOption = getArguments()
            .getInt(BundleIdentifiers.PREFERRED_ROUTE_OPTION_INDEX, 0);
        if (preferredRouteOption == 0) {
            carBikeSelector.performClick();
        } else if (preferredRouteOption == 1) {
            carWalkSelector.performClick();
        } else if (preferredRouteOption == 2) {
            cardDirectSelector.performClick();
        }

        return view;
    }
}
