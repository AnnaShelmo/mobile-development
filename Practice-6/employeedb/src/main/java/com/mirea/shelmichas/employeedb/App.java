package com.mirea.shelmichas.employeedb;

// =========================================================
// APPLICATION - ТОЧКА ВХОДА ПРИЛОЖЕНИЯ (SINGLETON)
// =========================================================
// Класс App создаётся один раз при запуске приложения
// Используется для инициализации глобальных компонентов
// Паттерн Singleton - один экземпляр на всё приложение
// =========================================================

import android.app.Application;

import androidx.room.Room;

public class App extends Application {

    // Статическая ссылка на себя (Singleton)
    public static App instance;

    // Объект базы данных (один на всё приложение)
    private AppDatabase database;

    // =====================================
    // ONCREATE - ВЫЗЫВАЕТСЯ ПРИ ЗАПУСКЕ ПРИЛОЖЕНИЯ
    // =====================================
    @Override
    public void onCreate() {
        super.onCreate();

        // Сохраняем экземпляр для статического доступа
        instance = this;

        // =====================================
        // СОЗДАНИЕ БАЗЫ ДАННЫХ
        // =====================================
        // Room.databaseBuilder - строитель для создания БД
        // Параметры: контекст, класс БД, имя файла .db
        // allowMainThreadQueries() - разрешаем запросы в главном потоке
        // (для учебных целей, в реальных проектах нужно в фоне)
        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .allowMainThreadQueries()
                .build();
    }

    // =====================================
    // ГЕТТЕРЫ ДЛЯ ДОСТУПА К БД
    // =====================================
    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
