package itmediaengineering.duksung.ootd.search.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;

public class ChipAdapter extends RecyclerView.Adapter<ChipAdapter.ChipViewHolder> {
    private ArrayList<String> categoryList;
    private Context context;
    private int checkedPosition = 0;
    private String result;

    public ChipAdapter(Context context) {
        this.context = context;
        this.categoryList = new ArrayList<>();
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
        holder.onBind(categoryList.get(position));
    }

    @Override
    public int getItemCount() {
        if(categoryList == null)
            return 0;
        return categoryList.size();
    }

    public void addCategories(ArrayList items) {
        this.categoryList.addAll(items);
        notifyDataSetChanged();
    }

    public void clearCategories() {
        categoryList.clear();
        notifyDataSetChanged();
    }

    class ChipViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.chip_item)
        Chip chip;

        public ChipViewHolder(final Context context, ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.chip_item, parent, false));
            ButterKnife.bind(this, itemView);
        }

        public void onBind(String title) {
            chip.setText(title);
            if (checkedPosition == -1) {
                chip.setChecked(false);
            } else {
                if (checkedPosition == getAdapterPosition()) {
                    chip.setChecked(true);
                    result = chip.getText().toString();
                } else {
                    chip.setChecked(false);
                }
            }

            chip.setOnClickListener(view -> {
                chip.setChecked(true);
                result = chip.getText().toString();
                if (checkedPosition != getAdapterPosition()) {
                    notifyItemChanged(checkedPosition);
                    checkedPosition = getAdapterPosition();
                }
            });
        }
    }

    public String getResearchCombi() {
        return result;
    }
}
