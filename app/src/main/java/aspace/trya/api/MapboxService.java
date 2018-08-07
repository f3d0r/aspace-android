package aspace.trya.api;

import aspace.trya.geojson.GeoJSON;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MapboxService {
    // SearchResults
    @GET("geocoding/v5/mapbox.places/{search}")
    Call<GeoJSON> getSearchSuggestions(@Path("search") String searchQuery, @Query("proximity") String lngLat, @Query("country") String country, @Query("access_token") String accessToken, @Query("limit") int searchLimit);
}
