package itmediaengineering.duksung.ootd.main.presenter;

import android.util.Log;

import java.util.List;

import itmediaengineering.duksung.ootd.location.Document;
import itmediaengineering.duksung.ootd.main.model.LocationCallback;
import itmediaengineering.duksung.ootd.main.model.LocationRetrofitModel;

public class LocationPresenter
    implements LocationContract.Presenter, LocationCallback.RetrofitCallback{
        private static final String TAG = LocationPresenter.class.getSimpleName();
        private LocationContract.View view;
        private LocationRetrofitModel locationRetrofitModel;

    public LocationPresenter(){
        locationRetrofitModel = new LocationRetrofitModel();
        locationRetrofitModel.setCallback(this);
    }

    @Override
    public void onSuccess(int code, List<Document> documents) {
        /*if (code == 0000 && items == null) {
            view.onNotFound();
            return;
        }*/

        /*if (code == ResponseCode.UNAUTHORIZED && data == null) {
            view.onUnauthorizedError();
            return;
        }*/
        if (code == 200 && documents != null) {
            view.onSuccessGetLocation(documents.get(0));
            Log.d(TAG, documents.get(0).getAddressName());
            //adapterModel.addItems(new ArrayList(data));
            //view.onSuccessGetLocation();
            return;
        }
        view.onUnknownError();
    }

    @Override
    public void onFailure() {
        view.onConnectFail();
    }

    @Override
    public void getLocation(String x, String y) {
        locationRetrofitModel.getLocation(x, y);
    }

    @Override
    public void attachView(LocationContract.View view) {
            this.view = view;
        }

    @Override
    public void detachView() {
            this.view = null;
        }
}
