package aspace.trya.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import aspace.trya.R;
import aspace.trya.api.AspaceService;
import aspace.trya.api.AspaceServiceGenerator;
import aspace.trya.models.AuthResponse;
import aspace.trya.misc.OnApplicationStateListener;
import aspace.trya.models.ResponseCodes;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class PhoneConfirmationFragment extends Fragment {

    @BindView(R.id.buttonResendPin)
    Button resendPinButton;
    @BindView(R.id.buttonSendPinCall)
    Button callPinButton;
    @BindView(R.id.buttonBack)
    Button backButton;
    @BindView(R.id.buttonContinue)
    Button continueButton;
    @BindView(R.id.txt_pin_entry)
    PinEntryEditText pinEntryEditText;

    private String loginPhoneNumber;
    private String deviceId;
    private boolean onboard;

    private OnApplicationStateListener mListener;

    private AspaceService aspaceService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_confirmation, parent, false);
        ButterKnife.bind(this, view);

        loginPhoneNumber = getArguments().getString("LOGIN_PHONE_NUMBER");
        deviceId = getArguments().getString("DEVICE_ID");
        onboard = getArguments().getBoolean("ONBOARD");

        continueButton.setEnabled(false);

        aspaceService = AspaceServiceGenerator.createService(AspaceService.class);

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
        createResendPinTimer(resendPinButton);
        resendPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createResendPinTimer(resendPinButton);
            }
        });
        pinEntryEditText.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                continueButton.setEnabled(true);
            }
        });

        callPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createResendPinTimer(callPinButton);
                resendPinButton.setEnabled(false);

            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinEntryEditText.getText().toString().length() != 6) {
                    pinEntryEditText.setError("Incomplete PIN");
                } else {
                    Call<AuthResponse> call = aspaceService.checkPin(loginPhoneNumber, deviceId, pinEntryEditText.getText().toString());
                    call.enqueue(new Callback<AuthResponse>() {
                        @Override
                        public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                            int responseCode = response.body().getResponseCode();
                            if (responseCode == ResponseCodes.EXPIRED_PIN) {
                                mListener.pinExpired();
                            } else if (responseCode == ResponseCodes.INVALID_PIN) {
                                pinEntryEditText.setError("Invalid PIN");
                                YoYo.with(Techniques.Shake).duration(500).playOn(view.findViewById(R.id.txt_pin_entry));
                            } else {
                                mListener.continueFromLogin(response.body().getAccessCode().getAccessCode());
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthResponse> call, Throwable t) {
                            Timber.e(t);
                            YoYo.with(Techniques.Shake).duration(500).playOn(view.findViewById(R.id.txt_pin_entry));
                        }
                    });
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.pinExpired();
            }
        });
    }

    public void createResendPinTimer(final Button blockedButton) {
        String sendPinText;
        final String finalCallPin = "Call again w/ PIN";
        final String finalTextPin = "Re-Send PIN";
        if (blockedButton.getId() == R.id.buttonSendPinCall) {
            resendPinButton.setEnabled(false);
            sendPinText = "Call again w/ PIN in ";
        } else {
            sendPinText = "Re-Send PIN in ";
        }
        final String finalSendPinText = sendPinText;
        CountDownTimer timer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                blockedButton.setEnabled(false);
                String newButtonText = finalSendPinText + (millisUntilFinished / 1000);
                blockedButton.setText(newButtonText);
            }

            public void onFinish() {
                blockedButton.setEnabled(true);
                if (blockedButton.getId() == R.id.buttonSendPinCall) {
                    blockedButton.setText(finalCallPin);
                } else {
                    blockedButton.setText(finalTextPin);
                }

            }
        }.start();
    }

}
