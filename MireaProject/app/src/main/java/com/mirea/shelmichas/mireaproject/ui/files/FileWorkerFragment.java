package com.mirea.shelmichas.mireaproject.ui.files;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mirea.shelmichas.mireaproject.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FileWorkerFragment extends Fragment {

    private LinearLayout notesContainer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_file_worker, container, false);

        notesContainer = view.findViewById(R.id.notesContainer);
        FloatingActionButton fabAdd = view.findViewById(R.id.fabAdd);

        showNotes();

        fabAdd.setOnClickListener(v -> showAddDialog());

        return view;
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Новая заметка");

        EditText editNote = new EditText(requireContext());
        editNote.setHint("Введите текст заметки");
        editNote.setMinLines(4);
        builder.setView(editNote);

        builder.setPositiveButton("Сохранить", (dialog, which) -> {
            String text = editNote.getText().toString().trim();
            if (!text.isEmpty()) {
                saveNote(text);
            }
        });

        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void saveNote(String text) {
        String fileName = "note_" + System.currentTimeMillis() + ".txt";
        try (FileOutputStream fos = requireContext().openFileOutput(fileName, 0)) {
            fos.write(text.getBytes(StandardCharsets.UTF_8));
            Toast.makeText(getContext(), "Заметка сохранена!", Toast.LENGTH_SHORT).show();
            showNotes();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showNotes() {
        notesContainer.removeAllViews();

        String[] files = requireContext().fileList();
        Arrays.sort(files);

        if (files.length == 0) {
            TextView empty = new TextView(requireContext());
            empty.setText("Нет заметок. Нажмите + чтобы создать.");
            empty.setPadding(0, 32, 0, 0);
            notesContainer.addView(empty);
            return;
        }

        for (String fileName : files) {
            if (!fileName.endsWith(".txt")) continue;

            File file = new File(requireContext().getFilesDir(), fileName);

            String firstLine = readFirstLine(file);
            String title = (firstLine != null && !firstLine.isEmpty()) ? firstLine : "(пусто)";

            TextView tv = new TextView(requireContext());
            tv.setText(title);
            tv.setPadding(32, 24, 32, 24);
            tv.setTextSize(16);
            tv.setBackgroundColor(0xFFF5F5F5);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 8);
            tv.setLayoutParams(lp);

            tv.setOnClickListener(v -> showNote(file));

            notesContainer.addView(tv);
        }
    }

    private String readFirstLine(File file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            return reader.readLine();
        } catch (Exception e) {
            return null;
        }
    }

    private void showNote(File file) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        String text = content.toString().trim();
        int charCount = text.length();
        int wordCount = text.isEmpty() ? 0 : text.split("\\s+").length;
        long fileSize = file.length();

        String info = text + "\n\n---\n"
                + "Символов: " + charCount + "\n"
                + "Слов: " + wordCount + "\n"
                + "Размер файла: " + fileSize + " байт";

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Заметка");
        builder.setMessage(info);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
