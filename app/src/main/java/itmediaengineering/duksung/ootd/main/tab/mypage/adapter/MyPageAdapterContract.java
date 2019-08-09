package itmediaengineering.duksung.ootd.main.tab.mypage.adapter;

import java.util.ArrayList;

import itmediaengineering.duksung.ootd.main.tab.feed.adapter.OnPositionListener;

public interface MyPageAdapterContract {
    interface View {
        void setOnPositionListener(OnPositionListener onPositionListener);
        void notifyAdapter();
    }

    interface Model {
        //void getPosts();
        //void setFeeds(ArrayList items);
        void addPosts(ArrayList items);
        void clearGallery();
    }
}
