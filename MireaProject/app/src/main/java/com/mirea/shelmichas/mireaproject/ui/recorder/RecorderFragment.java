package com.mirea.shelmichas.mireaproject.ui.recorder;

// =========================================================
// ДИКТОФОН - ЗАПИСЬ ГОЛОСОВЫХ ЗАМЕТОК
// =========================================================
// Приложение записывает аудио через микрофон
// После записи можно сохранить с названием
// Список заметок с кнопками воспроизведения
// =========================================================

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.mirea.shelmichas.mireaproject.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecorderFragment extends Fragment {

    // =====================================
    // ПЕРЕМЕННЫЕ
    // =====================================
    private Button btnRecord;               // Кнопка "Запись"
    private Button btnStopRecord;          // Кнопка "Стоп"
    private EditText editNoteName;         // Поле для названия заметки
    private Button btnSaveNote;            // Кнопка "Сохранить"
    private LinearLayout notesContainer;   // Контейнер для списка заметок
    private List<AudioNoteItem> notesList = new ArrayList<>();  // Список всех заметок
    
    private MediaRecorder recorder = null;  // Объект для записи аудио
    private MediaPlayer player = null;      // Объект для воспроизведения аудио
    private File currentFile = null;        // Текущий файл записи
    private static final int REQUEST_CODE_PERMISSION = 200;  // Код запроса (не используется)
    private boolean isRecording = false;    // Флаг состояния записи

    private ActivityResultLauncher<String> permissionLauncher;  // launcher для разрешений

    // Создание View (UI)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recorder, container, false);

        // =====================================
        // НАХОДИМ ЭЛЕМЕНТЫ UI
        // =====================================
        btnRecord = view.findViewById(R.id.btnRecord);
        btnStopRecord = view.findViewById(R.id.btnStopRecord);
        editNoteName = view.findViewById(R.id.editNoteName);
        btnSaveNote = view.findViewById(R.id.btnSaveNote);
        notesContainer = view.findViewById(R.id.notesContainer);

        // =====================================
        // РЕГИСТРИРУЕМ ЗАПРОС РАЗРЕШЕНИЙ НА МИКРОФОН
        // =====================================
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        startRecording();  // Разрешение получено - начинаем запись
                    } else {
                        Toast.makeText(getContext(), "Разрешение отклонено!", Toast.LENGTH_SHORT).show();
                    }
                });

        // =====================================
        // ОБРАБОТЧИК КНОПКИ "ЗАПИСЬ"
        // =====================================
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Проверяем разрешение на микрофон
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) 
                        == PackageManager.PERMISSION_GRANTED) {
                    startRecording();  // Есть разрешение - начинаем запись
                } else {
                    // Нет разрешения - запрашиваем
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
                }
            }
        });

        // =====================================
        // ОБРАБОТЧИК КНОПКИ "СТОП"
        // =====================================
        btnStopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();  // Останавливаем запись
            }
        });

        // =====================================
        // ОБРАБОТЧИК КНОПКИ "СОХРАНИТЬ"
        // =====================================
        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();  // Сохраняем заметку
            }
        });

        return view;
    }

    // =====================================
    // НАЧАЛО ЗАПИСИ
    // =====================================
    private void startRecording() {
        try {
            // Получаем папку для музыки во внешнем хранилище
            File musicDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC);
            if (musicDir == null) {
                Toast.makeText(getContext(), "Папка не найдена!", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Создаём файл с уникальным именем по времени
            currentFile = new File(musicDir, "record_" + System.currentTimeMillis() + ".3gp");
            
            // =====================================
            // MEDIARECORDER - ЗАПИСЬ АУДИО
            // =====================================
            recorder = new MediaRecorder();
            // Источник звука - микрофон
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // Формат вывода - 3GP (компактный, подходит для голоса)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            // Кодировщик звука - AMR-NB (оптимизирован для речи)
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            // Куда сохранять
            recorder.setOutputFile(currentFile.getAbsolutePath());
            
            // Подготавливаем и запускаем запись
            recorder.prepare();
            recorder.start();
            
            // Обновляем состояние
            isRecording = true;
            btnRecord.setEnabled(false);     // Блокируем кнопку записи
            btnStopRecord.setEnabled(true);  // Разблокируем кнопку стопа
            btnSaveNote.setEnabled(false);   // Блокируем сохранение
            
            Toast.makeText(getContext(), "Запись...", Toast.LENGTH_SHORT).show();
            
        } catch (Exception e) {
            Toast.makeText(getContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // =====================================
    // ОСТАНОВКА ЗАПИСИ
    // =====================================
    private void stopRecording() {
        if (recorder != null) {
            try {
                // Останавливаем запись
                recorder.stop();
                // Освобождаем ресурсы
                recorder.release();
                recorder = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // Обновляем состояние кнопок
        isRecording = false;
        btnRecord.setEnabled(true);      // Разблокируем запись
        btnStopRecord.setEnabled(false); // Блокируем стоп
        btnSaveNote.setEnabled(true);    // Разблокируем сохранение
        
        Toast.makeText(getContext(), "Запись завершена! Введите название и сохраните.", Toast.LENGTH_SHORT).show();
    }

    // =====================================
    // СОХРАНЕНИЕ ЗАМЕТКИ
    // =====================================
    private void saveNote() {
        String name = editNoteName.getText().toString().trim();
        
        // Проверяем название
        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Введите название!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Проверяем что файл существует
        if (currentFile == null || !currentFile.exists()) {
            Toast.makeText(getContext(), "Нет записи!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Ограничиваем количество заметок
        if (notesList.size() >= 5) {
            Toast.makeText(getContext(), "Максимум 5 заметок!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Создаём объект заметки и добавляем в список
        AudioNoteItem note = new AudioNoteItem(name, currentFile);
        notesList.add(note);
        
        // Отображаем заметку в списке
        addNoteToView(note);
        
        // Очищаем поля для следующей заметки
        currentFile = null;
        editNoteName.setText("");
        btnSaveNote.setEnabled(false);
        
        Toast.makeText(getContext(), "Заметка сохранена!", Toast.LENGTH_SHORT).show();
    }

    // =====================================
    // ДОБАВЛЕНИЕ ЗАМЕТКИ В СПИСОК
    // =====================================
    private void addNoteToView(AudioNoteItem note) {
        // Создаём View из layout-файла
        View noteView = LayoutInflater.from(getContext()).inflate(R.layout.item_audio_note, notesContainer, false);
        
        // Находим элементы в этом View
        TextView noteName = noteView.findViewById(R.id.noteName);
        Button btnPlay = noteView.findViewById(R.id.btnPlay);
        Button btnStop = noteView.findViewById(R.id.btnStop);
        
        // Устанавливаем название
        noteName.setText(note.name);
        
        // Обработчик воспроизведения
        btnPlay.setOnClickListener(v -> playNote(note));
        
        // Обработчик остановки
        btnStop.setOnClickListener(v -> {
            stopPlaying();
            Toast.makeText(getContext(), "Остановлено!", Toast.LENGTH_SHORT).show();
        });
        
        // Добавляем в контейнер
        notesContainer.addView(noteView);
    }

    // =====================================
    // ВОСПРОИЗВЕДЕНИЕ ЗАПИСИ
    // =====================================
    private void playNote(AudioNoteItem note) {
        stopPlaying();  // Останавливаем текущее воспроизведение
        
        // Создаём новый MediaPlayer
        player = new MediaPlayer();
        try {
            // Указываем источник звука - файл
            player.setDataSource(note.file.getAbsolutePath());
            // Подготавливаем к воспроизведению
            player.prepare();
            // Начинаем воспроизведение
            player.start();
            Toast.makeText(getContext(), "Воспроизведение: " + note.name, Toast.LENGTH_SHORT).show();
            
            // Слушатель окончания воспроизведения
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Toast.makeText(getContext(), "Готово!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            Toast.makeText(getContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // =====================================
    // ОСТАНОВКА ВОСПРОИЗВЕДЕНИЯ
    // =====================================
    private void stopPlaying() {
        if (player != null) {
            player.release();  // Освобождаем ресурсы
            player = null;
        }
    }

    // =====================================
    // УДАЛЕНИЕ ЗАМЕТКИ (не используется)
    // =====================================
    private void deleteNote(AudioNoteItem note, View noteView) {
        note.file.delete();  // Удаляем файл с диска
        notesList.remove(note);  // Удаляем из списка
        notesContainer.removeView(noteView);  // Удаляем из UI
        Toast.makeText(getContext(), "Удалено!", Toast.LENGTH_SHORT).show();
    }

    // =====================================
    // ОЧИСТКА ПРИ УХОДЕ С ЭКРАНА
    // =====================================
    @Override
    public void onPause() {
        super.onPause();
        // Освобождаем recorder если он ещё работает
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
        // Останавливаем воспроизведение
        stopPlaying();
    }

    // =====================================
    // ВНУТРЕННИЙ КЛАСС ДЛЯ АУДИО ЗАМЕТКИ
    // =====================================
    static class AudioNoteItem {
        String name;  // Название заметки
        File file;    // Файл с записью

        AudioNoteItem(String name, File file) {
            this.name = name;
            this.file = file;
        }
    }
}