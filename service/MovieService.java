package service;

import dto.MovieSearchDTO;
import model.Movie;
import model.Rating;

import java.util.List;

public interface MovieService {
    Movie getMovieDetails(int movieId, boolean checkAvailabilty);
    Movie getMovieById(int movieId);
    List<Movie> searchMoviesByTitle(String title);
    boolean addRating(Rating rating);
    boolean addMovieToFavorite(int movieId, int userId);

    List<Movie> getUserFavoriteMovie(int userId);

    List<Movie> searchMovies(MovieSearchDTO movieSearchDTO);

}
