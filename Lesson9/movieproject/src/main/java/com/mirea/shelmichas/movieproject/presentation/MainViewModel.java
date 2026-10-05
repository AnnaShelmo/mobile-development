package com.mirea.shelmichas.movieproject.presentation;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mirea.shelmichas.movieproject.domain.models.Movie;
import com.mirea.shelmichas.movieproject.domain.repository.MovieRepository;
import com.mirea.shelmichas.movieproject.domain.usecase.GetFavoriteFilmUseCase;
import com.mirea.shelmichas.movieproject.domain.usecase.SaveMovieToFavoriteUseCase;

public class MainViewModel extends ViewModel {

    private MutableLiveData<String> favoriteMovie = new MutableLiveData<>();

    private MovieRepository movieRepository;

    public MainViewModel(MovieRepository movieRepository) {
        Log.d(MainViewModel.class.getSimpleName().toString(), "MainViewModel created");
        this.movieRepository = movieRepository;
    }

    @Override
    protected void onCleared() {
        Log.d(MainViewModel.class.getSimpleName().toString(), "MainViewModel cleared");
        super.onCleared();
    }

    public MutableLiveData<String> getFavoriteMovie() {
        return favoriteMovie;
    }

    public void setText(Movie movie) {
        Boolean result = new SaveMovieToFavoriteUseCase(movieRepository).execute(movie);
        favoriteMovie.setValue(result.toString());
    }

    public void getText() {
        Movie movie = new GetFavoriteFilmUseCase(movieRepository).execute();
        favoriteMovie.setValue(String.format("My favorite movie is %s", movie.getName()));
    }
}