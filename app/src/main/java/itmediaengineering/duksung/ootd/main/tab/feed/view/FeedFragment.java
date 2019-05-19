package itmediaengineering.duksung.ootd.main.tab.feed.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.main.tab.feed.adapter.FeedAdapter;
import itmediaengineering.duksung.ootd.main.tab.feed.presenter.FeedContract;
import itmediaengineering.duksung.ootd.main.tab.feed.presenter.FeedPresenter;

public class FeedFragment extends Fragment implements FeedContract.View {

    @BindView(R.id.post_recycler_view)
    RecyclerView postRecyclerView;

    protected FeedAdapter adapter;
    protected FeedPresenter presenter;
    //private int sort = 0;
    //private String searchStr = "";

    public static FeedFragment newInstance(){
        return new FeedFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new FeedAdapter(getContext());

        postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postRecyclerView.setAdapter(adapter);

        presenter = new FeedPresenter();
        presenter.attachView(this);
        presenter.setAdapterModel(adapter);
        presenter.setAdapterView(adapter);
        presenter.getFeed();
    }

    //galleryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    //galleryAdapter = new GalleryAdapter(getContext());

        /*galleryAdapter.setOnItemClickListener((holder, view, position) -> {
            GalleryItem item = galleryAdapter.getItem(position);

            if (item.isPhoto()) {
                Intent intent = new Intent(getContext(), GalleryViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                intent.putExtra("url", item.getMediaUrl());
                startActivity(intent);
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getMediaUrl()));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
            }
        });*/

    //galleryRecyclerView.setAdapter(galleryAdapter);

    @Override
    public void toast(String msg) {
        /*Runnable r = () -> {
            Toast.makeText(msg, Toast.LENGTH_SHORT).show();
        };
        getActivity().runOnUiThread(r);*/
    }

    @Override
    public void onSuccessGetList() {

    }
}
