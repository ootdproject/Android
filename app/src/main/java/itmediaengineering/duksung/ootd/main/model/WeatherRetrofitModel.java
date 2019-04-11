package itmediaengineering.duksung.ootd.main.model;

import android.util.Log;

import java.util.List;

import itmediaengineering.duksung.ootd.retrofit.ResponseCode;
import itmediaengineering.duksung.ootd.retrofit.WeatherApi;
import itmediaengineering.duksung.ootd.retrofit.WeatherApiManager;
import itmediaengineering.duksung.ootd.weather.Item;
import itmediaengineering.duksung.ootd.weather.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRetrofitModel {
    private WeatherCallback.RetrofitCallback callback;
    private WeatherApi weatherApi;
    private String SERVICE_KEY = "ze8%2F5it%2Bz8c4W0AzgWV%2FkpD%2FNgJfqxfteF0desEMVJgnSTSKmeJbE3J5B2ExHUz8YSMYkEff%2FeKUt0FD7XO%2FWg%3D%3D";
    private String TYPE = "json";


    public WeatherRetrofitModel(){
        weatherApi = WeatherApiManager.getWeatherRetrofitInstance();
    }

    public void setCallback(WeatherCallback.RetrofitCallback callback){
        this.callback = callback;
    }

    /*@Query("ServiceKey")String serviceKey,
    @Query("base_date")String baseDate,
    @Query("base_time")String baseTime,
    @Query("nx")String nx,
    @Query("ny")String ny,
    @Query("numOfRows")String numOfRows,
    @Query("pageNo")String pageNo,
    @Query("_type")String type*/

    public void getWeather(String baseDate, String baseTime, String nx, String ny){
        Call<WeatherResponse> call = weatherApi.getNowWeather(SERVICE_KEY, baseDate, baseTime, nx, ny,TYPE);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {

                // 아직 응답 종류 파악 못했음..
                if (response.body().getResponse().getHeader().getResultCode() == ResponseCode.PARAMETER_ERROR) {
//                    callback.onSuccess(ResponseCode.PARAMETER_ERROR, null);
                    Log.d("Parameter Error", response.body().getResponse().getHeader().getResultMsg());
                    return;
                }
//                if (response.code() == ResponseCode.UNAUTHORIZED) {
//                    callback.onSuccess(ResponseCode.UNAUTHORIZED, null);
//                    return;
//                }
                List<Item> items = response.body().getResponse().getBody().getItems().getItem();
                callback.onSuccess(ResponseCode.SUCCESS, items);
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure();
            }
        });
    }
}
