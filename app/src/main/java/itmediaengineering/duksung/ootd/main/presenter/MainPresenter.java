package itmediaengineering.duksung.ootd.main.presenter;

import android.util.Log;

import java.util.List;

import itmediaengineering.duksung.ootd.data.location.Document;
import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.main.model.MainCallback;
import itmediaengineering.duksung.ootd.main.model.MainRetrofitModel;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;

public class MainPresenter implements MainContract.Presenter, MainCallback.RetrofitCallback {
    private static final String TAG = MainPresenter.class.getSimpleName();
    private MainContract.View view;
    private MainRetrofitModel mainRetrofitModel;

    public MainPresenter(){
        mainRetrofitModel = new MainRetrofitModel();
        mainRetrofitModel.setCallback(this);
    }

    @Override
    public void onSuccess(int code, List<Document> location) {
        if (code == 0000 && location == null) {
            view.onNotFound();
            return;
        }

        if (code == ResponseCode.UNAUTHORIZED && location == null) {
            view.onUnauthorizedError();
            return;
        }

        if (code == 200 && location != null) {
            view.onSuccessGetLocation(location.get(0));
            Log.d(TAG, location.get(0).getAddressName());
            //view.onSuccessGetWeather(weather.get(3));
            //Log.d(TAG, weather.get(3).getCategory() + " : " + weather.get(3).getObsrValue());
            return;
        }
        view.onUnknownError();
    }

    @Override
    public void onFailure() {
        view.onConnectFail();
    }

    @Override
    public void getLocation(double x, double y) {
        mainRetrofitModel.getLocation(String.valueOf(x), String.valueOf(y));
    }

    @Override
    public void attachView(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}
