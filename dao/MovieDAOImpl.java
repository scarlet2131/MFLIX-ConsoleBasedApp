package dao;

import model.Movie;
import utility.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieDAOImpl implements MovieDAO{
    @Override
    public boolean addMovie(Movie movie) {
        String sql = "INSERT INTO movies (Title, Genre, ReleaseYear, IsAvailable) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, movie.getTitle());
            pstmt.setString(2, movie.getGenre());
            pstmt.setInt(3, movie.getReleaseYear());
            pstmt.setBoolean(4, movie.isAvailable());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateMovie(Movie movie) {
        StringBuilder sql = new StringBuilder("UPDATE movies SET ");
        // Dynamically building the SQL query based on which fields are present
        int fieldsToUpdate = 0;
        if (movie.getTitle() != null) {
            sql.append("Title = ?, ");
            fieldsToUpdate++;
        }
        if (movie.getGenre() != null) {
            sql.append("Genre = ?, ");
            fieldsToUpdate++;
        }
        if (movie.getReleaseYear() != null) {
            sql.append("ReleaseYear = ?, ");
            fieldsToUpdate++;
        }
        if (movie.isAvailable() != null) {
            sql.append("IsAvailable = ? ");
            fieldsToUpdate++;
        }
        if (fieldsToUpdate == 0) {
            // No fields to update
            return false;
        }
        sql.append("WHERE MovieID = ?;");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (movie.getTitle() != null) pstmt.setString(paramIndex++, movie.getTitle());
            if (movie.getGenre() != null) pstmt.setString(paramIndex++, movie.getGenre());
            if (movie.getReleaseYear() != null) pstmt.setInt(paramIndex++, movie.getReleaseYear());
            if (movie.isAvailable() != null) pstmt.setBoolean(paramIndex++, movie.isAvailable());
            pstmt.setInt(paramIndex, movie.getMovieId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMovie(int movieId) {
        String sql = "DELETE FROM movies WHERE MovieID = ?;"; // SQL statement to delete a movie

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movieId); // Set the movie ID in the query

            int affectedRows = pstmt.executeUpdate(); // Execute the update
            return affectedRows > 0; // Return true if the movie was deleted
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false; // Return false if there was a problem deleting the movie
        }
    }

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT MovieID, Title, Genre, ReleaseYear, IsAvailable FROM movies;";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Movie movie = new Movie();
                movie.setMovieId(rs.getInt("MovieID"));
                movie.setTitle(rs.getString("Title"));
                movie.setGenre(rs.getString("Genre"));
                movie.setReleaseYear(rs.getInt("ReleaseYear"));
                movie.setAvailable(rs.getBoolean("IsAvailable"));
                movies.add(movie);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return movies;
    }
}
