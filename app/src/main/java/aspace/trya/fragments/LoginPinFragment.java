package aspace.trya.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import aspace.trya.R;
import aspace.trya.api.AspaceMainService;
import aspace.trya.api.AspaceResponseCodes;
import aspace.trya.api.RetrofitServiceGenerator;
import aspace.trya.listeners.OnApplicationStateListener;
import aspace.trya.misc.APIURLs;
import aspace.trya.models.AuthResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginPinFragment extends Fragment {

    @BindView(R.id.buttonBack)
    Button backButton;
    @BindView(R.id.buttonContinue)
    Button continueButton;
    @BindView(R.id.sendAgainButton)
    CardView sendAgainButton;
    @BindView(R.id.sendAgainText)
    TextView sendAgainText;
    @BindView(R.id.txt_pin_entry)
    PinEntryEditText pinEntryEditText;

    private String loginPhoneNumber;
    private String deviceId;

    private OnApplicationStateListener mListener;

    private AspaceMainService aspaceMainService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_pin, parent, false);
        ButterKnife.bind(this, view);

        loginPhoneNumber = getArguments().getString("LOGIN_PHONE_NUMBER");
        deviceId = getArguments().getString("DEVICE_ID");

        continueButton.setEnabled(true);

        aspaceMainService = RetrofitServiceGenerator
            .createService(AspaceMainService.class, APIURLs.ASPACE_MAIN_PROD_URL);

        pinEntryEditText.requestFocus();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnApplicationStateListener) {
            mListener = (OnApplicationStateListener) context;
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

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        continueButton.setOnClickListener(v -> {
            if (pinEntryEditText.getText().toString().length() != 6) {
                pinEntryEditText.setError("Incomplete PIN");
            } else {
                Call<AuthResponse> call = aspaceMainService
                    .checkPin(loginPhoneNumber, deviceId, pinEntryEditText.getText().toString());
                call.enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call,
                        Response<AuthResponse> response) {
                        int responseCode = response.body().getResponseCode();
                        if (responseCode == AspaceResponseCodes.PIN_EXPIRED) {
                            mListener.pinExpired();
                        } else if (responseCode == AspaceResponseCodes.INVALID_PIN) {
                            pinEntryEditText.setError("Invalid PIN");
                            YoYo.with(Techniques.Shake).duration(500)
                                .playOn(view.findViewById(R.id.txt_pin_entry));
                            pinEntryEditText.requestFocus();
                        } else {
                            mListener.continueFromLogin(response.body().getAccessCode());
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Toast
                            .makeText(getContext(), "Something went wrong, please restart the app.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        backButton.setOnClickListener(v -> mListener.pinExpired());

        sendAgainText.setOnClickListener(v -> createResendPinTimer());
    }

    public void createResendPinTimer() {
        final String sendPinText = "Re-Send PIN in ";
        final String finalSendPinText = "Re-Send PIN";
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                sendAgainText.setEnabled(false);
                String newButtonText = sendPinText + (millisUntilFinished / 1000);
                sendAgainText.setText(newButtonText);
            }

            public void onFinish() {
                sendAgainText.setEnabled(true);
                sendAgainText.setText(finalSendPinText);
            }
        }.start();
    }

}
