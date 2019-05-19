package itmediaengineering.duksung.ootd.intro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itmediaengineering.duksung.ootd.MainActivity;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.intro.presenter.IntroConnectContract;
import itmediaengineering.duksung.ootd.intro.presenter.IntroConnectPresenter;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;

public class IntroActivity extends AppCompatActivity implements IntroConnectContract.View{//, ObserverCallback {

    private static final String TAG = IntroActivity.class.getSimpleName();

    @BindView(R.id.intro_user_image)
    ImageView userImage;
    @BindView(R.id.intro_nickname)
    EditText nickname;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.intro_gender_women_btn)
    RadioButton womenBtn;
    @BindView(R.id.intro_gender_men_btn)
    RadioButton menBtn;

    private String userId;

    private IntroConnectPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        presenter = new IntroConnectPresenter();
        presenter.attachView(this);
    }

    @OnClick(R.id.intro_save_btn)
    public void onSaveBtnClick(){
        String nickName = nickname.getText().toString();
        String gender;

        if(womenBtn.isChecked()) {
            gender = "women";
        } else if(menBtn.isChecked()) {
            gender = "men";
        } else {
            toast("성별을 선택해주세요!");
            return;
        }

        if(nickName.equals("")){
            toast("닉네임을 입력해주세요!");
            return;
        }

        Log.d("test",nickName + " " + gender);
        // 일단은 서버 저장 없이 진행했을 때
        //presenter.login(userId, nickName, gender);
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void startMainActivity(int code) {
        if (code == ResponseCode.BAD_REQUEST) {
            toast("아이디 / 비밀번호를 다시 확인해 주세요.");
            return;
        }
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        //intent.putExtra("Token", response.getResult().getToken());
        //intent.putExtra("UserNickname", response.getResult().getUsername());
        //intent.putExtra("UserGender", response.getResult().getProfile_picture());
        startActivity(intent);
        finish();
    }

    @Override
    public void connectFail() {
        toast("서버연결에 실패했습니다. 다시 시도해주세요.");
    }

    @Override
    public void toast(String msg) {
        Runnable r = () -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        runOnUiThread(r);
    }
}
