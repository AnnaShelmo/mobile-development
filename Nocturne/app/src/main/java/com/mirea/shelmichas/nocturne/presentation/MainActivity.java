package com.mirea.shelmichas.nocturne.presentation;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mirea.shelmichas.nocturne.R;
import com.mirea.shelmichas.nocturne.data.repository.PlaceRepositoryImpl;
import com.mirea.shelmichas.nocturne.domain.models.Place;
import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;
import com.mirea.shelmichas.nocturne.domain.usecase.GetPlacesUseCase;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textViewPlaces = findViewById(R.id.textViewPlaces);

        PlaceRepository placeRepository = new PlaceRepositoryImpl();
        List<Place> places = new GetPlacesUseCase(placeRepository).execute();

        StringBuilder builder = new StringBuilder();
        for (Place place : places) {
            builder.append(place.getName()).append("\n");
        }
        textViewPlaces.setText(builder.toString());
    }
}