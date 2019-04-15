package itmediaengineering.duksung.ootd.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceManager {
    private static String url = "우리 서버 url";
    private static Retrofit retrofit = null;
    private static RetrofitService retrofitService;

    // called every time while " setting up a Retrofit interface "
    public static RetrofitService getRetrofitInstance() {
        if (retrofitService == null) {
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
            return retrofitService = retrofit.create(RetrofitService.class);
        }
        return retrofitService;
    }
}
