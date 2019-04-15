package itmediaengineering.duksung.ootd.main.presenter;

import itmediaengineering.duksung.ootd.data.location.Document;

public interface LocationContract {
    interface View {
        void toast(String msg);
        void onUnauthorizedError();
        void onUnknownError();
        void onSuccessGetLocation(Document document);
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
