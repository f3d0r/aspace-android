package aspace.trya.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wonderkiln.camerakit.CameraView;

import aspace.trya.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class OnboardingSlide2 extends Fragment {

    @BindView(R.id.cameraView) CameraView cameraView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding_slide_2, parent, false);
        ButterKnife.bind(this, view);
        cameraView.start();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Timber.d("RUNNING ON ATTACH!");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cameraView.stop();
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

    }
}