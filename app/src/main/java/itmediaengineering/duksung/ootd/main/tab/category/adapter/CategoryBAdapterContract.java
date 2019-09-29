package itmediaengineering.duksung.ootd.main.tab.category.adapter;

import java.util.ArrayList;

public interface CategoryBAdapterContract {
    interface View {
        void setOnClickListener(OnItemClickListener onClickListener);
        void notifyAdapter();
    }

    interface Model {
        //void getPosts();
        //void setFeeds(ArrayList items);
        void addCategoryB(ArrayList categoryB);
        void clearCategoryBView();
    }
}
