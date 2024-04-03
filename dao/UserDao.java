package dao;

import model.User;

import java.math.BigDecimal;

public interface UserDao {
    boolean addUser(User user);
    User findByUsername(String username);
    User findByUserId(int username);
    boolean updatePassword(String email, String newPassword);
    User findByEmail(String email);
    String getSecurityQuestion(String email) ;
    boolean validateSecurityAnswerByEmail(String email, String securityAnswer);
    boolean updateUserBalance(int userId, BigDecimal amount);
    boolean updateUserDetails(User user);
}
