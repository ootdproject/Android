package itmediaengineering.duksung.ootd.main.tab.feed.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
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
import itmediaengineering.duksung.ootd.main.tab.feed.presenter.FeedContract;
import itmediaengineering.duksung.ootd.main.tab.feed.presenter.FeedPresenter;

public class FeedFragment extends Fragment implements FeedContract.View {
    private static final String TAG = FeedFragment.class.getSimpleName();

    @BindView(R.id.post_recycler_view)
    RecyclerView postRecyclerView;

    public final static String SHARE_VIEW_NAME = "SHARE_VIEW_NAME";

    protected FeedAdapter adapter;
    protected FeedPresenter presenter;
    //private int sort = 0;
    //private String searchStr = "";

    public static FeedFragment newInstance(){
        return new FeedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, rootView);

        adapter = new FeedAdapter(getContext());

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        postRecyclerView.setLayoutManager(layoutManager);
        postRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
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
        setUpAdapter();

        return rootView;
    }

    private void setUpAdapter(){
        if(isAdded()){  // 프래그먼트가 액티비티에 연결되었는지(호스팅되고 있는지) 확인
            // 그렇지 않다면 액티비티에 의존한 오퍼레이션 실행은 실패
            // 프래그먼트는 어떤 액티비티에도 연결되지 않은 상태로 존재할 수 있다
            postRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenter = new FeedPresenter();
        presenter.attachView(this);
        presenter.setAdapterModel(adapter);
        presenter.setAdapterView(adapter);
        presenter.getFeed();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        //refreshList(null);
    }

    protected void refreshList(String search) {
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
        Runnable r = () -> Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        getActivity().runOnUiThread(r);
    }

    @Override
    public void onSuccessGetList() {
        Log.d(TAG, "Success getting Feed!");
    }

    @Override
    public void startPostDetailActivity(Post post, ImageView sharedView) {
        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        intent.putExtra("post", post);

        Pair<View, String> pair = new Pair(sharedView, SHARE_VIEW_NAME);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                sharedView, "movieWork");

        startActivity(intent, options.toBundle());
    }
}
