package com.mirea.shelmichas.nocturne.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mirea.shelmichas.nocturne.R;
import com.mirea.shelmichas.nocturne.domain.models.Place;
import com.mirea.shelmichas.nocturne.domain.models.Weather;
import com.squareup.picasso.Picasso;

public class PlaceDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_PLACE_ID = "extra_place_id";

    private PlaceDetailsViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        ImageView imageView = findViewById(R.id.imageViewPlace);
        TextView nameView = findViewById(R.id.textViewPlaceName);
        TextView cityView = findViewById(R.id.textViewPlaceCity);
        TextView categoryView = findViewById(R.id.textViewPlaceCategory);
        TextView descriptionView = findViewById(R.id.textViewPlaceDescription);
        ProgressBar progressWeather = findViewById(R.id.progressWeather);
        TextView weatherView = findViewById(R.id.textViewWeather);

        vm = new ViewModelProvider(this, new NocturneViewModelFactory(this))
                .get(PlaceDetailsViewModel.class);

        vm.getPlace().observe(this, place -> {
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

        vm.getWeather().observe(this, weather -> {
            progressWeather.setVisibility(View.GONE);
            if (weather != null) {
                weatherView.setText(formatWeather(weather));
            }
        });

        vm.getError().observe(this, message -> {
            progressWeather.setVisibility(View.GONE);
            if (message != null && !message.isEmpty()) {
                weatherView.setText("Погода недоступна");
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });

        int id = getIntent().getIntExtra(EXTRA_PLACE_ID, -1);
        vm.load(id);
    }

    private String formatWeather(Weather weather) {
        return String.format(java.util.Locale.getDefault(),
                "%.1f°C, %s\nВетер: %.1f м/с · Влажность: %d%%",
                weather.getTemperature(), weather.getCondition(),
                weather.getWindSpeed(), weather.getHumidity());
    }
}
