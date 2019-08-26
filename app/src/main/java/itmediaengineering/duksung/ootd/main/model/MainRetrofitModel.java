package itmediaengineering.duksung.ootd.main.model;

import java.util.List;

import itmediaengineering.duksung.ootd.data.location.Document;
import itmediaengineering.duksung.ootd.data.location.LocationResponse;
import itmediaengineering.duksung.ootd.retrofit.LocationApi;
import itmediaengineering.duksung.ootd.retrofit.LocationApiManager;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;
import itmediaengineering.duksung.ootd.retrofit.RetrofitService;
import itmediaengineering.duksung.ootd.retrofit.RetrofitServiceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRetrofitModel {
    private MainCallback.RetrofitCallback callback;
    private LocationApi locationApi;
    //private WeatherApi weatherApi;
    private String APP_KEY = "KakaoAK 8aa198813b232314bb0c0689b81ba6eb";

    private String TYPE = "json";

    public MainRetrofitModel(){
        locationApi = LocationApiManager.getLocationRetrofitInstance();
        //weatherApi = WeatherApiManager.getWeatherRetrofitInstance();
    }

    public void setCallback(MainCallback.RetrofitCallback callback){
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
//
//                 if (response.code() == ResponseCode.UNAUTHORIZED) {
//                    callback.onSuccess(ResponseCode.UNAUTHORIZED, null);
//                    return;
//                }
                // 날씨 정보도 요청
                List<Document> documents = response.body().getDocuments();
                /*if(documents != null) {
                    getWeather(nx, ny, documents);
                }*/
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
