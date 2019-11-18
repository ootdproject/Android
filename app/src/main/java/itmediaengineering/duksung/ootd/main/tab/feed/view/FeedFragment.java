package itmediaengineering.duksung.ootd.main.tab.feed.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.main.tab.detail.view.PostDetailActivity;
import itmediaengineering.duksung.ootd.main.tab.feed.adapter.FeedAdapter;
import itmediaengineering.duksung.ootd.main.tab.feed.adapter.FeedRecommendAdapter;
import itmediaengineering.duksung.ootd.main.tab.feed.presenter.FeedContract;
import itmediaengineering.duksung.ootd.main.tab.feed.presenter.FeedPresenter;

public class FeedFragment extends Fragment
        implements FeedContract.View, LocationUpdatable{
    private static final String TAG = FeedFragment.class.getSimpleName();

    @BindView(R.id.feed_recycler_view)
    RecyclerView feedRecyclerView;
    @BindView(R.id.feed_recommend_recycler_view)
    RecyclerView feedRecommendRecyclerView;

    protected FeedAdapter feedAdapter;
    protected FeedRecommendAdapter recommendFeedAdapter;
    protected FeedPresenter presenter = new FeedPresenter();

    public static FeedFragment newInstance(){
        return new FeedFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedAdapter = new FeedAdapter(getContext());
        recommendFeedAdapter = new FeedRecommendAdapter(getContext());

        presenter.setFeedAdapterModel(feedAdapter);
        presenter.setFeedAdapterView(feedAdapter);

        presenter.setRecommendFeedAdapterModel(recommendFeedAdapter);
        presenter.setRecommendFeedAdapterView(recommendFeedAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, rootView);

        presenter.attachView(this);

        LinearLayoutManager recommendLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager feedLayoutManager = new GridLayoutManager(getContext(), 2);

        feedRecommendRecyclerView.setLayoutManager(recommendLayoutManager);
        feedRecommendRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = 6;
                outRect.right = 6;
                outRect.bottom = 6;

                // Add top margin only for the first item to avoid double space between items
                if (parent.getChildLayoutPosition(view) != 0
                        || parent.getChildLayoutPosition(view) != 1) {
                    outRect.top = 6;
                } else {
                    outRect.top = 0;
                }
            }
        });

        feedRecyclerView.setLayoutManager(feedLayoutManager);
        feedRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = 6;
                outRect.right = 6;
                outRect.bottom = 6;

                // Add top margin only for the first item to avoid double space between items
                if (parent.getChildLayoutPosition(view) != 0
                        || parent.getChildLayoutPosition(view) != 1) {
                    outRect.top = 6;
                } else {
                    outRect.top = 0;
                }
            }
        });
        feedRecyclerView.setNestedScrollingEnabled(false);
        setUpAdapter();

        return rootView;
    }

    private void setUpAdapter(){
        if(isAdded()){  // 프래그먼트가 액티비티에 연결되었는지(호스팅되고 있는지) 확인
            // 그렇지 않다면 액티비티에 의존한 오퍼레이션 실행은 실패
            // 프래그먼트는 어떤 액티비티에도 연결되지 않은 상태로 존재할 수 있다
            feedRecyclerView.setAdapter(feedAdapter);
            feedRecommendRecyclerView.setAdapter(recommendFeedAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void toast(String msg) {
        Runnable r = () -> Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        getActivity().runOnUiThread(r);
    }

    @Override
    public void onSuccessGetList() {
        Log.d(TAG, "Success getting Feed!");
    }

    @Override
    public void onSuccessPostLike() {
        toast("Like Post!");
    }

    @Override
    public void startPostDetailActivity(Post post, ImageView sharedView) {
        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        intent.putExtra("post", post);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                sharedView, "movieWork");

        startActivity(intent, options.toBundle());
    }

    @Override
    public void onLocationUpdated(String location) {
        presenter.getFeedByLocationString(location);
    }

    @Override
    public void onFeedResumed() {
        //presenter.getFeedByLocationString();
    }
}
