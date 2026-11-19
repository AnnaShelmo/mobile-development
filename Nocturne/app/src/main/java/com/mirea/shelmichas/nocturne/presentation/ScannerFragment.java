package com.mirea.shelmichas.nocturne.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mirea.shelmichas.nocturne.R;

import java.util.List;

// Экран сканера: выбирает фото и показывает результат, который подготовил ViewModel.
public class ScannerFragment extends Fragment {

    // Ссылки на элементы разметки fragment_scanner.xml.
    private ScannerViewModel vm;
    private ImageView previewView;
    private Button pickButton;
    private ProgressBar progressView;
    private TextView statusView;
    private View resultLayout;
    private TextView topResultView;
    private TextView top3View;

    // Лаунчер системной галереи. Когда пользователь выберет фото,
    // сюда придёт Uri этого файла. Разрешения на галерею не нужны.
    private final ActivityResultLauncher<String> pickImage =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null && vm != null) {
                    // Отдаём фото ViewModel — там начнётся распознавание.
                    vm.recognize(uri);
                }
            });

    public ScannerFragment() {
        // Какой layout создаёт этот Fragment.
        super(R.layout.fragment_scanner);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Находим элементы экрана по их id.
        previewView = view.findViewById(R.id.imageViewScannerPreview);
        pickButton = view.findViewById(R.id.buttonPickImage);
        progressView = view.findViewById(R.id.progressScanner);
        statusView = view.findViewById(R.id.textViewScannerStatus);
        resultLayout = view.findViewById(R.id.layoutScannerResult);
        topResultView = view.findViewById(R.id.textViewScannerTopResult);
        top3View = view.findViewById(R.id.textViewScannerTop3);

        // 2. Получаем ViewModel через ViewModelProvider. Он переживает поворот экрана, поэтому выбранное фото и результат не пропадут.
        ViewModelProvider.AndroidViewModelFactory factory =
                ViewModelProvider.AndroidViewModelFactory.getInstance(
                        requireActivity().getApplication());
        vm = new ViewModelProvider(this, factory).get(ScannerViewModel.class);

        // 3. Вешаем обработчик на кнопку: открывает галерею и просит только изображения.
        pickButton.setOnClickListener(v -> pickImage.launch("image/*"));

        // 4. Fragment подписывается на 4 состояния, слушает ViewModel и обновляет экран.
        // 4.1 Выбранное фото: показываем его в предпросмотре.
        vm.getImage().observe(getViewLifecycleOwner(), bitmap -> {
            if (bitmap != null) {
                previewView.setImageBitmap(bitmap);
            }
        });

        // 4.2 Идёт распознавание: крутим ProgressBar и блокируем кнопку, чтобы нельзя было запустить второе распознавание одновременно.
        vm.getLoading().observe(getViewLifecycleOwner(), loading -> {
            boolean isLoading = Boolean.TRUE.equals(loading);
            progressView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            pickButton.setEnabled(!isLoading);
            if (isLoading) {
                statusView.setText("Распознавание изображения...");
                resultLayout.setVisibility(View.GONE);
            }
        });

        // 4.3 Если фото прочитать не удалось, показываем текст ошибки.
        vm.getError().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                statusView.setText(message);
                resultLayout.setVisibility(View.GONE);
            }
        });

        // 4.4 Готовый результат: список классов с вероятностями.
        vm.getPredictions().observe(getViewLifecycleOwner(), this::showPredictions);
    }

    // Показываем лучший класс крупно, а ниже — первые три класса списком.
    private void showPredictions(List<ScannerViewModel.Prediction> predictions) {
        // null означает, что распознавание ещё не закончилось.
        if (predictions == null) {
            return;
        }
        // Пустой список — модель не смогла выделить ни один класс.
        if (predictions.isEmpty()) {
            statusView.setText("Модель не смогла определить объект");
            resultLayout.setVisibility(View.GONE);
            return;
        }

        statusView.setText("");
        resultLayout.setVisibility(View.VISIBLE);

        // Когда ViewModel присылает список:
        // Первый элемент — самый вероятный класс.
        ScannerViewModel.Prediction top = predictions.get(0);
        topResultView.setText(top.getLabel() + " — " + top.getPercent() + "%");

        // Собираем строки "1. Класс — 48%" для топ-3.
        StringBuilder builder = new StringBuilder();
        int count = Math.min(3, predictions.size());
        for (int i = 0; i < count; i++) {
            ScannerViewModel.Prediction prediction = predictions.get(i);
            if (i > 0) {
                builder.append('\n');
            }
            builder.append(i + 1)
                    .append(". ")
                    .append(prediction.getLabel())
                    .append(" — ")
                    .append(prediction.getPercent())
                    .append('%');
        }
        top3View.setText(builder.toString());
    }
}
