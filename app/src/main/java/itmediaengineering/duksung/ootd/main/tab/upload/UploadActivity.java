package itmediaengineering.duksung.ootd.main.tab.upload;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.data.post.PostRequest;
import itmediaengineering.duksung.ootd.main.tab.upload.presenter.UploadContract;
import itmediaengineering.duksung.ootd.main.tab.upload.presenter.UploadPresenter;
import itmediaengineering.duksung.ootd.utils.PreferenceUtils;

public class UploadActivity extends AppCompatActivity implements UploadContract.View {
    public static final String TAG = UploadActivity.class.getSimpleName();

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
    EditText category;
    @BindView(R.id.upload_activity_dong)
    EditText dong;
    @BindView(R.id.upload_activity_description_view)
    EditText description;

    protected UploadPresenter presenter;

    private boolean isUploadMode = true;
    private int postId;
    private File file;

    private final int GET_GALLERY_IMAGE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);

        presenter = new UploadPresenter();
        presenter.attachView(this);

        if(getIntent().getExtras() != null) {
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
            category.setText(post.getCategoryA());
            dong.setText(post.getDong());
            description.setText(post.getDescription());
        }
    }

    @OnClick(R.id.upload_img_btn)
    void onGalleryIntentBtnClick(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);*/
        startActivityForResult(intent, GET_GALLERY_IMAGE);
        Log.d(TAG, "clicked!");
    }

    @OnClick(R.id.upload_back_btn)
    void onBackBtnClick(){
        finish();
    }

    @OnClick(R.id.upload_ok_btn)
    void onOkBtnClick(){
        String titleStr = title.getText().toString();
        String categoryStr = category.getText().toString();
        String costStr = cost.getText().toString();
        String dongStr = dong.getText().toString();
        String descStr = description.getText().toString();

        if(uploadCameraIcon.getVisibility() == View.INVISIBLE
            && uploadCameraText.getVisibility() == View.INVISIBLE){
            toast("이미지를 올려주셔야 해요!");
            return;
        } else if(titleStr.equals("")){
            toast("제목을 작성해주세요!");
            return;
        /*}else if(categoryStr.equals("")){
            toast("카테고리를 선택해주세요!");
            return;*/
        } else if(costStr.equals("")) {
            toast("가격을 작성해주세요!");
            return;
        }else if(dong.equals("")){
            toast("판매동네를 선택해주세요");
            return;
        }else {
            PostRequest postRequest = new PostRequest(
                    categoryStr, categoryStr, costStr, descStr, dongStr, isUploadMode, titleStr);
            if(isUploadMode){
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

        /*// CategoryFragment 로 교체
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mainFragment).commit();*/
        // Activity 에서도 뭔가 처리하고 싶은 내용이 있다면 하단 문장처럼 호출해주면 됩니다.
        // activity.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_GALLERY_IMAGE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = this.getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    uploadCameraIcon.setVisibility(View.GONE);
                    uploadCameraText.setVisibility(View.GONE);
                    uploadImgBtn.setVisibility(View.VISIBLE);
                    uploadImgBtn.setImageBitmap(img);

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "JPEG_" + timeStamp + "_";
                    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File file = File.createTempFile(
                            imageFileName,
                            ".jpg",
                            storageDir
                    );
                    FileOutputStream fileOutputStream = new FileOutputStream(file);

                    Bitmap resized = Bitmap.createScaledBitmap(img, img.getWidth(), img.getHeight(), true);
                    resized.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);

                    fileOutputStream.flush();
                    fileOutputStream.close();
                    //return file;

                    // 이미지 표시
                    this.file = file;
                    //this.file = bitmapToFile(img, PreferenceUtils.getNickname());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void toast(String msg) {
        Runnable r = () -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        this.runOnUiThread(r);
    }

    @Override
    public void onUploadSuccess() {
        if(isUploadMode){
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
