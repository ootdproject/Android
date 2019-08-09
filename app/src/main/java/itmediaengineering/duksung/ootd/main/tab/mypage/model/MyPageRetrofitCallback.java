package itmediaengineering.duksung.ootd.main.tab.mypage.model;

import java.util.List;

import itmediaengineering.duksung.ootd.data.mygallery.Photo;
import itmediaengineering.duksung.ootd.data.post.Post;

public interface MyPageRetrofitCallback {
    interface RetrofitCallback {
        void onSuccess(int code, List<Post> posts);
        void onFailure();
    }
}
