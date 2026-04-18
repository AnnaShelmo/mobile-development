package com.mirea.shelmichas.mireaproject.ui.recorder;

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

    private Button btnRecord;
    private Button btnStopRecord;
    private EditText editNoteName;
    private Button btnSaveNote;
    private LinearLayout notesContainer;
    private List<AudioNoteItem> notesList = new ArrayList<>();
    
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private File currentFile = null;
    private static final int REQUEST_CODE_PERMISSION = 200;
    private boolean isRecording = false;

    private ActivityResultLauncher<String> permissionLauncher;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recorder, container, false);

        btnRecord = view.findViewById(R.id.btnRecord);
        btnStopRecord = view.findViewById(R.id.btnStopRecord);
        editNoteName = view.findViewById(R.id.editNoteName);
        btnSaveNote = view.findViewById(R.id.btnSaveNote);
        notesContainer = view.findViewById(R.id.notesContainer);

        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        startRecording();
                    } else {
                        Toast.makeText(getContext(), "Разрешение отклонено!", Toast.LENGTH_SHORT).show();
                    }
                });

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) 
                        == PackageManager.PERMISSION_GRANTED) {
                    startRecording();
                } else {
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
                }
            }
        });

        btnStopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });

        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        return view;
    }

    private void startRecording() {
        try {
            File musicDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC);
            if (musicDir == null) {
                Toast.makeText(getContext(), "Папка не найдена!", Toast.LENGTH_SHORT).show();
                return;
            }
            
            currentFile = new File(musicDir, "record_" + System.currentTimeMillis() + ".3gp");
            
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(currentFile.getAbsolutePath());
            
            recorder.prepare();
            recorder.start();
            
            isRecording = true;
            btnRecord.setEnabled(false);
            btnStopRecord.setEnabled(true);
            btnSaveNote.setEnabled(false);
            
            Toast.makeText(getContext(), "Запись...", Toast.LENGTH_SHORT).show();
            
        } catch (Exception e) {
            Toast.makeText(getContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
                recorder = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        isRecording = false;
        btnRecord.setEnabled(true);
        btnStopRecord.setEnabled(false);
        btnSaveNote.setEnabled(true);
        
        Toast.makeText(getContext(), "Запись завершена! Введите название и сохраните.", Toast.LENGTH_SHORT).show();
    }

    private void saveNote() {
        String name = editNoteName.getText().toString().trim();
        
        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Введите название!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (currentFile == null || !currentFile.exists()) {
            Toast.makeText(getContext(), "Нет записи!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (notesList.size() >= 5) {
            Toast.makeText(getContext(), "Максимум 5 заметок!", Toast.LENGTH_SHORT).show();
            return;
        }

        AudioNoteItem note = new AudioNoteItem(name, currentFile);
        notesList.add(note);
        
        addNoteToView(note);
        
        currentFile = null;
        editNoteName.setText("");
        btnSaveNote.setEnabled(false);
        
        Toast.makeText(getContext(), "Заметка сохранена!", Toast.LENGTH_SHORT).show();
    }

    private void addNoteToView(AudioNoteItem note) {
        View noteView = LayoutInflater.from(getContext()).inflate(R.layout.item_audio_note, notesContainer, false);
        
        TextView noteName = noteView.findViewById(R.id.noteName);
        Button btnPlay = noteView.findViewById(R.id.btnPlay);
        Button btnStop = noteView.findViewById(R.id.btnStop);
        
        noteName.setText(note.name);
        
        btnPlay.setOnClickListener(v -> playNote(note));
        
        btnStop.setOnClickListener(v -> {
            stopPlaying();
            Toast.makeText(getContext(), "Остановлено!", Toast.LENGTH_SHORT).show();
        });
        
        notesContainer.addView(noteView);
    }

    private void playNote(AudioNoteItem note) {
        stopPlaying();
        
        player = new MediaPlayer();
        try {
            player.setDataSource(note.file.getAbsolutePath());
            player.prepare();
            player.start();
            Toast.makeText(getContext(), "Воспроизведение: " + note.name, Toast.LENGTH_SHORT).show();
            
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

    private void stopPlaying() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void deleteNote(AudioNoteItem note, View noteView) {
        note.file.delete();
        notesList.remove(note);
        notesContainer.removeView(noteView);
        Toast.makeText(getContext(), "Удалено!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
        stopPlaying();
    }

    static class AudioNoteItem {
        String name;
        File file;

        AudioNoteItem(String name, File file) {
            this.name = name;
            this.file = file;
        }
    }
}