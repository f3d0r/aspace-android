package aspace.trya.api;

import aspace.trya.models.geojson.GeoJSON;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MapboxService {
    // SearchResults
    @GET("geocoding/v5/mapbox.places/{search}")
    Call<GeoJSON> getSearchSuggestions(@Path("search") String searchQuery, @Query("proximity") RetrofitLatLng latLng, @Query("country") String country, @Query("access_token") String accessToken, @Query("limit") int searchLimit);

    @GET("directions/v5/mapbox/{profile}/{coordinates}?annotations=duration,distance,speed,congestion&banner_instructions=true&geometries=polyline6&language=en&overview=full&roundabout_exits=true&steps=true&voice_instructions=true&voice_units=imperial")
    Call<GeoJSON> getDirections(@Path("profile") String profileType, @Path("coordinates") RouteCoordinates routeCoordinates, @Query("waypoint_names") WaypointNames waypointNames, @Query("access_token") String accessToken);

}
