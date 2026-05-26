package com.mirea.shelmichas.employeedb;

// =========================================================
// DATABASE - БАЗА ДАННЫХ ROOM
// =========================================================
// Абстрактный класс, наследующий RoomDatabase
// Связывает Entity (таблицы) и DAO (запросы)
// Версия базы увеличивается при изменении структуры таблиц
// =========================================================

import androidx.room.Database;
import androidx.room.RoomDatabase;

// @Database указывает:
// - entities = какие таблицы входят в БД
// - version = версия схемы (при изменении таблиц нужно увеличить)
@Database(entities = {Employee.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    // Абстрактный метод, возвращающий DAO
    // Room сам генерирует реализацию этого метода
    public abstract EmployeeDao employeeDao();
}
