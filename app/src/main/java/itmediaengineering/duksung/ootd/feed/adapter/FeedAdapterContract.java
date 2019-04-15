package itmediaengineering.duksung.ootd.feed.adapter;

import java.util.ArrayList;

public interface FeedAdapterContract {
    interface View {
        void setOnPositionListener(OnPositionListener onPositionListener);
        void notifyAdapter();
    }

    interface Model {
        ArrayList getFeeds();
        void setFeeds(ArrayList items);
        void addFeeds(ArrayList items);
        void clearFeed();
    }
}
