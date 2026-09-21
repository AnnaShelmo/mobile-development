package com.mirea.shelmichas.movieproject.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mirea.shelmichas.movieproject.R;
import com.mirea.shelmichas.movieproject.data.repository.MovieRepositoryImpl;
import com.mirea.shelmichas.movieproject.domain.models.Movie;
import com.mirea.shelmichas.movieproject.domain.repository.MovieRepository;
import com.mirea.shelmichas.movieproject.domain.usecase.GetFavoriteFilmUseCase;
import com.mirea.shelmichas.movieproject.domain.usecase.SaveMovieToFavoriteUseCase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText text = findViewById(R.id.editTextMovie);
        TextView textView = findViewById(R.id.textViewMovie);

        MovieRepository movieRepository = new MovieRepositoryImpl(this);

        findViewById(R.id.buttonSaveMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = new SaveMovieToFavoriteUseCase(movieRepository)
                        .execute(new Movie(2, text.getText().toString()));
                textView.setText(String.format("Save result %s", result));
            }
        });

        findViewById(R.id.buttonGetMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie moview = new GetFavoriteFilmUseCase(movieRepository).execute();
                textView.setText(String.format("Save result %s", moview.getName()));
            }
        });
    }
}