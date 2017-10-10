package com.example.msharialsayari.weatherapp.Adappter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.msharialsayari.weatherapp.R;
import com.example.msharialsayari.weatherapp.Retrofit.Model.Datum_;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.msharialsayari.weatherapp.R.id.imageViewIcon;
import static com.example.msharialsayari.weatherapp.R.id.textViewTime;
import static com.example.msharialsayari.weatherapp.R2.id.textViewDay;
import static com.example.msharialsayari.weatherapp.R2.id.textViewTemp;

/**
 * Created by msharialsayari on 05/10/2017 AD.
 */

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.DaysHolder>  {

        List<Datum_> dataList;
    private int hour;
    private int minute;
    private  String AM_PM  ;
    private String day;

    public DaysAdapter(List<Datum_> dataDaily) {
        this.dataList = dataDaily;
    }

    @Override
    public DaysHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_list, parent, false);
        DaysHolder holder = new DaysHolder(row);
        return holder;

    }

    @Override
    public void onBindViewHolder(DaysHolder holder, int position) {

        Datum_ data = dataList.get(position);
        holder.bind(data);




    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    class DaysHolder extends RecyclerView.ViewHolder{
        TextView textViewDay;
        TextView textViewTemp;

        ImageView imageViewIcon;


        public DaysHolder(View itemView) {
               super(itemView);
                textViewDay = (TextView) itemView. findViewById(R.id.textViewDay);
                textViewTemp = (TextView) itemView.findViewById(R.id.textViewTemp);

                imageViewIcon = (ImageView) itemView.findViewById(R.id.imageViewIcon);

           }

        public void bind(Datum_ data) {
            converttimeZoneTo12systemTime(data.getTime());
            textViewDay.setText(day);
            imageViewIcon.setImageResource( determineIconeForSummaryDay(data.getSummary()));
            textViewTemp.setText(String.valueOf(convertToCelsius(data.getApparentTemperatureMax())) +"\u00B0"+ "  "+
                                 String.valueOf(convertToCelsius(data.getApparentTemperatureMin()))+"\u00B0");

        }
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

//            if (stringArray[i].equals("08"))
//                intArray[i] = 8;
//            else if (stringArray[i].equals("09"))
//                intArray[i] = 9;
//            else intArray[i] = Integer.parseInt(stringArray[i]);
            intArray[i] = Integer.parseInt(stringArray[i]);
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

    private int convertToCelsius (double tempFahrenheit){

        double tempCelsius = (tempFahrenheit-32) / 1.8 ;
        return (int) tempCelsius;
    }
}
