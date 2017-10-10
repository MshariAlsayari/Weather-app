package com.example.msharialsayari.weatherapp.Retrofit.Interface;

import com.example.msharialsayari.weatherapp.Retrofit.Model.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by msharialsayari on 04/10/2017 AD.
 */

public interface NetworkApi {


   @GET("60b4d503c32296a88a5760f7cea43074/{latiLong}")  //{latiLong}
    Observable<Weather>getWeatherInfoByRX(@Path (value = "latiLong") String LatiLongitude);

    @GET("60b4d503c32296a88a5760f7cea43074/24.7255553,47.1027045")
    Call<Weather>getWeatherInfo();
}
