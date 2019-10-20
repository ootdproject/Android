package itmediaengineering.duksung.ootd;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Trace;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sendbird.android.SendBird;

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
import itmediaengineering.duksung.ootd.chat_list.view.ChatListActivity;
import itmediaengineering.duksung.ootd.chat_list.view.GroupChannelListFragment;
import itmediaengineering.duksung.ootd.data.location.Document;
import itmediaengineering.duksung.ootd.login.GoogleSignInActivity;
import itmediaengineering.duksung.ootd.login.presenter.LoginState;
import itmediaengineering.duksung.ootd.main.adapter.MainPagerAdapter;
import itmediaengineering.duksung.ootd.main.presenter.MainContract;
import itmediaengineering.duksung.ootd.main.presenter.MainPresenter;
import itmediaengineering.duksung.ootd.main.tab.category.view.CategoryFragment;
import itmediaengineering.duksung.ootd.main.tab.detail.view.PostDetailActivity;
import itmediaengineering.duksung.ootd.main.tab.feed.view.FeedFragment;
import itmediaengineering.duksung.ootd.main.tab.feed.view.LocationUpdatable;
import itmediaengineering.duksung.ootd.main.tab.mypage.view.MyPageFragment;
import itmediaengineering.duksung.ootd.main.tab.upload.UploadActivity;
import itmediaengineering.duksung.ootd.main.tab.upload.UploadFragment;
import itmediaengineering.duksung.ootd.main.tab.upload.classifier.Classifier;
import itmediaengineering.duksung.ootd.main.tab.upload.classifier.TensorFlowImageClassifier;
import itmediaengineering.duksung.ootd.map.LocationDemoActivity;
import itmediaengineering.duksung.ootd.search.view.SearchActivity;
import itmediaengineering.duksung.ootd.utils.BundleKey;
import itmediaengineering.duksung.ootd.utils.ConnectionManager;
import itmediaengineering.duksung.ootd.utils.PreferenceUtils;
import itmediaengineering.duksung.ootd.utils.PushUtils;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int INTENT_REQUEST_LOCATION = 302;
    private static final int REQUEST_LOCATION_PERMISSION = 303;
    private static final int FEED_TAB = 0;
    private static final int CATEGORY_TAB = 1;
    private static final int UPLOAD_TAB = 2;
    private static final int CHATTING_TAB = 3;
    private static final int MY_PAGE_TAB = 4;

    /*private enum TAB_LIST {
        FEED_TAB(0),
        CATEGORY_TAB(1),
        UPLOAD_TAB(2),
        CHATTING_TAB(3),
        MY_PAGE_TAB(4);

        public int getTab() {
            return tab;
        }

        public final int tab;

        TAB_LIST(int tab) {
            this.tab = tab;
        }
    }*/

    @BindView(R.id.viewpager_content)
    ViewPager viewpagerContent;
    @BindView(R.id.tab_content)
    TabLayout tabContent;
    @BindView(R.id.main_fullScreen)
    ConstraintLayout mainFullscreen;
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.main_toolbar_title)
    ConstraintLayout locationTitle;
    @BindView(R.id.main_activity_location_view)
    TextView locationView;
    @BindView(R.id.main_activity_location_icon)
    ImageView locationChangeBtn;
    @BindView(R.id.main_activity_search_btn)
    ImageView SearchBtn;
    @BindView(R.id.main_activity_setting_btn)
    ImageView SettingBtn;

    protected MainPresenter mainPresenter;
    private MainPagerAdapter adapter;
    private LocationManager locationManager;
    private LocationUpdatable locationUpdatable;
    private String myLocation;
    private String locationProvider;
    private String myDongLocation;

    private int uploadCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupViewPager(viewpagerContent);
        tabContent.setupWithViewPager(viewpagerContent);
        tabContent.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case FEED_TAB:
                        locationTitle.setClickable(true);
                        locationView.setText(myLocation == null ? "" : myLocation);
                        locationUpdatable.onLocationUpdated(myDongLocation);
                        SettingBtn.setVisibility(View.GONE);
                        locationChangeBtn.setVisibility(View.VISIBLE);
                        SearchBtn.setVisibility(View.VISIBLE);
                        break;
                    case CATEGORY_TAB:
                        locationTitle.setClickable(false);
                        locationView.setText(R.string.tab_category_title);
                        SettingBtn.setVisibility(View.GONE);
                        locationChangeBtn.setVisibility(View.GONE);
                        SearchBtn.setVisibility(View.VISIBLE);
                        break;
                    case UPLOAD_TAB:
                        if (uploadCnt == 0) {
                            Intent intent = new Intent(MainActivity.this, UploadActivity.class);
                            startActivity(intent);
                            uploadCnt++;
                        }
                        locationTitle.setClickable(false);
                        locationView.setText(R.string.tab_upload_title);
                        SettingBtn.setVisibility(View.GONE);
                        locationChangeBtn.setVisibility(View.GONE);
                        SearchBtn.setVisibility(View.GONE);
                        break;
                    case CHATTING_TAB:
                        locationTitle.setClickable(false);
                        locationView.setText(R.string.tab_chatting_title);
                        SettingBtn.setVisibility(View.GONE);
                        locationChangeBtn.setVisibility(View.GONE);
                        SearchBtn.setVisibility(View.GONE);
                        break;
                    case MY_PAGE_TAB:
                        locationTitle.setClickable(false);
                        locationView.setText(R.string.tab_my_page_title);
                        SettingBtn.setVisibility(View.VISIBLE);
                        locationChangeBtn.setVisibility(View.GONE);
                        SearchBtn.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        setupTabIcons();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationProvider = LocationManager.PASSIVE_PROVIDER;
        if(locationProvider == null) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }

        mainPresenter = new MainPresenter();
        mainPresenter.attachView(this);
        requestPermissionIfNeeded();
        refreshLocation();

        String userId = PreferenceUtils.getProviderUserId();
        String userNickname = PreferenceUtils.getNickname();
        connectToSendBird(userId, userNickname);
    }

    private void requestPermissionIfNeeded() {
        if (HasLocationPermission()) { return; }
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION_PERMISSION);
    }

    private boolean HasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        FeedFragment feedFragment = FeedFragment.newInstance();
        locationUpdatable = feedFragment;
        adapter.addFragment(feedFragment, "First");
        adapter.addFragment(CategoryFragment.newInstance(), "Second");
        adapter.addFragment(UploadFragment.newInstance(), "Third");
        adapter.addFragment(GroupChannelListFragment.newInstance(), "Fourth");
        adapter.addFragment(MyPageFragment.newInstance(), "Fifth");
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        Bitmap feedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tab_icon_feed_line);
        Bitmap categoryBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tab_icon_category_line);
        Bitmap uploadBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tab_icon_upload_line);
        Bitmap chatBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tab_icon_chat_line2);
        Bitmap myPageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tab_icon_mypage_line);

        settingCustomTab(feedBitmap, "Feed", FEED_TAB);
        settingCustomTab(categoryBitmap, "Category", CATEGORY_TAB);
        settingCustomTab(uploadBitmap, "Upload", UPLOAD_TAB);
        settingCustomTab(chatBitmap, "Chat", CHATTING_TAB);
        settingCustomTab(myPageBitmap, "My Page", MY_PAGE_TAB);
    }

    private void settingCustomTab(Bitmap bitmap, String tabText, int tabCount) {
        View tabView = getLayoutInflater().inflate(R.layout.custom_tab, null);
        ImageView tabImg = tabView.findViewById(R.id.img_tab);
        tabImg.setImageBitmap(bitmap);
        TextView tabTxt = tabView.findViewById(R.id.txt_tab);
        tabTxt.setText(tabText);
        tabContent.getTabAt(tabCount).setCustomView(tabView);
    }

    private void refreshLocation() {
        if (!HasLocationPermission()) { return; }
        @SuppressLint("MissingPermission")
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if(lastKnownLocation == null) {
            toast("위치정보를 켜주세요");
            new Handler().postDelayed(() -> finish(), 300);
        } else {
            double longitude = lastKnownLocation.getLongitude();// 경도
            double latitude = lastKnownLocation.getLatitude();// 위도
            refreshLocation(longitude, latitude);
        }
    }

    private void refreshLocation(double longitude, double latitude) {
        mainPresenter.getLocation(latitude, longitude);
    }

    @OnClick(R.id.main_toolbar_title)
    public void onChangeLocationBtnClick() {
        /*Intent intent = new Intent(this, AppPermissionHandlerActivity.class);
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        intent.putExtra(PERMISSION_STRINGS, permissions);
        startActivity(intent);*/

        if (!HasLocationPermission()) { return; }
            Intent intent = new Intent(this, LocationDemoActivity.class);
            startActivityForResult(intent, INTENT_REQUEST_LOCATION);
    }

    @OnClick(R.id.main_activity_search_btn)
    public void onSearchImageClick() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.main_activity_setting_btn)
    public void onSettingBtnClick() {
        Intent intent = new Intent(this, GoogleSignInActivity.class);
        intent.putExtra(BundleKey.LOGIN_STATE, LoginState.logout.toLogin);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 권한 요청에 대한 결과 처리
        // If request is cancelled, the result arrays are empty.
        if (requestCode == REQUEST_LOCATION_PERMISSION &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            refreshLocation();
        }
        else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INTENT_REQUEST_LOCATION && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Log.d(TAG, "data is null!");
                return;
            }

            double latitude = data.getDoubleExtra("latitude", 0.0d);
            double longitude = data.getDoubleExtra("longitude", 0.0d);
            //locationView.setText(data.getStringExtra("location"));
            refreshLocation(longitude, latitude);
        }
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
                        this, "" + e.getCode() + ": " + e.getMessage(),
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
            /*Intent intent = new Intent(PostDetailActivity.this, MainActivity.class);
            startActivity(intent);
            finish();*/
        });
    }

    private void updateCurrentUserPushToken() {
        PushUtils.registerPushTokenForCurrentUser(this, null);
    }

    private void updateCurrentUserInfo(final String userNickname) {
        SendBird.updateCurrentUserInfo(userNickname, null, e -> {
            if (e != null) {
                // Error!
                Toast.makeText(
                        this, "" + e.getCode() + ":" + e.getMessage(),
                        Toast.LENGTH_SHORT)
                        .show();

                // Show update failed snackbar
                //showSnackbar("Update user nickname failed");
                return;
            }
            PreferenceUtils.setNickname(userNickname);
        });
    }

    @Override
    public void toast(String msg) {
        Runnable r = () -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        this.runOnUiThread(r);
    }

    @Override
    public void onUnauthorizedError() {

    }

    @Override
    public void onUnknownError() {

    }

    @Override
    public void onSuccessGetLocation(Document document) {
        String location = document.getRegion2depthName() + " [" + document.getRegion3depthName() + "]";
        myLocation = location;
        locationView.setText(location);
        myDongLocation = document.getRegion2depthName();
        locationUpdatable.onLocationUpdated(myDongLocation);
    }

    @Override
    public void onConnectFail() {

    }

    @Override
    public void onNotFound() {

    }

    public interface onBackPressedListener {
        boolean onBack();
    }

    private MainActivity.onBackPressedListener onBackPressedListener;

    public void setOnBackPressedListener(onBackPressedListener listener) {
        onBackPressedListener = listener;
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null && onBackPressedListener.onBack()) {
            return;
        }
        super.onBackPressed();
    }
}