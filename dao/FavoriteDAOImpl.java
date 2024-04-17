package dao;

import model.Movie;
import model.Rating;
import utility.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class FavoriteDAOImpl implements FavoriteDAO{
    @Override
    public boolean addMovieToFavorite( int movieID, int userID) {
        String query = "INSERT INTO favorites (UserID, MovieID) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userID);
            pstmt.setInt(2, movieID);
            int affectedRows = pstmt.executeUpdate();

            // Checking if the insert was successful
            return affectedRows > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("This movie is already in the user's list of favorites.");
            return false;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Movie> getFavoritesByUserId(int userId) {
        List<Movie> favorites = new ArrayList<>();
        // Include average rating in the select query
        String query = "SELECT m.MovieID, m.Title, m.Genre, m.ReleaseYear, m.IsAvailable, AVG(r.Rating) AS AverageRating " +
                "FROM movies m " +
                "INNER JOIN favorites f ON m.MovieID = f.MovieID " +
                "LEFT JOIN ratings r ON m.MovieID = r.MovieID " +
                "WHERE f.UserID = ? " +
                "GROUP BY m.MovieID, m.Title, m.Genre, m.ReleaseYear, m.IsAvailable";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Movie movie = new Movie();
                    movie.setMovieId(rs.getInt("MovieID"));
                    movie.setTitle(rs.getString("Title"));
                    movie.setGenre(rs.getString("Genre"));
                    movie.setReleaseYear(rs.getInt("ReleaseYear"));
                    movie.setAvailable(rs.getBoolean("IsAvailable"));
                    movie.setAverageRating(rs.getDouble("AverageRating")); // Assuming Movie class has this field
                    favorites.add(movie);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return favorites;
    }

}
