package itmediaengineering.duksung.ootd.main.tab.category_detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.main.tab.category.adapter.CategoryAdapter;
import itmediaengineering.duksung.ootd.main.tab.category.presenter.CategoryContract;
import itmediaengineering.duksung.ootd.main.tab.category.presenter.CategoryPresenter;

public class CategoryActivity extends AppCompatActivity
        implements CategoryContract.View{
    private static final String TAG = CategoryActivity.class.getSimpleName();

    @BindView(R.id.category_recycler_view)
    RecyclerView categoryRecyclerView;

    protected CategoryPresenter presenter;
    protected CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
    }

    @Override
    public void toast(String msg) {

    }
}
