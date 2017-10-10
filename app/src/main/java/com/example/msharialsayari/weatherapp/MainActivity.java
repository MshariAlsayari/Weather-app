package com.example.msharialsayari.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msharialsayari.weatherapp.Adappter.DaysAdapter;
import com.example.msharialsayari.weatherapp.Adappter.HoursAdappter;
import com.example.msharialsayari.weatherapp.Dagger.Component.DaggerWeatherAppComponent;
import com.example.msharialsayari.weatherapp.Dagger.Component.WeatherAppComponent;
import com.example.msharialsayari.weatherapp.Dagger.Module.WeatherAppModule;
import com.example.msharialsayari.weatherapp.Retrofit.Interface.NetworkApi;
import com.example.msharialsayari.weatherapp.Retrofit.Model.Datum;
import com.example.msharialsayari.weatherapp.Retrofit.Model.Datum_;
import com.example.msharialsayari.weatherapp.Retrofit.Model.Weather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity  {

    @BindView(R2.id.textViewCityName)
    TextView cityName;

    @BindView(R2.id.textViewWeatherSummary)
    TextView weatherSummary;

    @BindView(R2.id.textViewCurrentTempreture)
    TextView currentTempreture;

    @BindView(R2.id.textViewToday)
    TextView tody;

    @BindView(R2.id.textViewTempreture)
    TextView tempreture;

    @BindView(R2.id.textViewTempretureMin)
    TextView tempretureMin;

    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R2.id.recyclerViewDays)
    RecyclerView recyclerViewDays;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    private WeatherAppComponent component;


    @Inject
    NetworkApi networkApi;

    NetworkApi networkApi2;


    private String day;

    private List<Datum> dataHourly;
    private List<Datum_> dataDaily;
    private HoursAdappter hoursAdapter;
    private DaysAdapter daysAdapter;
    private int hour;
    private int minute;
    private String AM_PM;
    private double longitude;
    private double latitude;
    private String longitudeString;
    private String latitudeString;
    private static final int REQUEST_LOCATION =1;
    private LocationManager locationManager;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        } else {
            showGPSDisabledAlertToUser();

        }


        component = DaggerWeatherAppComponent.builder().weatherAppModule(new WeatherAppModule()).build();
        component.inject(this);


        if (savedInstanceState != null) {


            dataHourly = savedInstanceState.getParcelableArrayList("DATA_HOURLY");
            dataDaily = savedInstanceState.getParcelableArrayList("DATA_DAILY");

            hoursAdapter = new HoursAdappter(dataHourly);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, true));
            recyclerView.setAdapter(hoursAdapter);


            daysAdapter = new DaysAdapter(dataDaily);
            recyclerViewDays.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recyclerViewDays.setAdapter(daysAdapter);

            cityName.setText(savedInstanceState.getString("CityName"));
            weatherSummary.setText(savedInstanceState.getString("WeatherSummary"));
            currentTempreture.setText(String.valueOf(savedInstanceState.getString("CurrentTempreture")));
            tody.setText(savedInstanceState.getString("Today"));
            tempreture.setText(String.valueOf(savedInstanceState.getString("Tempreture")));
            tempretureMin.setText(String.valueOf(savedInstanceState.getString("TempretureMin")));


        } else {
            retrieveDataByRX();
        }


    }

    private void  getLocation() {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                this.latitude = location.getLatitude();
                this.longitude = location.getLongitude();
                this.latitudeString = String.valueOf(latitude);
                this.longitudeString = String.valueOf(longitude);
                Log.d("Latitude", latitudeString);
                Log.d("Longitude", longitudeString);
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_LOCATION: getLocation();
                break;
        }
    }





    private void showGPSDisabledAlertToUser() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Go to Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);

                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    private void retrieveDataByRX() {

        progressBar.setVisibility(View.VISIBLE);
        networkApi.getWeatherInfoByRX(this.latitudeString + "," + this.longitudeString).map(new Func1<Weather, Weather>() {
            @Override
            public Weather call(Weather weather) {
                return weather;
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Weather>() {

            @Override
            public void onCompleted() {
                hoursAdapter = new HoursAdappter(dataHourly);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, true));
                recyclerView.setAdapter(hoursAdapter);



                daysAdapter = new DaysAdapter(dataDaily);
                recyclerViewDays.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerViewDays.setAdapter(daysAdapter);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onError(Throwable e) {

                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);


            }

            @Override
            public void onNext(Weather weather) {
                cityName.setText(getJustCity(weather.getTimezone()));
                weatherSummary.setText(weather.getCurrently().getSummary());
                currentTempreture.setText(String.valueOf(convertToCelsius(weather.getCurrently().getTemperature()))+"\u00B0");
                converttimeZoneTo12systemTime(weather.getCurrently().getTime());
                tody.setText(day);
                tempreture.setText(String.valueOf(convertToCelsius(weather.getDaily().getData().get(0).getTemperatureMax()))+"\u00B0");
                tempretureMin.setText(String.valueOf(convertToCelsius(weather.getDaily().getData().get(0).getTemperatureMin()))+"\u00B0");
                dataHourly = weather.getHourly().getData();
                dataDaily = weather.getDaily().getData();

            }
        });


    }


    private void converttimeZoneTo12systemTime(int timezone) {
        long unixSeconds = timezone;
        Date date = new Date(); // *1000 is to convert seconds to milliseconds
        date.setTime(unixSeconds * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdfDate = new SimpleDateFormat("M-d-yyyy");// the format of your date
        String formattedDate = sdfDate.format(date);
        String formattedTime = sdf.format(date);

        this.hour = Integer.parseInt(formattedTime.substring(0, 2)) % 12;
        this.minute = Integer.parseInt(formattedTime.substring(3, 5));
        this.AM_PM = (Integer.parseInt(formattedTime.substring(0, 2)) >= 12) ? "PM" : "AM";
        this.day = getSpicificDay(formattedDate);


    }

    private String getSpicificDay(String date) {

        //formattedDate = formattedDate.substring(0,2);
        String stringArray[] = date.split("-");
        int intArray[] = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {

            if (stringArray[i].equals("08"))
                intArray[i] = 8;
            else if (stringArray[i].equals("09"))
                intArray[i] = 9;
            else intArray[i] = Integer.parseInt(stringArray[i]);
        }

        Calendar calendar = Calendar.getInstance();

        calendar.set(intArray[2], intArray[0], intArray[1]);
        int dayInnumber = calendar.get(Calendar.DAY_OF_WEEK);

        switch (dayInnumber) {
            case 1:
                return "Thursday";
            case 2:
                return "Friday";
            case 3:
                return "Saturday";
            case 4:
                return "Sunday";
            case 5:
                return "Monday";
            case 6:
                return "Tuesday";
            case 7:
                return "Wednesday";
        }
        return "";

    }

    private String getJustCity(String continentًWithCity) {

        String day[] = continentًWithCity.split("/");
        return day[1];

    }


    private int convertToCelsius(double tempFahrenheit) {

        double tempCelsius = (tempFahrenheit - 32) / 1.8;
        return (int) tempCelsius;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("DATA_HOURLY", (ArrayList<? extends Parcelable>) dataHourly);
        outState.putParcelableArrayList("DATA_DAILY", (ArrayList<? extends Parcelable>) dataDaily);
        outState.putString("CityName", cityName.getText().toString());
        outState.putString("WeatherSummary", weatherSummary.getText().toString());
        outState.putString("CurrentTempreture", currentTempreture.getText().toString());
        outState.putString("Today", tody.getText().toString());
        outState.putString("Tempreture", tempreture.getText().toString());
        outState.putString("TempretureMin", tempretureMin.getText().toString());


    }


}
