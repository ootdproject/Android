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
import itmediaengineering.duksung.ootd.main.tab.feed.adapter.OnPositionListener;

public class MyPageGalleryAdapter extends RecyclerView.Adapter<MyPageGalleryViewHolder>
        implements MyPageAdapterContract.View, MyPageAdapterContract.Model {

    private List<Photo> galleryItems;
    private Context context;

    public MyPageGalleryAdapter(Context context){
        this.context = context;
        this.galleryItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyPageGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.mypage_gallery_item, viewGroup, false);
        return new MyPageGalleryViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPageGalleryViewHolder photoHolder, int position) {
        Photo galleryItem = this.galleryItems.get(position);

        //thumbnailDownloader.queueThumbnail(photoHolder, galleryItem.getUrl());
        photoHolder.bindDrawable(galleryItem.getUrlS());
    }

    @Override
    public void addPhotos(ArrayList items) {
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