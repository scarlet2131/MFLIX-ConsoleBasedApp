package service;

import dao.MovieDAO;
import dao.MovieDAOImpl;
import model.Movie;

import java.util.List;

public class AdminServiceImpl implements AdminService{

    private MovieDAO movieDAO = new MovieDAOImpl();


    @Override
    public boolean addMovie(Movie movie) {
        return movieDAO.addMovie(movie);
    }

    @Override
    public boolean updateMovie( Movie movie) {
        return movieDAO.updateMovie(movie);
    }

    @Override
    public boolean deleteMovie(int movieId) {
        return movieDAO.deleteMovie(movieId); // Call the DAO method to delete the movie
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieDAO.getAllMovies(); // Simply pass through the call to DAO
    }
}
