package com.mirea.shelmichas.mireaproject.ui.camera;

// =========================================================
// КАМЕРА - ЗАМЕТКИ С ФОТО
// =========================================================
// Приложение использует системную камеру через Intent
// После съёмки фото сохраняется с текстовой заметкой
// Список заметок отображается внизу экрана
// =========================================================

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.mirea.shelmichas.mireaproject.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CameraFragment extends Fragment {

    // =====================================
    // ПЕРЕМЕННЫЕ
    // =====================================
    private Uri imageUri;                    // URI сохранённого фото
    private ImageView imageView;              // Превью фото
    private EditText editNote;                // Поле для заметки
    private Button btnTakePhoto;              // Кнопка "Сделать фото"
    private Button btnSaveNote;               // Кнопка "Сохранить"
    private LinearLayout notesContainer;      // Контейнер для списка заметок
    private List<NoteItem> notesList = new ArrayList<>();  // Список всех заметок
    private File photoFile;                   // Файл для сохранения фото
    private Bitmap currentPhoto;             // Текущее фото (в памяти)

    // =====================================
    // ActivityResultLauncher - для работы с результатами
    // =====================================
    // launcher для запроса разрешений
    private ActivityResultLauncher<String> permissionLauncher;
    // launcher для запуска камеры и получения результата
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;

    // Создание View (UI)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        // =====================================
        // НАХОДИМ ЭЛЕМЕНТЫ UI
        // =====================================
        imageView = view.findViewById(R.id.imageView);
        editNote = view.findViewById(R.id.editNote);
        btnTakePhoto = view.findViewById(R.id.btnTakePhoto);
        btnSaveNote = view.findViewById(R.id.btnSaveNote);
        notesContainer = view.findViewById(R.id.notesContainer);

        // =====================================
        // РЕГИСТРИРУЕМ ЗАПРОС РАЗРЕШЕНИЙ
        // =====================================
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        takePhoto();  // Разрешение получено - запускаем камеру
                    } else {
                        Toast.makeText(getContext(), "Разрешение отклонено!", Toast.LENGTH_SHORT).show();
                    }
                });

        // =====================================
        // РЕГИСТРИРУЕМ ПОЛУЧЕНИЕ РЕЗУЛЬТАТА ОТ КАМЕРЫ
        // =====================================
        ActivityResultCallback<ActivityResult> callback = new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                // RESULT_OK - пользователь сделал фото
                if (result.getResultCode() == android.app.Activity.RESULT_OK && imageUri != null) {
                    try {
                        // Загружаем фото из URI в Bitmap
                        InputStream is = requireContext().getContentResolver().openInputStream(imageUri);
                        currentPhoto = BitmapFactory.decodeStream(is);
                        // Показываем фото в превью
                        imageView.setImageBitmap(currentPhoto);
                        Toast.makeText(getContext(), "Фото готово! Добавьте заметку и сохраните.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        // Регистрируем launcher для запуска камеры
        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                callback);

        // =====================================
        // ОБРАБОТЧИК КНОПКИ "СДЕЛАТЬ ФОТО"
        // =====================================
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Проверяем разрешение на камеру
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) 
                        == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();  // Есть разрешение - запускаем камеру
                } else {
                    // Нет разрешения - запрашиваем
                    permissionLauncher.launch(Manifest.permission.CAMERA);
                }
            }
        });

        // =====================================
        // ОБРАБОТЧИК КНОПКИ "СОХРАНИТЬ"
        // =====================================
        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();  // Сохраняем заметку с фото
            }
        });

        return view;
    }

    // =====================================
    // ЗАПУСК КАМЕРЫ
    // =====================================
    private void takePhoto() {
        try {
            // Создаём файл для сохранения фото
            photoFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (photoFile == null) {
            Toast.makeText(getContext(), "Ошибка создания файла!", Toast.LENGTH_SHORT).show();
            return;
        }

        // =====================================
        // FILEPROVIDER - безопасная передача файлов
        // =====================================
        // FileProvider позволяет безопасно передавать файлы
        // между приложениями (вместо file:// используется content://)
        String authorities = requireContext().getPackageName() + ".fileprovider";
        imageUri = FileProvider.getUriForFile(requireContext(), authorities, photoFile);
        
        // Создаём Intent для запуска системной камеры
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Говорим камере куда сохранить фото
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        // Запускаем камеру и ждём результата
        cameraActivityResultLauncher.launch(cameraIntent);
    }

    // =====================================
    // СОХРАНЕНИЕ ЗАМЕТКИ
    // =====================================
    private void saveNote() {
        String noteText = editNote.getText().toString();
        
        // Проверяем что фото сделано
        if (currentPhoto == null) {
            Toast.makeText(getContext(), "Сначала сделайте фото!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Проверяем что заметка введена
        if (noteText.isEmpty()) {
            Toast.makeText(getContext(), "Введите заметку!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ограничиваем количество заметок
        if (notesList.size() >= 5) {
            Toast.makeText(getContext(), "Максимум 5 заметок!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Создаём объект заметки и добавляем в список
        NoteItem note = new NoteItem(currentPhoto, noteText);
        notesList.add(note);

        // Отображаем заметку внизу экрана
        addNoteToView(note);

        // Очищаем поля для следующей заметки
        currentPhoto = null;
        imageView.setImageBitmap(null);
        editNote.setText("");
        
        Toast.makeText(getContext(), "Заметка сохранена!", Toast.LENGTH_SHORT).show();
    }

    // =====================================
    // ДОБАВЛЕНИЕ ЗАМЕТКИ В СПИСОК
    // =====================================
    private void addNoteToView(NoteItem note) {
        // Создаём View из layout-файла item_note.xml
        View noteView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, notesContainer, false);
        
        // Находим элементы в этом View
        ImageView noteImage = noteView.findViewById(R.id.noteImage);
        TextView noteText = noteView.findViewById(R.id.noteText);
        
        // Устанавливаем данные
        noteImage.setImageBitmap(note.photo);
        noteText.setText(note.text);
        
        // Добавляем в контейнер
        notesContainer.addView(noteView);
    }

    // =====================================
    // СОЗДАНИЕ ФАЙЛА ДЛЯ ФОТО
    // =====================================
    // Генерирует уникальное имя файла на основе времени
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.ENGLISH).format(new Date());
        String imageFileName = "PHOTO_" + timeStamp;
        // Папка: Android/data/.../files/Pictures/
        File storageDirectory = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(storageDirectory, imageFileName + ".jpg");
    }

    // =====================================
    // ВНУТРЕННИЙ КЛАСС ДЛЯ ЗАМЕТКИ
    // =====================================
    static class NoteItem {
        Bitmap photo;   // Фото
        String text;    // Текст заметки

        NoteItem(Bitmap photo, String text) {
            this.photo = photo;
            this.text = text;
        }
    }
}