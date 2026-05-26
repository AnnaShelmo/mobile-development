package com.mirea.shelmichas.employeedb;

// =========================================================
// ENTITY (СУЩНОСТЬ) - ПРЕДСТАВЛЕНИЕ ТАБЛИЦЫ В БАЗЕ ДАННЫХ
// =========================================================
// Аннотация @Entity говорит Room, что этот класс - таблица
// Имя таблицы = имя класса (employee)
// Каждое поле класса = столбец в таблице
// =========================================================

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity  // Указывает, что это таблица БД
public class Employee {

    @PrimaryKey(autoGenerate = true)  // Первичный ключ, авто-инкремент
    public long id;     // Уникальный идентификатор сотрудника

    public String name;  // Имя сотрудника (TEXT в SQLite)

    public int salary;   // Зарплата (INTEGER в SQLite)
}
