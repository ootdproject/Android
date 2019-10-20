package itmediaengineering.duksung.ootd.search.adapter;

import android.content.Context;
import android.support.design.chip.Chip;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;

public class ChipViewHolder extends RecyclerView.ViewHolder {
    private Context context;

    @BindView(R.id.chip_item)
    Chip chip;

    public ChipViewHolder(final Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.chip_item, parent, false));
        ButterKnife.bind(this, itemView);
        this.context = context;
    }

    public void onBind(String title) {
        chip.setText(title);
    }
}
