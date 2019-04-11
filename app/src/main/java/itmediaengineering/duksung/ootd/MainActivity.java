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

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.api.WeatherGridChange;
import itmediaengineering.duksung.ootd.main.presenter.LocationContract;
import itmediaengineering.duksung.ootd.main.presenter.LocationPresenter;
import itmediaengineering.duksung.ootd.main.presenter.WeatherPresenter;

public class MainActivity extends AppCompatActivity{

    public static final String TAG = "TAG MESSAGE";

    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.txtResult)
    TextView txtResult;
    @BindView(R.id.locationView)
    TextView locationView;

    protected WeatherPresenter weatherPresenter;
    protected LocationPresenter locationPresenter;

    private WeatherGridChange gridChange;

    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private static final int MIN_DISTANCE = 50;
    private int cnt = 1;
    private Date today;
    private SimpleDateFormat date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        locationPresenter = new LocationPresenter();
        weatherPresenter = new WeatherPresenter();

        gridChange = new WeatherGridChange();
        //weatherPresenter.attachView(this);

        today = new Date();
        date = new SimpleDateFormat("yyyyMMdd kk:mm");
        final String nowDate = date.format(today).split(" ")[0];
        final String nowTime;
        String tmpTime1 = date.format(today).split(" ")[1].split(":")[0];
        String tmpTime2 = date.format(today).split(" ")[1].split(":")[1];
        if(Integer.valueOf(tmpTime2) < 30){
            int tmp = Integer.valueOf(tmpTime1)-1;
            nowTime = String.valueOf(tmp) + "00";
        }else{
            nowTime = tmpTime1 + "00";
        }


        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        final String locationProvider = LocationManager.NETWORK_PROVIDER;

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    double altitude = lastKnownLocation.getAltitude();

                    WeatherGridChange.LatXLngY latXLngY = gridChange.convertGRID_GPS(0, latitude, longitude);

                    locationPresenter.getLocation(String.valueOf(longitude), String.valueOf(latitude));
                    locationView.setText("위치 정보 표시 할 곳");

                    txtResult.setText(
                            "시간 정보\n" +
                            "오늘 날짜 : " + nowDate + "\n" +
                            "현재 시간 : " + nowTime + "\n\n" +
                            "위치정보 : " + provider + "\n" +
                            "위도 : " + longitude + "\n" +
                            "경도 : " + latitude + "\n" +
                            "고도  : " + altitude + "\n" +
                            "위치정보 호출횟수  : " + cnt + "\n" +
                            "변환된 위도 : " + latXLngY.getX() + "\n" +
                            "변환된 경도 : " + latXLngY.getY()
                            );

                    weatherPresenter.getWeather(nowDate,
                            nowTime,
                            String.valueOf(latXLngY.getX()),
                            String.valueOf(latXLngY.getY()));

                    // Register the listener with the Location Manager to receive location updates
                    /*locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            1000,
                            1,
                            locationListener);*/
                    locationManager.requestLocationUpdates(
                            locationProvider,
                            TWO_MINUTES,
                            MIN_DISTANCE,
                            locationListener);
                }

                // Remove the listener you previously added
                //locationManager.removeUpdates(locationListener);
            }
        });

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
}