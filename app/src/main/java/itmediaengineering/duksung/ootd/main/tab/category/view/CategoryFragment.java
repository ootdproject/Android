package itmediaengineering.duksung.ootd.main.tab.category.view;

import android.app.ActivityOptions;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itmediaengineering.duksung.ootd.MainActivity;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.main.tab.category.adapter.CategoryAdapter;
import itmediaengineering.duksung.ootd.main.tab.category.adapter.CategoryBAdatper;
import itmediaengineering.duksung.ootd.main.tab.category.presenter.CategoryContract;
import itmediaengineering.duksung.ootd.main.tab.category.presenter.CategoryPresenter;
import itmediaengineering.duksung.ootd.main.tab.detail.view.PostDetailActivity;
import itmediaengineering.duksung.ootd.utils.CategoryBList;

public class CategoryFragment extends Fragment implements MainActivity.onBackPressedListener,
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
    RecyclerView resultRecyclerView;
    @BindView(R.id.category_btn_recyclerview)
    RecyclerView categoryBRecyclerView;

    protected CategoryPresenter presenter;
    protected CategoryAdapter adapter;
    protected CategoryBAdatper categoryBAdatper;

    private CategoryBList categoryBList;

    private static CategoryFragment categoryFragment;

    public static CategoryFragment newInstance(){
        if(categoryFragment == null){
            categoryFragment = new CategoryFragment();
        }
        return categoryFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_category_2, container, false);
        ButterKnife.bind(this, rootView);

        categoryBList = new CategoryBList();

        adapter = new CategoryAdapter(getActivity());
        categoryBAdatper = new CategoryBAdatper(getActivity());

        presenter = new CategoryPresenter();
        presenter.setAdapterView(adapter);
        presenter.setAdapterModel(adapter);
        presenter.setCategoryListAdapterView(categoryBAdatper);
        presenter.setCategoryListAdapterModel(categoryBAdatper);
        presenter.attachView(this);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        categoryBRecyclerView.setLayoutManager(linearLayoutManager);
        categoryBRecyclerView.setNestedScrollingEnabled(false);
        categoryBRecyclerView.setAdapter(categoryBAdatper);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        resultRecyclerView.setLayoutManager(layoutManager);
        resultRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
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
        resultRecyclerView.setNestedScrollingEnabled(false);
        resultRecyclerView.setAdapter(adapter);

        categoryTop.setSelected(true);
        categoryBAdatper.addCategoryB(categoryBList.getTopList());
        presenter.getCategoryPost("top");

        return rootView;
    }

    @OnClick(R.id.category_top)
    public void onClickCategoryTop(){
        categoryTop.setSelected(true);
        categoryBottom.setSelected(false);
        categoryOuter.setSelected(false);
        categoryPair.setSelected(false);

        categoryBAdatper.clearCategoryBView();
        categoryBAdatper.addCategoryB(categoryBList.getTopList());
        presenter.getCategoryPost("top");

    }

    @OnClick(R.id.category_bottom)
    public void onClickCategoryBottom(){
        categoryTop.setSelected(false);
        categoryBottom.setSelected(true);
        categoryOuter.setSelected(false);
        categoryPair.setSelected(false);

        categoryBAdatper.clearCategoryBView();
        categoryBAdatper.addCategoryB(categoryBList.getBottomList());
        presenter.getCategoryPost("bottom");
    }

    @OnClick(R.id.category_outer)
    public void onClickCategoryOuter(){
        categoryTop.setSelected(false);
        categoryBottom.setSelected(false);
        categoryOuter.setSelected(true);
        categoryPair.setSelected(false);

        categoryBAdatper.clearCategoryBView();
        categoryBAdatper.addCategoryB(categoryBList.getOuterList());
        presenter.getCategoryPost("outer");
    }

    @OnClick(R.id.category_pair)
    public void onClickCategoryPair(){
        categoryTop.setSelected(false);
        categoryBottom.setSelected(false);
        categoryOuter.setSelected(false);
        categoryPair.setSelected(true);

        categoryBAdatper.clearCategoryBView();
        categoryBAdatper.addCategoryB(categoryBList.getPairList());
        presenter.getCategoryPost("pair");
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public boolean onBack() {
        MainActivity activity = (MainActivity)getActivity();
        activity.setOnBackPressedListener(null);
        return true;
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
