package itmediaengineering.duksung.ootd.search.presenter;

import android.widget.ImageView;

import java.util.ArrayList;

import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.search.adapter.SearchAdapterContract;

public interface SearchContract {
    interface View {
        void toast(String msg);
        //void onUnauthorizedError();
        //void onUnknownError();
        //void onSuccessGetLocation(Document document);
        //void onSuccessGetWeather(Item item);
        //void onConnectFail();
        //void startPostDetailActivity(Data item);
        void startPostDetailActivity(Post post, ImageView sharedView);
        void setTagText(String str);
        //void onNotFound();
    }

    interface Presenter {
        void getCategoryPost(ArrayList<String> categoryList);
        void getColorPost(ArrayList<String> categoryAndColorList);
        void getSearchImgResult(String category, String color);
        //void getWeather(String baseDate, String baseTime, String nx, String ny);
        void attachView(View view);
        void detachView();
        void setAdapterView(SearchAdapterContract.View adapterView);
        void setAdapterModel(SearchAdapterContract.Model adapterModel);
    }
}
