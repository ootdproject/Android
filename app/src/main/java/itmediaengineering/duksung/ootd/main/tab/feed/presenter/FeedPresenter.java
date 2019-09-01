package itmediaengineering.duksung.ootd.main.tab.feed.presenter;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.main.tab.feed.adapter.FeedAdapterContract;
import itmediaengineering.duksung.ootd.main.tab.feed.adapter.OnItemClickListener;
import itmediaengineering.duksung.ootd.main.tab.feed.adapter.OnPositionListener;
import itmediaengineering.duksung.ootd.main.tab.feed.model.FeedRetrofitCallback;
import itmediaengineering.duksung.ootd.main.tab.feed.model.FeedRetrofitModel;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;
import itmediaengineering.duksung.ootd.utils.LikeType;

public class FeedPresenter implements FeedContract.Presenter, FeedRetrofitCallback.RetrofitCallback,
        OnPositionListener, OnItemClickListener {
    private static final String TAG = FeedPresenter.class.getSimpleName();
    private FeedContract.View view;
    private FeedRetrofitModel retrofitModel;
    private FeedAdapterContract.View adapterView;
    private FeedAdapterContract.View recommendAdapterView;
    private FeedAdapterContract.Model adapterModel;
    private FeedAdapterContract.Model recommendAdapterModel;

    private int page;
    private String dong;

    public FeedPresenter(){
        retrofitModel = new FeedRetrofitModel();
        retrofitModel.setCallback(this);
    }

    @Override
    public void getFeedByLocationString(@Nullable String dongStr) {
        page = 0;
        adapterModel.clearFeed();
        recommendAdapterModel.clearFeed();
        if(dongStr != null){
            dong = dongStr;
            retrofitModel.getQueryLocationPosts(dongStr, page);
            retrofitModel.getRecommendLocationPosts(dongStr, page);
        } else {
            retrofitModel.getPosts(page);
        }
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
            //Log.d(TAG, posts.get(0).toString());
            adapterModel.addPosts(new ArrayList(posts));
            view.onSuccessGetList();
            return;
        }
    }

    @Override
    public void onSuccessRecommendGuPost(int code, List<Post> posts) {
        if (code == ResponseCode.NOT_FOUND && posts == null) {
            //view.onNotFound();
            return;
        }

        if (code == ResponseCode.UNAUTHORIZED && posts == null) {
            //view.onUnauthorizedError();
            return;
        }

        if (code == ResponseCode.SUCCESS && posts != null) {
            //Log.d(TAG, posts.get(0).toString());
            recommendAdapterModel.addPosts(new ArrayList(posts));
            view.onSuccessGetList();
            return;
        }
    }

    @Override
    public void onSuccessLikePost(int code) {
        if (code == ResponseCode.NOT_FOUND) {
            //view.onNotFound();
            return;
        }

        if (code == ResponseCode.UNAUTHORIZED) {
            //view.onUnauthorizedError();
            return;
        }

        if (code == ResponseCode.SUCCESS) {
            view.onSuccessPostLike();
            return;
        }
    }

    @Override
    public void onFailure() {
        //view.onConnectFail();
    }

    @Override
    public void attachView(FeedContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void setFeedAdapterView(FeedAdapterContract.View adapterView) {
        this.adapterView = adapterView;
        this.adapterView.setOnPositionListener(this);
        this.adapterView.setOnClickListener(this);
    }

    @Override
    public void setRecommendFeedAdapterView(FeedAdapterContract.View adapterView) {
        this.recommendAdapterView = adapterView;
        this.recommendAdapterView.setOnPositionListener(this);
        this.recommendAdapterView.setOnClickListener(this);
    }

    @Override
    public void setFeedAdapterModel(FeedAdapterContract.Model adapterModel) {
        this.adapterModel = adapterModel;
    }

    @Override
    public void setRecommendFeedAdapterModel(FeedAdapterContract.Model adapterModel) {
        this.recommendAdapterModel = adapterModel;
    }

    @Override
    public void onItemClick(Post post, ImageView sharedView) {
        view.startPostDetailActivity(post, sharedView);
    }

    @Override
    public void onItemLikeClick(Post post, LikeType likeType, Button likeView) {
        retrofitModel.likePost(post.getId(), likeType);
    }

    @Override
    public void onLoad(int page) {
        if (this.page == page)
            return;
        this.page = page;
        Log.d(TAG, "page : " + page);
        retrofitModel.getQueryLocationPosts(dong, page);
    }
}
