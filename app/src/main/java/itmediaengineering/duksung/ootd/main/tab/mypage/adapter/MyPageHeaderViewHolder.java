package itmediaengineering.duksung.ootd.main.tab.mypage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;

public class MyPageHeaderViewHolder extends RecyclerView.ViewHolder{

    private static final String TAG = MyPageHeaderViewHolder.class.getSimpleName();

    private Context context;

    @BindView(R.id.my_page_user_image)
    ImageView userImg;
    @BindView(R.id.my_page_user_nickname)
    TextView userNick;
    @BindView(R.id.my_page_user_description)
    TextView userDscrp;

    public MyPageHeaderViewHolder(final Context context, @NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}