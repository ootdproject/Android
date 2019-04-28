package itmediaengineering.duksung.ootd.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApiManager {
    private static String url = "http://newsky2.kma.go.kr/";
    private static Retrofit retrofit = null;
    private static WeatherApi weatherApi;

    // called every time while " setting up a Retrofit interface "
    public static WeatherApi getWeatherRetrofitInstance() {
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

/*public class OpenWeatherMapClient {
    private static final String BASE_URL = "http://api.openweathermap.org";
    private static Retrofit retrofit = null;
    public static Retrofit getClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}*/