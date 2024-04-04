package dao;

import model.Rating;
import utility.DBConnection;

import java.sql.*;


public class RatingDAOImpl implements RatingDAO{


    @Override
    public boolean addRating(Rating rating) {
        String query = "INSERT INTO ratings (MovieID, UserID, Rating) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, rating.getMovieID());
            pstmt.setInt(2, rating.getUserID());
            pstmt.setInt(3, rating.getRating());
            pstmt.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("You have already rated this movie.");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public double getAverageRating(int movieID) {
        String query = "SELECT AVG(Rating) as AverageRating FROM Ratings WHERE MovieID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, movieID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("AverageRating");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

}
