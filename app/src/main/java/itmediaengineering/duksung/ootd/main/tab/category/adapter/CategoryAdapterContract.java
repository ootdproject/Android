package itmediaengineering.duksung.ootd.main.tab.category.adapter;

import java.util.ArrayList;

public interface CategoryAdapterContract {
    interface View {
        void setOnPositionListener(OnPositionListener onPositionListener);
        void setOnClickListener(OnItemClickListener onClickListener);
        void notifyAdapter();
    }

    interface Model {
        //void getPosts();
        //void setFeeds(ArrayList items);
        void addPosts(ArrayList items);
        void clearFeed();
    }
}
