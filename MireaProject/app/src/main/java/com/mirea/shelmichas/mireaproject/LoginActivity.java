package com.mirea.shelmichas.mireaproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mirea.shelmichas.mireaproject.databinding.ActivityLoginBinding;

// Экран входа/регистрации через Firebase Authentication
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private boolean isAuthInProgress = false; // защита от двойного клика

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(binding.fieldEmail.getText().toString(),
                        binding.fieldPassword.getText().toString());
            }
        });

        binding.buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(binding.fieldEmail.getText().toString(),
                        binding.fieldPassword.getText().toString());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Если уже вошёл — сразу на главную (с защитой isFinishing)
        if (mAuth.getCurrentUser() != null && !isFinishing()) {
            startMainActivity();
        }
    }

    // Регистрация — isAuthInProgress блокирует двойной клик
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm() || isAuthInProgress) return;
        isAuthInProgress = true;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        isAuthInProgress = false;
                        if (task.isSuccessful()) {
                            startMainActivity();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Ошибка: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // Вход — isAuthInProgress блокирует двойной клик
    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm() || isAuthInProgress) return;
        isAuthInProgress = true;

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        isAuthInProgress = false;
                        if (task.isSuccessful()) {
                            startMainActivity();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Ошибка: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // Защита: не стартуем MainActivity если Activity уже умирает
    private void startMainActivity() {
        if (isFinishing() || isDestroyed()) return;
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validateForm() {
        boolean valid = true;
        String email = binding.fieldEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            binding.fieldEmail.setError("Required.");
            valid = false;
        } else {
            binding.fieldEmail.setError(null);
        }
        String password = binding.fieldPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            binding.fieldPassword.setError("Required.");
            valid = false;
        } else {
            binding.fieldPassword.setError(null);
        }
        return valid;
    }
}
