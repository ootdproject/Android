package itmediaengineering.duksung.ootd.login.model;

import itmediaengineering.duksung.ootd.data.ResponseAuth;

public interface LoginCallback {
    interface RetrofitCallback {
        void onSuccess(int code, ResponseAuth responseAuth);//, UserResponse response);
        void onFailure();
    }
}
