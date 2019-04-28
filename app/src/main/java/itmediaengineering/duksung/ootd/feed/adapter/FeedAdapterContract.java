package itmediaengineering.duksung.ootd.feed.adapter;

import java.util.ArrayList;

public interface FeedAdapterContract {
    interface View {
        void setOnPositionListener(OnPositionListener onPositionListener);
        void notifyAdapter();
    }

    interface Model {
        //void getPosts();
        //void setFeeds(ArrayList items);
        void addPosts(ArrayList items);
        void clearFeed();
    }
}
