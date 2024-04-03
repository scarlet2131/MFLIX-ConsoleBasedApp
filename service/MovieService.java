package service;

import model.Movie;

import java.util.List;

public interface MovieService {
    Movie getMovieDetails(int movieId);
    List<Movie> searchMoviesByTitle(String title);

}
