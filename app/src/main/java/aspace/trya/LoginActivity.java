package aspace.trya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import aspace.trya.fragments.LoginPhoneFragment;
import aspace.trya.fragments.LoginPinFragment;
import aspace.trya.listeners.OnApplicationStateListener;
import aspace.trya.misc.ApplicationState;
import aspace.trya.models.AccessCode;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements OnApplicationStateListener {

    ApplicationState applicationStateModifier;
    private HashMap<String, String> applicationState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applicationState = new HashMap<>();
        applicationStateModifier = new ApplicationState(LoginActivity.this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_container, new LoginPhoneFragment());
        ft.commit();
    }

    @Override
    public void phoneLoginToConfirm(String phoneNumber, String deviceId, boolean onboard) {
        applicationState.put("LOGIN_PHONE_NUMBER", phoneNumber);
        applicationState.put("DEVICE_ID", deviceId);
        applicationState.put("ONBOARD", onboard + "");

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        LoginPinFragment loginPinFragment = new LoginPinFragment();

        Bundle data = new Bundle();
        data.putString("LOGIN_PHONE_NUMBER", applicationState.get("LOGIN_PHONE_NUMBER"));
        data.putString("DEVICE_ID", applicationState.get("DEVICE_ID"));
        data.putBoolean("ONBOARD", onboard);

        loginPinFragment.setArguments(data);
        ft.replace(R.id.main_container, loginPinFragment);
        ft.commit();
    }

    @Override
    public void pinExpired() {
        applicationStateModifier.logout();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);

        ft.replace(R.id.main_container, new LoginPhoneFragment());
        ft.commit();
    }

    @Override
    public void skipLogin(String deviceId) {
        applicationState.put("DEVICE_ID", deviceId);
        startActivity(new Intent(LoginActivity.this, MapActivity.class));
        finish();
    }

    @Override
    public void continueFromLogin(AccessCode accessCode) {
        applicationStateModifier.login(applicationState.get("LOGIN_PHONE_NUMBER"), accessCode,
            applicationState.get("DEVICE_ID"));
        if (applicationState.get("ONBOARD").equals("true")) {
            startActivity(new Intent(LoginActivity.this, OnboardingActivity.class));
        } else {
            startActivity(new Intent(LoginActivity.this, MapActivity.class));
        }
        finish();
    }
}
