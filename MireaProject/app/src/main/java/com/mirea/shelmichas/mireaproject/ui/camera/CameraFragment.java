package com.mirea.shelmichas.mireaproject.ui.camera;

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

    private Uri imageUri;
    private ImageView imageView;
    private EditText editNote;
    private Button btnTakePhoto;
    private Button btnSaveNote;
    private LinearLayout notesContainer;
    private List<NoteItem> notesList = new ArrayList<>();
    private File photoFile;
    private Bitmap currentPhoto;

    private ActivityResultLauncher<String> permissionLauncher;
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        imageView = view.findViewById(R.id.imageView);
        editNote = view.findViewById(R.id.editNote);
        btnTakePhoto = view.findViewById(R.id.btnTakePhoto);
        btnSaveNote = view.findViewById(R.id.btnSaveNote);
        notesContainer = view.findViewById(R.id.notesContainer);

        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        takePhoto();
                    } else {
                        Toast.makeText(getContext(), "Разрешение отклонено!", Toast.LENGTH_SHORT).show();
                    }
                });

        ActivityResultCallback<ActivityResult> callback = new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == android.app.Activity.RESULT_OK && imageUri != null) {
                    try {
                        InputStream is = requireContext().getContentResolver().openInputStream(imageUri);
                        currentPhoto = BitmapFactory.decodeStream(is);
                        imageView.setImageBitmap(currentPhoto);
                        Toast.makeText(getContext(), "Фото готово! Добавьте заметку и сохраните.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                callback);

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) 
                        == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA);
                }
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

    private void takePhoto() {
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (photoFile == null) {
            Toast.makeText(getContext(), "Ошибка создания файла!", Toast.LENGTH_SHORT).show();
            return;
        }

        String authorities = requireContext().getPackageName() + ".fileprovider";
        imageUri = FileProvider.getUriForFile(requireContext(), authorities, photoFile);
        
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(cameraIntent);
    }

    private void saveNote() {
        String noteText = editNote.getText().toString();
        
        if (currentPhoto == null) {
            Toast.makeText(getContext(), "Сначала сделайте фото!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (noteText.isEmpty()) {
            Toast.makeText(getContext(), "Введите заметку!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (notesList.size() >= 5) {
            Toast.makeText(getContext(), "Максимум 5 заметок! Удалите старые.", Toast.LENGTH_SHORT).show();
            return;
        }

        NoteItem note = new NoteItem(currentPhoto, noteText);
        notesList.add(note);

        addNoteToView(note);

        currentPhoto = null;
        imageView.setImageBitmap(null);
        editNote.setText("");
        
        Toast.makeText(getContext(), "Заметка сохранена!", Toast.LENGTH_SHORT).show();
    }

    private void addNoteToView(NoteItem note) {
        View noteView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, notesContainer, false);
        
        ImageView noteImage = noteView.findViewById(R.id.noteImage);
        TextView noteText = noteView.findViewById(R.id.noteText);
        
        noteImage.setImageBitmap(note.photo);
        noteText.setText(note.text);
        
        notesContainer.addView(noteView);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.ENGLISH).format(new Date());
        String imageFileName = "PHOTO_" + timeStamp;
        File storageDirectory = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(storageDirectory, imageFileName + ".jpg");
    }

    static class NoteItem {
        Bitmap photo;
        String text;

        NoteItem(Bitmap photo, String text) {
            this.photo = photo;
            this.text = text;
        }
    }
}