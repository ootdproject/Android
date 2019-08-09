package itmediaengineering.duksung.ootd.main.tab.mypage.presenter;

import java.util.ArrayList;
import java.util.List;

import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.main.tab.mypage.adapter.MyPageAdapterContract;
import itmediaengineering.duksung.ootd.main.tab.mypage.model.MyPageRetrofitCallback;
import itmediaengineering.duksung.ootd.main.tab.mypage.model.MyPageRetrofitModel;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;

public class MyPagePresenter implements MyPageContract.Presenter, MyPageRetrofitCallback.RetrofitCallback{//}, OnPositionListener {
    private static final String TAG = MyPagePresenter.class.getSimpleName();
    private MyPageContract.View view;
    private MyPageRetrofitModel retrofitModel;
    private MyPageAdapterContract.View adapterView;
    private MyPageAdapterContract.Model adapterModel;
    //private String search;
    //private int sort;
    //private int page;

    public MyPagePresenter() {
        retrofitModel = new MyPageRetrofitModel();
        retrofitModel.setCallback(this);
    }

    @Override
    public void getMyGallery() {
        adapterModel.clearGallery();
        retrofitModel.getGallery();
    }

    @Override
    public void onSuccess(int code, List<Post> posts) {
        if (code == ResponseCode.NOT_FOUND && posts == null) {
            //view.onNotFound();
            return;
        }

        if (code == ResponseCode.UNAUTHORIZED && posts == null) {
            //view.onUnauthorizedError();
            return;
        }

        if (code == ResponseCode.SUCCESS && posts != null) {
            //Log.d(TAG, photos.get(0).toString());
            adapterModel.addPosts(new ArrayList(posts));
            view.onSuccessGetGallery();
            return;
        }
        //view.onUnknownError();
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void attachView(MyPageContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void setAdapterView(MyPageAdapterContract.View adapterView) {
        this.adapterView = adapterView;
    }

    @Override
    public void setAdapterModel(MyPageAdapterContract.Model adapterModel) {
        this.adapterModel = adapterModel;
    }
}