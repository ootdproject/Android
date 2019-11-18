package itmediaengineering.duksung.ootd.main.tab.feed.model;

import java.util.List;

import itmediaengineering.duksung.ootd.data.post.Post;

public interface FeedRetrofitCallback {
    interface RetrofitCallback {
        void onSuccess(int code, List<Post> posts);
        void onSuccessRecommendGuPost(int code, List<Post> posts);
        void onSuccessLikePost(int code);
        void onFailure();
    }
}
