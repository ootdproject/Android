package itmediaengineering.duksung.ootd.main.tab.home.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.data.location.Document;
import itmediaengineering.duksung.ootd.data.weather.Item;
import itmediaengineering.duksung.ootd.main.tab.home.adapter.CardPagerAdapter;
import itmediaengineering.duksung.ootd.main.tab.home.adapter.CardPagerViewHolder;
import itmediaengineering.duksung.ootd.main.tab.home.presenter.MainContract;
import itmediaengineering.duksung.ootd.main.tab.home.presenter.MainPresenter;

/*MainFragment는 home 탭을 보여준다
날씨정보와 위치정보 추천받은 이미지 카드뷰 페이저를 관리해야하며
추가적으로 서버로 부터 받은 추천 데이터의 이미지와 해시태그 값에 대해 웹 검색을 연결해야 함
-------------------------------------------------------------------------
현재 위치정보와 날씨정보 요청은 날씨정보 트래픽 용량 부족으로 막아놓은 상태
서버 추천알고리즘 미완성으로 통신 없이 가상 이미지만 뿌려주고 있음
*/

public class MainFragment extends Fragment
        implements MainContract.View {

    public static final String TAG = "TAG MESSAGE";

    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.location_view)
    TextView locationView;
    @BindView(R.id.now_weather_Info)
    TextView nowWeatherInfo;
    @BindView(R.id.time_info)
    TextView timeInfo;
    @BindView(R.id.pager)
    ViewPager viewPager;

    protected FragmentManager fm;
    protected MainPresenter mainPresenter;
    private LocationManager locationManager;
    private String locationProvider;

    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private static final int MIN_DISTANCE = 50;
    private int cnt = 1;

    public static MainFragment newInstance(){
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        fm = getChildFragmentManager();

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationProvider = LocationManager.NETWORK_PROVIDER;

        //가짜 온도
        nowWeatherInfo.setText("19" + "\u00B0");

        //가짜 위치
        locationView.setText("도봉구 쌍문동");

        button1.setOnClickListener(v -> {
            // 호출 중단
            //onResume();

            // Remove the listener you previously added
            //locationManager.removeUpdates(locationListener);
        });

        CardPagerAdapter pagerAdapter = new CardPagerAdapter(getActivity().getSupportFragmentManager());
        pagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);

        for (int i = 0; i < 5; i++) {
            pagerAdapter.addItem(CardPagerViewHolder.newInstance());
        }

        fm.beginTransaction()
                .add(R.id.pager, MainRecommendFragment.newInstance(), "mainFragment")
                .commit();

        return rootView;
    }

    /*@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.pager, MainRecommendFragment.newInstance()).commit();
    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //spinner.setSelection(0, false);
        mainPresenter = new MainPresenter();
        mainPresenter.attachView(this);
        //presenter.setAdapterModel(adapter);
        //presenter.setAdapterView(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Date today = new Date();
        SimpleDateFormat date = new SimpleDateFormat("MM/dd HH:mm");
        String nowMonth = date.format(today).split("/")[0];
        String nowDay = date.format(today).split("/")[1].split(" ")[0];
        String nowTime = date.format(today).split(" ")[1];

        timeInfo.setText(nowMonth + "월 " + nowDay + "일 " + nowTime);

        if ( Build.VERSION.SDK_INT >= 26 &&
                ContextCompat.checkSelfPermission( getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {  Manifest.permission.ACCESS_COARSE_LOCATION  },
                    0 );
        }
        else{
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            String provider = lastKnownLocation.getProvider();
            double longitude = lastKnownLocation.getLongitude();
            double latitude = lastKnownLocation.getLatitude();

            // 호출 잠시 중단
            //mainPresenter.getData(String.valueOf(latitude), String.valueOf(longitude));

            /*txtResult.setText(
                    "시간 정보\n" +
                            "위치정보 : " + provider + "\n" +
                            "경도 : " + latitude + "\n" +
                            "위도 : " + longitude + "\n" +
                            //"고도  : " + altitude + "\n" +
                            "위치정보 호출횟수  : " + cnt + "\n"
            );*/

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
            cnt++;
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

    @Override
    public void toast(String msg) { }

    @Override
    public void onUnauthorizedError() { }

    @Override
    public void onUnknownError() { }

    @Override
    public void onSuccessGetWeather(Item item) {
        nowWeatherInfo.setText(
                (int)Math.floor(item.getObsrValue()) + "\u00B0");
    }

    @Override
    public void onSuccessGetLocation(Document document) {
        locationView.setText(document.getAddressName());
    }

    @Override
    public void onConnectFail() { }

    @Override
    public void onNotFound() { }
}
