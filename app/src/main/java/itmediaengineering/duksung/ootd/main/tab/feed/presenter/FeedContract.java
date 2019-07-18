package itmediaengineering.duksung.ootd.main.tab.feed.presenter;

import itmediaengineering.duksung.ootd.data.feed.Post;
import itmediaengineering.duksung.ootd.main.tab.feed.adapter.FeedAdapterContract;

public interface FeedContract {
    interface View {
        void toast(String msg);
        //void onUnauthorizedError();
        //void onUnknownError();
        void onSuccessGetList();
        //void onConnectFail();
        void startPostDetailActivity(Post post);
        //void onNotFound();
    }

    interface Presenter {
        void getFeed();
        //void getCreatedPosts();
        //void getLikedFeeds();
        void attachView(View view);
        void detachView();
        void setAdapterView(FeedAdapterContract.View adapterView);
        void setAdapterModel(FeedAdapterContract.Model adapterModel);
    }
}
