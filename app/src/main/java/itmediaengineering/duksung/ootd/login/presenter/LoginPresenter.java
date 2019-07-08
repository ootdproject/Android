package itmediaengineering.duksung.ootd.login.presenter;


import android.util.Log;

import java.util.ArrayList;

import itmediaengineering.duksung.ootd.login.model.LoginCallback;
import itmediaengineering.duksung.ootd.login.model.LoginRetrofitModel;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;

public class LoginPresenter
        implements LoginContract.Presenter, LoginCallback.RetrofitCallback {

    private LoginContract.View view;
    private LoginRetrofitModel retrofitModel;

    public LoginPresenter() {
        retrofitModel = new LoginRetrofitModel();
        retrofitModel.setCallback(this);
    }

    @Override
    public void login(String userId) {
        retrofitModel.login(userId);
    }

    @Override
    public void onSuccess(int code) {
        if (code == ResponseCode.NOT_FOUND) {
            view.startIntroActivity();
            return;
        }

        if (code == ResponseCode.BAD_REQUEST) {
            view.onUnauthorizedError();
            return;
        }

        if (code == ResponseCode.SUCCESS) {
            view.startMainActivity(code);
            return;
        }
        view.onUnknownError();
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void attachView(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}
