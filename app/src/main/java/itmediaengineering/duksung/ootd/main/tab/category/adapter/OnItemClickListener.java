package itmediaengineering.duksung.ootd.main.tab.feed.adapter;

import android.widget.ImageView;

import itmediaengineering.duksung.ootd.data.post.Post;

public interface OnItemClickListener {
    //void onItemClick(Post post, int position);
    void onItemClick(Post post, ImageView sharedView);
}