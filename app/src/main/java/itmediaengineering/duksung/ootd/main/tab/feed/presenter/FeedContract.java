package itmediaengineering.duksung.ootd.main.tab.feed.presenter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.main.tab.feed.adapter.FeedAdapterContract;

public interface FeedContract {
    interface View {
        void toast(String msg);
        //void onUnauthorizedError();
        //void onUnknownError();
        void onSuccessGetList();
        //void onConnectFail();
        void startPostDetailActivity(Post post, ImageView sharedView);
        //void onNotFound();
    }

    interface Presenter {
        void getFeedByLocationString(@Nullable String dongStr);
        //void getCreatedPosts();
        //void getLikedFeeds();
        void attachView(View view);
        void detachView();
        void setAdapterView(FeedAdapterContract.View adapterView);
        void setAdapterModel(FeedAdapterContract.Model adapterModel);
    }
}
