package com.mirea.shelmichas.movieproject.data.storage;

import com.mirea.shelmichas.movieproject.data.storage.models.Movie;

public interface MovieStorage {
    Movie get();

    boolean save(Movie movie);
}