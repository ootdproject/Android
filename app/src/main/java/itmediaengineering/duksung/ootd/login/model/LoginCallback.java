package itmediaengineering.duksung.ootd.login.model;

public interface LoginCallback {
    interface RetrofitCallback {
        void onSuccess(int code);//, UserResponse response);
        void onFailure();
    }
}
