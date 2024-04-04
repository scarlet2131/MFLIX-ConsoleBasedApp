package dao;

import model.Movie;
import service.MovieService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface MovieDAO {
    boolean addMovie(Movie movie);
    boolean updateMovie(Movie movie);
    boolean deleteMovie(int movieId);
    List<Movie> getAllMovies();
    boolean isMovieAvailable(int movieId);
//    boolean rentMovieAndUpdateBalance(int userId, int movieId, LocalDate startDate, LocalDate dueDate, BigDecimal cost);
    Movie getMovieById(int movieId, boolean checkAvailabilty);

    List<Movie> searchMoviesByTitle(String title);

    List<Movie> searchMovies(String genre, Integer year, Double rating, String sortBy);
}
