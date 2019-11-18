package itmediaengineering.duksung.ootd.main.tab.category.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import itmediaengineering.duksung.ootd.data.post.Post;

public interface OnItemClickListener {
    //void onItemClick(Post post, int position);
    void onItemClick(Post post, ImageView sharedView);
    void onCategoryBClick(String categoryBName, TextView textView);
}
