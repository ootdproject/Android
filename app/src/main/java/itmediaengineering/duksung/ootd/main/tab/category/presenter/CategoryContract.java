package itmediaengineering.duksung.ootd.main.tab.category.presenter;

import android.widget.ImageView;

import itmediaengineering.duksung.ootd.data.location.Document;
import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.data.weather.Item;
import itmediaengineering.duksung.ootd.main.tab.category.adapter.CategoryAdapterContract;
import itmediaengineering.duksung.ootd.main.tab.category.adapter.CategoryBAdapterContract;

public interface CategoryContract {
    interface View {
        void toast(String msg);
        //void onUnauthorizedError();
        //void onUnknownError();
        //void onSuccessGetLocation(Document document);
        //void onSuccessGetWeather(Item item);
        //void onConnectFail();
        //void startPostDetailActivity(Data item);
        void startPostDetailActivity(Post post, ImageView sharedView);
        //void onNotFound();
    }

    interface Presenter {
        void getCategoryPost(String category);
        //void getWeather(String baseDate, String baseTime, String nx, String ny);
        void attachView(View view);
        void detachView();
        void setAdapterView(CategoryAdapterContract.View adapterView);
        void setCategoryListAdapterView(CategoryBAdapterContract.View adapterView);
        void setAdapterModel(CategoryAdapterContract.Model adapterModel);
        void setCategoryListAdapterModel(CategoryBAdapterContract.Model adapterModel);
    }
}
