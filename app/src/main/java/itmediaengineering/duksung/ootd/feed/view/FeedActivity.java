package itmediaengineering.duksung.ootd.feed.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.feed.adapter.FeedAdapter;
import itmediaengineering.duksung.ootd.feed.presenter.FeedContract;
import itmediaengineering.duksung.ootd.feed.presenter.FeedPresenter;

public class FeedActivity extends AppCompatActivity
        implements FeedContract.View{

    @BindView(R.id.post_recycler_view)
    RecyclerView postRecyclerView;

    protected FeedAdapter adapter;
    protected FeedPresenter presenter;
    private int sort = 0;
    private String searchStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);

        adapter = new FeedAdapter(this);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postRecyclerView.setAdapter(adapter);
        presenter = new FeedPresenter();
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
