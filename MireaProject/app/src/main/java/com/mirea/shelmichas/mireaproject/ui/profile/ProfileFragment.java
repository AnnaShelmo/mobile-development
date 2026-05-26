package com.mirea.shelmichas.mireaproject.ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mirea.shelmichas.mireaproject.R;

public class ProfileFragment extends Fragment {

    private static final String PREFS_NAME = "profile_prefs";
    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENRE = "genre";

    private EditText editName;
    private EditText editAge;
    private Spinner spinnerGenre;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        editName = view.findViewById(R.id.editName);
        editAge = view.findViewById(R.id.editAge);
        spinnerGenre = view.findViewById(R.id.spinnerGenre);
        Button btnSave = view.findViewById(R.id.btnSave);

        // Настройка Spinner с жанрами
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.genres, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenre.setAdapter(adapter);

        // Загружаем сохранённые данные
        loadProfile();

        // Сохраняем данные
        btnSave.setOnClickListener(v -> saveProfile());

        return view;
    }

    private void saveProfile() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_NAME, editName.getText().toString());
        editor.putString(KEY_AGE, editAge.getText().toString());
        editor.putString(KEY_GENRE, spinnerGenre.getSelectedItem().toString());
        editor.apply();
        Toast.makeText(getContext(), "Профиль сохранён!", Toast.LENGTH_SHORT).show();
    }

    private void loadProfile() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, 0);
        editName.setText(prefs.getString(KEY_NAME, ""));
        editAge.setText(prefs.getString(KEY_AGE, ""));

        String savedGenre = prefs.getString(KEY_GENRE, "");
        if (!savedGenre.isEmpty()) {
            for (int i = 0; i < spinnerGenre.getCount(); i++) {
                if (spinnerGenre.getItemAtPosition(i).equals(savedGenre)) {
                    spinnerGenre.setSelection(i);
                    break;
                }
            }
        }
    }
}
