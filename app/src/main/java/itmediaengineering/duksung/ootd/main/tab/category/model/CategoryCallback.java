package itmediaengineering.duksung.ootd.main.tab.category.model;

import java.util.List;

import itmediaengineering.duksung.ootd.data.post.Post;

public interface CategoryCallback {
    interface RetrofitCallback{
        void onSuccess(int code, List<Post> posts);
        void onFailure();
    }
}
