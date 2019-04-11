package itmediaengineering.duksung.ootd.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationApiManager {
    private static String url = "https://dapi.kakao.com/";
    private static Retrofit retrofit = null;
    private static LocationApi locationApi;

    // called every time while " setting up a Retrofit interface "
    public static LocationApi getLocationRetrofitInstance() {
        if (locationApi == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
            OkHttpClient okHttpClient = builder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            // create an instance of our WeatherApi interface.
            return locationApi = retrofit.create(LocationApi.class);
        }
        return locationApi;
    }

}
