package com.mirea.shelmichas.movieproject.domain.usecase;


import com.mirea.shelmichas.movieproject.domain.models.Movie;
import com.mirea.shelmichas.movieproject.domain.repository.MovieRepository;

public class GetFavoriteFilmUseCase {
    private final MovieRepository movieRepository;

    public GetFavoriteFilmUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie execute() {
        return movieRepository.getMovie();
    }
}