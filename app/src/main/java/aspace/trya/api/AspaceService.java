package aspace.trya.api;

import aspace.trya.bodyobjects.BoundingBox;
import aspace.trya.bodyobjects.LngLat;
import aspace.trya.bodyobjects.WaypointSearch;
import aspace.trya.models.AuthResponse;
import aspace.trya.models.CodeResponse;
import aspace.trya.models.ParkingResponse;
import aspace.trya.models.RoutingResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AspaceService {
    // Authentication Endpoints
    @GET("auth/ping")
    Call<AuthResponse> authPing();

    @POST("auth/phone_login")
    Call<AuthResponse> phoneLogin(@Query("phone_number") String phoneNumber, @Query("device_id") String deviceId, @Query("call_verify") String placeCall);

    @POST("auth/check_pin")
    Call<AuthResponse> checkPin(@Query("phone_number") String phoneNumber, @Query("device_id") String deviceID, @Query("verify_pin") String verifyPIN);

    @GET("user/check_auth")
    Call<CodeResponse> checkAuth(@Query("access_code") String accessCode, @Query("device_id") String deviceID);

    //Parking Endpoints
    @GET("parking/ping")
    Call<ParkingResponse> parkingPing();

    @GET("parking/get_status")
    Call<ParkingResponse> getStatusByID(@Query("spot_id") String spotID, @Query("block_id") String blockID);

    @POST("parking/get_status_bbox")
    Call<ParkingResponse> getStatusByBbox(@Body BoundingBox boundingBox);

    @POST("parking/get_status_radius")
    Call<ParkingResponse> getStatusByRadius(@Body LngLat lngLat, @Query("radius_feet") double radiusFeet);

    @POST("parking/get_min_size_parking")
    Call<ParkingResponse> getMinSizedParkingRadius(@Body LngLat lngLat, @Query("radius_feet") double radiusFeet, @Query("spot_size_feet") double spotSizeFeet);

    //Routing Endpoints
    @POST("routing/get_route_waypoints")
    Call<RoutingResponse> getRouteWaypoints(@Body WaypointSearch waypointSearch);

}
