package itmediaengineering.duksung.ootd.search.model;

import java.util.List;

import itmediaengineering.duksung.ootd.data.post.Post;

public interface SearchCallback {
    interface RetrofitCallback{
        void onSuccess(int code, List<Post> posts);
        void onCategorySuccess(int code, List<Post> posts, int listCount);
        void onColorSuccess(int code, List<Post> posts, int listCount);
        void onFailure();
    }
}
