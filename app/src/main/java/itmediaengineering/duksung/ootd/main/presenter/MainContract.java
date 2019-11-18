package itmediaengineering.duksung.ootd.main.presenter;

import itmediaengineering.duksung.ootd.data.location.Document;

public interface MainContract {
    interface View {
        void toast(String msg);
        void onUnauthorizedError();
        void onUnknownError();
        void onSuccessGetLocation(Document document);
        void onConnectFail();
        //void startPostDetailActivity(Data item);
        void onNotFound();
    }

    interface Presenter {
        void getLocation(double x, double y);
        //void getWeather(String baseDate, String baseTime, String nx, String ny);
        void attachView(View view);
        void detachView();
    }
}
