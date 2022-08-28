package com.hieubui.consumer;

import java.util.List;

public interface LabsRepository {

    void createMovie(Movie movie);

    List<Movie> getMovies();

    Movie getMovie(String movieId);

    boolean updateMovie(Movie movie);

    boolean deleteMovie(String movieId);

    void deleteMovies();
}
