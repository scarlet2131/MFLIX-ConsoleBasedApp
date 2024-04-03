package service;

import dto.UserLoginDTO;
import dto.UserPasswordUpdateDTO;
import dto.UserRegistrationDTO;
import enums.Role;
import model.User;

import java.math.BigDecimal;

public interface UserService {
    boolean registerUser(UserRegistrationDTO user);
    User loginUser(UserLoginDTO userLoginDTO);
    boolean forgotPassword(UserPasswordUpdateDTO userPasswordUpdateDTO);
    String getSecurityQuestion(String email);
    boolean updateUserBalance(int userId, BigDecimal amount);
    User getUserDetails(int userId);
    boolean updateUserDetails(User user);

}