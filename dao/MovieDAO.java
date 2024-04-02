package dao;

import model.Movie;

import java.util.List;

public interface MovieDAO {
    boolean addMovie(Movie movie);
    boolean updateMovie(Movie movie);
    boolean deleteMovie(int movieId);
    List<Movie> getAllMovies();
}
