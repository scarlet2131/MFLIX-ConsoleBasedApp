package dao;

import model.User;

public interface UserDao {
    boolean addUser(User user);
    User findByUsername(String username);
    boolean updatePassword(String email, String newPassword);
    User findByEmail(String email);
    String getSecurityQuestion(String email) ;
    boolean validateSecurityAnswerByEmail(String email, String securityAnswer);
}
