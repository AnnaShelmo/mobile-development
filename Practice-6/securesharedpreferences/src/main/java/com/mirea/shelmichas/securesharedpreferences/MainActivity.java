package com.mirea.shelmichas.securesharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {

    private ImageView imagePoet;
    private static final String PREFS_NAME = "secret_shared_prefs";
    private static final String KEY_POET = "FAVORITE_POET";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagePoet = findViewById(R.id.imagePoet);
        Button btnSave = findViewById(R.id.btnSave);

        // Устанавливаем фото
        imagePoet.setImageResource(R.drawable.haruki);

        // Загружаем данные
        loadData();

        // Сохраняем при нажатии
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void saveData() {
        try {
            // Создаём мастер-ключ
            KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
            String mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);

            // Создаём зашифрованные SharedPreferences
            SharedPreferences secureSharedPreferences = EncryptedSharedPreferences.create(
                    PREFS_NAME,
                    mainKeyAlias,
                    getBaseContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            // Сохраняем данные
            secureSharedPreferences.edit()
                    .putString(KEY_POET, "Харуки Мураками")
                    .apply();

            Toast.makeText(this, "Сохранено в зашифрованном виде!", Toast.LENGTH_SHORT).show();

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadData() {
        try {
            KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
            String mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);

            SharedPreferences secureSharedPreferences = EncryptedSharedPreferences.create(
                    PREFS_NAME,
                    mainKeyAlias,
                    getBaseContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            String poet = secureSharedPreferences.getString(KEY_POET, "Харуки Мураками");

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }
}