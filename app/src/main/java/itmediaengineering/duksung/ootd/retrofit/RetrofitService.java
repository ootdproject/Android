package itmediaengineering.duksung.ootd.retrofit;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import itmediaengineering.duksung.ootd.data.mygallery.GalleryResponse;
import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.data.post.PostRequest;
import itmediaengineering.duksung.ootd.data.weather.WeatherResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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
            @Query("providerUserId") String token
    );

    @GET("/posts")
    Call<List<Post>> getPosts(
            @Header("Authorization") String authorization
    );

    @Multipart
    @POST("/posts")
    Call<Post> createPost(
            @Header("Authorization") String auth,
            @Part MultipartBody.Part file,
            @Part MultipartBody.Part postRequest,
            //@PartMap Map<String, RequestBody> postRequest,
            @Header("x-providerUserId") String pUid
    );

    @GET("/myposts")
    Call<List<Post>> getMyPosts(
            @Header("Authorization") String authorization,
            @Header("x-providerUserId") String pUid
    );
    /*@GET(FLICKR_SUB_URL)
    Call<GalleryResponse> getGallery(
            @Query("api_key") String key,
            @Query ("method") String method,
            @Query("format") String format,
            @Query("nojsoncallback") String jsoncallback,
            @Query("extras") String extras
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
