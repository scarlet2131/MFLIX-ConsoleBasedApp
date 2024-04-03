package service;

import dao.MovieDAO;
import dao.MovieDAOImpl;
import dao.RentalDAO;
import dao.RentalDAOImpl;
import enums.MovieRentalStatus;
import model.Movie;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MovieServiceImpl implements MovieService{

    private MovieDAO movieDAO = new MovieDAOImpl();
    @Override
    public Movie getMovieDetails(int movieId){
        return movieDAO.getMovieById(movieId);
    }

    @Override
    public List<Movie> searchMoviesByTitle(String title){
        return movieDAO.searchMoviesByTitle(title);
    }

}
