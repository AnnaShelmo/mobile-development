package com.mirea.shelmichas.movieproject.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.mirea.shelmichas.movieproject.domain.models.Movie;
import com.mirea.shelmichas.movieproject.domain.repository.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {

    private static final String PREFS_NAME = "movie_preferences";
    private static final String KEY_MOVIE_NAME = "FAVORITE_MOVIE_NAME";

    private final Context context;

    public MovieRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean saveMovie(Movie movie) {
        if (movie.getName().isEmpty()) {
            return false;
        }
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        preferences.edit()
                .putString(KEY_MOVIE_NAME, movie.getName())
                .apply();
        return true;
    }

    @Override
    public Movie getMovie() {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String name = preferences.getString(KEY_MOVIE_NAME, "Game of throne");
        return new Movie(1, name);
    }
}