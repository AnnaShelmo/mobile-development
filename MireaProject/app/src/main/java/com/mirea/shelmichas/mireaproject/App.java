package com.mirea.shelmichas.mireaproject;

import android.app.Application;

import com.yandex.mapkit.MapKitFactory;

// Application-класс — инициализация при запуске приложения
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Яндекс MapKit: ключ + инициализация один раз с App context
        MapKitFactory.setApiKey("f0176943-02f5-4092-a4fc-95dc37e7bafd");
        MapKitFactory.initialize(this);
    }
}
