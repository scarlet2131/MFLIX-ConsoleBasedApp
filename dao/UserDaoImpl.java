package dao;

import model.User;
import utility.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    @Override
    public boolean addUser(User user) {
        String sql = "INSERT INTO users (username, password, email, contactDetails, security_question, security_answer, role, balance) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword()); // Consider hashing
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getContactDetails());
            pstmt.setString(5, user.getSecurityQuestion());
            pstmt.setString(6, user.getSecurityAnswer());
            pstmt.setString(7, user.getRole().toString());
            pstmt.setBigDecimal(8, user.getBalance());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Unable to add user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ? and is_active = true";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Assuming a constructor User(ResultSet rs) exists that maps the result set to a User object
                return new User(rs);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User findByUserId(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ? and is_active = true";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Assuming a constructor User(ResultSet rs) exists that maps the result set to a User object
                return new User(rs);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Assuming a constructor User(ResultSet rs) exists that maps the result set to a User object
                return new User(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public String getSecurityQuestion(String email) {
        String sql = "SELECT security_question FROM users WHERE email = ? and is_active = true";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("security_question");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean validateSecurityAnswerByEmail(String email, String securityAnswer) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND security_answer = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, securityAnswer);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updatePassword(String email, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword); // Hashing should be applied here
            pstmt.setString(2, email);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Password updated successfully.");
                return true;
            } else {
                System.out.println("Failed to update password.");
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Failed to update password: " + e.getMessage());
        }
        return false;
    }

    public boolean updateUserBalance(int userId, BigDecimal amount) {
        String sql = "UPDATE users SET balance = balance + ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBigDecimal(1, amount);
            pstmt.setInt(2, userId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Balance updated successfully.");
                return true;
            } else {
                System.out.println("Failed to update balance.");
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Failed to update balance: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateUserDetails(User user) {
        StringBuilder sql = new StringBuilder("UPDATE users SET ");
        List<Object> parameters = new ArrayList<>();
        boolean isFirst = true;

        // Conditionally append fields to update based on non-null attributes in User object
        if (user.getEmail() != null) {
            sql.append(isFirst ? "" : ", ").append("email = ?");
            parameters.add(user.getEmail());
            isFirst = false;
        }
        if (user.getUsername() != null) {
            sql.append(isFirst ? "" : ", ").append("username = ?");
            parameters.add(user.getUsername());
            isFirst = false;
        }
        if (user.getContactDetails() != null) {
            sql.append(isFirst ? "" : ", ").append("contactDetails = ?");
            parameters.add(user.getContactDetails());
            isFirst = false;
        }
        // Example for password - Ensure this is appropriately hashed if updating passwords
        if (user.getPassword() != null) {
            sql.append(isFirst ? "" : ", ").append("password = ?");
            parameters.add(user.getPassword()); // Assume hashing is handled elsewhere
            isFirst = false;
        }
        // Add any additional fields to update in a similar fashion

        // Avoid updating if no fields were provided for update
        if (isFirst) {
            return false; // No fields to update were specified
        }

        sql.append(" WHERE user_id = ?");
        parameters.add(user.getUserId());

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(String.valueOf(sql))) {

            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

}
