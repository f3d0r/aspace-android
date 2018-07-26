package aspace.trya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

import aspace.trya.fragments.PhoneConfirmationFragment;
import aspace.trya.fragments.PhoneLoginFragment;
import aspace.trya.misc.OnApplicationStateListener;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements OnApplicationStateListener {

    private HashMap<String, String> applicationState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applicationState = new HashMap<String, String>();

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.main_container, new PhoneLoginFragment());
        ft.commit();
    }

    @Override
    public void phoneLoginToConfirm(String phoneNumber, String deviceId, boolean onboard) {
        applicationState.put("LOGIN_PHONE_NUMBER", phoneNumber);
        applicationState.put("DEVICE_ID", deviceId);
        applicationState.put("ONBOARD", onboard + "");

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        PhoneConfirmationFragment phoneConfirmationFragment = new PhoneConfirmationFragment();

        Bundle data = new Bundle();
        data.putString("LOGIN_PHONE_NUMBER", applicationState.get("LOGIN_PHONE_NUMBER"));
        data.putString("DEVICE_ID", applicationState.get("DEVICE_ID"));
        data.putBoolean("ONBOARD", onboard);

        phoneConfirmationFragment.setArguments(data);
        ft.replace(R.id.main_container, phoneConfirmationFragment);
        ft.commit();
    }

    @Override
    public void pinExpired() {
        applicationState.remove("LOGIN_PHONE_NUMBER");
        applicationState.remove("DEVICE_ID");
        applicationState.remove("ONBOARD");

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);

        ft.replace(R.id.main_container, new PhoneLoginFragment());
        ft.commit();
    }

    @Override
    public void continueFromLogin(String accessCode) {
        applicationState.put("ACCESS_CODE", accessCode);
        if (applicationState.get("ONBOARD").equals("true")) {
            Timber.e("SHOULD BE ONBOARDING!");
            startActivity(new Intent(MainActivity.this, OnboardingActivity.class));
        } else {
//            startActivity(new Intent(MapActivity.this, OnboardingActivity.class));
        }
        finish();
    }
}
