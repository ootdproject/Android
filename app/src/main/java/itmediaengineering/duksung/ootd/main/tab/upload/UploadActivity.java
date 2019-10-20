package itmediaengineering.duksung.ootd.main.tab.upload;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Trace;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.data.post.PostRequest;
import itmediaengineering.duksung.ootd.main.tab.upload.classifier.Classifier;
import itmediaengineering.duksung.ootd.main.tab.upload.classifier.TensorFlowImageClassifier;
import itmediaengineering.duksung.ootd.main.tab.upload.presenter.UploadContract;
import itmediaengineering.duksung.ootd.main.tab.upload.presenter.UploadPresenter;
import itmediaengineering.duksung.ootd.utils.BundleKey;
import itmediaengineering.duksung.ootd.utils.DeepModelPath;
import itmediaengineering.duksung.ootd.utils.PaymentTextWatcher;

public class UploadActivity extends AppCompatActivity implements UploadContract.View {
    public static final String TAG = UploadActivity.class.getSimpleName();

    private Classifier categoryClassifier;
    private Classifier colorClassifier;

    ArrayList<Classifier.Recognition> recognitions = new ArrayList<>();
    ArrayList<Classifier.Recognition> colorRecognitions = new ArrayList<>();

    @BindView(R.id.upload_activity_toolbar)
    ConstraintLayout toolbar;
    @BindView(R.id.upload_back_btn)
    ImageView backBtn;
    @BindView(R.id.upload_ok_btn)
    ImageView okBtn;
    @BindView(R.id.upload_img_btn)
    ImageView uploadImgBtn;
    @BindView(R.id.upload_camara_icon)
    ImageView uploadCameraIcon;
    @BindView(R.id.upload_camera_text)
    TextView uploadCameraText;
    @BindView(R.id.upload_activity_title_view)
    EditText title;
    @BindView(R.id.upload_activity_cost_view)
    EditText cost;
    @BindView(R.id.upload_activity_category_view)
    TextView category;
    @BindView(R.id.upload_activity_color_view)
    TextView color;
    @BindView(R.id.upload_activity_dong)
    EditText dong;
    @BindView(R.id.upload_activity_description_view)
    EditText description;

    protected UploadPresenter presenter;

    private boolean isUploadMode = true;
    private int postId;
    private File file;

    private Handler handler;
    private HandlerThread handlerThread;

    private final int GET_GALLERY_IMAGE = 200;
    private final int GET_CORRECT_CATEGORY_RECOGNITION = 201;
    private final int GET_CORRECT_COLOR_RECOGNITION = 202;

    @Override
    protected synchronized void onResume() {
        super.onResume();

        handlerThread = new HandlerThread("inference");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);

        cost.addTextChangedListener(new PaymentTextWatcher(cost));

        presenter = new UploadPresenter();
        presenter.attachView(this);

