package itmediaengineering.duksung.ootd.main.tab.category.view;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itmediaengineering.duksung.ootd.MainActivity;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.main.tab.category.adapter.CardPagerAdapter;
import itmediaengineering.duksung.ootd.main.tab.category.adapter.CardPagerViewHolder;
import itmediaengineering.duksung.ootd.main.tab.category.adapter.CategoryAdapter;
import itmediaengineering.duksung.ootd.main.tab.category.presenter.CategoryContract;
import itmediaengineering.duksung.ootd.main.tab.category.presenter.CategoryPresenter;
import itmediaengineering.duksung.ootd.main.tab.detail.view.PostDetailActivity;
import itmediaengineering.duksung.ootd.main.tab.upload.UploadActivity;

public class CategoryFragment extends Fragment implements MainActivity.onKeyBackPressedListener,
                                                        CategoryContract.View{
    public static final String TAG = CategoryFragment.class.getSimpleName();

    @BindView(R.id.category_top)
    TextView categoryTop;
    @BindView(R.id.category_bottom)
    TextView categoryBottom;
    @BindView(R.id.category_pair)
    TextView categoryPair;
    @BindView(R.id.category_outer)
    TextView categoryOuter;
    @BindView(R.id.category_fragment_recyclerview)
    RecyclerView categoryRecyclerView;
    /*@BindView(R.id.pager)
    ViewPager viewPager;*/

    protected FragmentManager fm;

    protected CategoryPresenter presenter;
    protected CategoryAdapter adapter;

    public static CategoryFragment newInstance(){
        return new CategoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_category_2, container, false);
        ButterKnife.bind(this, rootView);

        fm = getChildFragmentManager();

        /*CardPagerAdapter pagerAdapter = new CardPagerAdapter(getActivity().getSupportFragmentManager());
        pagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(3);
        viewPager.setPadding(100, 0, 100, 0);

        for (int i = 0; i < 5; i++) {
            pagerAdapter.addItem(CardPagerViewHolder.newInstance());
        }

        fm.beginTransaction()
                .add(R.id.pager, CategoryRecommendFragment.newInstance(), "mainFragment")
                .commit();*/

        adapter = new CategoryAdapter(getActivity());

        presenter = new CategoryPresenter();
        presenter.setAdapterModel(adapter);
        presenter.setAdapterView(adapter);
        presenter.attachView(this);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
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

        categoryRecyclerView.setAdapter(adapter);
        categoryTop.setSelected(true);
        presenter.getCategoryPost("top");

        return rootView;
    }

    @OnClick(R.id.category_top)
    public void onClickCategoryTop(){
        categoryTop.setSelected(true);
        categoryBottom.setSelected(false);
        categoryOuter.setSelected(false);
        categoryPair.setSelected(false);
        presenter.getCategoryPost("top");
    }

    @OnClick(R.id.category_bottom)
    public void onClickCategoryBottom(){
        categoryTop.setSelected(false);
        categoryBottom.setSelected(true);
        categoryOuter.setSelected(false);
        categoryPair.setSelected(false);
        presenter.getCategoryPost("bottom");
    }

    @OnClick(R.id.category_outer)
    public void onClickCategoryOuter(){
        categoryTop.setSelected(false);
        categoryBottom.setSelected(false);
        categoryOuter.setSelected(true);
        categoryPair.setSelected(false);
        presenter.getCategoryPost("outer");
    }

    @OnClick(R.id.category_pair)
    public void onClickCategoryPair(){
        categoryTop.setSelected(false);
        categoryBottom.setSelected(false);
        categoryOuter.setSelected(false);
        categoryPair.setSelected(true);
        presenter.getCategoryPost("pair");
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onBack() {
        MainActivity activity = (MainActivity)getActivity();
        activity.setOnBackPressedListener(null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("Other", "onAttach()");
        ((MainActivity)context).setOnBackPressedListener(this);
    }

    @Override
    public void toast(String msg) {
        Runnable r = () -> Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        getActivity().runOnUiThread(r);
    }

    @Override
    public void startPostDetailActivity(Post post, ImageView sharedView) {
        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        intent.putExtra("post", post);

        //Pair<View, String> pair = new Pair(sharedView, SHARE_VIEW_NAME);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                sharedView, "movieWork");

        startActivity(intent, options.toBundle());
    }
}
