package itmediaengineering.duksung.ootd.feed.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.feed.adapter.FeedAdapter;
import itmediaengineering.duksung.ootd.feed.presenter.FeedContract;
import itmediaengineering.duksung.ootd.feed.presenter.FeedPresenter;

public class FeedActivity extends AppCompatActivity
        implements FeedContract.View{

    protected FeedAdapter adapter;
    protected FeedPresenter presenter;
    private int sort = 0;
    private String searchStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);
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
