package itmediaengineering.duksung.ootd.retrofit;

import itmediaengineering.duksung.ootd.data.weather.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("api/posts")
    Call<Void> getPosts(
            @Header("Authorization") String authorization,
            @Query("search") String search,
            @Query("page") int page
    );

    /*@GET("service/SecndSrtpFrcstInfoService2/ForecastGridb")
    Call<WeatherResponse> getNowWeather(
            @Query(value = "ServiceKey", encoded = true)String serviceKey,
            @Query("base_date")String baseDate,
            @Query("base_time")String baseTime,
            @Query("nx")String nx,
            @Query("ny")String ny,
            @Query("_type")String type
    );*/
}
