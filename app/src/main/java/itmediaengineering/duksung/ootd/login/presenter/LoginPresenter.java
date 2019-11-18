package itmediaengineering.duksung.ootd.login.presenter;


import itmediaengineering.duksung.ootd.data.ResponseAuth;
import itmediaengineering.duksung.ootd.login.model.LoginCallback;
import itmediaengineering.duksung.ootd.login.model.LoginRetrofitModel;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;
import itmediaengineering.duksung.ootd.utils.PreferenceUtils;

public class LoginPresenter
        implements LoginContract.Presenter, LoginCallback.RetrofitCallback {

    private LoginContract.View view;
    private LoginRetrofitModel retrofitModel;

    public LoginPresenter() {
        retrofitModel = new LoginRetrofitModel();
        retrofitModel.setCallback(this);
    }

    @Override
    public void login(String providerUserId) {
        PreferenceUtils.setProviderUserId(providerUserId);
        retrofitModel.login(providerUserId);
    }

    @Override
    public void onSuccess(int code, ResponseAuth responseAuth) {
        if (code == ResponseCode.NOT_FOUND) {
            view.startIntroActivity();
            return;
        }

        if (code == ResponseCode.BAD_REQUEST) {
            view.onUnauthorizedError();
            return;
        }

        if (code == ResponseCode.CREATED) {
            PreferenceUtils.setAuth(responseAuth.getAuthorization());
            PreferenceUtils.setNickname(responseAuth.getNickname());
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
