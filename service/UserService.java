package service;

import dto.UserLoginDTO;
import dto.UserPasswordUpdateDTO;
import dto.UserRegistrationDTO;
import enums.Role;
import model.User;

public interface UserService {
    boolean registerUser(UserRegistrationDTO user);
    Role loginUser(UserLoginDTO userLoginDTO);
    boolean forgotPassword(UserPasswordUpdateDTO userPasswordUpdateDTO);
    String getSecurityQuestion(String email);
}