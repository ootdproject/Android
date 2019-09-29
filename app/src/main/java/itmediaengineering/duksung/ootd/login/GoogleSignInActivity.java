package itmediaengineering.duksung.ootd.login;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.BaseActivity;
import itmediaengineering.duksung.ootd.MainActivity;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.intro.IntroActivity;
import itmediaengineering.duksung.ootd.login.presenter.LoginContract;
import itmediaengineering.duksung.ootd.login.presenter.LoginPresenter;
import itmediaengineering.duksung.ootd.login.presenter.LoginState;
import itmediaengineering.duksung.ootd.utils.BundleKey;

public class GoogleSignInActivity extends BaseActivity implements
        View.OnClickListener, LoginContract.View{

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    // Firebase Authentication 관리클래스
    private FirebaseAuth auth;
    // [END declare_auth]

    // ?
    //private FirebaseAuth.AuthStateListener mAuthListener;

    //private GoogleApiClient mGoogleApiClient;

    // 구글 로그인 관리 클래스
    private GoogleSignInClient googleSignInClient;
    private Boolean toLogin;

    //SharedPreferences sharedPreferences = getSharedPreferences("sFile",MODE_PRIVATE);

    @BindView(R.id.status)
    TextView mStatusTextView;
    @BindView(R.id.detail)
    TextView mDetailTextView;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        toLogin = intent.getBooleanExtra(BundleKey.LOGIN_STATE, true);

        try {
            PackageInfo info = getPackageManager().getPackageInfo("itmediaengineering.duksung.ootd", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // ?
        // FirebaseApp.initializeApp(GoogleSignInActivity.this);

        // Button listeners 구글 로그인 버튼 이벤트
        findViewById(R.id.signInButton).setOnClickListener(this);
        findViewById(R.id.signOutButton).setOnClickListener(this);
        findViewById(R.id.disconnectButton).setOnClickListener(this);

        presenter = new LoginPresenter();
        presenter.attachView(this);

        // ?
        /*mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                *//*Intent intent = new Intent(GoogleSignInActivity.this, IntroActivity.class);
                intent.putExtra("userId", user.getUid());
                startActivity(intent);
                finish();*//*
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
            // ...
        };*/

        // [START config_signin]
        // Configure Google Sign In 구글 로그인 옵션
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // 여기서 전달하는 정보는 firebase 와 android app 사이의 인증 토큰
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail() // 이메일 요청
                .build(); // 코드를 끝마침
        // [END config_signin]

        // 구글 로그인 클래스 만듦
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        // Firebase 로그인 통합 관리하는 Object 생성
        auth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        //auth.addAuthStateListener(mAuthListener);
        // Check if user is signed in (non-null) and update UI accordingly.
        if(auth.getCurrentUser() != null){
            FirebaseUser currentUser = auth.getCurrentUser();
            updateUI(currentUser);
        }
    }
    // [END on_start_check_user]

    @Override public void onStop() {
        super.onStop();
        // if (mAuthListener != null) auth.removeAuthStateListener(mAuthListener);
    }

    // [START signin]
    // 구글 로그인 코드
    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    // [START onactivityresult]
    // 구글 로그인 성공했을 때 결과값이 넘어오는 함수
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        // 구글에서 승인된 정보 가지고 오기
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // 구글 로그인 성공시 토큰 값 파베로 넘겨주어 계정 생성하는 콜
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = auth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                    // [START_EXCLUDE]
                    hideProgressDialog();
                    // [END_EXCLUDE]
                });
    }
    // [END auth_with_google]

    private void signOut() {
        // Firebase sign out
        auth.signOut();

        Intent intent = new Intent(this, GoogleSignInActivity.class);
        intent.putExtra(BundleKey.LOGIN_STATE, LoginState.login.toLogin);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        // Google sign out
        /*googleSignInClient.signOut().addOnCompleteListener(this,
                task -> updateUI(null));
        ActivityCompat.finishAffinity(this);*/
    }

    private void revokeAccess() {
        // Firebase sign out
        auth.signOut();

        // Google revoke access
        googleSignInClient.revokeAccess().addOnCompleteListener(this,
                task -> updateUI(null));
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if(user != null && toLogin == LoginState.logout.toLogin) {
            mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.signInButton).setVisibility(View.GONE);
            findViewById(R.id.signOutAndDisconnect).setVisibility(View.VISIBLE);
        } else if (user != null && toLogin == LoginState.login.toLogin) {
            presenter.login(user.getUid());
            mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.signInButton).setVisibility(View.GONE);
            findViewById(R.id.signOutAndDisconnect).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.signInButton).setVisibility(View.VISIBLE);
            findViewById(R.id.signOutAndDisconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void startIntroActivity() {
        Intent intent = new Intent(GoogleSignInActivity.this, IntroActivity.class);
        FirebaseUser user = auth.getCurrentUser();
        intent.putExtra("userId", user.getUid());
        startActivity(intent);
        finish();
    }

    @Override
    public void startMainActivity(int code) {
        Intent intent = new Intent(GoogleSignInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUnauthorizedError() {
        toast("권한 없음");
    }

    @Override
    public void onUnknownError() {
        toast("알 수 없는 에러");
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signInButton) {
            signIn();
        } else if (i == R.id.signOutButton) {
            signOut();
        } else if (i == R.id.disconnectButton) {
            revokeAccess();
        }
    }

    @Override
    public void toast(String msg) {
        Runnable r = () -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        this.runOnUiThread(r);
    }

    @Override
    public void connectFail() {

    }
}