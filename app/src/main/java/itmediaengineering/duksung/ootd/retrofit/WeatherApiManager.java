package itmediaengineering.duksung.ootd.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApiManager {
    private static String url = "http://newsky2.kma.go.kr";
    private static Retrofit retrofit;
    private static WeatherApi weatherApi;

    public static WeatherApi getInstance() {
        if (weatherApi == null) {
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
            return weatherApi = retrofit.create(WeatherApi.class);
        }
        return weatherApi;
    }
}
