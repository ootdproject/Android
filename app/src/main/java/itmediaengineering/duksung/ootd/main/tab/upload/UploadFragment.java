package itmediaengineering.duksung.ootd.main.tab.upload;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itmediaengineering.duksung.ootd.MainActivity;
import itmediaengineering.duksung.ootd.R;

/*
UploadFragment는 사용자가 이미지를 업로드하는 탭
갤러리와 카메라를 통해서 업로드를 해야하고 사진을 뷰에 보여준뒤
NEXT 버튼을 누르면 올려진 tflite 파일을 통해 자동태깅을 한 뒤
서버로 태그값과 날씨값, 이미지를 함께 전달해야 함

*/

public class UploadFragment extends Fragment
                    implements MainActivity.onBackPressedListener {
    public static final String TAG = UploadFragment.class.getSimpleName();


    @BindView(R.id.upload_state_view)
    TextView view;
    @BindView(R.id.upload_floating_button)
    FloatingActionButton uploadBtn;

    public static UploadFragment newInstance(){
        return new UploadFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_upload, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick(R.id.upload_floating_button)
    void onUploadBtnClick(){
        Intent intent = new Intent(getActivity(), UploadActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onBack() {
        Log.e("Other", "onBack()");
        MainActivity activity = (MainActivity)getActivity();
        // 한번 뒤로가기 버튼을 눌렀다면 Listener 를 null 로 해제해줍니다.
        activity.setOnBackPressedListener(null);

        return true;
    }

    // Fragment 호출 시 반드시 호출되는 오버라이드 메소드
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("Other", "onAttach()");
        ((MainActivity)context).setOnBackPressedListener(this);
    }
}
