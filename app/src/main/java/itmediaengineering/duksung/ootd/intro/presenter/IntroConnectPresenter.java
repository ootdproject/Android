package itmediaengineering.duksung.ootd.intro.presenter;

import itmediaengineering.duksung.ootd.data.User;
import itmediaengineering.duksung.ootd.intro.model.IntroConnectCallback;
import itmediaengineering.duksung.ootd.intro.model.IntroConnectRetrofitModel;

public class IntroConnectPresenter
        implements IntroConnectContract.Presenter, IntroConnectCallback.RetrofitCallback {

    private IntroConnectContract.View view;
    private IntroConnectRetrofitModel retrofitModel;

    public IntroConnectPresenter() {
        retrofitModel = new IntroConnectRetrofitModel();
        retrofitModel.setCallback(this);
    }

    @Override
    public void onSuccess(int code){//, UserResponse response) {
        view.startMainActivity(code);//, response);
    }

    /*@Override
    public void onSuccessJoin(int code) {
        view.startLoginActivity(code);
    }

    @Override
    public void onFailure() {
        view.setProgressbar(false);
    }*/

    @Override
    public void onFailure() {
        view.connectFail();
    }

    @Override
    public void attachView(IntroConnectContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void join(User user) {
        retrofitModel.join(user);
    }
}