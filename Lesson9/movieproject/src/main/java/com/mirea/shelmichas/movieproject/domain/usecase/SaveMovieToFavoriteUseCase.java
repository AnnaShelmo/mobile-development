package com.mirea.shelmichas.movieproject.domain.usecase;

import com.mirea.shelmichas.movieproject.domain.models.Movie;
import com.mirea.shelmichas.movieproject.domain.repository.MovieRepository;

public class SaveMovieToFavoriteUseCase {
    private final MovieRepository movieRepository;

    public SaveMovieToFavoriteUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public boolean execute(Movie movie) {
        return movieRepository.saveMovie(movie);
    }
}
