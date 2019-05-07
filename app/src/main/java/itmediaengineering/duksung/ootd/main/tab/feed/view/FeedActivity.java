package itmediaengineering.duksung.ootd.main.tab.feed.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.main.tab.feed.adapter.FeedAdapter;
import itmediaengineering.duksung.ootd.main.tab.feed.presenter.FeedContract;
import itmediaengineering.duksung.ootd.main.tab.feed.presenter.FeedPresenter;

public class FeedActivity extends AppCompatActivity
        implements FeedContract.View{

    @BindView(R.id.post_recycler_view)
    RecyclerView postRecyclerView;

    protected FeedAdapter adapter;
    protected FeedPresenter presenter;
    //private int sort = 0;
    //private String searchStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feed);
        ButterKnife.bind(this);

        adapter = new FeedAdapter(this);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postRecyclerView.setAdapter(adapter);

        presenter = new FeedPresenter();
        presenter.attachView(this);
        presenter.setAdapterModel(adapter);
        presenter.setAdapterView(adapter);

        presenter.getFeed();

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
    }

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
