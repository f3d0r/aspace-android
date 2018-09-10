package aspace.trya.api;

public final class AspaceResponseCodes {

    public static final int VALID_OR_MISSING_OUTPUT_TYPE = -7;
    public static final int INVALID_PARAMETER = -6;
    public static final int MULTI_PART_BODY_MISSING = -5;
    public static final int EXPIRED_ACCESS_CODE = -3;
    public static final int INVALID_ACCESS_CODE = -2;
    public static final int MISSING_PARAMETER = -1;

    public static final int NEW_PHONE = 1;
    public static final int RETURNING_PHONE = 2;
    public static final int INVALID_PIN = 4;
    public static final int INVALID_PHONE = 12;
    public static final int INVALID_AUTH_KEY = 13;
    public static final int INVALID_SPOT_ID = 15;
    public static final int INVALID_BASIC_AUTH = 16;
    public static final int AUTH_KEY_ADDED = 17;
    public static final int SPOT_STATUS_CHANGED = 19;
    public static final int MISSING_BODY = 20;
    public static final int INVALID_STATUS_ENTERED = 21;
    public static final int NEW_ACCESS_CODE = 22;
    public static final int PIN_EXPIRED = 23;
    public static final int INVALID_BLOCK_ID = 24;
    public static final int INVALID_BLOCK_ID_OR_SPOT_ID = 25;
    public static final int INVALID_PERMISSION = 26;
    public static final int ROUTE_CALCULATION_ERROR = 27;
    public static final int MAIN_ENDPOINT_FUNCTION_SUCCESS = 30;
    public static final int ROUTING_ENDPOINT_FUNCTION_SUCCESS = 31;
    public static final int ADMIN_ENDPOINT_FUNCTION_SUCCESS = 32;
    public static final int AUTH_ENDPOINT_FUNCTION_SUCCESS = 33;
    public static final int PARKING_ENDPOINT_FUNCTION_SUCCESS = 34;
    public static final int USER_ENDPOINT_FUNCTION_SUCCESS = 35;
    public static final int PROFILE_PIC_EXISTS = 36;
    public static final int PROFILE_PIC_NULL = 37;
    public static final int PROFILE_PIC_UPDATED = 38;
    public static final int INVALID_VEHICLE_ID = 39;
    public static final int VALID_ACCESS_CODE = 40;
    public static final int BIKES_ENDPOINT_FUNCTION_SUCCESS = 41;

    private AspaceResponseCodes() {
        // Restrict instantiation
    }
}