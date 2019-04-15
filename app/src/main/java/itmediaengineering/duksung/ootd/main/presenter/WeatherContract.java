package itmediaengineering.duksung.ootd.main.presenter;

import itmediaengineering.duksung.ootd.data.weather.Item;

public interface WeatherContract {
    interface View {
        void toast(String msg);
        void onUnauthorizedError();
        void onUnknownError();
        void onSuccessGetWeather(Item itme);
        void onConnectFail();
        //void startDetailActivity(Data item);
        void onNotFound();
    }

    interface Presenter {
        void getWeather(String baseDate, String baseTime, String nx, String ny);
        void attachView(View view);
        void detachView();
    }
}
