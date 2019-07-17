package itmediaengineering.duksung.ootd.main.tab.category.model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import itmediaengineering.duksung.ootd.api.WeatherGridChange;
import itmediaengineering.duksung.ootd.data.location.Document;
import itmediaengineering.duksung.ootd.data.location.LocationResponse;
import itmediaengineering.duksung.ootd.data.weather.Item;
import itmediaengineering.duksung.ootd.data.weather.WeatherResponse;
import itmediaengineering.duksung.ootd.retrofit.LocationApi;
import itmediaengineering.duksung.ootd.retrofit.LocationApiManager;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;
import itmediaengineering.duksung.ootd.retrofit.WeatherApi;
import itmediaengineering.duksung.ootd.retrofit.WeatherApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRetrofitModel {
    private CategoryCallback.RetrofitCallback callback;
    private LocationApi locationApi;
    private WeatherApi weatherApi;
    private String APP_KEY = "KakaoAK 8aa198813b232314bb0c0689b81ba6eb";
    private String SERVICE_KEY = "ze8%2F5it%2Bz8c4W0AzgWV%2FkpD%2FNgJfqxfteF0desEMVJgnSTSKmeJbE3J5B2ExHUz8YSMYkEff%2FeKUt0FD7XO%2FWg%3D%3D";
    private String TYPE = "json";

    public CategoryRetrofitModel(){
        locationApi = LocationApiManager.getLocationRetrofitInstance();
        weatherApi = WeatherApiManager.getWeatherRetrofitInstance();
    }

    public void setCallback(CategoryCallback.RetrofitCallback callback){
        this.callback = callback;
    }

    public void getLocation(String latitude, String longitude){
        final String nx = latitude;
        final String ny = longitude;
        Call<LocationResponse> call = locationApi.getLocation(APP_KEY, longitude, latitude);
        call.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {

                // 아직 응답 종류 파악 못했음..
                /*if (response.code() == ResponseCode.PARAMETER_ERROR) {
//                    callback.onSuccess(ResponseCode.PARAMETER_ERROR, null);
                    Log.d("Parameter Error", response.body().getResponse().getHeader().getResultMsg());
                    return;
                }*/
//                if (response.code() == ResponseCode.UNAUTHORIZED) {
//                    callback.onSuccess(ResponseCode.UNAUTHORIZED, null);
//                    return;
//                }
                List<Document> documents = response.body().getDocuments();
                if(documents != null) {
                    getWeather(nx, ny, documents);
                }
                //callback.onSuccess(ResponseCode.SUCCESS, documents);
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure();
            }
        });
    }

    public void getWeather(String latitude, String longitude, final List<Document> documents){
        Date today = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd kk:mm");
        String nowDate = date.format(today).split(" ")[0];
        String nowTime;
        String tmpTime1 = date.format(today).split(" ")[1].split(":")[0];
        String tmpTime2 = date.format(today).split(" ")[1].split(":")[1];
        if(Integer.valueOf(tmpTime2) < 30){
            int tmp = Integer.valueOf(tmpTime1)-1;
            if(tmp < 10){
                nowTime = "0" + String.valueOf(tmp) + "00";
            }else {
                nowTime = String.valueOf(tmp) + "00";
            }
        }else{
            nowTime = tmpTime1 + "00";
        }
        WeatherGridChange gridChange = new WeatherGridChange();
        WeatherGridChange.LatXLngY latXLngY = gridChange.convertGRID_GPS(0,
                Double.valueOf(latitude),
                Double.valueOf(longitude));

        Call<WeatherResponse> call = weatherApi.getNowWeather(
                SERVICE_KEY,
                nowDate,
                nowTime,
                String.valueOf(latXLngY.getX()),
                String.valueOf(latXLngY.getY()),
                TYPE);
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
                callback.onSuccess(ResponseCode.SUCCESS, items, documents);
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure();
            }
        });
    }
}
