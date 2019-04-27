package itmediaengineering.duksung.ootd;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.data.location.Document;
import itmediaengineering.duksung.ootd.main.presenter.MainPresenter;
import itmediaengineering.duksung.ootd.main.presenter.MainContract;
import itmediaengineering.duksung.ootd.data.weather.Item;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    public static final String TAG = "TAG MESSAGE";

    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.txtResult)
    TextView txtResult;
    @BindView(R.id.locationView)
    TextView locationView;
    @BindView(R.id.now_weather_Info)
    TextView nowWeatherInfo;

    protected MainPresenter mainPresenter;
    private LocationManager locationManager;
    private String locationProvider;

    //private WeatherGridChange gridChange;

    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private static final int MIN_DISTANCE = 50;
    private int cnt = 1;
    /*private Date today;
    private SimpleDateFormat date;
    private String nowDate = null;
    private String nowTime = null;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainPresenter = new MainPresenter();
        mainPresenter.attachView(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationProvider = LocationManager.NETWORK_PROVIDER;

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
                // Remove the listener you previously added
                //locationManager.removeUpdates(locationListener);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( Build.VERSION.SDK_INT >= 26 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( MainActivity.this, new String[] {  Manifest.permission.ACCESS_COARSE_LOCATION  },
                    0 );
        }
        else{
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            String provider = lastKnownLocation.getProvider();
            double longitude = lastKnownLocation.getLongitude();
            double latitude = lastKnownLocation.getLatitude();

            mainPresenter.getData(String.valueOf(latitude), String.valueOf(longitude));

            txtResult.setText(
                    "시간 정보\n" +
                            "위치정보 : " + provider + "\n" +
                            "경도 : " + latitude + "\n" +
                            "위도 : " + longitude + "\n" +
                            //"고도  : " + altitude + "\n" +
                            "위치정보 호출횟수  : " + cnt + "\n"
            );

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

            txtResult.setText("바뀐위치정보 : " + provider + "\n" +
                    "위도 : " + longitude + "\n" +
                    "경도 : " + latitude + "\n" +
                    "고도  : " + altitude + "\n" +
                    "위치정보 호출횟수  : " + cnt);

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

    @Override
    public void toast(String msg) {

    }

    @Override
    public void onUnauthorizedError() {

    }

    @Override
    public void onUnknownError() {

    }

    @Override
    public void onSuccessGetWeather(Item item) {
        nowWeatherInfo.setText(
                "현재 온도 : " + item.getObsrValue() +
                "\n시간 정보\n" +
                "오늘 날짜 : " + item.getBaseDate() + "\n" +
                "현재 시간 : " + item.getBaseTime());
    }

    @Override
    public void onSuccessGetLocation(Document document) {
        locationView.setText(document.getAddressName());
    }

    @Override
    public void onConnectFail() {

    }

    @Override
    public void onNotFound() {

    }
}