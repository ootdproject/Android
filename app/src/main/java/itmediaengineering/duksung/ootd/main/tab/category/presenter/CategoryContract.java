package itmediaengineering.duksung.ootd.main.tab.category.presenter;

import android.widget.ImageView;

import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.main.tab.category.adapter.CategoryAdapterContract;
import itmediaengineering.duksung.ootd.main.tab.category.adapter.CategoryBAdapterContract;

public interface CategoryContract {
    interface View {
        void toast(String msg);
        void startPostDetailActivity(Post post, ImageView sharedView);

    }

    interface Presenter {
        void getCategoryPost(String category);
        void attachView(View view);
        void detachView();
        void setAdapterView(CategoryAdapterContract.View adapterView);
        void setCategoryListAdapterView(CategoryBAdapterContract.View adapterView);
        void setAdapterModel(CategoryAdapterContract.Model adapterModel);
        void setCategoryListAdapterModel(CategoryBAdapterContract.Model adapterModel);
    }
}
