package com.mirea.shelmichas.employeedb;

// Демонстрирует CRUD-операции с Room:
// 1. INSERT - добавляем сотрудника
// 2. SELECT (getAll) - получаем всех
// 3. SELECT (getById) - получаем одного
// 4. UPDATE - изменяем зарплату

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Тэг для Logcat
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ПОЛУЧАЕМ ДОСТУП К БД ЧЕРЕЗ SINGLETON
        // App.getInstance() - получаем экземпляр приложения
        // getDatabase() - получаем БД
        // employeeDao() - получаем DAO для работы с таблицей employee
        AppDatabase db = App.getInstance().getDatabase();
        EmployeeDao employeeDao = db.employeeDao();

        // 1. CREATE (INSERT) - ДОБАВЛЕНИЕ
        // Создаём объект Employee (строка таблицы)
        // id не задаём - autoGenerate проставит сам
        Employee employee = new Employee();
        employee.name = "John Smith";
        employee.salary = 10000;

        // Вставляем в базу (INSERT)
        // Room сам генерирует SQL: INSERT INTO employee (name, salary) VALUES (?, ?)
        employeeDao.insert(employee);
        Log.d(TAG, "Сотрудник добавлен: " + employee.name);

        // 2. READ (SELECT) - ЧТЕНИЕ ВСЕХ ЗАПИСЕЙ
        // SELECT * FROM employee
        List<Employee> employees = employeeDao.getAll();
        Log.d(TAG, "Всего сотрудников: " + employees.size());

        // 3. READ (SELECT) - ЧТЕНИЕ ПО ID
        // SELECT * FROM employee WHERE id = 1
        employee = employeeDao.getById(1);
        Log.d(TAG, "Сотрудник с id=1: " + employee.name);

        // 4. UPDATE - ОБНОВЛЕНИЕ
        // Меняем поле объекта и обновляем запись
        // Room находит запись по id и обновляет все поля
        employee.salary = 20000;  // Повысили зарплату
        employeeDao.update(employee);
        Log.d(TAG, "Зарплата обновлена");


        // ВЫВОД РЕЗУЛЬТАТА В LOGCAT
        // Проверяем что изменилось
        Log.d(TAG, employee.name + " " + employee.salary);
    }
}
