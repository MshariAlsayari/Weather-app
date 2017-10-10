package com.example.msharialsayari.weatherapp;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailsActivity extends AppCompatActivity {

    private int time;
    private String summary;
    private double temprature;
    private double humidity;
    private double cloudCover;
    private double windSpeed;
    private double visibility;
    private int hour;
    private int minute;
    private String AM_PM;

    @BindView(R2.id.textViewTime)
    TextView textViewTime;

    @BindView(R2.id.textViewSummary)
    TextView textViewSummary;

    @BindView(R2.id.textViewTempreture)
    TextView textViewTempreture;

    @BindView(R2.id.textViewHumidity)
    TextView textViewHumidity;

    @BindView(R2.id.textViewCloudCover)
    TextView textViewCloudCover;

    @BindView(R2.id.textViewVisibility)
    TextView textViewVisibility;

    @BindView(R2.id.textViewWindSpeed)
    TextView textViewWindSpeed;

    @BindView(R2.id.imageViewIcon)
    ImageView imageViewIcon;

    @OnClick(R2.id.toolbar)
    void backTopreviousActivity(){
//        Intent i = new Intent(DetailsActivity.this,MainActivity.class);
//        startActivity(i);
        finish();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);


         time = getIntent().getIntExtra("time",0);
         summary = getIntent().getStringExtra("summary");
         temprature = getIntent().getDoubleExtra("temperature" , 0 );
         humidity = getIntent().getDoubleExtra("humidity",0);
         cloudCover =  getIntent().getDoubleExtra("cloudCover",0);
         windSpeed  =  getIntent().getDoubleExtra("windSpeed",0);
         visibility =  getIntent().getDoubleExtra("visibility",0);
        converttimeZoneTo12systemTime (time);

        textViewTime.setText(String.valueOf(hour) +" "+ AM_PM);
        textViewSummary.setText(summary);
        textViewTempreture.setText(String.valueOf(convertToCelsius(temprature))+"\u00B0");
        textViewHumidity.setText(String.valueOf(new DecimalFormat("##.##").format(humidity*100)) + " %");
        textViewCloudCover.setText(String.valueOf(new DecimalFormat("##.##").format(cloudCover*100)) + " %");
        textViewVisibility.setText(String.valueOf(new DecimalFormat("##.##").format(visibility)) + " M/S");
        textViewWindSpeed.setText(String.valueOf(new DecimalFormat("##.##").format(windSpeed))+ " KM");
        imageViewIcon.setImageResource( determineIconeForSummaryDay(summary));

    }

    private int determineIconeForSummaryDay(String summary){

        if (summary.toLowerCase().indexOf("rain") != -1)
            return R.drawable.rain;
        else if (summary.toLowerCase().indexOf("snow") != -1)
            return R.drawable.snowflake;
        else  if (summary.toLowerCase().indexOf("clear") != -1)
            return R.drawable.sun;
        else  if (summary.toLowerCase().indexOf("sleet") != -1)
            return R.drawable.snowy;
        else if (summary.toLowerCase().indexOf("wind") != -1)
            return R.drawable.wind;
        else if (summary.toLowerCase().indexOf("fog") != -1)
            return R.drawable.fog;
        else if (summary.toLowerCase().indexOf("tornado") != -1)
            return R.drawable.tornado;
        else if (summary.toLowerCase().indexOf("cloudy") != -1)
            return R.drawable.cloudy;
        else if (summary.toLowerCase().indexOf("hail") != -1)
            return R.drawable.rain;

        return R.drawable.sun;

    }

    private void converttimeZoneTo12systemTime (int timezone){
        long unixSeconds = timezone;
        Date date = new Date(); // *1000 is to convert seconds to milliseconds
        date.setTime(unixSeconds*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); // the format of your date
        String formattedDate = sdf.format(date);
        this.hour = Integer.parseInt(formattedDate.substring(0,2))%12;
        this.hour = (hour==0) ? 12 : this.hour;
        this.minute = Integer.parseInt(formattedDate.substring(3,5));
        this.AM_PM = (Integer.parseInt(formattedDate.substring(0,2))>=12) ? "PM" : "AM";
    }

    private int convertToCelsius (double tempFahrenheit){

        double tempCelsius = (tempFahrenheit-32) / 1.8 ;
        return (int) tempCelsius;
    }
}
