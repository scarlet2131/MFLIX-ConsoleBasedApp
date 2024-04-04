package service;

import dto.MovieSearchDTO;
import model.Movie;
import model.Rating;

import java.util.List;

public interface MovieService {
    Movie getMovieDetails(int movieId, boolean checkAvailabilty);
    List<Movie> searchMoviesByTitle(String title);
    boolean addRating(Rating rating);
    List<Movie> searchMovies(MovieSearchDTO movieSearchDTO);

}
