package itmediaengineering.duksung.ootd.login.presenter;

import itmediaengineering.duksung.ootd.data.User;

public interface LoginContract {
    interface View {
        void toast(String msg);
        void startIntroActivity();
        void startMainActivity(int code);//, UserResponse response);
        void onUnauthorizedError();
        void onUnknownError();
        void connectFail();
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void login(String userId);
    }
}
