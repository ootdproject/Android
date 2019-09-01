package itmediaengineering.duksung.ootd.intro.model;

public interface IntroConnectCallback {
    interface RetrofitCallback {
        void onSuccess(int code, String nickname);
        void onFailure();
    }
}
