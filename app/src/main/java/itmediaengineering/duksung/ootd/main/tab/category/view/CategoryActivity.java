package itmediaengineering.duksung.ootd.main.tab.category.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.main.tab.category.adapter.CategoryAdapter;
import itmediaengineering.duksung.ootd.main.tab.category.presenter.CategoryContract;
import itmediaengineering.duksung.ootd.main.tab.category.presenter.CategoryPresenter;
import itmediaengineering.duksung.ootd.main.tab.detail.view.PostDetailActivity;

public class CategoryActivity extends AppCompatActivity
        implements CategoryContract.View{
    private static final String TAG = CategoryActivity.class.getSimpleName();

    @BindView(R.id.category_recycler_view)
    RecyclerView categoryRecyclerView;
    @BindView(R.id.category_toolbar)
    Toolbar toolbar;

    protected CategoryPresenter presenter;
    protected CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        String category = getIntent().getStringExtra("category");

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getIntent().getStringExtra("title"));
            //actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }

        adapter = new CategoryAdapter(this);

        presenter = new CategoryPresenter();
        presenter.setAdapterModel(adapter);
        presenter.setAdapterView(adapter);
        presenter.attachView(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

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
        presenter.getCategoryPost(category);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void toast(String msg) {
        Runnable r = () -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        this.runOnUiThread(r);
    }

    @Override
    public void startPostDetailActivity(Post post, ImageView sharedView) {
        Intent intent = new Intent(this, PostDetailActivity.class);
        intent.putExtra("post", post);

        //Pair<View, String> pair = new Pair(sharedView, SHARE_VIEW_NAME);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                sharedView, "movieWork");

        startActivity(intent, options.toBundle());
    }

    public interface onBackPressedListener {
        boolean onBack();
    }
    private PostDetailActivity.onBackPressedListener mOnBackPressedListener;

    public void setOnBackPressedListener(PostDetailActivity.onBackPressedListener listener) {
        mOnBackPressedListener = listener;
    }

    @Override
    public void onBackPressed() {
        if (mOnBackPressedListener != null && mOnBackPressedListener.onBack()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
