package com.example.msharialsayari.weatherapp.Adappter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.msharialsayari.weatherapp.DetailsActivity;
import com.example.msharialsayari.weatherapp.R;
import com.example.msharialsayari.weatherapp.R2;
import com.example.msharialsayari.weatherapp.Retrofit.Model.Datum;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.data;

/**
 * Created by msharialsayari on 04/10/2017 AD.
 */

public class HoursAdappter extends RecyclerView.Adapter<HoursAdappter.HoursHolder> {

    private List<Datum> dataList;

    public HoursAdappter(List<Datum> dataList) {
        this.dataList=dataList;
    }

    @Override
    public HoursHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        HoursHolder holder = new HoursHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(HoursHolder holder, int position) {

        Datum data = dataList.get(position);
        holder.bind(data);

    }

    @Override
    public int getItemCount() {
        return 24;
    }


    class HoursHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.textViewTime)
        TextView time;

        @BindView(R2.id.imageViewIcone)
        ImageView icone;

        @BindView(R2.id.textViewDegree)
        TextView degree;

        private int hour;
        private int minute;
        private String AM_PM;



        public HoursHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Datum data) {
            converttimeZoneTo12systemTime(data.getTime());
            time.setText(String.valueOf(this.hour) + this.AM_PM);
            icone.setImageResource(determineIcone(this.hour,this.AM_PM));
            degree.setText(String.valueOf(convertToCelsius(data.getTemperature()))+"\u00B0");

            time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent (itemView.getContext(), DetailsActivity.class);
                    i.putExtra("time" , data.getTime());
                    i.putExtra("summary" , data.getSummary());
                    i.putExtra("icon" , data.getIcon());
                    i.putExtra("temperature" , data.getTemperature());
                    i.putExtra("humidity" , data.getHumidity());
                    i.putExtra("cloudCover" , data.getCloudCover());
                    i.putExtra("windSpeed" , data.getWindSpeed());
                    i.putExtra("visibility" , data.getVisibility());
                    v.getContext().startActivity(i);

                }
            });

            icone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent (itemView.getContext(), DetailsActivity.class);
                    i.putExtra("time" , data.getTime());
                    i.putExtra("summary" , data.getSummary());
                    i.putExtra("icon" , data.getIcon());
                    i.putExtra("temperature" , data.getTemperature());
                    i.putExtra("humidity" , data.getHumidity());
                    i.putExtra("cloudCover" , data.getCloudCover());
                    i.putExtra("windSpeed" , data.getWindSpeed());
                    i.putExtra("visibility" , data.getVisibility());
                    v.getContext().startActivity(i);

                }
            });

            degree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent (itemView.getContext(), DetailsActivity.class);
                    i.putExtra("time" , data.getTime());
                    i.putExtra("summary" , data.getSummary());
                    i.putExtra("icon" , data.getIcon());
                    i.putExtra("temperature" , data.getTemperature());
                    i.putExtra("humidity" , data.getHumidity());
                    i.putExtra("cloudCover" , data.getCloudCover());
                    i.putExtra("windSpeed" , data.getWindSpeed());
                    i.putExtra("visibility" , data.getVisibility());
                    v.getContext().startActivity(i);

                }
            });
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

        private int determineIcone(int hour , String AM_PM){

            int icone = R.drawable.sun;

            switch (hour){

                case 1:
                case 2:
                case 3:
                case 4:icone = (AM_PM.equals("AM")) ?  R.drawable.night : R.drawable.sun;
                           break;

                case 5:
                case 6:   icone = (AM_PM.equals("AM")) ?  R.drawable.sunrise : R.drawable.sunset;
                           break;


                case 7:
                case 8:
                case 9:
                case 10:
                case 11:icone = (AM_PM.equals("AM")) ?  R.drawable.sun : R.drawable.night;
                        break;
                case 12: icone = (AM_PM.equals("AM")) ?  R.drawable.night : R.drawable.sun;
                               break;


            }
            return icone;

        }

        private int convertToCelsius (double tempFahrenheit){

            double tempCelsius = (tempFahrenheit-32) / 1.8 ;
            return (int) tempCelsius;
        }


    }

}

