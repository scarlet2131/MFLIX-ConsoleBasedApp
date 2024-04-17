package dao;

import model.Rating;
import utility.DBConnection;

import java.sql.*;


public class RatingDAOImpl implements RatingDAO{


    @Override
    public boolean addRating(Rating rating) {
        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement updateStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            // Start by checking if a rating already exists
            String checkQuery = "SELECT COUNT(*) FROM ratings WHERE MovieID = ? AND UserID = ?";
            checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, rating.getMovieID());
            checkStmt.setInt(2, rating.getUserID());
            rs = checkStmt.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }

            if (count > 0) {
                // Rating exists, update it
                String updateQuery = "UPDATE ratings SET Rating = ? WHERE MovieID = ? AND UserID = ?";
                updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, rating.getRating());
                updateStmt.setInt(2, rating.getMovieID());
                updateStmt.setInt(3, rating.getUserID());
                updateStmt.executeUpdate();
            } else {
                // No existing rating, insert new one
                String insertQuery = "INSERT INTO ratings (MovieID, UserID, Rating) VALUES (?, ?, ?)";
                insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setInt(1, rating.getMovieID());
                insertStmt.setInt(2, rating.getUserID());
                insertStmt.setInt(3, rating.getRating());
                insertStmt.executeUpdate();
            }
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("There was a problem with the rating entry: " + e.getMessage());
            return false;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (checkStmt != null) checkStmt.close();
                if (updateStmt != null) updateStmt.close();
                if (insertStmt != null) insertStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
