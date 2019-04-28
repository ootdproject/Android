package itmediaengineering.duksung.ootd.main.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;

public class MainPagerViewHolder extends Fragment {

    @BindView(R.id.recmd_img)
    ImageView recommendImg;
    @BindView(R.id.recmd_info)
    TextView recommendInfo;
    private MainPagerCallback callback;

    //private String posterUrl;
    //private MovieInfo movieData;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof MainPagerCallback){
            callback = (MainPagerCallback) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if(callback != null){
            callback = null;
        }
    }
    //public static MainPagerViewHolder newInstance(MovieInfo data, int index){
    public static MainPagerViewHolder newInstance(){

        MainPagerViewHolder fragment = new MainPagerViewHolder();
        /*Bundle args = new Bundle();
        args.putInt("index", index);
        args.putParcelable("movie_list_item_data", data);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.recommend_list_item, container, false);
        ButterKnife.bind(this, rootView);

        //recommendImg.setImageDrawable(getActivity().getDrawable(R.drawable.recmd_img_2));

        //Bundle bundle = getArguments();

        /*if(bundle !=null) {
            movieData = bundle.getParcelable("movie_list_item_data");
            movieTitle.setText(bundle.getInt("index") +". " + movieData.getTitle());
            movieInfo.setText("예매율 "+ String.valueOf(movieData.getReservationRate()) +"% | "+ String.valueOf(movieData.getGrade()) + "세 관람가");
            posterUrl = movieData.getImage();
        }*/

        //서버에서 넘어온 것 쓸 때 사용
        /*Glide.with(getActivity())
                .load(posterUrl)
                .into(moviePoster);*/

        /*Button detailViewBtn = rootView.findViewById(R.id.detail_view_open);
        detailViewBtn.setOnClickListener(v -> {
            if(callback != null){
                callback.onDetailFragmentSelected(movieData.getId());
            }
        });*/

        return rootView;
    }

}
