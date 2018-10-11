package aspace.trya.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import butterknife.OnClick;
import com.hbb20.CountryCodePicker;
import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPhoneFragment extends Fragment {

    @BindView(R.id.country_code_picker)
    CountryCodePicker countryCodePicker;
    @BindView(R.id.btn_login)
    CardView loginButton;
    @BindView(R.id.et_phone_number)
    EditText phoneNumberEditText;
    @BindView(R.id.btn_skip_login)
    CardView skipLoginButton;

    private PhoneNumberUtil phoneNumberUtil;

    private OnApplicationStateListener mListener;

    public String deviceId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_phone, parent, false);
        ButterKnife.bind(this, view);

        phoneNumberUtil = PhoneNumberUtil.createInstance(getContext());

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

    @OnClick(R.id.btn_skip_login)
    public void skipLogin(View view) {
        mListener.skipLogin(deviceId);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        phoneNumberEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        phoneNumberEditText.requestFocus();
        deviceId = UUID.randomUUID().toString();

        loginButton.setOnClickListener(v -> {
            loginButton.setEnabled(false);
            Phonenumber.PhoneNumber phoneNumber;
            if (phoneNumberEditText.getText().toString().length() >= 10) {
                try {
                    phoneNumber = phoneNumberUtil
                        .parse(phoneNumberEditText.getText().toString(), "US");
                } catch (NumberParseException e) {
                    phoneNumberEditText.setError("Invalid phone #");
                    phoneNumberEditText.requestFocus();
                    return;
                }
                if (!phoneNumberUtil.isValidNumber(phoneNumber)) {
                    phoneNumberEditText.setError("Invalid phone #");
                    phoneNumberEditText.requestFocus();
                } else {

                    Call<AuthResponse> call = RetrofitServiceGenerator
                        .createService(AspaceMainService.class,
                            APIURLs.ASPACE_MAIN_PROD_URL)
                        .phoneLogin(phoneNumberEditText.getText().toString(), deviceId, "F");
                    call.enqueue(new Callback<AuthResponse>() {
                        @Override
                        public void onResponse(Call<AuthResponse> call,
                            Response<AuthResponse> response) {
                            int responseCode = response.body().getResponseCode();
                            if (responseCode == AspaceResponseCodes.INVALID_PHONE) {
                                phoneNumberEditText.setError("Invalid phone #");
                                phoneNumberEditText.requestFocus();
                            } else if (responseCode == AspaceResponseCodes.NEW_PHONE
                                || responseCode == AspaceResponseCodes.RETURNING_PHONE) {
                                mListener
                                    .phoneLoginToConfirm(phoneNumberEditText.getText().toString(),
                                        deviceId,
                                        response.body().getResponseCode()
                                            == AspaceResponseCodes.NEW_PHONE);
                            } else {
                                Toast.makeText(getContext(),
                                    "Something went wrong, please restart the app.",
                                    Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthResponse> call, Throwable t) {
                            Toast
                                .makeText(getContext(), "Something went wrong... Please try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                phoneNumberEditText.setError("Invalid phone #");
                phoneNumberEditText.requestFocus();
            }
        });
    }

    private class PhoneNumberFormattingTextWatcher implements TextWatcher {

        private boolean backspacingFlag = false;
        private boolean editedFlag = false;
        private int cursorComplement;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            cursorComplement = s.length() - phoneNumberEditText.getSelectionStart();
            backspacingFlag = count > after;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String string = s.toString();
            String phone = string.replaceAll("[^\\d]", "");
            if (!editedFlag) {
                if (phone.length() >= 6 && !backspacingFlag) {
                    editedFlag = true;
                    String ans =
                        "(" + phone.substring(0, 3) + ") " + phone.substring(3, 6) + "-" + phone
                            .substring(6);
                    phoneNumberEditText.setText(ans);
                    phoneNumberEditText
                        .setSelection(phoneNumberEditText.getText().length() - cursorComplement);
                } else if (phone.length() >= 3 && !backspacingFlag) {
                    editedFlag = true;
                    String ans = "(" + phone.substring(0, 3) + ") " + phone.substring(3);
                    phoneNumberEditText.setText(ans);
                    phoneNumberEditText
                        .setSelection(phoneNumberEditText.getText().length() - cursorComplement);
                }
            } else {
                editedFlag = false;
            }
            loginButton.setEnabled(s.toString().length() == 14);
        }
    }
}
