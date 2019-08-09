package itmediaengineering.duksung.ootd.chat_list.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.main.tab.feed.adapter.OnPositionListener;

public class ChatViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = ChatViewHolder.class.getSimpleName();
    private OnChatItemClickListener onItemClickListener;

    private Context context;

    public ChatViewHolder(final Context context, ViewGroup parent,
                          OnChatItemClickListener onItemClickListener
            , OnPositionListener onPositionListener) {
        super(LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false));
        ButterKnife.bind(this, itemView);
        this.onItemClickListener = onItemClickListener;
        //this.onPositionListener = onPositionListener;
        this.context = context;
    }

    public void onBind(Post post, int position, int listSize){
        //userIdView.setText(post.getMemberId());

        //feedItem.setOnClickListener(v -> onItemClickListener.onItemClick());

        //onItemClickListener.onItemClick(post, position));
        //feedItemTitle.setText(post.getMemberId());
        //feedItemCost.setText(post.getPostCount());
        /*Glide.with(context)
                .load(post.getImageUrl())
                .into(feedContentImg);*/

        /*feedItemLikeButton.setOnClickListener(view -> {
            if(feedItemLikeButton.isSelected())
                feedItemLikeButton.setSelected(false);
            else
                feedItemLikeButton.setSelected(true);
        });

        setLikeButtonView();*/
    }
}
