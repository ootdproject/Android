package itmediaengineering.duksung.ootd.main.tab.feed.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.data.feed.Post;

public class FeedViewHolder extends RecyclerView.ViewHolder
                    implements View.OnClickListener {

    //private AdapterView.OnItemClickListener onItemClickListener;

    @BindView(R.id.user_thum_nail_img)
    ImageView userThumNailImg;
    @BindView(R.id.user_id_view)
    TextView userIdView;
    @BindView(R.id.feed_content_img)
    ImageView feedContentImg;
    @BindView(R.id.feed_item_like_button)
    Button feedItemLikeButton;
    private Context context;

    @Override
    public void onClick(View v) {
        Toast.makeText(context, "좋아요 눌림!", Toast.LENGTH_SHORT).show();
    }

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

    public FeedViewHolder(final Context context, ViewGroup parent, AdapterView.OnItemClickListener onItemClickListener) {
        super(LayoutInflater.from(context).inflate(R.layout.feed_item, parent, false));
        ButterKnife.bind(this, itemView);
        parent.setOnClickListener(this);
        //this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    public void onBind(Post post, int position, int listSize){
        userIdView.setText(post.getMemberId());

        Glide.with(context)
                .load(post.getPostUrl())
                .into(feedContentImg);

        feedItemLikeButton.setOnClickListener(v -> {
            if(feedItemLikeButton.isSelected())
                feedItemLikeButton.setSelected(false);
            else
            feedItemLikeButton.setSelected(true);
        });

        setLikeButtonView();
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
