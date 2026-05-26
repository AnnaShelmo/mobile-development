package com.mirea.shelmichas.employeedb;

// =========================================================
// DAO (DATA ACCESS OBJECT) - ИНТЕРФЕЙС ДЛЯ ЗАПРОСОВ К БД
// =========================================================
// DAO - слой абстракции между приложением и базой данных
// Содержит методы CRUD: Create, Read, Update, Delete
// Room сам генерирует реализацию этого интерфейса
// =========================================================

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao  // Указывает, что это DAO-интерфейс
public interface EmployeeDao {

    // READ - ВЫБОРКА ДАННЫХ

    // @Query - произвольный SQL-запрос
    // SELECT * FROM employee - получить все строки из таблицы employee
    // Room проверяет запрос на этапе компиляции!
    @Query("SELECT * FROM employee")
    List<Employee> getAll();  // Возвращает список всех сотрудников

    // Запрос с параметром :id
    // WHERE id = :id - фильтр по идентификатору
    @Query("SELECT * FROM employee WHERE id = :id")
    Employee getById(long id);  // Возвращает одного сотрудника по id

    // CREATE - ДОБАВЛЕНИЕ НОВОЙ ЗАПИСИ
    // @Insert автоматически генерирует INSERT-запрос
    @Insert
    void insert(Employee employee);  // Вставляет запись в таблицу

    // UPDATE - ОБНОВЛЕНИЕ СУЩЕСТВУЮЩЕЙ ЗАПИСИ
    // @Update находит запись по первичному ключу и обновляет поля
    @Update
    void update(Employee employee);  // Обновляет данные сотрудника

    // DELETE - УДАЛЕНИЕ ЗАПИСИ
    // @Delete находит запись по первичному ключу и удаляет
    @Delete
    void delete(Employee employee);  // Удаляет сотрудника из таблицы
}
