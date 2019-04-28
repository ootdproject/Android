package itmediaengineering.duksung.ootd.feed.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.data.feed.Post;

public class FeedViewHolder extends RecyclerView.ViewHolder{

    private AdapterView.OnItemClickListener onItemClickListener;

    @BindView(R.id.user_thum_nail_img)
    ImageView userThumNailImg;
    @BindView(R.id.user_id_view)
    TextView userIdView;
    @BindView(R.id.feed_content_img)
    ImageView feedContentImg;
    @BindView(R.id.feed_item_like_button)
    Button feedItemLikeButton;
    private Context context;

    public FeedViewHolder(final Context context, ViewGroup parent, AdapterView.OnItemClickListener onItemClickListener) {
        super(LayoutInflater.from(context).inflate(R.layout.feed_item, parent, false));
        ButterKnife.bind(this, itemView);
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    public void onBind(Post post, int position, int listSize){
        userIdView.setText(post.getMemberId());

        Glide.with(context)
                .load(post.getPostUrl())
                .into(feedContentImg);
    }
}
