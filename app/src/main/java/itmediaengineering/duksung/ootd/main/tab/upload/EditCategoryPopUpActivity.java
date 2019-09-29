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
import itmediaengineering.duksung.ootd.utils.BundleKey;

public class EditCategoryPopUpActivity extends AppCompatActivity
implements OnItemClickListener{

    @BindView(R.id.edit_category_popup_title)
    TextView popUpTitle;
    @BindView(R.id.edit_category_popup_recyclerview)
    RecyclerView recyclerView;

    protected EditRecognitionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category_pop_up);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        EditRecognitionType editType =
                (EditRecognitionType) intent.getSerializableExtra(BundleKey.CORRECT_RECOGNITION);
        if(editType == EditRecognitionType.CATEGORY){
            popUpTitle.setText("카테고리 재설정");
        } else if (editType == EditRecognitionType.COLOR) {
            popUpTitle.setText("색상 재설정");
        }

        ArrayList<Classifier.Recognition> recognitions =
                getIntent().getParcelableArrayListExtra("recognition");

        adapter = new EditRecognitionAdapter(this, editType);
        adapter.setOnClickListener(this);
        adapter.clearCategories();
        adapter.addCategories(recognitions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(String category) {
        Intent intent = new Intent();
        intent.putExtra(BundleKey.CORRECT_RECOGNITION, category);
        setResult(RESULT_OK, intent);
        finish();
    }
}

interface OnItemClickListener {
    void onItemClick(String category);
}

class EditRecognitionAdapter extends RecyclerView.Adapter<EditRecognitionViewHolder>{
    private OnItemClickListener onItemClickListener;
    private ArrayList<Classifier.Recognition> recognitions;
    private Context context;
    private EditRecognitionType editRecognitionType;

    EditRecognitionAdapter(Context context, EditRecognitionType editRecognitionType) {
        this.context = context;
        this.recognitions = new ArrayList<>();
        this.editRecognitionType = editRecognitionType;
    }

    @NonNull
    @Override
    public EditRecognitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new EditRecognitionViewHolder(context, parent, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EditRecognitionViewHolder editRecognitionViewHolder, int position) {
        if(editRecognitionViewHolder == null)
            return;
        if(editRecognitionType == EditRecognitionType.CATEGORY) {
            editRecognitionViewHolder.onCategoryBind(recognitions.get(position));
        } else if(editRecognitionType == EditRecognitionType.COLOR) {
            editRecognitionViewHolder.onColorBind(recognitions.get(position));
        }
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

class EditRecognitionViewHolder extends RecyclerView.ViewHolder{
    OnItemClickListener onItemClickListener;

    @BindView(R.id.category_edit_recommend_view)
    ConstraintLayout categoryEditRecommendView;
    @BindView(R.id.category_edit_recommend_title)
    TextView categoryTitle;
    @BindView(R.id.category_edit_recommend_confidence)
    TextView categoryConfidence;

    public EditRecognitionViewHolder(final Context context, ViewGroup parent,
                                     OnItemClickListener onItemClickListener) {
        super(LayoutInflater.from(context).inflate(R.layout.category_edit_item, parent, false));
        ButterKnife.bind(this, itemView);
        this.onItemClickListener = onItemClickListener;
    }

    void onCategoryBind(Classifier.Recognition categoryRecognition){
        final String categoryA = categoryRecognition.getTitle().split(":")[1].split("_")[0];
        final String categoryB = categoryRecognition.getTitle().split(":")[1]
                .split(categoryRecognition.getTitle().split(":")[1].split("_")[0] + "_")[1];
        String categoryStr = categoryA + " > " + categoryB;
        final String categoryConfidenceStr = Math.round(categoryRecognition.getConfidence()*10000)/100.0f + " %";
        categoryTitle.setText(categoryStr);
        categoryConfidence.setText(categoryConfidenceStr);

        categoryEditRecommendView.setOnClickListener(v -> onItemClickListener.onItemClick(categoryStr));
    }

    void onColorBind(Classifier.Recognition colorRecognition){
        final String colorStr = colorRecognition.getTitle().split(":")[1];
        final String colorConfidenceStr = Math.round(colorRecognition.getConfidence()*10000)/100.0f + " %";
        categoryTitle.setText(colorStr);
        categoryConfidence.setText(colorConfidenceStr);

        categoryEditRecommendView.setOnClickListener(v -> onItemClickListener.onItemClick(colorStr));
    }
}
