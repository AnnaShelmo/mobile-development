package com.mirea.shelmichas.nocturne.presentation;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.mirea.shelmichas.nocturne.R;

// Приветственный экран: вход или гостевой режим.
public class WelcomeFragment extends Fragment {

    private WelcomeViewModel vm;

    public WelcomeFragment() {
        super(R.layout.fragment_welcome);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vm = new ViewModelProvider(this, new NocturneViewModelFactory(requireContext()))
                .get(WelcomeViewModel.class);

        NavController navController = NavHostFragment.findNavController(this);

        view.findViewById(R.id.buttonLogin).setOnClickListener(v -> {
            // Если сессия уже сохранена, сразу открываем каталог.
            if (vm.isLoggedIn()) {
                navController.navigate(R.id.action_welcome_to_catalog);
            } else {
                navController.navigate(R.id.action_welcome_to_login);
            }
        });

        // Гость не должен оставаться в аккаунте с прошлого запуска.
        view.findViewById(R.id.textGuest).setOnClickListener(v -> {
            vm.enterGuestMode();
            navController.navigate(R.id.action_welcome_to_catalog);
        });
    }
}
