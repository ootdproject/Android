package itmediaengineering.duksung.ootd.intro.model;

public interface IntroConnectCallback {
    interface RetrofitCallback {
        void onSuccess(int code);//, UserResponse response);
        void onFailure();
    }
}
