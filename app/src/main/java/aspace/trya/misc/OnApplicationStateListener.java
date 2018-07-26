package aspace.trya.misc;

public interface OnApplicationStateListener {
    void phoneLoginToConfirm(String phoneNumber, String deviceId, boolean onboard);

    void pinExpired();

    void continueFromLogin(String accessCode);
}
