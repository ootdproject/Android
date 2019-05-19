package itmediaengineering.duksung.ootd.main.tab.mypage.presenter;

import itmediaengineering.duksung.ootd.main.tab.feed.adapter.FeedAdapterContract;
import itmediaengineering.duksung.ootd.main.tab.mypage.adapter.MyPageAdapterContract;

public interface MyPageContract {
    interface View {
        void toast(String msg);
        //void onUnauthorizedError();
        //void onUnknownError();
        void onSuccessGetGallery();
        //void onConnectFail();
        //void startDetailActivity(Post post);
        //void onNotFound();
    }

    interface Presenter {
        void getMyGallery();
        //void getCreatedPosts();
        //void getLikedFeeds();
        void attachView(View view);
        void detachView();
        void setAdapterView(MyPageAdapterContract.View adapterView);
        void setAdapterModel(MyPageAdapterContract.Model adapterModel);
    }
}
