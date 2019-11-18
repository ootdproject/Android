package itmediaengineering.duksung.ootd.main.tab.category.presenter;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import itmediaengineering.duksung.ootd.data.location.Document;
import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.data.weather.Item;
import itmediaengineering.duksung.ootd.main.tab.category.adapter.CategoryAdapterContract;
import itmediaengineering.duksung.ootd.main.tab.category.adapter.CategoryBAdapterContract;
import itmediaengineering.duksung.ootd.main.tab.category.adapter.OnItemClickListener;
import itmediaengineering.duksung.ootd.main.tab.category.adapter.OnPositionListener;
import itmediaengineering.duksung.ootd.main.tab.category.model.CategoryCallback;
import itmediaengineering.duksung.ootd.main.tab.category.model.CategoryRetrofitModel;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;

public class CategoryPresenter implements CategoryContract.Presenter, CategoryCallback.RetrofitCallback,
        OnPositionListener, OnItemClickListener {
    private static final String TAG = CategoryPresenter.class.getSimpleName();
    private CategoryContract.View view;
    private CategoryRetrofitModel retrofitModel;
    private CategoryAdapterContract.View adapterView;
    private CategoryBAdapterContract.View categoryBAdapterView;
    private CategoryAdapterContract.Model adapterModel;
    private CategoryBAdapterContract.Model categoryBAdapterModel;

    private int page;
    private String category;

    public CategoryPresenter(){
        retrofitModel = new CategoryRetrofitModel();
        retrofitModel.setCallback(this);
    }

    @Override
    public void getCategoryPost(String categoryStr) {
        page = 0;
        category = categoryStr;
        adapterModel.clearFeed();
        retrofitModel.getCategoryPosts(categoryStr, page, CategoryType.categoryA);
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
            //view.onSuccessGetList();
            return;
        }
    }

    @Override
    public void onFailure() {
        //view.onConnectFail();
    }

    @Override
    public void attachView(CategoryContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void setAdapterView(CategoryAdapterContract.View adapterView) {
        this.adapterView = adapterView;
        this.adapterView.setOnPositionListener(this);
        this.adapterView.setOnClickListener(this);
    }

    @Override
    public void setCategoryListAdapterView(CategoryBAdapterContract.View adapterView) {
        this.categoryBAdapterView = adapterView;
        this.categoryBAdapterView.setOnClickListener(this);
    }

    @Override
    public void setAdapterModel(CategoryAdapterContract.Model adapterModel) {
        this.adapterModel = adapterModel;
    }

    @Override
    public void setCategoryListAdapterModel(CategoryBAdapterContract.Model adapterModel) {
        this.categoryBAdapterModel = adapterModel;
    }

    @Override
    public void onItemClick(Post post, ImageView sharedView) {
        view.startPostDetailActivity(post, sharedView);
    }

    @Override
    public void onCategoryBClick(String categoryBName, TextView textView) {
        page = 0;
        category = categoryBName;
        adapterModel.clearFeed();
        //categoryBAdapterModel.clearCategoryBView();
        retrofitModel.getCategoryPosts(categoryBName, page, CategoryType.categoryB);
    }

    @Override
    public void onLoad(int page) {
        if (this.page == page)
            return;
        this.page = page;
        Log.d(TAG, "page : " + page);
        retrofitModel.getCategoryPosts(category, page, CategoryType.categoryA);
    }
}
