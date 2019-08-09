package itmediaengineering.duksung.ootd.intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itmediaengineering.duksung.ootd.MainActivity;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.data.User;
import itmediaengineering.duksung.ootd.intro.presenter.IntroConnectContract;
import itmediaengineering.duksung.ootd.intro.presenter.IntroConnectPresenter;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;
import itmediaengineering.duksung.ootd.utils.ConnectionManager;
import itmediaengineering.duksung.ootd.utils.PreferenceUtils;
import itmediaengineering.duksung.ootd.utils.PushUtils;

/*
구글 로그인 이후 받아오는 아이디 값과 사용자가 입력한 정보를 서버에 넘겨야 하는 액티비티
서버와 연결이 안된 관계로 presenter의 로그인 함수 호출 없이 main 액티비티로 넘어가도록 설정되어 있음
*/

public class IntroActivity extends AppCompatActivity implements IntroConnectContract.View{//, ObserverCallback {

    private static final String TAG = IntroActivity.class.getSimpleName();

    //SharedPreferences sharedPreferences = getSharedPreferences("sFile",MODE_PRIVATE);

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

        // 구글 로그인 이후 사용자 아이디가 생성된 것을 전달받으면 인텐트에 실어서 보냄
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        presenter = new IntroConnectPresenter();
        presenter.attachView(this);
    }

    @OnClick(R.id.intro_save_btn)
    public void onSaveBtnClick(){
        String nickNameStr = nickname.getText().toString();
        String gender;

        if(womenBtn.isChecked()) {
            gender = "woman";
        } else if(menBtn.isChecked()) {
            gender = "man";
        } else {
            toast("성별을 선택해주세요!");
            return;
        }

        if(nickNameStr.equals("")){
            toast("닉네임을 입력해주세요!");
            return;
        }

        Log.d("test",nickNameStr + " " + gender);

        /*SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nickname", nickNameStr);
        editor.commit();*/

        // 서버로 사용자 정보 전송
        User user = new User(gender, nickNameStr, "GOOGLE", userId, "null");
        presenter.join(user);

        PreferenceUtils.setUserId(userId);
        PreferenceUtils.setNickname(nickNameStr);

        connectToSendBird(userId, nickNameStr);

        //Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        //startActivity(intent);
        Toast.makeText(this, "가입을 축하합니다!", Toast.LENGTH_SHORT).show();
        //finish();
    }

    @Override
    public void startMainActivity(int code) {
        if (code == ResponseCode.BAD_REQUEST) {
            toast("아이디 / 비밀번호를 다시 확인해 주세요.");
            return;
        } else if (code == ResponseCode.CREATED) {
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            //intent.putExtra("Token", response.getResult().getToken());
            //intent.putExtra("UserNickname", response.getResult().getUsername());
            //intent.putExtra("UserGender", response.getResult().getProfile_picture());
            startActivity(intent);
            finish();
        }
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

    private void connectToSendBird(final String userId, final String userNickname) {
        // Show the loading indicator
        //showProgressBar(true);
        //mConnectButton.setEnabled(false);

        ConnectionManager.login(userId, (user, e) -> {
            // Callback received; hide the progress bar.
            //showProgressBar(false);

            if (e != null) {
                // Error!
                Toast.makeText(
                        IntroActivity.this, "" + e.getCode() + ": " + e.getMessage(),
                        Toast.LENGTH_SHORT)
                        .show();

                // Show login failure snackbar
                //showSnackbar("Login to SendBird failed");
                //mConnectButton.setEnabled(true);
                PreferenceUtils.setConnected(false);
                return;
            }

            PreferenceUtils.setConnected(true);

            // Update the user's nickname
            updateCurrentUserInfo(userNickname);
            updateCurrentUserPushToken();

            // Proceed to MainActivity
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void updateCurrentUserPushToken() {
        PushUtils.registerPushTokenForCurrentUser(IntroActivity.this, null);
    }

    private void updateCurrentUserInfo(final String userNickname) {
        SendBird.updateCurrentUserInfo(userNickname, null, e -> {
            if (e != null) {
                // Error!
                Toast.makeText(
                        IntroActivity.this, "" + e.getCode() + ":" + e.getMessage(),
                        Toast.LENGTH_SHORT)
                        .show();

                // Show update failed snackbar
                //showSnackbar("Update user nickname failed");

                return;
            }

            PreferenceUtils.setNickname(userNickname);
        });
    }
}
