package aspace.trya.models;

public final class ResponseCodes {
    public static final int INVALID_PIN = 4;
    public static final int INVALID_PHONE = 12;
    public static final int INVALID_AUTH_KEY = 13;
    public static final int EXPIRED_PIN = 23;

    public static final int SUCCESS_NEW_PHONE = 1;
    public static final int SUCCESS_RETURN_PHONE = 2;
    public static final int SUCCESS_NEW_ACCESS_CODE = 22;

    private ResponseCodes() {
        // restrict instantiation
    }
}