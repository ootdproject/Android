package itmediaengineering.duksung.ootd.search.adapter;

import java.util.ArrayList;

public interface SearchAdapterContract {
    interface View {
        void setOnPositionListener(OnPositionListener onPositionListener);
        void setOnClickListener(OnItemClickListener onClickListener);
        void notifyAdapter();
    }

    interface Model {
        void addPosts(ArrayList items);
        void clearFeed();
    }
}
