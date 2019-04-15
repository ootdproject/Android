package itmediaengineering.duksung.ootd.feed.model;

import java.util.List;

import itmediaengineering.duksung.ootd.data.feed.Post;

public interface FeedRetrofitCallback {
    interface RetrofitCallback {
        void onSuccess(int code, List<Post> posts);
        void onFailure();
    }
}
