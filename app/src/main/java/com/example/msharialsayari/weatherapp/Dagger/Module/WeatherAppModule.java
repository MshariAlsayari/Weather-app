package com.example.msharialsayari.weatherapp.Dagger.Module;

import com.example.msharialsayari.weatherapp.Retrofit.Interface.NetworkApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by msharialsayari on 04/10/2017 AD.
 */
@Module
public class WeatherAppModule {

    @Provides
    @Singleton
    public Retrofit createRetrofitObject (){

        return new Retrofit.Builder()
                .baseUrl("https://api.darksky.net/forecast/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


    }

    @Provides
    @Singleton
    NetworkApi provideNetworkApi(Retrofit retrofit) {
        return retrofit.create(NetworkApi.class);
    }
}
