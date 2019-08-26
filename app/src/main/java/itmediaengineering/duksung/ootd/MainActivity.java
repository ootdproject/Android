package itmediaengineering.duksung.ootd;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itmediaengineering.duksung.ootd.chat_list.view.ChatListActivity;
import itmediaengineering.duksung.ootd.data.location.Document;
import itmediaengineering.duksung.ootd.main.adapter.MainPagerAdapter;
import itmediaengineering.duksung.ootd.main.presenter.MainContract;
import itmediaengineering.duksung.ootd.main.presenter.MainPresenter;
import itmediaengineering.duksung.ootd.main.tab.category.view.CategoryFragment;
import itmediaengineering.duksung.ootd.main.tab.feed.view.FeedFragment;
import itmediaengineering.duksung.ootd.main.tab.feed.view.LocationUpdatable;
import itmediaengineering.duksung.ootd.main.tab.mypage.view.MyPageFragment;
import itmediaengineering.duksung.ootd.main.tab.upload.UploadActivity;
import itmediaengineering.duksung.ootd.main.tab.upload.UploadFragment;
import itmediaengineering.duksung.ootd.map.LocationDemoActivity;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int INTENT_REQUEST_LOCATION = 302;
    private static final int REQUEST_LOCATION_PERMISSION = 303;
    private static final int FEED_TAB = 0;
    private static final int CATEGORY_TAB = 1;
    private static final int UPLOAD_TAB = 2;
    private static final int MY_PAGE_TAB = 3;

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
    @BindView(R.id.main_activity_noti_btn)
    ImageView notiBtn;

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
                        locationChangeBtn.setVisibility(View.VISIBLE);
                        locationUpdatable.onLocationUpdated(myDongLocation);
                        break;
                    case CATEGORY_TAB:
                        locationTitle.setClickable(false);
                        locationView.setText(R.string.tab_category_title);
                        locationChangeBtn.setVisibility(View.GONE);
                        break;
                    case UPLOAD_TAB:
                        if (uploadCnt == 0) {
                            Intent intent = new Intent(MainActivity.this, UploadActivity.class);
                            startActivity(intent);
                            uploadCnt++;
                        }
                        locationTitle.setClickable(false);
                        locationView.setText(R.string.tab_upload_title);
                        locationChangeBtn.setVisibility(View.GONE);
                        break;
                    case MY_PAGE_TAB:
                        locationTitle.setClickable(false);
                        locationView.setText(R.string.tab_my_page_title);
                        locationChangeBtn.setVisibility(View.GONE);
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
        locationProvider = LocationManager.NETWORK_PROVIDER;

        mainPresenter = new MainPresenter();
        mainPresenter.attachView(this);
        requestPermissionIfNeeded();
        refreshLocation();
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
        adapter.addFragment(MyPageFragment.newInstance(), "Fourth");
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        View viewFirst = getLayoutInflater().inflate(R.layout.custom_tab, null);
        View viewSecond = getLayoutInflater().inflate(R.layout.custom_tab, null);
        View viewThird = getLayoutInflater().inflate(R.layout.custom_tab, null);
        View viewFourth = getLayoutInflater().inflate(R.layout.custom_tab, null);

        ImageView imgFirst = viewFirst.findViewById(R.id.img_tab);
        ImageView imgSecond = viewSecond.findViewById(R.id.img_tab);
        ImageView imgThird = viewThird.findViewById(R.id.img_tab);
        ImageView imgFourth = viewFourth.findViewById(R.id.img_tab);

        TextView txtFirst = viewFirst.findViewById(R.id.txt_tab);
        TextView txtSecond = viewSecond.findViewById(R.id.txt_tab);
        TextView txtThird = viewThird.findViewById(R.id.txt_tab);
        TextView txtFourth = viewFourth.findViewById(R.id.txt_tab);

        imgFirst.setImageResource(R.drawable.tab_icon_feed_line);
        imgSecond.setImageResource(R.drawable.tab_icon_category_line);
        imgThird.setImageResource(R.drawable.tab_icon_upload_line);
        imgFourth.setImageResource(R.drawable.tab_icon_mypage_line);

        txtFirst.setText("Feed");
        txtSecond.setText("Category");
        txtThird.setText("Upload");
        txtFourth.setText("My Page");

        tabContent.getTabAt(FEED_TAB).setCustomView(viewFirst);
        tabContent.getTabAt(CATEGORY_TAB).setCustomView(viewSecond);
        tabContent.getTabAt(UPLOAD_TAB).setCustomView(viewThird);
        tabContent.getTabAt(MY_PAGE_TAB).setCustomView(viewFourth);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void refreshLocation() {
        if (!HasLocationPermission()) { return; }
        @SuppressLint("MissingPermission")
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        double longitude = lastKnownLocation.getLongitude();// 경도
        double latitude = lastKnownLocation.getLatitude();// 위도

        refreshLocation(longitude, latitude);
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

    @OnClick(R.id.main_activity_noti_btn)
    public void onChattingAlarmBtnClick() {
        Intent intent = new Intent(this, ChatListActivity.class);
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

        // Set this as true to restart auto-background detection.
        // This means that you will be automatically disconnected from SendBird when your
        // app enters the background.

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

    public interface onKeyBackPressedListener {
        void onBack();
    }

    private onKeyBackPressedListener onKeyBackPressedListener;

    public void setOnBackPressedListener(onKeyBackPressedListener listener) {
        onKeyBackPressedListener = listener;
    }
}