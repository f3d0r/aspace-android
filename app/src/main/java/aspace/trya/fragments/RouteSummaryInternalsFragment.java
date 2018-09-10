package aspace.trya.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import aspace.trya.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RouteSummaryInternalsFragment extends Fragment {

    @BindView(R.id.first_text_view)
    TextView firstTextView;
    @BindView(R.id.second_text_view)
    TextView secondTextView;
    private Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        String first = getArguments().getString("TEST_FIRST", "Default First");
        String second = getArguments().getString("TEST_SECOND", "Default Second");
        View view = inflater.inflate(R.layout.fragment_route_summary, container, false);
        unbinder = ButterKnife.bind(this, view);

        firstTextView.setText(first);
        secondTextView.setText(second);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
