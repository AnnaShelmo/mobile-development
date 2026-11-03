package com.mirea.shelmichas.nocturne.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mirea.shelmichas.nocturne.R;
import com.mirea.shelmichas.nocturne.domain.models.Place;

import java.util.List;

// Избранное: список мест, доступный только после входа.
public class FavoritesFragment extends Fragment {

    private FavoritesViewModel vm;
    private RecyclerView recyclerView;
    private TextView messageView;
    private Button loginButton;

    public FavoritesFragment() {
        super(R.layout.fragment_favorites);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        messageView = view.findViewById(R.id.textViewFavoritesMessage);
        loginButton = view.findViewById(R.id.buttonFavoritesLogin);

        vm = new ViewModelProvider(this, new NocturneViewModelFactory(requireContext()))
                .get(FavoritesViewModel.class);

        NavController navController = NavHostFragment.findNavController(this);

        PlaceRecyclerViewAdapter adapter = new PlaceRecyclerViewAdapter(place -> {
            Bundle args = new Bundle();
            args.putInt(PlaceDetailsFragment.ARG_PLACE_ID, place.getId());
            navController.navigate(R.id.action_favorites_to_place_details, args);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        vm.getFavorites().observe(getViewLifecycleOwner(), favorites -> {
            adapter.setItems(favorites);
            updateUi();
        });
        vm.getLoggedIn().observe(getViewLifecycleOwner(), loggedIn -> updateUi());

        loginButton.setOnClickListener(v ->
                navController.navigate(R.id.action_favorites_to_login));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (vm != null) {
            vm.load();
        }
    }

    private void updateUi() {
        boolean isLoggedIn = Boolean.TRUE.equals(vm.getLoggedIn().getValue());
        List<Place> favorites = vm.getFavorites().getValue();

        if (!isLoggedIn) {
            recyclerView.setVisibility(View.GONE);
            messageView.setVisibility(View.VISIBLE);
            messageView.setText("Войдите, чтобы видеть избранное");
            loginButton.setVisibility(View.VISIBLE);
        } else if (favorites == null || favorites.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            messageView.setVisibility(View.VISIBLE);
            messageView.setText("Пока ничего не добавлено");
            loginButton.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            messageView.setVisibility(View.GONE);
            loginButton.setVisibility(View.GONE);
        }
    }
}
