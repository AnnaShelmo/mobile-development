package com.mirea.shelmichas.movieproject.domain.repository;

import com.mirea.shelmichas.movieproject.domain.models.Movie;

public interface MovieRepository {
    boolean saveMovie(Movie movie);
    Movie getMovie();
}
