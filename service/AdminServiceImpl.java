package service;

import dao.MovieDAO;
import dao.MovieDAOImpl;
import dto.MovieDTO;
import model.Movie;

import java.util.List;

public class AdminServiceImpl implements AdminService{

    private MovieDAO movieDAO = new MovieDAOImpl();

    private Movie convertToMovieModel(MovieDTO movieDTO) {
        return new Movie(
                movieDTO.getTitle(),
                movieDTO.getGenre(),
                movieDTO.getReleaseYear(),
                movieDTO.isAvailable()
        );
    }
    @Override
    public boolean addMovie(MovieDTO movieDTO) {
        // Convert MovieDTO to Movie model
        Movie movie = convertToMovieModel(movieDTO);

        return movieDAO.addMovie(movie);
    }

    @Override
    public boolean updateMovie(int movieId, MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setMovieId(movieId);

        // Check for null instead of isEmpty for String fields
        if (movieDTO.getTitle() != null) movie.setTitle(movieDTO.getTitle());
        if (movieDTO.getGenre() != null) movie.setGenre(movieDTO.getGenre());

        // Directly check for null since getReleaseYear and isAvailable can be null
        if (movieDTO.getReleaseYear() != null) movie.setReleaseYear(movieDTO.getReleaseYear());
        if (movieDTO.isAvailable() != null) movie.setAvailable(movieDTO.isAvailable());

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
