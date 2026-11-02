package com.mirea.shelmichas.nocturne.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mirea.shelmichas.nocturne.R;

// Экран авторизации: вход и регистрация через Firebase.
public class LoginFragment extends Fragment {

    private LoginViewModel vm;

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText fieldEmail = view.findViewById(R.id.fieldEmail);
        EditText fieldPassword = view.findViewById(R.id.fieldPassword);
        TextView statusView = view.findViewById(R.id.textViewAuthStatus);

        vm = new ViewModelProvider(this, new NocturneViewModelFactory(requireContext()))
                .get(LoginViewModel.class);

        vm.getAuthStatus().observe(getViewLifecycleOwner(), statusView::setText);

        // После успешного входа сразу открываем каталог.
        vm.getAuthCompleted().observe(getViewLifecycleOwner(), completed -> {
            if (!Boolean.TRUE.equals(completed)) {
                return;
            }
            vm.consumeAuthCompleted();
            Toast.makeText(requireContext(), "Вход выполнен", Toast.LENGTH_SHORT).show();
            requireView().post(() -> ((MainActivity) requireActivity()).openCatalog());
        });

        view.findViewById(R.id.buttonSignIn).setOnClickListener(v -> {
            String email = fieldEmail.getText().toString().trim();
            String password = fieldPassword.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Введите email и пароль", Toast.LENGTH_SHORT).show();
                return;
            }
            vm.signIn(email, password);
        });

        view.findViewById(R.id.buttonSignUp).setOnClickListener(v -> {
            String email = fieldEmail.getText().toString().trim();
            String password = fieldPassword.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Введите email и пароль", Toast.LENGTH_SHORT).show();
                return;
            }
            vm.signUp(email, password);
        });
    }
}
