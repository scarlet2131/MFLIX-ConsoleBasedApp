package dao;

import model.Movie;
import model.User;
import utility.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
        String sql = "SELECT m.MovieID, m.Title, m.Genre, m.ReleaseYear, m.IsAvailable, AVG(r.rating) as averageRating " +
                "FROM movies m " +
                "LEFT JOIN ratings r ON m.MovieID = r.MovieID " +
                "WHERE m.IsAvailable = true " +
                "GROUP BY m.MovieID, m.Title, m.Genre, m.ReleaseYear, m.IsAvailable;";
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
                movie.setAverageRating(rs.getDouble("averageRating"));
                movies.add(movie);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return movies;
    }
    @Override
    public boolean isMovieAvailable(int movieId) {
        String query = "SELECT IsAvailable FROM movies WHERE MovieID = ? AND IsAvailable = TRUE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, movieId);
            try (ResultSet rs = pstmt.executeQuery()) {
                // If there's a result, the movie exists and is available
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    @Override
    public Movie getMovieById(int movieId, boolean checkAvailabilty){
        String sql = "SELECT m.*, AVG(r.rating) AS averageRating " +
                "FROM movies m " +
                "LEFT JOIN ratings r ON m.MovieID = r.MovieID " +
                "WHERE m.MovieID = ?  " ;
        if(checkAvailabilty){
            sql += " IsAvailable = TRUE ";
        }
        String groupBy = "GROUP BY m.MovieID, m.Title, m.Genre, m.ReleaseYear, m.IsAvailable";
        sql += groupBy;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Assuming a constructor User(ResultSet rs) exists that maps the result set to a User object
                return new Movie(rs);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Movie> searchMoviesByTitle(String title) {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT m.MovieID, m.Title, m.Genre, m.ReleaseYear, m.IsAvailable, IFNULL(AVG(r.Rating), -1) AS averageRating " +
                "FROM movies m " +
                "LEFT JOIN ratings r ON m.MovieID = r.MovieID " +
                "WHERE m.Title LIKE ? " +
                "GROUP BY m.MovieID, m.Title, m.Genre, m.ReleaseYear, m.IsAvailable";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + title + "%"); // Use LIKE for partial matches
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Movie movie = new Movie();
                movie.setMovieId(rs.getInt("MovieID"));
                movie.setTitle(rs.getString("Title"));
                movie.setGenre(rs.getString("Genre"));
                movie.setReleaseYear(rs.getInt("ReleaseYear"));
                movie.setAvailable(rs.getBoolean("IsAvailable"));
                movie.setAverageRating(rs.getDouble("averageRating"));
                movies.add(movie);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return movies;
    }

    @Override
    public List<Movie> searchMovies(String genre, Integer year, Double rating, String sortBy) {
        List<Movie> movies = new ArrayList<>();
        // Include a JOIN with the ratings table if filtering or sorting by rating
        String baseQuery = "SELECT m.*, AVG(r.rating) as averageRating FROM movies m " +
                "LEFT JOIN ratings r ON m.MovieID = r.MovieID WHERE 1=1 ";
        List<Object> params = new ArrayList<>();

        if (genre != null) {
            baseQuery += " AND m.genre = ?";
            params.add(genre);
        }
        if (year != null) {
            baseQuery += " AND m.releaseYear = ?";
            params.add(year);
        }
        if (rating != null) {
            baseQuery += " HAVING AVG(r.rating) >= ?";
            params.add(rating);
        }

        // Sorting
        if (sortBy != null && !sortBy.isEmpty()) {
            if ("rating".equals(sortBy)) {
                baseQuery += " GROUP BY m.MovieID ORDER BY averageRating"; // When sorting by rating
            } else {
                baseQuery += " GROUP BY m.MovieID ORDER BY m." + sortBy; // Ensure sortBy is a controlled or sanitized input
            }
        } else {
            baseQuery += " GROUP BY m.MovieID";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(baseQuery)) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // Assuming Movie class has a constructor that includes averageRating
                movies.add(new Movie(rs.getInt("MovieID"), rs.getString("Title"),
                        rs.getString("Genre"), rs.getInt("ReleaseYear"),
                        rs.getBoolean("IsAvailable"), rs.getDouble("averageRating")));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return movies;
    }


//    @Override
//    public List<Movie> searchMovies(String genre, Integer year, Double rating, String sortBy) {
//        List<Movie> movies = new ArrayList<>();
//        String baseQuery = "SELECT * FROM movies WHERE 1=1";
//        List<Object> params = new ArrayList<>();
//
//        if (genre != null) {
//            baseQuery += " AND genre = ?";
//            params.add(genre);
//        }
//        if (year != null) {
//            baseQuery += " AND releaseYear = ?";
//            params.add(year);
//        }
////        if (rating != null) {
////            baseQuery += " AND rating >= ?";
////            params.add(rating);
////        }
//        if (sortBy != null) {
//            baseQuery += " ORDER BY " + sortBy; // Ensure sortBy is a controlled or sanitized input to avoid SQL injection
//        }
//
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(baseQuery)) {
//
//            for (int i = 0; i < params.size(); i++) {
//                pstmt.setObject(i + 1, params.get(i));
//            }
//
//            ResultSet rs = pstmt.executeQuery();
//            while (rs.next()) {
//                movies.add(new Movie(rs.getInt("MovieID"), rs.getString("Title"), rs.getString("Genre"),
//                        rs.getInt("ReleaseYear"), rs.getBoolean("IsAvailable")));
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return movies;
//    }


}
