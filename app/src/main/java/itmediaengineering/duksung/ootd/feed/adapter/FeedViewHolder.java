package itmediaengineering.duksung.ootd.feed.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.data.feed.Post;

public class FeedViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.user_thum_nail_img)
    ImageView userThumNailImg;
    @BindView(R.id.user_id_view)
    TextView userIdView;
    @BindView(R.id.feed_content_img)
    ImageView feedContentImg;
    @BindView(R.id.feed_item_like_button)
    Button feedItemLikeButton;

    public FeedViewHolder(final Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false));
        ButterKnife.bind(this, itemView);
    }

    public void onBind(Post post, int position, int listSize){

    }
}
