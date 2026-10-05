package com.mirea.shelmichas.movieproject.data.repository;

import com.mirea.shelmichas.movieproject.data.storage.MovieStorage;
import com.mirea.shelmichas.movieproject.data.storage.models.Movie;
import com.mirea.shelmichas.movieproject.domain.repository.MovieRepository;

import java.time.LocalDate;

public class MovieRepositoryImpl implements MovieRepository {

    private final MovieStorage sharedPrefMovieStorage;

    public MovieRepositoryImpl(MovieStorage sharedPrefMovieStorage) {
        this.sharedPrefMovieStorage = sharedPrefMovieStorage;
    }

    @Override
    public boolean saveMovie(com.mirea.shelmichas.movieproject.domain.models.Movie movie) {
        sharedPrefMovieStorage.save(mapToStorage(movie));
        return true;
    }

    @Override
    public com.mirea.shelmichas.movieproject.domain.models.Movie getMovie() {
        Movie movie = sharedPrefMovieStorage.get();
        return mapToDomain(movie);
    }

    private Movie mapToStorage(com.mirea.shelmichas.movieproject.domain.models.Movie movie) {
        String name = movie.getName();
        return new Movie(2, name, LocalDate.now().toString());
    }

    private com.mirea.shelmichas.movieproject.domain.models.Movie mapToDomain(Movie movie) {
        String name = movie.getName();
        return new com.mirea.shelmichas.movieproject.domain.models.Movie(movie.getId(), name);
    }
}