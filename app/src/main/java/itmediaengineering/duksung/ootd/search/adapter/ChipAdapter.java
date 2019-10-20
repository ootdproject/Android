package itmediaengineering.duksung.ootd.search.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ChipAdapter extends RecyclerView.Adapter<ChipViewHolder> {
    private ArrayList<String> items;
    private Context context;

    public ChipAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
    }
    @NonNull
    @Override
    public ChipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ChipViewHolder(context, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ChipViewHolder holder, int position) {
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

    public void addCategories(ArrayList items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clearCategories() {
        items.clear();
        notifyDataSetChanged();
    }
}
