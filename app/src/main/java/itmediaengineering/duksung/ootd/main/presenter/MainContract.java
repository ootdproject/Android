package itmediaengineering.duksung.ootd.main.presenter;

import itmediaengineering.duksung.ootd.data.location.Document;
import itmediaengineering.duksung.ootd.data.weather.Item;

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
        void getData(String x, String y);
        //void getWeather(String baseDate, String baseTime, String nx, String ny);
        void attachView(View view);
        void detachView();
    }
}
