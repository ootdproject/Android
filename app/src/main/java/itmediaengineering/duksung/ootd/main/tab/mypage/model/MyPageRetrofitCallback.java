package itmediaengineering.duksung.ootd.main.tab.mypage.model;

import java.util.List;

import itmediaengineering.duksung.ootd.data.post.Post;

public interface MyPageRetrofitCallback {
    interface RetrofitCallback {
        void onSuccess(int code, List<Post> posts);
        void onSuccessEditSaleState(int code);
        void onSuccessDeleteMyPost(int code);
        void onFailure();
    }
}
