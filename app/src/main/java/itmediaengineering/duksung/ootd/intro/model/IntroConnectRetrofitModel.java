package itmediaengineering.duksung.ootd.intro.model;

import itmediaengineering.duksung.ootd.retrofit.RetrofitService;
import itmediaengineering.duksung.ootd.retrofit.RetrofitServiceManager;

public class IntroConnectRetrofitModel {
    private static final String TAG = IntroConnectRetrofitModel.class.getSimpleName();
    private IntroConnectCallback.RetrofitCallback callback;
    private RetrofitService retrofitService;

    public IntroConnectRetrofitModel() {
        //retrofitService = RetrofitServiceManager.getInstance();
    }

    public void setCallback(IntroConnectCallback.RetrofitCallback callback) {
        this.callback = callback;
    }

    public void login(String email, String password) {}

}
