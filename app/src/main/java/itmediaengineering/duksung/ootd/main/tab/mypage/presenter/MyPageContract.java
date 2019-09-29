package itmediaengineering.duksung.ootd.main.tab.mypage.presenter;

import android.widget.ImageView;

import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.main.tab.mypage.adapter.MyPageAdapterContract;

public interface MyPageContract {
    interface View {
        void toast(String msg);
        //void onUnauthorizedError();
        //void onUnknownError();
        void onSuccessEditPostSaleState();
        void onSuccessGetGallery();
        void onSuccessDeletePost();
        //void onConnectFail();
        void onStartPopUp(Post post);
        //void onNotFound();
    }

    interface PickView {
        void toast(String msg);
        //void onUnauthorizedError();
        //void onUnknownError();
        void onSuccessGetGallery();
        //void onConnectFail();
        void onStartDetailActivity(Post post, ImageView shareView);
        //void onNotFound();
    }

    interface Presenter {
        void getMyPosts(SaleType saleType);
        void editPostSaleState(Post post);
        void deletePost(Post post);
        //void getCreatedPosts();
        //void getLikedFeeds();
        void attachView(View view);
        void attachPickView(PickView view);
        void detachView();
        void setAdapterView(MyPageAdapterContract.View adapterView);
        void setAdapterModel(MyPageAdapterContract.Model adapterModel);
    }
}
