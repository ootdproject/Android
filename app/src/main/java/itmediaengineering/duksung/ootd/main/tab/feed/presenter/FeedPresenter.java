package itmediaengineering.duksung.ootd.main.tab.feed.presenter;

import android.view.View;
import android.view.ViewGroup;
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

public class FeedPresenter implements FeedContract.Presenter, FeedRetrofitCallback.RetrofitCallback,
        OnPositionListener, OnItemClickListener {
    private static final String TAG = FeedPresenter.class.getSimpleName();
    private FeedContract.View view;
    private FeedRetrofitModel retrofitModel;
    private FeedAdapterContract.View adapterView;
    private FeedAdapterContract.Model adapterModel;
    //private String search;
    //private int sort;
    //private int page;

    public FeedPresenter(){
        retrofitModel = new FeedRetrofitModel();
        retrofitModel.setCallback(this);

    }

    @Override
    public void getFeed() {
        //this.search = search;
        //this.sort = sort;
        //page = 1;
        adapterModel.clearFeed();
        retrofitModel.getPosts();
    }

    /*@Override
    public void onLoad(int page) {
        if (this.page == page)
            return;
        this.page = page;
        Log.d(TAG, "page : " + page);
        retrofitModel.getPosts(search, sort, page);
    }*/

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
        //view.onUnknownError();
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
    public void setAdapterView(FeedAdapterContract.View adapterView) {
        this.adapterView = adapterView;
        this.adapterView.setOnPositionListener(this);
        this.adapterView.setOnClickListener(this);
    }

    @Override
    public void setAdapterModel(FeedAdapterContract.Model adapterModel) {
        this.adapterModel = adapterModel;
    }

    @Override
    public void onItemClick(Post post, ImageView sharedView) {
        view.startPostDetailActivity(post, sharedView);
    }
}
