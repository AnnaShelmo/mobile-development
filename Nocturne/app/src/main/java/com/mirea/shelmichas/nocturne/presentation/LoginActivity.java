package com.mirea.shelmichas.nocturne.presentation;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mirea.shelmichas.nocturne.R;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText fieldEmail = findViewById(R.id.fieldEmail);
        EditText fieldPassword = findViewById(R.id.fieldPassword);
        TextView statusTextView = findViewById(R.id.textViewAuthStatus);

        vm = new ViewModelProvider(this, new NocturneViewModelFactory(this)).get(LoginViewModel.class);

        vm.getAuthStatus().observe(this, statusTextView::setText);

        vm.getLoggedIn().observe(this, loggedIn -> {
            boolean isLoggedIn = Boolean.TRUE.equals(loggedIn);
            findViewById(R.id.buttonSignOut).setEnabled(isLoggedIn);
        });

        findViewById(R.id.buttonSignIn).setOnClickListener(v ->
                vm.signIn(fieldEmail.getText().toString().trim(),
                        fieldPassword.getText().toString().trim()));

        findViewById(R.id.buttonSignUp).setOnClickListener(v ->
                vm.signUp(fieldEmail.getText().toString().trim(),
                        fieldPassword.getText().toString().trim()));

        findViewById(R.id.buttonSignOut).setOnClickListener(v -> vm.signOut());
    }
}