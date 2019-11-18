package itmediaengineering.duksung.ootd.main.tab.category.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;

public class CategoryBViewHolder extends RecyclerView.ViewHolder {
    private OnItemClickListener onItemClickListener;

    private Context context;

    @BindView(R.id.category_b_list_item)
    TextView categoryBListItem;

    public CategoryBViewHolder(final Context context, ViewGroup parent, OnItemClickListener onItemClickListener) {
        super(LayoutInflater.from(context).inflate(R.layout.category_b_list_item, parent, false));
        ButterKnife.bind(this, itemView);
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    public void onBind(String categoryName) {
        categoryBListItem.setText(categoryName);

        categoryBListItem.setOnClickListener(v ->
                onItemClickListener.onCategoryBClick(categoryName, categoryBListItem));
    }
}
