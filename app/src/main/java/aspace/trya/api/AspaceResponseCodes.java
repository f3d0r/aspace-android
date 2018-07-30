package aspace.trya.api;

public final class AspaceResponseCodes {
    public static final int INVALID_PIN = 4;
    public static final int INVALID_PHONE = 12;
    public static final int PIN_EXPIRED = 23;

    public static final int NEW_PHONE = 1;
    public static final int RETURNING_PHONE = 2;
    public static final int NEW_ACCESS_CODE = 22;

    private AspaceResponseCodes() {
        // Restrict instantiation
    }
}