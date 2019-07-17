package itmediaengineering.duksung.ootd.main.tab.category.presenter;

import android.util.Log;

import java.util.List;

import itmediaengineering.duksung.ootd.data.location.Document;
import itmediaengineering.duksung.ootd.data.weather.Item;
import itmediaengineering.duksung.ootd.main.tab.category.model.CategoryCallback;
import itmediaengineering.duksung.ootd.main.tab.category.model.CategoryRetrofitModel;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;

public class CategoryPresenter
        implements CategoryContract.Presenter, CategoryCallback.RetrofitCallback {
    private static final String TAG = CategoryPresenter.class.getSimpleName();
    private CategoryContract.View view;
    private CategoryRetrofitModel categoryRetrofitModel;

    public CategoryPresenter(){
        categoryRetrofitModel = new CategoryRetrofitModel();
        categoryRetrofitModel.setCallback(this);
    }

    @Override
    public void onSuccess(int code, List<Item> weather, List<Document> location) {
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
            view.onSuccessGetWeather(weather.get(3));
            Log.d(TAG, location.get(0).getAddressName());
            Log.d(TAG, weather.get(3).getCategory() + " : " + weather.get(3).getObsrValue());
            return;
        }
        view.onUnknownError();
    }

    @Override
    public void onFailure() {
        view.onConnectFail();
    }

    @Override
    public void getData(String x, String y) {
        categoryRetrofitModel.getLocation(x, y);
    }

    @Override
    public void attachView(CategoryContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}
