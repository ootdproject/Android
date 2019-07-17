package itmediaengineering.duksung.ootd.main.tab.mypage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;

public class MyPageGalleryViewHolder extends RecyclerView.ViewHolder{

    private static final String TAG = MyPageGalleryViewHolder.class.getSimpleName();

    private Context context;

    @BindView(R.id.my_page_gallery_image_view )
    ImageView itemImageView;

    public MyPageGalleryViewHolder(final Context context, @NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
        //this.onItemClickListener = onItemClickListener;

    }

    public void bindDrawable(String url){
        Glide.with(context)
                .load(url)
                .into(itemImageView);
    }
}
