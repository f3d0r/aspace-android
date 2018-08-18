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

import java.util.Arrays;
import java.util.List;

import aspace.trya.R;
import aspace.trya.misc.RouteOptionsListener;
import aspace.trya.models.geojson.Feature;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @BindViews({R.id.first_option_car_iv, R.id.first_option_then1_iv, R.id.first_option_park_iv, R.id.first_option_then2_iv, R.id.first_option_walk_iv})
    List<ImageView> firstOptionImageViews;

    @BindViews({R.id.second_option_car_iv, R.id.second_option_then1_iv, R.id.second_option_park_iv, R.id.second_option_then2_iv, R.id.second_option_bike_iv})
    List<ImageView> secondOptionImageViews;

    @BindViews({R.id.third_option_car_iv, R.id.third_option_then_iv, R.id.third_option_park_iv})
    List<ImageView> thirdOptionImageViews;

    private RouteOptionsListener mListener;

    private boolean[] routeOptionsSelected = {false, false, false};

    public static RouteOptionsPreviewFragment newInstance(Feature routeOrigin, Feature routeDestination, int preferredRouteOption) {
        RouteOptionsPreviewFragment fragment = new RouteOptionsPreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ORIGIN", routeOrigin);
        bundle.putSerializable("DESTINATION", routeDestination);
        bundle.putInt("PREFERRED_ROUTE_OPTION", preferredRouteOption);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route_options_preview, parent, false);
        ButterKnife.bind(this, view);

        Feature destination = (Feature) getArguments().getSerializable("DESTINATION");
        tvEndLocation.setText(destination.getPlaceNameLine1());

        carBikeSelector.setOnClickListener(v -> {
            Arrays.fill(routeOptionsSelected, false);
            resetRouteSelectorColors();
            routeOptionsSelected[0] = true;
            for (ImageView currentImageView : firstOptionImageViews) {
                currentImageView.setColorFilter(getResources().getColor(android.R.color.white));
            }
            carBikeSelector.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        });

        carWalkSelector.setOnClickListener(v -> {
            Arrays.fill(routeOptionsSelected, false);
            resetRouteSelectorColors();
            routeOptionsSelected[1] = true;
            for (ImageView currentImageView : secondOptionImageViews) {
                currentImageView.setColorFilter(getResources().getColor(android.R.color.white));
            }
            carWalkSelector.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        });

        cardDirectSelector.setOnClickListener(v -> {
            Arrays.fill(routeOptionsSelected, false);
            resetRouteSelectorColors();
            routeOptionsSelected[2] = true;
            for (ImageView currentImageView : thirdOptionImageViews) {
                currentImageView.setColorFilter(getResources().getColor(android.R.color.white));
            }
            cardDirectSelector.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        });

        return view;
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
        carBikeSelector.setCardBackgroundColor(getResources().getColor(R.color.white));
        carWalkSelector.setCardBackgroundColor(getResources().getColor(R.color.white));
        cardDirectSelector.setCardBackgroundColor(getResources().getColor(R.color.white));
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
}
