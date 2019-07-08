package itmediaengineering.duksung.ootd.retrofit;

import com.google.gson.JsonObject;

import itmediaengineering.duksung.ootd.data.mygallery.GalleryResponse;
import itmediaengineering.duksung.ootd.data.weather.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitService {
    //String FLICKR_SUB_URL = "/services/rest/";
    //String API_KEY = "b727dc9341180f8b23d6b4a3043f687e";

    @POST("/members")
    Call<Void> createUser(
            @Body JsonObject userData
    );

    @POST("/members/login")
    Call<Void> login(
            @Query("requestToken") String token
    );

    /*@GET(FLICKR_SUB_URL)
    Call<GalleryResponse> getGallery(
            @Query("api_key") String key,
            @Query ("method") String method,
            @Query("format") String format,
            @Query("nojsoncallback") String jsoncallback,
            @Query("extras") String extras
    );*/

    /*@GET("api/posts")
    Call<Void> getPosts(
            @Header("Authorization") String authorization,
            @Query("search") String search,
            @Query("page") int page
    );*/

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
