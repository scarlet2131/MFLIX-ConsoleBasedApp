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
    private FavoriteDAO favoriteDAO = new FavoriteDAOImpl();
    @Override
    public Movie getMovieDetails(int movieId, boolean checkAvailabilty){
        return movieDAO.getMovieById(movieId,checkAvailabilty);
    }
    @Override
    public Movie getMovieById(int movieId) {
        return movieDAO.getMovieById(movieId,true);
    }


    @Override
    public List<Movie> searchMoviesByTitle(String title){
        return movieDAO.searchMoviesByTitle(title);
    }

    @Override
    public boolean addRating(Rating rating) {
        if(!checkMovieAvailabilty(rating.getMovieID())) return false;
        return ratingDAO.addRating(rating);
    }
    public List<Movie> searchMovies(MovieSearchDTO movieSearchDTO) {
        return movieDAO.searchMovies(movieSearchDTO.getGenre(), movieSearchDTO.getYear(), movieSearchDTO.getMinimumRating(), movieSearchDTO.getSortBy());
    }

    public boolean checkMovieAvailabilty(int movieId){
        boolean isAvailable = movieDAO.isMovieAvailable(movieId);
        if(!isAvailable){
            System.out.println("Movie not available, please enter correct movieId!!");
            return false;
        }
        return true;
    }
    public boolean addMovieToFavorite(int movieId, int userId){
        if(!checkMovieAvailabilty(movieId)) return false;
        return favoriteDAO.addMovieToFavorite(movieId, userId);
    }

    public List<Movie> getUserFavoriteMovie(int userId){
        return favoriteDAO.getFavoritesByUserId(userId);
    }

}
