package itmediaengineering.duksung.ootd.retrofit;

import com.google.gson.JsonObject;

import java.util.List;

import itmediaengineering.duksung.ootd.data.ResponseAuth;
import itmediaengineering.duksung.ootd.data.User;
import itmediaengineering.duksung.ootd.data.post.Post;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {
    @POST("/members")
    Call<User> createUser(
            @Body JsonObject userData
    );

    @POST("/members/login")
    Call<ResponseAuth> login(
            @Query("providerUserId") String token
    );

    @GET("/posts")
    Call<List<Post>> getPosts(
            @Header("Authorization") String authorization,
            @Query("page") int pageNum,
            @Query("sort") String sort,
            @Query("sale") boolean sale
    );

    @GET("/posts")
    Call<List<Post>> getMyPickPosts(
            @Header("Authorization") String authorization,
            @Query("onlyLike") boolean isPick,
            @Header("x-providerUserId") String pUid,
            @Query("size") int size);

    @GET("/posts")
    Call<List<Post>> getGuPosts(
            @Header("Authorization") String authorization,
            @Query("q") String gu,
            @Query("page") int pageNum,
            @Query("sort") String sort,
            @Query("size") int pageSize);

    @POST("/posts/{postId}/like")
    Call<Post> likePost(
            @Header("Authorization") String authorization,
            @Path("postId") int postId,
            @Header("x-providerUserId") String pUid
    );

    @POST("/posts/{postId}/unlike")
    Call<Post> unlikePost(
            @Header("Authorization") String authorization,
            @Path("postId") int postId,
            @Header("x-providerUserId") String pUid
    );

    @GET("/posts")
    Call<List<Post>> getRecommendGuPosts(
            @Header("Authorization") String authorization,
            @Query("q") String gu,
            @Query("page") int pageNum,
            @Query("sort") String sort,
            @Query("size") int size
    );

    @GET("/posts")
    Call<List<Post>> getColorQueryPosts(
            @Header("Authorization") String authorization,
            @Query("color") String color,
            @Query("page") int pageNum,
            @Query("sort") String sort,
            @Query("size") int size
    );

    @GET("/posts")
    Call<List<Post>> getCategoryAndColorQueryPosts(
            @Header("Authorization") String authorization,
            @Query("categoryB") String category,
            @Query("color") String color,
            @Query("page") int pageNum,
            @Query("sort") String sort,
            @Query("size") int size
    );

    @GET("/posts/categorymaxlike")
    Call<List<Post>> getCategoryAPosts(
            @Header("Authorization") String authorization,
            @Query("category") String category,
            @Query("page") int pageNum,
            @Query("sort") String sort,
            @Query("size") int size
    );

    @GET("/posts/categorymaxlike2")
    Call<List<Post>> getCategoryBPosts(
            @Header("Authorization") String authorization,
            @Query("category") String category,
            @Query("page") int pageNum,
            @Query("sort") String sort,
            @Query("size") int size
    );

    @POST("/posts/{postId}")
    Call<Void> editPost(
            @Header("Authorization") String authorization,
            @Path("postId") int postId,
            @Body JsonObject postRequest,
            @Header("x-providerUserId") String pUid
    );

    @Multipart
    @POST("/posts")
    Call<ResponseBody> createPost(
            @Header("Authorization") String auth,
            @Header("x-providerUserId") String pUid,
            @Part MultipartBody.Part file,
            //@Part("postRequest") RequestBody postRequest
            @Part MultipartBody.Part postRequest
            //@PartMap Map<String, RequestBody> postRequest,
    );

    @GET("/myposts")
    Call<List<Post>> getMyPosts(
            @Header("Authorization") String authorization,
            @Header("x-providerUserId") String pUid,
            @Query ("sale") Boolean isSale
    );

    @DELETE("/posts/{postId}/delete")
    Call<Void> deleteMyPost(
            @Header("Authorization") String authorization,
            @Path("postId") int postId,
            @Header("x-providerUserId") String pUid
    );

    /*@GET(FLICKR_SUB_URL)
    Call<GalleryResponse> getMyPosts(
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
