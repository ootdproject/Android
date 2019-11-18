package itmediaengineering.duksung.ootd.main.tab.category.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

public class CategoryBAdatper extends RecyclerView.Adapter<CategoryBViewHolder>
        implements CategoryBAdapterContract.View, CategoryBAdapterContract.Model {
    private ArrayList<String> items;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public CategoryBAdatper(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
    }

    @NonNull
    @Override
    public CategoryBViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new CategoryBViewHolder(context, parent, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryBViewHolder holder, int position) {
        if(holder == null)
            return;
        holder.onBind(items.get(position));
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
    public void notifyAdapter() {
        notifyDataSetChanged();
    }

    @Override
    public void addCategoryB(ArrayList categoryB) {
        this.items.addAll(categoryB);
        notifyAdapter();
    }

    @Override
    public void clearCategoryBView() {
        items.clear();
        notifyAdapter();
    }
}