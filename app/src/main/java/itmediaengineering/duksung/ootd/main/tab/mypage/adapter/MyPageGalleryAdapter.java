package itmediaengineering.duksung.ootd.main.tab.mypage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.data.mygallery.Photo;
import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.main.tab.feed.adapter.OnPositionListener;
import itmediaengineering.duksung.ootd.main.tab.mypage.presenter.SaleType;

public class MyPageGalleryAdapter extends RecyclerView.Adapter<MyPageGalleryViewHolder>
        implements MyPageAdapterContract.View, MyPageAdapterContract.Model {
    private static final String TAG = MyPageGalleryAdapter.class.getSimpleName();
    private OnItemClickListener onItemClickListener;
    private List<Post> galleryItems;
    private Context context;
    private SaleType saleType;

    public MyPageGalleryAdapter(Context context, SaleType saleType) {
        this.context = context;
        this.saleType = saleType;
        this.galleryItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyPageGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new MyPageGalleryViewHolder(context, parent, saleType, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPageGalleryViewHolder holder, int position) {
        if(holder == null)
            return;
        holder.onBind(galleryItems.get(position));
    }

    @Override
    public void addPosts(ArrayList items) {
        this.galleryItems.addAll(items);
        notifyAdapter();
    }

    @Override
    public int getItemCount() {
        return this.galleryItems.size();
    }

    @Override
    public void setOnPositionListener(OnPositionListener onPositionListener) {

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
    public void clearGallery() {
        galleryItems.clear();
        notifyAdapter();
    }
}