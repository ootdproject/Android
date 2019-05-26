package itmediaengineering.duksung.ootd.main.tab.upload;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itmediaengineering.duksung.ootd.MainActivity;
import itmediaengineering.duksung.ootd.R;

import static android.app.Activity.RESULT_OK;

public class UploadFragment extends Fragment
                    implements MainActivity.onKeyBackPressedListener {
    public static final String TAG = UploadFragment.class.getSimpleName();

    @BindView(R.id.upload_gallery_btn)
    LinearLayout galleryUploadBtn;
    @BindView(R.id.upload_camera_btn)
    LinearLayout cameraUploadBtn;
    @BindView(R.id.upload_selected_img)
    ImageView selectedImg;
    @BindView(R.id.upload_next_btn)
    Button uploadNextBtn;

    public static UploadFragment newInstance(){
        return new UploadFragment();
    }

    public UploadFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_upload, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick(R.id.upload_gallery_btn)
    void onGalleryIntentBtnClick(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
        Log.d(TAG, "clicked!");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getActivity().getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    selectedImg.setVisibility(View.VISIBLE);
                    galleryUploadBtn.setVisibility(View.INVISIBLE);
                    cameraUploadBtn.setVisibility(View.INVISIBLE);
                    uploadNextBtn.setVisibility(View.VISIBLE);

                    selectedImg.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @OnClick(R.id.upload_camera_btn)
    void onCameraIntentBtnClick(){
        Log.d(TAG, "clicked!");
    }

    @Override
    public void onBack() {
        Log.e("Other", "onBack()");
        // 리스너를 설정하기 위해 Activity 를 받아옵니다.
        MainActivity activity = (MainActivity)getActivity();
        // 한번 뒤로가기 버튼을 눌렀다면 Listener 를 null 로 해제해줍니다.
        activity.setOnBackPressedListener(null);

        galleryUploadBtn.setVisibility(View.VISIBLE);
        cameraUploadBtn.setVisibility(View.VISIBLE);

        selectedImg.setVisibility(View.INVISIBLE);

        /*// MainFragment 로 교체
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mainFragment).commit();*/
        // Activity 에서도 뭔가 처리하고 싶은 내용이 있다면 하단 문장처럼 호출해주면 됩니다.
        // activity.onBackPressed();
    }

    // Fragment 호출 시 반드시 호출되는 오버라이드 메소드입니다.
    @Override
    // 혹시 Context 로 안되시는분은 Activity 로 바꿔보시기 바랍니다.
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("Other", "onAttach()");
        ((MainActivity)context).setOnBackPressedListener(this);
    }
}
