package com.mirea.shelmichas.audiorecord;

// Подключаем нужные библиотеки
import android.Manifest;                    // Для работы с разрешениями
import android.content.pm.PackageManager;   // Для проверки разрешений
import android.media.MediaPlayer;           // Для воспроизведения звука
import android.media.MediaRecorder;         // Для записи звука
import android.os.Bundle;                   // Для сохранения состояния
import android.os.Environment;              // Для доступа к папкам на устройстве
import android.view.View;                   // Для обработки нажатий
import android.widget.Button;               // Для кнопок

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;  // Базовая активность
import androidx.core.app.ActivityCompat;         // Для запроса разрешений
import androidx.core.content.ContextCompat;      // Для проверки разрешений

import com.mirea.shelmichas.audiorecord.databinding.ActivityMainBinding;  // ViewBinding

import java.io.File;       // Для работы с файлами
import java.io.IOException;

// Главный класс - активность
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private static final int REQUEST_CODE_PERMISSION = 200;  // Код запроса разрешений

    private boolean isWork;                     // Работает ли приложение (есть ли разрешения)
    private String recordFilePath = null;       // Путь к файлу, куда будем сохранять запись

    private Button recordButton = null;         // Кнопка записи
    private Button playButton = null;           // Кнопка воспроизведения

    private MediaRecorder recorder = null;      // Объект для записи звука
    private MediaPlayer player = null;          // Объект для воспроизведения

    boolean isStartRecording = true;   // Флаг: начинаем запись (true) или останавливаем (false)
    boolean isStartPlaying = true;     // Флаг: начинаем воспроизведение (true) или останавливаем (false)

    // Метод, который выполняется при создании активности
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Создаём layout через ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Получаем кнопки из layout
        recordButton = binding.recordButton;
        playButton = binding.playButton;

        // Изначально кнопка воспроизведения недоступна (нет записи)
        playButton.setEnabled(false);

        // Создаём путь к файлу записи
        // Файл будет в: Android/data/.../files/Music/audiorecordtest.3gp
        recordFilePath = new File(
                getExternalFilesDir(Environment.DIRECTORY_MUSIC),  // Папка Music
                "audiorecordtest.3gp"                               // Имя файла
        ).getAbsolutePath();

        // === ПРОВЕРКА РАЗРЕШЕНИЙ ===

        // Проверяем: есть ли разрешение на запись звука
        int audioRecordPermissionStatus = ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECORD_AUDIO);

        // Проверяем: есть ли разрешение на запись в память
        int storagePermissionStatus = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // Если ОБА разрешения есть - работаем
        if (audioRecordPermissionStatus == PackageManager.PERMISSION_GRANTED
                && storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;  // Разрешения есть, работаем
        } else {
            // Если нет - запрашиваем у пользователя
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.RECORD_AUDIO,        // Запись звука
                            Manifest.permission.WRITE_EXTERNAL_STORAGE  // Запись в память
                    },
                    REQUEST_CODE_PERMISSION);  // Код запроса
        }

        // === ОБРАБОТЧИК КНОПКИ ЗАПИСИ ===

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStartRecording) {
                    // НАЧАТЬ ЗАПИСЬ
                    startRecording();
                    recordButton.setText("Stop recording");  // Меняем текст на "Остановить"
                    playButton.setEnabled(false);            // Блокируем кнопку воспроизведения
                } else {
                    // ОСТАНОВИТЬ ЗАПИСЬ
                    stopRecording();
                    recordButton.setText("Start recording"); // Меняем текст на "Записать"
                    playButton.setEnabled(true);             // Разблокируем кнопку воспроизведения
                }
                // Инвертируем флаг (меняем true на false или наоборот)
                isStartRecording = !isStartRecording;
            }
        });

        // === ОБРАБОТЧИК КНОПКИ ВОСПРОИЗВЕДЕНИЯ ===

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStartPlaying) {
                    // НАЧАТЬ ВОСПРОИЗВЕДЕНИЕ
                    startPlaying();
                    playButton.setText("Stop playing");     // Меняем текст на "Остановить"
                    recordButton.setEnabled(false);         // Блокируем кнопку записи
                } else {
                    // ОСТАНОВИТЬ ВОСПРОИЗВЕДЕНИЕ
                    stopPlaying();
                    playButton.setText("Start playing");    // Меняем текст на "Воспроизвести"
                    recordButton.setEnabled(true);          // Разблокируем кнопку записи
                }
                // Инвертируем флаг
                isStartPlaying = !isStartPlaying;
            }
        });
    }

    // Метод, который вызывается после ответа пользователя на запрос разрешений
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Проверяем, что это ответ на наш запрос
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                // Проверяем, дал ли пользователь разрешение
                isWork = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        // Если разрешение не дано - закрываем приложение
        if (!isWork) finish();
    }

    // === МЕТОДЫ ДЛЯ РАБОТЫ С ЗАПИСЬЮ ===

    // Начать запись
    private void startRecording() {
        recorder = new MediaRecorder();  // Создаём новый recorder

        // 1. Указываем источник звука - микрофон
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        // 2. Указываем формат вывода - 3GP (компактный)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        // 3. Указываем кодек - AMR NB (для голоса, маленький размер)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        // 4. Указываем путь к файлу для сохранения
        recorder.setOutputFile(recordFilePath);

        try {
            recorder.prepare();  // Подготавливаем recorder к работе
        } catch (IOException e) {
            e.printStackTrace(); // Ошибка
        }

        recorder.start();  // Начинаем запись
    }

    // Остановить запись
    private void stopRecording() {
        recorder.stop();      // Останавливаем запись
        recorder.release();   // Освобождаем ресурсы
        recorder = null;      // Обнуляем ссылку
    }

    // === МЕТОДЫ ДЛЯ РАБОТЫ С ВОСПРОИЗВЕДЕНИЕМ ===

    // Начать воспроизведение
    private void startPlaying() {
        player = new MediaPlayer();  // Создаём новый player

        try {
            // 1. Указываем откуда читать файл
            player.setDataSource(recordFilePath);

            player.prepare();  // Подготавливаем к работе

            player.start();    // Начинаем воспроизведение
        } catch (IOException e) {
            e.printStackTrace(); // Ошибка
        }
    }

    // Остановить воспроизведение
    private void stopPlaying() {
        player.release();  // Освобождаем ресурсы
        player = null;     // Обнуляем ссылку
    }
}