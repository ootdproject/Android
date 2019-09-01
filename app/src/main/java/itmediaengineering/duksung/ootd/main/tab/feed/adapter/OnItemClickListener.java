package itmediaengineering.duksung.ootd.main.tab.feed.adapter;

import android.widget.Button;
import android.widget.ImageView;

import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.utils.LikeType;

public interface OnItemClickListener {
    //void onItemClick(Post post, int position);
    void onItemClick(Post post, ImageView sharedView);
    void onItemLikeClick(Post post, LikeType isLike, Button likeView);
}
