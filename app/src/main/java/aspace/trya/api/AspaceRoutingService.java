package aspace.trya.api;

import aspace.trya.models.RouteOptionsResponse;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AspaceRoutingService {

    // Routing Endpoints
    @GET("ping")
    Call<RouteOptionsResponse> ping();

    @POST("get_drive_bike_route")
    Observable<RouteOptionsResponse> getDriveBikeRoute(@Query("origin_lat") double originLat,
        @Query("origin_lng") double originLng, @Query("dest_lat") double destLat,
        @Query("dest_lng") double destLng, @Query("session_starting") int sessionStarting,
        @Query("access_code") String accessCode, @Query("device_id") String deviceId);

    @POST("get_drive_walk_route")
    Observable<RouteOptionsResponse> getDriveWalkWaypoints(@Query("origin_lat") double originLat,
        @Query("origin_lng") double originLng, @Query("dest_lat") double destLat,
        @Query("dest_lng") double destLng, @Query("session_starting") int sessionStarting,
        @Query("access_code") String accessCode, @Query("device_id") String deviceId);

    @POST("get_drive_direct_route")
    Observable<RouteOptionsResponse> getDriveDirectRoute(@Query("origin_lat") double originLat,
        @Query("origin_lng") double originLng, @Query("dest_lat") double destLat,
        @Query("dest_lng") double destLng, @Query("session_starting") int sessionStarting,
        @Query("access_code") String accessCode, @Query("device_id") String deviceId);

    @POST("get_drive_direct_route")
    Call<RouteOptionsResponse> testReq(@Query("origin_lat") double originLat,
        @Query("origin_lng") double originLng, @Query("dest_lat") double destLat,
        @Query("dest_lng") double destLng, @Query("session_starting") int sessionStarting,
        @Query("access_code") String accessCode, @Query("device_id") String deviceId);

}
