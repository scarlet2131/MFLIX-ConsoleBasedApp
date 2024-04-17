package dao;

import model.Rental;
import utility.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalDAOImpl implements RentalDAO{
    public boolean createRental(int userId, int movieId, LocalDate startDate, LocalDate dueDate, String status) {
        String sql = "INSERT INTO rentals (UserID, MovieID, StartDate, DueDate, Status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, movieId);
            pstmt.setObject(3, startDate);
            pstmt.setObject(4, dueDate);
            pstmt.setString(5, status);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Rental> getRentalHistoryForUser(int userId) {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT * FROM rentals WHERE UserID = ? ORDER BY StartDate DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                rentals.add(new Rental(
                        rs.getInt("RentalID"),
                        userId,
                        rs.getInt("MovieID"),
                        rs.getDate("StartDate").toLocalDate(),
                        rs.getDate("DueDate").toLocalDate(),
                        rs.getString("Status")
                ));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return rentals;
    }

    @Override
    public boolean rentMovieAndUpdateBalance(int userId, int movieId, LocalDate startDate, LocalDate dueDate, BigDecimal cost) {
        // Check if the movie is already rented
        String checkAvailabilityQuery = "SELECT COUNT(*) FROM rentals WHERE MovieID = ? AND UserID = ? AND Status = 'RENTED' AND DueDate > CURRENT_DATE";
        String createRentalQuery = "INSERT INTO rentals (UserID, MovieID, StartDate, DueDate, Status) VALUES (?, ?, ?, ?, 'RENTED')";
        String updateUserBalanceQuery = "UPDATE users SET balance = balance - ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement checkStmt = conn.prepareStatement(checkAvailabilityQuery);
                 PreparedStatement createRentalStmt = conn.prepareStatement(createRentalQuery);
                 PreparedStatement updateUserBalanceStmt = conn.prepareStatement(updateUserBalanceQuery)) {

                checkStmt.setInt(1, movieId);
                checkStmt.setInt(2,userId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // Movie is currently rented
                }

                // Create rental record
                createRentalStmt.setInt(1, userId);
                createRentalStmt.setInt(2, movieId);
                createRentalStmt.setDate(3, java.sql.Date.valueOf(startDate));
                createRentalStmt.setDate(4, java.sql.Date.valueOf(dueDate));
                createRentalStmt.executeUpdate();

                // Update user balance
                updateUserBalanceStmt.setBigDecimal(1, cost);
                updateUserBalanceStmt.setInt(2, userId);
                int balanceUpdated = updateUserBalanceStmt.executeUpdate();

                if (balanceUpdated == 0) {
                    conn.rollback(); // No balance updated, rollback
                    return false;
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback(); // Rollback on any error within try
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}

