package itmediaengineering.duksung.ootd.main.tab.category.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

import itmediaengineering.duksung.ootd.data.post.Post;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder>
        implements CategoryAdapterContract.View, CategoryAdapterContract.Model {
    private ArrayList<Post> items;
    private OnItemClickListener onItemClickListener;
    private OnPositionListener onPositionListener;
    private Context context;

    public CategoryAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new CategoryViewHolder(context, parent, onItemClickListener, onPositionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
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
}
