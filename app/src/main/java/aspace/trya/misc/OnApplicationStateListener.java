package aspace.trya.misc;

import aspace.trya.models.AccessCode;

public interface OnApplicationStateListener {
    void phoneLoginToConfirm(String phoneNumber, String deviceId, boolean onboard);

    void pinExpired();

    void continueFromLogin(AccessCode accessCode);
}
