package itmediaengineering.duksung.ootd.search.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Trace;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.main.tab.detail.view.PostDetailActivity;
import itmediaengineering.duksung.ootd.main.tab.upload.EditCategoryPopUpActivity;
import itmediaengineering.duksung.ootd.main.tab.upload.RecognitionType;
import itmediaengineering.duksung.ootd.main.tab.upload.classifier.Classifier;
import itmediaengineering.duksung.ootd.main.tab.upload.classifier.TensorFlowImageClassifier;
import itmediaengineering.duksung.ootd.search.adapter.SearchResultAdapter;
import itmediaengineering.duksung.ootd.search.presenter.SearchContract;
import itmediaengineering.duksung.ootd.search.presenter.SearchPresenter;
import itmediaengineering.duksung.ootd.utils.BundleKey;
import itmediaengineering.duksung.ootd.utils.DeepModelPath;

public class SearchActivity extends AppCompatActivity implements RecognitionContract,
        SearchContract.View {

    private final int GET_GALLERY_IMAGE = 200;
    private final int GET_CORRECT_FILTER_ITEM = 201;

    private Handler handler;
    private HandlerThread handlerThread;

    private Classifier categoryClassifier;
    private Classifier colorClassifier;

    ArrayList<String> categoryList = new ArrayList<>();
    ArrayList<String> colorList = new ArrayList<>();

    private Bitmap img;

    @BindView(R.id.search_activity_image_view)
    ImageView searchImgView;
    @BindView(R.id.search_activity_toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_activity_recognition_tag)
    TextView recognitionTag;
    @BindView(R.id.search_result_recycler_view)
    RecyclerView resultRecyclerView;
    @BindView(R.id.search_activity_filter)
    LinearLayout filterBtn;

    protected SearchPresenter presenter;
    protected SearchResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        adapter = new SearchResultAdapter(this);

        presenter = new SearchPresenter();
        presenter.setAdapterModel(adapter);
        presenter.setAdapterView(adapter);
        presenter.attachView(this);

        if(recognitionTag.getText() == null) {
            recognitionTag.setText("");
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

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

        resultRecyclerView.setAdapter(adapter);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setTitle("판매 정보");
            //actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }

        handlerThread = new HandlerThread("inference");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        // classifier 인식 결과를 콜백으로 알려주기 위한 Contract 설정 1
        RecognitionManager.getInstance().addRecognitionContract(this);
        /*RecognitionManager.getInstance().addRecognitionContract(new RecognitionContract() {
            @Override
            public void onRecognitionChanged(ArrayList<Classifier.Recognition> recognitions) {


            }
        });*/
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GET_GALLERY_IMAGE);
    }

    @Override
    protected synchronized void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap resized = null;

        if (requestCode == GET_GALLERY_IMAGE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = this.getContentResolver().openInputStream(data.getData());
                    img = BitmapFactory.decodeStream(in);

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "JPEG_" + timeStamp + "_";
                    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File file = File.createTempFile(
                            imageFileName,
                            ".jpg",
                            storageDir
                    );

                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    resized = Bitmap.createScaledBitmap(img, 224, 224, true);
                    resized.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);

                    fileOutputStream.flush();
                    fileOutputStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                finish();
            }

            if (resized != null) {
                searchImgView.setImageBitmap(img);
                classifierImg(resized);
            }
        }
    }

    protected synchronized void classifierImg(Bitmap img) {
        categoryClassifier =
                TensorFlowImageClassifier.create(
                        getAssets(),
                        DeepModelPath.MODEL_FILE,
                        DeepModelPath.LABEL_FILE,
                        DeepModelPath.INPUT_SIZE,
                        DeepModelPath.IMAGE_MEAN,
                        DeepModelPath.IMAGE_STD,
                        DeepModelPath.INPUT_NAME,
                        DeepModelPath.OUTPUT_NAME);

        colorClassifier =
                TensorFlowImageClassifier.create(
                        getAssets(),
                        DeepModelPath.COLOR_MODEL_FILE,
                        DeepModelPath.COLOR_LABEL_FILE,
                        DeepModelPath.INPUT_SIZE,
                        DeepModelPath.IMAGE_MEAN,
                        DeepModelPath.IMAGE_STD,
                        DeepModelPath.INPUT_NAME,
                        DeepModelPath.OUTPUT_NAME);

        if (handler != null) {
            handler.post(
                    () -> {
                        List<Classifier.Recognition> categoryResults = categoryClassifier.recognizeImage(img);
                        List<Classifier.Recognition> colorResults = colorClassifier.recognizeImage(img);
                        // 결과 값이 나오면 알 수 있도록 콜백 설정 2
                        RecognitionManager.getInstance().setCategoryRecognitions((ArrayList<Classifier.Recognition>) categoryResults);
                        RecognitionManager.getInstance().setColorRecognitions((ArrayList<Classifier.Recognition>) colorResults);
                    });
        }
        Trace.endSection();
    }

    @Override
    public void onCategoryRecognitionChanged(ArrayList<Classifier.Recognition> recognitions) {
        for (int i = 0; i < recognitions.size(); i++) {
            final String categoryA = recognitions.get(i).getTitle().split(":")[1].split("_")[0];
            final String categoryB = recognitions.get(i).getTitle().split(":")[1].split(categoryA + "_")[1];
            categoryList.add(categoryB);
        }

        if(!colorList.isEmpty()) {
            presenter.getSearchImgResult(categoryList, colorList);
        }
    }

    @Override
    public void onColorRecognitionChanged(ArrayList<Classifier.Recognition> recognitions) {
        for (int i = 0; i < recognitions.size(); i++){
            final String color = recognitions.get(i).getTitle().split(":")[1].split("_")[0];
            colorList.add(color);
        }

        if(!categoryList.isEmpty()) {
            presenter.getSearchImgResult(categoryList, colorList);
        }
    }

    @Override
    public void setTagText(String str) {
        recognitionTag.setText(str);
    }

    @OnClick(R.id.search_activity_filter)
    public void onFilterClick() {
        Intent intent = new Intent(this, SearchFilterPopUpActivity.class);
        intent.putStringArrayListExtra(BundleKey.CATEGORY_LIST, categoryList);
        intent.putStringArrayListExtra(BundleKey.COLOR_LIST, colorList);
        startActivity(intent);
        //startActivityForResult(intent, GET_CORRECT_FILTER_ITEM);
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

    private SearchActivity.onBackPressedListener mOnBackPressedListener;

    public void setOnBackPressedListener(SearchActivity.onBackPressedListener listener) {
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