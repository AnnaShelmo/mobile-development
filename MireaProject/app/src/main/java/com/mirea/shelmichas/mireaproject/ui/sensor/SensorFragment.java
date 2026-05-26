package com.mirea.shelmichas.mireaproject.ui.sensor;

// =========================================================
// АКСЕЛЕРОМЕТР - ОПРЕДЕЛЕНИЕ ПОЛОЖЕНИЯ УСТРОЙСТВА
// =========================================================
// Акселерометр измеряет ускорение по трём осям: X, Y, Z
// Зная эти значения можно определить как лежит телефон:
// - Экран вверх/вниз
// - На левом/правом боку
// - Вертикально/вверх ногами
// =========================================================

import android.content.Context;
import android.hardware.Sensor;          // Базовый класс датчика
import android.hardware.SensorEvent;    // Событие изменения показаний
import android.hardware.SensorEventListener;  // Слушатель датчика
import android.hardware.SensorManager;  // Менеджер датчиков
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class SensorFragment extends Fragment implements SensorEventListener {

    // =====================================
    // ПЕРЕМЕННЫЕ
    // =====================================
    private SensorManager sensorManager;          // Менеджер датчиков
    private Sensor accelerometerSensor;            // Акселерометр (датчик ускорения)
    private TextView textPosition;                 // Текст с положением
    private TextView textDetails;                  // Текст с деталями (X, Y, Z)

    // Создание View (UI)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(com.mirea.shelmichas.mireaproject.R.layout.fragment_sensor, container, false);

        // =====================================
        // ПОЛУЧАЕМ ДОСТУП К ДАТЧИКАМ
        // =====================================
        // SensorManager - системный сервис для работы с датчиками
        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        
        // Получаем акселерометр по типу
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Находим TextView в layout
        textPosition = view.findViewById(com.mirea.shelmichas.mireaproject.R.id.textPosition);
        textDetails = view.findViewById(com.mirea.shelmichas.mireaproject.R.id.textDetails);

        // Проверяем доступность акселерометра
        if (accelerometerSensor == null) {
            Toast.makeText(getContext(), "Акселерометр недоступен!", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    // =====================================
    // РЕГИСТРАЦИЯ СЛУШАТЕЛЯ (когда экран виден)
    // =====================================
    // onResume вызывается когда фрагмент становится видимым
    @Override
    public void onResume() {
        super.onResume();
        // Регистрируем слушатель для акселерометра
        // 3-й параметр - частота обновления (SENSOR_DELAY_NORMAL - ~200мс)
        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    // =====================================
    // ОТМЕНА РЕГИСТРАЦИИ (когда экран скрыт)
    // =====================================
    // onPause вызывается когда фрагмент скрывается
    // Важно отменить регистрацию чтобы экономить батарею
    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);  // Отменяем слушатель
    }

    // =====================================
    // ПОЛУЧЕНИЕ ДАННЫХ ОТ ДАТЧИКА
    // =====================================
    // Этот метод вызывается автоматически при изменении показаний
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Проверяем что это данные от акселерометра
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            
            // Получаем значения по трём осям
            // X - влево/вправо (боковое ускорение)
            // Y - вперёд/назад (продольное ускорение)
            // Z - вверх/вниз (вертикальное ускорение)
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Определяем положение по значениям
            String position = determinePosition(x, y, z);
            
            // Обновляем текст на экране
            if (textPosition != null) {
                textPosition.setText(position);
            }
            if (textDetails != null) {
                textDetails.setText(String.format("X: %.2f  Y: %.2f  Z: %.2f", x, y, z));
            }
        }
    }

    // =====================================
    // ОПРЕДЕЛЕНИЕ ПОЛОЖЕНИЯ
    // =====================================
    // Логика определения положения телефона по значениям акселерометра
    private String determinePosition(float x, float y, float z) {
        float threshold = 5.0f;  // Пороговое значение (гравитация ~9.81)
        
        // Проверяем какая ось имеет наибольшее значение
        // Если |X| > threshold - лежим на боку
        if (Math.abs(x) > threshold) {
            return x > 0 ? "На левом боку" : "На правом боку";
        } 
        // Если |Y| > threshold - вертикально
        else if (Math.abs(y) > threshold) {
            return y > 0 ? "Вертикально" : "Вверх ногами";
        } 
        // Если Z > threshold - экран вверх (Z положительное = гравитация вниз)
        else if (z > threshold) {
            return "Экран вверх";
        } 
        // Если Z < -threshold - экран вниз
        else if (z < -threshold) {
            return "Экран вниз";
        }
        
        return "Определение...";
    }

    // Метод для изменения точности датчика (не используем)
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Не используется в этом приложении
    }
}