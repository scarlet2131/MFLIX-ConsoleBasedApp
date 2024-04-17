package dao;

import model.Movie;

import java.util.List;

public interface FavoriteDAO {
    boolean addMovieToFavorite(int movieId, int userId);
    List<Movie> getFavoritesByUserId(int userId);
}
