package com.mirea.shelmichas.nocturne.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mirea.shelmichas.nocturne.R;
import com.mirea.shelmichas.nocturne.domain.models.Weather;
import com.squareup.picasso.Picasso;

// Страница места: описание, фото, погода и избранное.
public class PlaceDetailsFragment extends Fragment {

    public static final String ARG_PLACE_ID = "arg_place_id";

    private PlaceDetailsViewModel vm;
    private Button favoriteButton;

    public PlaceDetailsFragment() {
        super(R.layout.fragment_place_details);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imageView = view.findViewById(R.id.imageViewPlace);
        TextView nameView = view.findViewById(R.id.textViewPlaceName);
        TextView cityView = view.findViewById(R.id.textViewPlaceCity);
        TextView categoryView = view.findViewById(R.id.textViewPlaceCategory);
        TextView descriptionView = view.findViewById(R.id.textViewPlaceDescription);
        ProgressBar progressWeather = view.findViewById(R.id.progressWeather);
        TextView weatherView = view.findViewById(R.id.textViewWeather);
        favoriteButton = view.findViewById(R.id.buttonFavorite);

        vm = new ViewModelProvider(this, new NocturneViewModelFactory(requireContext()))
                .get(PlaceDetailsViewModel.class);

        vm.getPlace().observe(getViewLifecycleOwner(), place -> {
            if (place == null) {
                return;
            }
            nameView.setText(place.getName());
            cityView.setText(place.getCity() + ", " + place.getCountry() + " · " + place.getYear());
            categoryView.setText(place.getCategory());
            descriptionView.setText(place.getDescription());

            Picasso.get()
                    .load(place.getImageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);
        });

        // Избранное доступно только авторизованному пользователю.
        vm.getLoggedIn().observe(getViewLifecycleOwner(), loggedIn ->
                favoriteButton.setVisibility(Boolean.TRUE.equals(loggedIn)
                        ? View.VISIBLE : View.GONE));

        vm.getFavorite().observe(getViewLifecycleOwner(), favorite ->
                favoriteButton.setText(Boolean.TRUE.equals(favorite)
                        ? "Убрать из избранного" : "В избранное"));

        favoriteButton.setOnClickListener(v -> {
            vm.toggleFavorite();
            Toast.makeText(requireContext(), "Избранное обновлено", Toast.LENGTH_SHORT).show();
        });

        vm.getWeather().observe(getViewLifecycleOwner(), weather -> {
            progressWeather.setVisibility(View.GONE);
            if (weather != null) {
                weatherView.setText(formatWeather(weather));
            }
        });

        vm.getError().observe(getViewLifecycleOwner(), message -> {
            progressWeather.setVisibility(View.GONE);
            if (message != null && !message.isEmpty()) {
                weatherView.setText("Погода недоступна");
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
            }
        });

        int id = getArguments() != null ? getArguments().getInt(ARG_PLACE_ID, -1) : -1;
        vm.load(id);
    }

    private String formatWeather(Weather weather) {
        return String.format(java.util.Locale.getDefault(),
                "%.1f°C, %s\nВетер: %.1f м/с · Влажность: %d%%",
                weather.getTemperature(), weather.getCondition(),
                weather.getWindSpeed(), weather.getHumidity());
    }
}
