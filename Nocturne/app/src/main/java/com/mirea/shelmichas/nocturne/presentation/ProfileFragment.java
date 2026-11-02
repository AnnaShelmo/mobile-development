package com.mirea.shelmichas.nocturne.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mirea.shelmichas.nocturne.R;

// Профиль: статус входа, email и данные клиента.
public class ProfileFragment extends Fragment {

    private ProfileViewModel vm;
    private EditText nameField;
    private EditText phoneField;
    private Button authButton;

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView statusView = view.findViewById(R.id.textViewProfileStatus);
        TextView emailView = view.findViewById(R.id.textViewProfileEmail);
        nameField = view.findViewById(R.id.fieldClientName);
        phoneField = view.findViewById(R.id.fieldClientPhone);
        authButton = view.findViewById(R.id.buttonProfileAuth);

        vm = new ViewModelProvider(this, new NocturneViewModelFactory(requireContext()))
                .get(ProfileViewModel.class);

        vm.getLoggedIn().observe(getViewLifecycleOwner(), loggedIn -> {
            boolean isLoggedIn = Boolean.TRUE.equals(loggedIn);
            statusView.setText(isLoggedIn ? "Статус: авторизован" : "Статус: гость");
            if (!isLoggedIn) {
                emailView.setText("Вы вошли как гость");
            }
            authButton.setText(isLoggedIn ? "Выйти" : "Войти");
        });

        vm.getEmail().observe(getViewLifecycleOwner(), email -> {
            if (Boolean.TRUE.equals(vm.getLoggedIn().getValue())) {
                emailView.setText(email == null ? "" : email);
            }
        });

        vm.getName().observe(getViewLifecycleOwner(), name -> nameField.setText(clean(name)));
        vm.getPhone().observe(getViewLifecycleOwner(), phone -> phoneField.setText(clean(phone)));

        view.findViewById(R.id.buttonSaveClient).setOnClickListener(v -> {
            vm.saveClientInfo(nameField.getText().toString().trim(),
                    phoneField.getText().toString().trim());
            Toast.makeText(requireContext(), "Данные клиента сохранены", Toast.LENGTH_SHORT).show();
        });

        authButton.setOnClickListener(v -> {
            if (Boolean.TRUE.equals(vm.getLoggedIn().getValue())) {
                vm.signOut();
                Toast.makeText(requireContext(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();
                ((MainActivity) requireActivity()).openWelcome();
            } else {
                ((MainActivity) requireActivity()).openLogin();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (vm != null) {
            vm.refresh();
        }
    }

    // В SharedPreferences по умолчанию записано "unknown".
    private String clean(String value) {
        if (value == null || "unknown".equals(value)) {
            return "";
        }
        return value;
    }
}
