package itmediaengineering.duksung.ootd.main.tab.mypage.model;

import java.util.List;

import itmediaengineering.duksung.ootd.data.mygallery.Photo;

public interface MyPageRetrofitCallback {
    interface RetrofitCallback {
        void onSuccess(int code, List<Photo> posts);
        void onFailure();
    }
}
