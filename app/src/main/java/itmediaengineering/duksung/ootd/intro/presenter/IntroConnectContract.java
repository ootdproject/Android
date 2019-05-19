package itmediaengineering.duksung.ootd.intro.presenter;

public interface IntroConnectContract {
    interface View {
        void toast(String msg);
        void startMainActivity(int code);//, UserResponse response);
        void connectFail();
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void login(String uId, String email, String password);
    }
}
