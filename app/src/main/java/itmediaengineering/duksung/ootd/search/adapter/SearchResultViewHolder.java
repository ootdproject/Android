package itmediaengineering.duksung.ootd.search.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.data.post.Post;

public class SearchResultViewHolder extends RecyclerView.ViewHolder {
    private OnItemClickListener onItemClickListener;
    private OnPositionListener onPositionListener;

    private Context context;

    @BindView(R.id.feed_item)
    ConstraintLayout feedItem;
    @BindView(R.id.feed_content_img)
    ImageView feedContentImg;
    @BindView(R.id.feed_content_img_sold_filter)
    ImageView feedContentImgSoldFilter;
    @BindView(R.id.feed_item_like_button)
    Button feedItemLikeButton;
    @BindView(R.id.feed_item_title)
    TextView feedItemTitle;
    @BindView(R.id.feed_item_cost)
    TextView feedItemCost;

    public SearchResultViewHolder(final Context context, ViewGroup parent,
                                  OnItemClickListener onItemClickListener, OnPositionListener onPositionListener) {
        super(LayoutInflater.from(context).inflate(R.layout.feed_item, parent, false));
        ButterKnife.bind(this, itemView);
        this.onItemClickListener = onItemClickListener;
        this.onPositionListener = onPositionListener;
        this.context = context;

        //View view = findViewById(R..fab_b);
        //feedContentImg.setTransitionName("movieWork");
    }

    public void onBind(Post post, int position, int listSize){
        //userIdView.setText(post.getMemberId());

        feedItem.setOnClickListener(v ->
                onItemClickListener.onItemClick(post, feedContentImg));
        //onItemClickListener.onItemClick(post, position));
        //feedItemTitle.setText(post.getMemberId());
        //feedItemCost.setText(post.getPostCount());
        if(!post.getSale()) {
            feedContentImgSoldFilter.setVisibility(View.VISIBLE);
        } else {
            feedContentImgSoldFilter.setVisibility(View.INVISIBLE);
        }

        Glide.with(context)
                .load(post.getImageUrl())
                .into(feedContentImg);

        if(post.getTitle().length()>16){
            feedItemTitle.setText(post.getTitle().substring(0,15) + "...");
        }else{
            feedItemTitle.setText(post.getTitle());
        }
        feedItemCost.setText(post.getCost());

        feedItemLikeButton.setOnClickListener(view -> {
            if(feedItemLikeButton.isSelected())
                feedItemLikeButton.setSelected(false);
            else
                feedItemLikeButton.setSelected(true);
        });

        if (position == listSize - 1) {
            int page = listSize / 20;
            onPositionListener.onLoad(page);
        }
    }
}
