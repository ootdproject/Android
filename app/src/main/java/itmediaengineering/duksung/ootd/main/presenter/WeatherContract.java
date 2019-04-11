package itmediaengineering.duksung.ootd.main.presenter;

public interface WeatherContract {
    interface View {
        void toast(String msg);
        void onUnauthorizedError();
        void onUnknownError();
        void onSuccessGetWeather();
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
