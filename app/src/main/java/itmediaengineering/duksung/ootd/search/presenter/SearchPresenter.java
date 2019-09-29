package itmediaengineering.duksung.ootd.search.presenter;

import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;
import itmediaengineering.duksung.ootd.search.adapter.OnItemClickListener;
import itmediaengineering.duksung.ootd.search.adapter.OnPositionListener;
import itmediaengineering.duksung.ootd.search.adapter.SearchAdapterContract;
import itmediaengineering.duksung.ootd.search.model.SearchCallback;
import itmediaengineering.duksung.ootd.search.model.SearchRetrofitModel;

public class SearchPresenter implements SearchContract.Presenter, SearchCallback.RetrofitCallback,
        OnItemClickListener, OnPositionListener {
    private static final String TAG = SearchContract.class.getSimpleName();
    private SearchContract.View view;
    private SearchRetrofitModel retrofitModel;
    private SearchAdapterContract.View adapterView;
    private SearchAdapterContract.Model adapterModel;

    private int page;
    private int categoryListSize;
    private int colorListSize;
    private int categoryIdx = 0;
    private int colorIdx = 0;
    private String category;
    private String color;
    private String tagStr = "";

    public SearchPresenter() {
        retrofitModel = new SearchRetrofitModel();
        retrofitModel.setCallback(this);
    }

    @Override
    public void onItemClick(Post post, ImageView sharedView) {
        view.startPostDetailActivity(post, sharedView);
    }

    @Override
    public void onLoad(int page) {
        if (this.page == page)
            return;
        this.page = page;
        Log.d(TAG, "page : " + page);
        retrofitModel.getSearchPosts(category, page, 0);
    }

    @Override
    public void getCategoryPost(ArrayList<String> categoryList) {
        page = 0;
        categoryListSize = categoryList.size();
        for (String categoryStr : categoryList) {
            category = categoryStr;
            tagStr += "#" + categoryStr + "  ";
            retrofitModel.getSearchPosts(categoryStr, page, categoryIdx);
            categoryIdx++;
        }
    }

    @Override
    public void getColorPost(ArrayList<String> categoryAndColorList) {
        page = 0;
        colorListSize = categoryAndColorList.size();
        for (String colorStr : categoryAndColorList) {
            //category = categoryStr;
            tagStr += "#" + colorStr + "  ";
            retrofitModel.getColorSearchPosts(colorStr, page, colorIdx);
            colorIdx++;
        }
    }

    @Override
    public void getSearchImgResult(ArrayList<String> categoryList, ArrayList<String> colorList) {
        page = 0;
        category = categoryList.get(0);
        color = colorList.get(0);
        tagStr += "#" + category + "  " + "#" + color + "  ";
        retrofitModel.getCategoryAndColorSearchPosts(category, color, page);
    }

    @Override
    public void attachView(SearchContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void setAdapterView(SearchAdapterContract.View adapterView) {
        this.adapterView = adapterView;
        this.adapterView.setOnPositionListener(this);
        this.adapterView.setOnClickListener(this);
    }

    @Override
    public void setAdapterModel(SearchAdapterContract.Model adapterModel) {
        this.adapterModel = adapterModel;
    }

    @Override
    public void onSuccess(int code, List<Post> posts) {
        if (code == ResponseCode.NOT_FOUND && posts == null) {
            return;
        }

        if (code == ResponseCode.UNAUTHORIZED && posts == null) {
            return;
        }

        if (code == ResponseCode.SUCCESS && posts != null) {
            view.setTagText(tagStr);
            adapterModel.addPosts(new ArrayList(posts));
            return;
        }
    }

    @Override
    public void onCategorySuccess(int code, List<Post> posts, int listCount) {
        if (code == ResponseCode.NOT_FOUND && posts == null) {
            return;
        }

        if (code == ResponseCode.UNAUTHORIZED && posts == null) {
            return;
        }

        if (code == ResponseCode.SUCCESS && posts != null) {
            if (listCount == categoryListSize - 1) {
                view.setTagText(tagStr);
            }
            adapterModel.addPosts(new ArrayList(posts));
            return;
        }
    }

    @Override
    public void onColorSuccess(int code, List<Post> posts, int listCount) {
        if (code == ResponseCode.NOT_FOUND && posts == null) {
            return;
        }

        if (code == ResponseCode.UNAUTHORIZED && posts == null) {
            return;
        }

        if (code == ResponseCode.SUCCESS && posts != null) {
            if (listCount == colorListSize - 1) {
                view.setTagText(tagStr);
            }
            adapterModel.addPosts(new ArrayList(posts));
            return;
        }
    }

    @Override
    public void onFailure() {

    }
}
