package itmediaengineering.duksung.ootd.main.tab.upload;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.main.tab.upload.classifier.Classifier;

public class EditCategoryPopUpActivity extends AppCompatActivity
implements OnItemClickListener{

    @BindView(R.id.edit_category_popup_recyclerview)
    RecyclerView recyclerView;

    protected CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category_pop_up);
        ButterKnife.bind(this);

        ArrayList<Classifier.Recognition> recognitions =
                getIntent().getParcelableArrayListExtra("recognition");

        adapter = new CategoryAdapter(this);
        adapter.setOnClickListener(this);
        adapter.clearCategories();
        adapter.addCategories(recognitions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(String category) {
        Intent intent = new Intent();
        intent.putExtra("correct_category_string", category);
        setResult(RESULT_OK, intent);
        finish();
    }
}

interface OnItemClickListener {
    void onItemClick(String category);
}

class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder>{
    private OnItemClickListener onItemClickListener;
    private ArrayList<Classifier.Recognition> recognitions;
    private Context context;

    CategoryAdapter(Context context) {
        this.context = context;
        this.recognitions = new ArrayList<>();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new CategoryViewHolder(context, parent, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int position) {
        if(categoryViewHolder == null)
            return;
        categoryViewHolder.onBind(recognitions.get(position));
    }

    public void addCategories(ArrayList items) {
        this.recognitions.addAll(items);
        notifyAdapter();
    }

    @Override
    public int getItemCount() {
        return this.recognitions.size();
    }

    public void notifyAdapter() {
        notifyDataSetChanged();
    }

    public void clearCategories() {
        recognitions.clear();
        notifyAdapter();
    }

    public void setOnClickListener(OnItemClickListener onClickListener) {
        this.onItemClickListener = onClickListener;
    }
}

class CategoryViewHolder extends RecyclerView.ViewHolder{
    OnItemClickListener onItemClickListener;

    @BindView(R.id.category_edit_recommend_view)
    ConstraintLayout categoryEditRecommendView;
    @BindView(R.id.category_edit_recommend_title)
    TextView categoryTitle;
    @BindView(R.id.category_edit_recommend_confidence)
    TextView categoryConfidence;

    public CategoryViewHolder(final Context context, ViewGroup parent,
                              OnItemClickListener onItemClickListener) {
        super(LayoutInflater.from(context).inflate(R.layout.category_edit_item, parent, false));
        ButterKnife.bind(this, itemView);
        this.onItemClickListener = onItemClickListener;
    }

    void onBind(Classifier.Recognition categoryRecognition){
        final String categoryA = categoryRecognition.getTitle().split(":")[1].split("_")[0];
        final String categoryB = categoryRecognition.getTitle().split(":")[1]
                .split(categoryRecognition.getTitle().split(":")[1].split("_")[0] + "_")[1];
        String categoryStr = categoryA + " > " + categoryB;
        final String categoryConfidenceStr = Math.round(categoryRecognition.getConfidence()*10000)/100.0f + " %";
        categoryTitle.setText(categoryStr);
        categoryConfidence.setText(categoryConfidenceStr);

        categoryEditRecommendView.setOnClickListener(v -> onItemClickListener.onItemClick(categoryStr));
    }
}
