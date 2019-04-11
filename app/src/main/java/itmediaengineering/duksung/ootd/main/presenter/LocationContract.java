package itmediaengineering.duksung.ootd.main.presenter;

public class LocationContract {
    interface View {
        void toast(String msg);
        void onUnauthorizedError();
        void onUnknownError();
        void onSuccessGetLocation();
        void onConnectFail();
        //void startDetailActivity(Data item);
        void onNotFound();
    }

    interface Presenter {
        void getLocation(String x, String y);
        void attachView(View view);
        void detachView();
    }
}
