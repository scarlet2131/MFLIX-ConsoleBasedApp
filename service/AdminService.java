package service;

import dto.MovieDTO;
import model.Movie;

import java.util.List;

public interface AdminService {
    boolean addMovie(MovieDTO movieDTO);
    boolean updateMovie(int movieId, MovieDTO movieDTO);
    boolean deleteMovie(int movieId);
    List<Movie> getAllMovies();
}
