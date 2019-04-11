package itmediaengineering.duksung.ootd.main.presenter;

import android.util.Log;

import java.util.List;

import itmediaengineering.duksung.ootd.main.model.WeatherCallback;
import itmediaengineering.duksung.ootd.main.model.WeatherRetrofitModel;
import itmediaengineering.duksung.ootd.weather.Item;

public class WeatherPresenter
        implements WeatherContract.Presenter, WeatherCallback.RetrofitCallback {
    private static final String TAG = WeatherPresenter.class.getSimpleName();
    private WeatherContract.View view;
    private WeatherRetrofitModel weatherRetrofitModel;

    public WeatherPresenter(){
        weatherRetrofitModel = new WeatherRetrofitModel();
        weatherRetrofitModel.setCallback(this);
    }

    @Override
    public void onSuccess(int code, List<Item> items) {
        /*if (code == 0000 && items == null) {
            view.onNotFound();
            return;
        }*/

        /*if (code == ResponseCode.UNAUTHORIZED && data == null) {
            view.onUnauthorizedError();
            return;
        }*/

        if (code == 200 && items != null) {
            Log.d(TAG, items.get(3).getCategory() + items.get(3).getObsrValue());
            //adapterModel.addItems(new ArrayList(data));
            //view.onSuccessGetList();
            return;
        }
        view.onUnknownError();
    }

    @Override
    public void onFailure() {
        view.onConnectFail();
    }

    @Override
    public void getWeather(String baseDate, String baseTime, String nx, String ny) {
        weatherRetrofitModel.getWeather(baseDate, baseTime, nx, ny);
    }
    @Override
    public void attachView(WeatherContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}
