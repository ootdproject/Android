package itmediaengineering.duksung.ootd.retrofit;

import itmediaengineering.duksung.ootd.location.LocationResponse;
import itmediaengineering.duksung.ootd.weather.WeatherResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface WeatherApi {
    String WEATHER_SUB_URL = "service/SecndSrtpdFrcstInfoService2/ForecastGrib";

    @GET(WEATHER_SUB_URL)
    Call<WeatherResponse> getNowWeather(
            @Query(value = "ServiceKey", encoded = true)String serviceKey,
            @Query("base_date")String baseDate,
            @Query("base_time")String baseTime,
            @Query("nx")String nx,
            @Query("ny")String ny,
            @Query("_type")String type
    );

    /*@POST(SERVER_URL)
    @Multipart
    Call<Void> insertPost(
            @Header("Authorization") String authorization,
            @Part MultipartBody.Part image
            //@Body JsonObject party
    );

    @POST(SERVER_URL)
    Call<Void> updatePost(
            @Header("Authorization") String authorization
            //@Body JsonObject party
    );
*/
    /*@POST("api/users/validate/email/")
    Call<Void> validateEmail(
            @Body JsonObject email
    );

    @POST("api/users/validate/username/")
    Call<Void> validateNickname(
            @Body JsonObject username
    );

    @POST("/api/users/")
    Call<Void> insertUser(
            @Body JsonObject userData
    );

    @POST("/api/users/login/")
    Call<UserResponse> login(
            @Body JsonObject loginData
    );*/


    /*@POST("/api/parties/")
    Call<Void> insertParty(
            @Header("Authorization") String authorization,
            @Body JsonObject party
    );

    @GET("/api/parties/{slug}/")
    Call<PartyOneResponse> getParty(
            @Header("Authorization") String authorization,
            @Path("slug") String slug
    );

    @DELETE("/api/parties/{slug}/")
    Call<Void> deleteParty(
            @Header("Authorization") String authorization,
            @Path("slug") String slug
    );

    @GET("/api/parties/created/")
    Call<PartyResponse> getCreatedParties(
            @Header("Authorization") String authorization,
            @Query("page") int page
    );

    @GET("/api/parties/joined/")
    Call<PartyResponse> getJoinedParties(
            @Header("Authorization") String authorization,
            @Query("page") int page
    );

    @Multipart
    @POST("/api/profiles/")
    Call<UserResponse> updateUser(
            @Header("Authorization") String authorization,
            @Part MultipartBody.Part image,
            @Part("username") RequestBody username
    );

    @PUT("/api/parties/{slug}/")
    Call<PartyOneResponse> editParty(
            @Header("Authorization") String authorization,
            @Path("slug") String slug,
            @Body JsonObject party
    );

    @GET("/api/parties/{slug}/comments/")
    Call<ReplyResponse> getComments(
            @Header("Authorization") String authorization,
            @Path("slug") String slug
    );

    @POST("/api/parties/{slug}/comments/")
    Call<Void> sendComment(
            @Header("Authorization") String authorization,
            @Path("slug") String slug,
            @Body JsonObject data
    );

    @PUT("/api/parties/{slug}/comments/{comment_slug}/")
    Call<Void> updateComment(
            @Header("Authorization") String authorization,
            @Path("slug") String slug,
            @Path("comment_slug") String commentSlug,
            @Body JsonObject comment
    );

    @DELETE("/api/parties/{slug}/comments/{comment_slug}/")
    Call<Void> deleteComment(
            @Header("Authorization") String authorization,
            @Path("slug") String slug,
            @Path("comment_slug") String commentSlug
    );

    @GET("/api/parties/{slug}/participants/")
    Call<ParticipantResponse> getParticipants(
            @Header("Authorization") String authorization,
            @Path("slug") String slug
    );

    @POST("/api/parties/{slug}/participants/")
    Call<ParticipantResponse> joinParty(
            @Header("Authorization") String authorization,
            @Path("slug") String slug
    );

    @DELETE("/api/parties/{slug}/participants/")
    Call<ParticipantResponse> leaveParty(
            @Header("Authorization") String authorization,
            @Path("slug") String slug
    );

    @GET("/api/parties/{slug}/owner/")
    Call<OwnerResponse> getOwner(
            @Header("Authorization") String authorization,
            @Path("slug") String slug
    );

    @PUT("/api/parties/{slug}/owner/")
    Call<Void> updateOwner(
            @Header("Authorization") String authorization,
            @Path("slug") String slug,
            @Body JsonObject owner
    );

    @GET("/api/notifications/")
    Call<HistoryResponse> getHistory(
            @Header("Authorization") String authorization
    );*/
}
