package itmediaengineering.duksung.ootd.retrofit;

import itmediaengineering.duksung.ootd.data.location.LocationResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface LocationApi {
    String LOCATION_SUB_URL = "v2/local/geo/coord2regioncode.json";

    @GET(LOCATION_SUB_URL)
    Call<LocationResponse> getLocation(
        @Header("Authorization")String appKey,
        @Query("x")String x,
        @Query("y")String y
    );
}
