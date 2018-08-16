package aspace.trya.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import aspace.trya.R;
import aspace.trya.misc.RouteOptionsListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RouteOptionsPreviewFragment extends Fragment {

    @BindView(R.id.first_row_layout)
    LinearLayout llFirstRow;
    @BindView(R.id.back_button)
    ImageButton btBack;
    @BindView(R.id.start_location_ic)
    ImageView selectStartLocation;
    @BindView(R.id.end_location_tv)
    TextView tvEndLocation;
    @BindView(R.id.car_bike_selector)
    LinearLayout selectCarBikeMethod;
    @BindView(R.id.car_walk_selector)
    LinearLayout selectCarWalkMethod;
    @BindView(R.id.car_direct_selector)
    LinearLayout selectCarDirectMethod;

    private RouteOptionsListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address_view, parent, false);
        ButterKnife.bind(this, view);

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

    @OnClick(R.id.start_location_ic)
    public void originClicked() {
        mListener.routeOptionsOriginSelectorClicked();
    }

    @OnClick(R.id.end_location_tv)
    public void destinationClicked() {
        mListener.routeOptionsDestinationSelectorClicked();
    }

    @OnClick(R.id.back_button)
    public void backButtonClicked() {
        mListener.routeOptionsBackButtonClicked();
    }
}
