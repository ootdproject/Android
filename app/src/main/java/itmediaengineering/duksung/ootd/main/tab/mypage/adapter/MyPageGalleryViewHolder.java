package itmediaengineering.duksung.ootd.main.tab.mypage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.data.post.Post;

public class MyPageGalleryViewHolder extends RecyclerView.ViewHolder{
    private static final String TAG = MyPageGalleryViewHolder.class.getSimpleName();
    private OnItemClickListener onItemClickListener;

    private Context context;

    @BindView(R.id.my_page_gallery_image_view)
    ImageView itemImageView;

    public MyPageGalleryViewHolder(final Context context, ViewGroup parent,
                                   OnItemClickListener onItemClickListener) {
        super(LayoutInflater.from(context).inflate(R.layout.mypage_gallery_item, parent, false));
        ButterKnife.bind(this, itemView);
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    public void onBind(Post post){

        Glide.with(context)
                .load(post.getImageUrl())
                .into(itemImageView);

        itemImageView.setOnClickListener(v -> {
            if(onItemClickListener != null) {
                onItemClickListener.onItemClick(post);
            }
        });
    }
}
