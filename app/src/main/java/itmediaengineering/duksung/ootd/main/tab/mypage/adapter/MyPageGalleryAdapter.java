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

public class MyPageGalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements MyPageAdapterContract.View, MyPageAdapterContract.Model {
    private static final String TAG = MyPageGalleryAdapter.class.getSimpleName();
    private List<Post> galleryItems;
    private Context context;
    /*public MyPageGalleryAdapter(Context context){
        this.context = context;
        this.galleryItems = new ArrayList<>();
    }*/

    public MyPageGalleryAdapter(Context context) {
        this.context = context;
        this.galleryItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // ?
        RecyclerView.ViewHolder holder;
        View view = inflater.inflate(R.layout.mypage_gallery_item, viewGroup, false);
        holder = new MyPageGalleryViewHolder(context, view);
            /*
        } else {
            view = inflater.inflate(R.layout.mypage_gallery_item, viewGroup, false);
            holder = new MyPageGalleryViewHolder(context, view);
        }*/
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        /*if (viewHolder instanceof MyPageHeaderViewHolder) {
            MyPageHeaderViewHolder headerViewHolder = (MyPageHeaderViewHolder) viewHolder;
        } else { // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.*/
        Post galleryItem = this.galleryItems.get(position);
        MyPageGalleryViewHolder galleryViewHolder = (MyPageGalleryViewHolder) viewHolder;
        //galleryViewHolder.onBind(listData.get(position - 1), position);
        galleryViewHolder.bindDrawable(galleryItem.getImageUrl());

    }

    /*@Override
    public void onBindViewHolder(@NonNull MyPageGalleryViewHolder photoHolder, int position) {
        Photo galleryItem = this.galleryItems.get(position);

        photoHolder.bindDrawable(galleryItem.getUrlS());
    }*/

    /*@Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }*/

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
    public void notifyAdapter() {
        notifyDataSetChanged();
    }

    @Override
    public void clearGallery() {
        galleryItems.clear();
        notifyAdapter();
    }
}