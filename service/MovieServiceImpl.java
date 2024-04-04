package service;

import dao.*;
import dto.MovieSearchDTO;
import enums.MovieRentalStatus;
import model.Movie;
import model.Rating;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MovieServiceImpl implements MovieService{

    private MovieDAO movieDAO = new MovieDAOImpl();
    private RatingDAO ratingDAO = new RatingDAOImpl();
    @Override
    public Movie getMovieDetails(int movieId, boolean checkAvailabilty){
        return movieDAO.getMovieById(movieId,checkAvailabilty);
    }

    @Override
    public List<Movie> searchMoviesByTitle(String title){
        return movieDAO.searchMoviesByTitle(title);
    }

    @Override
    public boolean addRating(Rating rating) {
        return ratingDAO.addRating(rating);
    }
    public List<Movie> searchMovies(MovieSearchDTO movieSearchDTO) {
        return movieDAO.searchMovies(movieSearchDTO.getGenre(), movieSearchDTO.getYear(), movieSearchDTO.getMinimumRating(), movieSearchDTO.getSortBy());
    }

}
