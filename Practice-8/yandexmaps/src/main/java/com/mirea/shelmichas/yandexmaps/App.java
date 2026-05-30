package com.mirea.shelmichas.yandexmaps;

import android.app.Application;

import com.yandex.mapkit.MapKitFactory;

// Класс Application для инициализации MapKit с API-ключом
public class App extends Application {

    // API-ключ получен в кабинете разработчика Яндекс
    private final String MAPKIT_API_KEY = "f0176943-02f5-4092-a4fc-95dc37e7bafd";

    @Override
    public void onCreate() {
        super.onCreate();
        // Установка ключа перед инициализацией MapKitFactory
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
    }
}
