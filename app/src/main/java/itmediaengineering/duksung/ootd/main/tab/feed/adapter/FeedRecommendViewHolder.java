package itmediaengineering.duksung.ootd.main.tab.feed.adapter;

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

public class FeedRecommendViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = FeedViewHolder.class.getSimpleName();
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

    private class LikeView {
        private static final String LIKEYN = "likeyn";
        //private static final String DISLIKEYN = "dislikeyn";

        private Button likeButton;
        private TextView likeCountView;
        private int count;


        LikeView(Button likeButton, TextView likeCountView, int likeCount) {
            this.likeButton = likeButton;
            this.likeCountView = likeCountView;
            this.count = likeCount;
        }

        void like() {
            //incrCount(LIKEYN);
            count++;
            likeCountView.setText(String.valueOf(count));
            likeButton.setSelected(true);
        }

        void unlike() {
            //incrCount(DISLIKEYN);
            count--;
            likeCountView.setText(String.valueOf(count));
            likeButton.setSelected(false);
        }

        /*private void incrCount(String type) {
            requestIncreaseLikeDislike(movieDetailInfo.getId(), type, typeToAnswer(type), response -> {
                        CommentPostRequestResponse info = new Gson().fromJson(response.toString(), CommentPostRequestResponse.class);
                        if (info.getCode() == OK){
                            Log.d("success", info.getMessage());
                        }
                    },
                    error -> {
                        Log.d("error", "MovieDetailFragment requestIncreaseLikeDislike error");
                    });
        }*/
        private String typeToAnswer(String type) {
            return type.equals(LIKEYN) ? "Y" : "N";
        }
    }

    public FeedRecommendViewHolder(final Context context, ViewGroup parent,
                          OnItemClickListener onItemClickListener, OnPositionListener onPositionListener) {
        super(LayoutInflater.from(context).inflate(R.layout.recommend_feed_item, parent, false));
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

        setLikeButtonView();

        if (position == listSize - 1) {
            int page = listSize / 20;
            onPositionListener.onLoad(page);
        }
    }

    private void setLikeButtonView() {
        /*likeCountView.setText(String.valueOf(movieDetailInfo.getLike()));
        hateCountView.setText(String.valueOf(movieDetailInfo.getDislike()));

        int likeCount = movieDetailInfo.getLike();
        int hateCount = movieDetailInfo.getDislike();

        LikeView likeView = new LikeView(likeButton, likeCountView, likeCount);
        LikeView hateView = new LikeView(hateButton, hateCountView, hateCount);

        likeButton.setOnClickListener(v ->
                setLikeButtonClickEvent(likeView, likeButton, hateView, hateButton));

        hateButton.setOnClickListener(v ->
                setLikeButtonClickEvent(hateView, hateButton, likeView, likeButton));*/
    }
}