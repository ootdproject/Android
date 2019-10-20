package itmediaengineering.duksung.ootd.search.view;

import android.content.Intent;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.search.adapter.ChipAdapter;
import itmediaengineering.duksung.ootd.utils.BundleKey;

public class SearchFilterPopUpActivity extends AppCompatActivity {

    @BindView(R.id.search_filter_category_recycler_view)
    RecyclerView categoryRecyclerView;
    @BindView(R.id.chipGroupCategory)
    ChipGroup chipGroupCategory;
    @BindView(R.id.search_filter_color_recycler_view)
    RecyclerView colorRecyclerView;
    @BindView(R.id.chipGroupColor)
    ChipGroup chipGroupColor;

    protected ChipAdapter categoryAdapter;
    protected ChipAdapter colorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        ArrayList<String> categoryList = intent.getStringArrayListExtra(BundleKey.CATEGORY_LIST);
        ArrayList<String> colorList = intent.getStringArrayListExtra(BundleKey.COLOR_LIST);

        categoryAdapter = new ChipAdapter(this);
        colorAdapter = new ChipAdapter(this);
        setUpAdapter(categoryList, categoryAdapter);
        setUpAdapter(colorList, colorAdapter);

        LinearLayoutManager categoryLinearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        categoryRecyclerView.setLayoutManager(categoryLinearLayoutManager);
        categoryRecyclerView.setAdapter(categoryAdapter);

        LinearLayoutManager colorLinearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        colorRecyclerView.setLayoutManager(colorLinearLayoutManager);
        colorRecyclerView.setAdapter(colorAdapter);

        //chipGroupCategory.getCheckedChipId();
        //chipGroupCategory.setSingleSelection(true);
        chipGroupCategory.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                if (chip != null)
                    Toast.makeText(getApplicationContext(), "Chip is " + chip.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setUpAdapter(ArrayList list, ChipAdapter adapter) {
        adapter.clearCategories();
        adapter.addCategories(list);
    }
}


