package itmediaengineering.duksung.ootd;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itmediaengineering.duksung.ootd.data.location.Document;
import itmediaengineering.duksung.ootd.main.adapter.MainPagerAdapter;
import itmediaengineering.duksung.ootd.main.presenter.MainContract;
import itmediaengineering.duksung.ootd.main.presenter.MainPresenter;
import itmediaengineering.duksung.ootd.main.tab.category.view.CategoryFragment;
import itmediaengineering.duksung.ootd.main.tab.feed.view.FeedFragment;
import itmediaengineering.duksung.ootd.main.tab.mypage.MyPageFragment;
import itmediaengineering.duksung.ootd.main.tab.upload.UploadActivity;
import itmediaengineering.duksung.ootd.main.tab.upload.UploadFragment;

/*
탭을 구현하는 MainActivity
탭과 함께 뷰페이져 구성하여 각 탭들의 상태유지를 생각해야 함
*/

public class MainActivity extends AppCompatActivity
    implements MainContract.View{

    public static final String TAG = "TAG MESSAGE";

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
    private  MainPagerAdapter adapter;
    private LocationManager locationManager;
    private String locationProvider;
    private String myLocation;

    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private static final int MIN_DISTANCE = 50;

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
                    case 0:
                        locationTitle.setClickable(true);
                        locationView.setText(myLocation);
                        //onResume();
                        locationChangeBtn.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        locationTitle.setClickable(false);
                        locationView.setText("카테고리");
                        locationChangeBtn.setVisibility(View.GONE);
                        break;
                    case 2:
                        if(uploadCnt == 0) {
                            Intent intent = new Intent(MainActivity.this, UploadActivity.class);
                            startActivity(intent);
                            uploadCnt++;
                        }
                        locationTitle.setClickable(false);
                        locationView.setText("나의 물품 판매하기");
                        locationChangeBtn.setVisibility(View.GONE);
                        break;
                    case 3:
                        locationTitle.setClickable(false);
                        locationView.setText("마이 페이지");
                        locationChangeBtn.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        setupTabIcons();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationProvider = LocationManager.NETWORK_PROVIDER;

        mainPresenter = new MainPresenter();
        mainPresenter.attachView(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(FeedFragment.newInstance(), "First");
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

        imgFirst.setImageResource(R.drawable.icn_home_inactive);
        imgSecond.setImageResource(R.drawable.icn_menu_outlined);
        imgThird.setImageResource(R.drawable.icn_add_outlined);
        imgFourth.setImageResource(R.drawable.icn_profile_inactive);

        txtFirst.setText("Feed");
        txtSecond.setText("Category");
        txtThird.setText("Upload");
        txtFourth.setText("My Page");

        tabContent.getTabAt(0).setCustomView(viewFirst);
        tabContent.getTabAt(1).setCustomView(viewSecond);
        tabContent.getTabAt(2).setCustomView(viewThird);
        tabContent.getTabAt(3).setCustomView(viewFourth);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( Build.VERSION.SDK_INT >= 26 &&
                ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[] {  Manifest.permission.ACCESS_COARSE_LOCATION  },
                    0 );
        }
        else{
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            String provider = lastKnownLocation.getProvider();
            double longitude = lastKnownLocation.getLongitude(); // 경도
            double latitude = lastKnownLocation.getLatitude();  // 위도

            // 위치정보 호출
            if(tabContent.getSelectedTabPosition() == 0) {
                mainPresenter.getData(String.valueOf(latitude), String.valueOf(longitude));
            }

            locationManager.requestLocationUpdates(
                    locationProvider,
                    TWO_MINUTES,
                    MIN_DISTANCE,
                    locationListener);
        }
    }

    // 이 부분은 사용자가 새로고침 눌렀을 때 실행되도록 하자!
    final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //네트워크 위치 제공자에 의해 새 위치가 인식되었을 경우
            //makeUseOfNewLocation(location);
            //cnt++;
            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();

            /*txtResult.setText("바뀐위치정보 : " + provider + "\n" +
                    "위도 : " + longitude + "\n" +
                    "경도 : " + latitude + "\n" +
                    "고도  : " + altitude + "\n" +
                    "위치정보 호출횟수  : " + cnt);*/
        }

        public void onStatusChanged(String provider, int status, Bundle extras) { }

        public void onProviderEnabled(String provider) { }

        public void onProviderDisabled(String provider) { }
    };

    @OnClick(R.id.main_toolbar_title)
    public void onChangeLocationBtnClick(){

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
        locationView.setText(document.getRegion3depthName());
        myLocation = document.getRegion3depthName();
    }

    @Override
    public void onConnectFail() {

    }

    @Override
    public void onNotFound() {

    }

    public interface onKeyBackPressedListener{
        void onBack();
    }

    private onKeyBackPressedListener onKeyBackPressedListener;
    public void setOnBackPressedListener(onKeyBackPressedListener listener){
        onKeyBackPressedListener = listener;
    }
}