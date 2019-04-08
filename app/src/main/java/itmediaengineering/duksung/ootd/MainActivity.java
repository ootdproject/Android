package itmediaengineering.duksung.ootd;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kwabenaberko.openweathermaplib.constants.Lang;
import com.kwabenaberko.openweathermaplib.constants.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.implementation.callbacks.CurrentWeatherCallback;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TAG MESSAGE";

    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.txtResult)
    TextView txtResult;

    private int cnt = 1;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private static final int MIN_DISTANCE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final OpenWeatherMapHelper helper = new OpenWeatherMapHelper(getString(R.string.OPEN_WEATHER_MAP_API_KEY));
        helper.setUnits(Units.METRIC);
        helper.setLang(Lang.KOREAN);

        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        final String locationProvider = LocationManager.NETWORK_PROVIDER;

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( Build.VERSION.SDK_INT >= 26 &&
                        ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( MainActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                            0 );
                }
                else{
                    Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
                    String provider = lastKnownLocation.getProvider();
                    double longitude = lastKnownLocation.getLongitude();
                    double latitude = lastKnownLocation.getLatitude();
                    double altitude = lastKnownLocation.getAltitude();

                    txtResult.setText("위치정보 : " + provider + "\n" +
                            "위도 : " + longitude + "\n" +
                            "경도 : " + latitude + "\n" +
                            "고도  : " + altitude + "\n" +
                            "위치정보 호출횟수  : " + cnt);

                    helper.getCurrentWeatherByGeoCoordinates(latitude, longitude, new CurrentWeatherCallback() {
                        @Override
                        public void onSuccess(CurrentWeather currentWeather) {
                            Log.v(TAG, "Coordinates: " + currentWeather.getCoord().getLat() + ", "+currentWeather.getCoord().getLon() +"\n"
                                    +"Weather Description: " + currentWeather.getWeather().get(0).getDescription() + "\n"
                                    +"Temperature: " + currentWeather.getMain().getTempMax()+"\n"
                                    +"Wind Speed: " + currentWeather.getWind().getSpeed() + "\n"
                                    +"City, Country: " + currentWeather.getName() + ", " + currentWeather.getSys().getCountry()
                            );
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.v(TAG, throwable.getMessage());
                        }
                    });

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