package itmediaengineering.duksung.ootd.search.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

import itmediaengineering.duksung.ootd.data.post.Post;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder>
        implements SearchAdapterContract.View, SearchAdapterContract.Model {
    private ArrayList<Post> items;
    private OnItemClickListener onItemClickListener;
    private OnPositionListener onPositionListener;
    private Context context;

    public SearchResultAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new SearchResultViewHolder(context, parent, onItemClickListener, onPositionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        if (holder == null)
            return;
        holder.onBind(items.get(position), position, getItemCount());
    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;
        return items.size();
    }

    @Override
    public void addPosts(ArrayList items) {
        this.items.addAll(items);
        notifyAdapter();
    }

    @Override
    public void clearFeed() {
        items.clear();
        notifyAdapter();
    }

    @Override
    public void setOnPositionListener(OnPositionListener onPositionListener) {
        this.onPositionListener = onPositionListener;
    }

    @Override
    public void setOnClickListener(OnItemClickListener onClickListener) {
        this.onItemClickListener = onClickListener;
    }

    @Override
    public void notifyAdapter() {
        notifyDataSetChanged();
    }
}
