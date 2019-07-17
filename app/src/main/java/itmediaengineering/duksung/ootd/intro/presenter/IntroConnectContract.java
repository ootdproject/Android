package itmediaengineering.duksung.ootd.intro.presenter;

import itmediaengineering.duksung.ootd.data.User;

public interface IntroConnectContract {
    interface View {
        void toast(String msg);
        void startMainActivity(int code);//, UserResponse response);
        void connectFail();
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void join(User user);
    }
}
