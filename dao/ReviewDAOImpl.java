package dao;

import model.Review;
import utility.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAOImpl implements ReviewDAO {

    public boolean addReview(Review review) {
        String query = "INSERT INTO reviews (MovieID, UserID, Review, ReviewDate) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, review.getMovieID());
            pstmt.setInt(2, review.getUserID());
            pstmt.setString(3, review.getReview());
            pstmt.setDate(4, new java.sql.Date(review.getReviewDate().getTime()));
            pstmt.executeUpdate();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Review> getReviewsByMovieId(int movieId) {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT * FROM reviews WHERE MovieID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                reviews.add(new Review(
                        rs.getInt("ReviewID"),
                        rs.getInt("MovieID"),
                        rs.getInt("UserID"),
                        rs.getString("Review"),
                        rs.getDate("ReviewDate")
                ));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public boolean updateReview(Review review) {
        String query = "UPDATE reviews SET Review = ?, ReviewDate = ? WHERE ReviewID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, review.getReview());
            pstmt.setDate(2, new java.sql.Date(review.getReviewDate().getTime()));
            pstmt.setInt(3, review.getReviewID());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Review> getReviewsByUserId(int userId) {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT ReviewID, MovieID, Review, ReviewDate FROM reviews WHERE UserID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Review review = new Review(
                        rs.getInt("ReviewID"),
                        rs.getInt("MovieID"),
                        userId,
                        rs.getString("Review"),
                        rs.getDate("ReviewDate")
                );
                reviews.add(review);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
