package itmediaengineering.duksung.ootd.intro.model;

import itmediaengineering.duksung.ootd.data.ResponseAuth;

public interface IntroConnectCallback {
    interface RetrofitCallback {
        void onSuccess(int code, ResponseAuth responseAuth);//, UserResponse response);
        void onFailure();
    }
}
