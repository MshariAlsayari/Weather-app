package com.example.msharialsayari.weatherapp.Dagger.Component;

import com.example.msharialsayari.weatherapp.Dagger.Module.WeatherAppModule;
import com.example.msharialsayari.weatherapp.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by msharialsayari on 04/10/2017 AD.
 */
@Singleton
@Component(modules = WeatherAppModule.class)
public interface WeatherAppComponent {

    void inject(MainActivity target);
}
