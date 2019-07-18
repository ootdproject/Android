package itmediaengineering.duksung.ootd.main.tab.feed.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import java.util.ArrayList;

import itmediaengineering.duksung.ootd.data.feed.Post;

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder>
        implements FeedAdapterContract.View, FeedAdapterContract.Model{
    private static final String TAG = FeedAdapter.class.getSimpleName();
    private ArrayList<Post> items;
    private OnItemClickListener onItemClickListener;
    private OnPositionListener onPositionListener;
    private Context context;

    public FeedAdapter(Context context){
        this.context = context;
        this.items = new ArrayList<>();
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        return new FeedViewHolder(context, parent, onItemClickListener, onPositionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        if(holder == null)
            return;
        holder.onBind(items.get(position), position, getItemCount());
    }

    @Override
    public int getItemCount() {
        if(items == null)
            return 0;
        return items.size();
    }

    @Override
    public void setOnClickListener(OnItemClickListener onClickListener) {
        this.onItemClickListener = onClickListener;
    }

    @Override
    public void setOnPositionListener(OnPositionListener onPositionListener) {
        this.onPositionListener = onPositionListener;
    }

    @Override
    public void notifyAdapter() {
        notifyDataSetChanged();
    }

    /*@Override
    public void setFeeds(ArrayList items) {
        Log.d(TAG, "setItems");
        this.items.clear();
        this.items = items;
        notifyDataSetChanged();
    }*/

    @Override
    public void addPosts(ArrayList items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public void clearFeed() {
        items.clear();
        notifyDataSetChanged();
    }
}
