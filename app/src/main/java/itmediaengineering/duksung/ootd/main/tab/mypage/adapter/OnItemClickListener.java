package itmediaengineering.duksung.ootd.main.tab.mypage.adapter;

import android.widget.ImageView;

import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.main.tab.mypage.presenter.SaleType;

public interface OnItemClickListener {
    void onItemClick(Post post, SaleType saleType, ImageView shareView);
}
