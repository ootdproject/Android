package itmediaengineering.duksung.ootd.main.model;

import android.util.Log;

import java.util.List;

import itmediaengineering.duksung.ootd.location.Document;
import itmediaengineering.duksung.ootd.location.LocationResponse;
import itmediaengineering.duksung.ootd.retrofit.LocationApi;
import itmediaengineering.duksung.ootd.retrofit.LocationApiManager;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;
import itmediaengineering.duksung.ootd.retrofit.WeatherApi;
import itmediaengineering.duksung.ootd.retrofit.WeatherApiManager;
import itmediaengineering.duksung.ootd.weather.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationRetrofitModel {
    private LocationCallback.RetrofitCallback callback;
    private LocationApi locationApi;
    private String APP_KEY = "KakaoAK 8aa198813b232314bb0c0689b81ba6eb";

    public LocationRetrofitModel(){
        locationApi = LocationApiManager.getLocationRetrofitInstance();
    }

    public void setCallback(LocationCallback.RetrofitCallback callback){
        this.callback = callback;
    }

    public void getLocation(String x, String y){
        Call<LocationResponse> call = locationApi.getLocation(APP_KEY, x, y);
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
                callback.onSuccess(ResponseCode.SUCCESS, documents);
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure();
            }
        });
    }
}