        if (getIntent().getExtras() != null) {
            Post post = getIntent().getParcelableExtra("post");
            isUploadMode = false;
            postId = post.getId();

            uploadCameraIcon.setVisibility(View.GONE);
            uploadCameraText.setVisibility(View.GONE);
            uploadImgBtn.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(post.getImageUrl())
                    .into(uploadImgBtn);

            title.setText(post.getTitle());
            cost.setText(post.getCost());
            category.setText(post.getCategoryA() + " > " + post.getCategoryB());
            color.setText(post.getColor());
            dong.setText(post.getDong());
            description.setText(post.getDescription());
        }
    }

    @OnClick(R.id.upload_img_btn)
    void onGalleryIntentBtnClick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);*/
        startActivityForResult(intent, GET_GALLERY_IMAGE);
        Log.d(TAG, "clicked!");
    }

    @OnClick(R.id.upload_activity_category_view)
    void onCategoryBtnClick(){
        Intent intent = new Intent(this, EditCategoryPopUpActivity.class);
        intent.putParcelableArrayListExtra("recognition", recognitions);
        intent.putExtra(BundleKey.CORRECT_RECOGNITION, RecognitionType.CATEGORY);
        startActivityForResult(intent, GET_CORRECT_CATEGORY_RECOGNITION);
    }

    @OnClick(R.id.upload_activity_color_view)
    void onColorBtnClick(){
        Intent intent = new Intent(this, EditCategoryPopUpActivity.class);
        intent.putParcelableArrayListExtra("recognition", colorRecognitions);
        intent.putExtra(BundleKey.CORRECT_RECOGNITION, RecognitionType.COLOR);
        startActivityForResult(intent, GET_CORRECT_COLOR_RECOGNITION);
    }

    @OnClick(R.id.upload_back_btn)
    void onBackBtnClick() {
        finish();
    }

    @OnClick(R.id.upload_ok_btn)
    void onOkBtnClick() {
        String titleStr = title.getText().toString();
        String costStr = cost.getText().toString() + "원";
        String dongStr = dong.getText().toString();
        String descStr = description.getText().toString();

        if (uploadCameraIcon.getVisibility() == View.VISIBLE
                && uploadCameraText.getVisibility() == View.VISIBLE) {
            toast("이미지를 올려주셔야 해요!");
            return;
        } else if (titleStr.equals("")) {
            toast("제목을 작성해주세요!");
            return;
        } else if (costStr.equals("")) {
            toast("가격을 작성해주세요!");
            return;
        } else if (dong.equals("")) {
            toast("판매동네를 선택해주세요");
            return;
        } else {
            String categoryStrA = category.getText().toString().split(" > ")[0];
            String categoryStrB = category.getText().toString().split(" > ")[1];
            PostRequest postRequest = new PostRequest(
                    categoryStrA, categoryStrB, costStr, descStr, dongStr, isUploadMode, titleStr);
            if (isUploadMode) {
                presenter.upload(postRequest, file);
            } else {
                presenter.editPostContents(postRequest, postId);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("Other", "onBack()");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap img = null;
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

                    this.file = file;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (resized != null) {
                uploadCameraIcon.setVisibility(View.GONE);
                uploadCameraText.setVisibility(View.GONE);
                uploadImgBtn.setVisibility(View.VISIBLE);
                uploadImgBtn.setImageBitmap(img);
                classifierImg(resized);
            }
        }

        if (requestCode == GET_CORRECT_CATEGORY_RECOGNITION) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                final String categoryStr = data.getStringExtra(BundleKey.CORRECT_RECOGNITION);
                category.setText(categoryStr);
            }
        }

        if (requestCode == GET_CORRECT_COLOR_RECOGNITION) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                final String colorStr = data.getStringExtra(BundleKey.CORRECT_RECOGNITION);
                color.setText(colorStr);
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

        /*if (handler != null) {
            handler.post(
                    () -> {
                        List<Classifier.Recognition> results = categoryClassifier.recognizeImage(img);
                        List<Classifier.Recognition> colorResults = colorClassifier.recognizeImage(img);
                        RecognitionManager.getInstance().setCategoryRecognitions((ArrayList<Classifier.Recognition>) results);
                        RecognitionManager.getInstance().setColorRecognitions((ArrayList<Classifier.Recognition>) colorResults);
                    });
        }*/

        if (handler != null) {
            handler.post(
                    () -> {
                        List<Classifier.Recognition> categoryResults = categoryClassifier.recognizeImage(img);
                        List<Classifier.Recognition> colorResults = colorClassifier.recognizeImage(img);
                        final String categoryA = categoryResults.get(0).getTitle().split(":")[1].split("_")[0];
                        final String categoryB = categoryResults.get(0).getTitle().split(":")[1]
                                .split(categoryResults.get(0).getTitle().split(":")[1].split("_")[0] + "_")[1];
                        final String colorStr = colorResults.get(0).getTitle().split(":")[1];
                        category.setText(categoryA + " > " + categoryB);
                        color.setText(colorStr);
                        recognitions = (ArrayList<Classifier.Recognition>) categoryResults;
                        colorRecognitions = (ArrayList<Classifier.Recognition>) colorResults;
                    });
        }
        Trace.endSection();
    }

    @Override
    public void toast(String msg) {
        Runnable r = () -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        this.runOnUiThread(r);
    }

    @Override
    public void onUploadSuccess() {
        if (isUploadMode) {
            toast("업로드 완료");
        } else {
            toast("게시물 수정 완료");
        }
        finish();
    }

    @Override
    public void resumeUpLoadFragment(int code) {
        finish();
    }

    @Override
    public void connectFail() {

    }

    private File bitmapToFile(Bitmap bitmap, String fileName) {
        File file = new File(this.getCacheDir(), fileName);
        OutputStream outputStream = null;

        try {
            file.createNewFile();
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
