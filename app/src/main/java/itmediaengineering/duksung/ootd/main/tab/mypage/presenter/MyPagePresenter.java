package itmediaengineering.duksung.ootd.main.tab.mypage.presenter;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.main.tab.mypage.adapter.MyPageAdapterContract;
import itmediaengineering.duksung.ootd.main.tab.mypage.adapter.OnItemClickListener;
import itmediaengineering.duksung.ootd.main.tab.mypage.model.MyPageRetrofitCallback;
import itmediaengineering.duksung.ootd.main.tab.mypage.model.MyPageRetrofitModel;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;

public class MyPagePresenter implements MyPageContract.Presenter,
        MyPageRetrofitCallback.RetrofitCallback, OnItemClickListener {
    private MyPageContract.View view;
    private MyPageContract.PickView pickView;
    private MyPageRetrofitModel retrofitModel;
    private MyPageAdapterContract.View adapterView;
    private MyPageAdapterContract.Model adapterModel;

    public MyPagePresenter() {
        retrofitModel = new MyPageRetrofitModel();
        retrofitModel.setCallback(this);
    }

    @Override
    public void getMyPosts(SaleType saleType) {
        adapterModel.clearGallery();
        retrofitModel.getMyPosts(saleType);
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
            //view.onSuccessGetGallery();
            return;
        }
        //view.onUnknownError();
    }

    @Override
    public void editPostSaleState(Post post) {
        //adapterModel.clearGallery();
        retrofitModel.editPostSaleToSold(post);
    }

    @Override
    public void onSuccessEditSaleState(int code) {
        if (code == ResponseCode.NOT_FOUND) {
            //view.onNotFound();
            return;
        }
        if (code == ResponseCode.BAD_REQUEST) {
            //view.onUnauthorizedError();
            return;
        }
        if (code == ResponseCode.UNDOCUMENTED) {
            //view.onUnauthorizedError();
            return;
        }
        if (code == ResponseCode.SUCCESS) {
            //Log.d(TAG, posts.get(0).toString());
            //adapterModel.addPosts(new ArrayList(posts));
            //view.onUploadSuccess();
            //view.resumeUpLoadFragment(code);
            //adapterModel.clearGallery();
            //retrofitModel.getMyPosts();
            view.onSuccessEditPostSaleState();
            return;
        }
    }

    @Override
    public void deletePost(Post post) {
        retrofitModel.deleteMyPost(post);
    }

    @Override
    public void onSuccessDeleteMyPost(int code) {
        if (code == ResponseCode.NOT_FOUND) {
            //view.onNotFound();
            return;
        }
        if (code == ResponseCode.BAD_REQUEST) {
            //view.onUnauthorizedError();
            return;
        }
        if (code == ResponseCode.UNDOCUMENTED) {
            //view.onUnauthorizedError();
            return;
        }
        if (code == ResponseCode.SUCCESS) {
            view.onSuccessDeletePost();
            return;
        }
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void attachView(MyPageContract.View view) {
        this.view = view;
    }

    @Override
    public void attachPickView(MyPageContract.PickView view) {
        this.pickView = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void setAdapterView(MyPageAdapterContract.View adapterView) {
        this.adapterView = adapterView;
        this.adapterView.setOnClickListener(this);
    }

    @Override
    public void setAdapterModel(MyPageAdapterContract.Model adapterModel) {
        this.adapterModel = adapterModel;
    }

    @Override
    public void onItemClick(Post post, SaleType saleType, ImageView shareView) {
        if(saleType == SaleType.onSale) {
            view.onStartPopUp(post);
        } else if(saleType == SaleType.pick) {
            pickView.onStartDetailActivity(post, shareView);
        }
    }
}