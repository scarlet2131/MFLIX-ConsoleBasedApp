package service;

import dao.UserDao;
import dao.UserDaoImpl;
import dto.UserLoginDTO;
import dto.UserPasswordUpdateDTO;
import dto.UserRegistrationDTO;
import enums.Role;
import model.User;
import utility.EmailValidator;

import java.math.BigDecimal;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();


    public User convertToUserEntity(UserRegistrationDTO registrationDTO) {
        if (registrationDTO.getBalance() == null) {
            registrationDTO.setBalance(BigDecimal.ZERO); // Default balance
        }

        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(registrationDTO.getPassword()); // Ensure to hash the password here
        user.setContactDetails(registrationDTO.getContactDetails());
        user.setSecurityQuestion(registrationDTO.getSecurityQuestion());
        user.setSecurityAnswer(registrationDTO.getSecurityAnswer());
        user.setBalance(registrationDTO.getBalance());

        // Set default values for fields not present in the DTO
        user.setRole(Role.USER); // Default role
        user.setActive(true); // Active by default, or based on your application's requirements
        return user;
    }
    @Override
    public boolean registerUser(UserRegistrationDTO registrationDTO) {
        // Implement registration logic, possibly involving validation
        // Validate email format
        if (!EmailValidator.validate(registrationDTO.getEmail())) {
            System.out.println("Registration failed: Invalid email format.");
            return false;
        }

        // Check for unique username
        if (userDao.findByUsername(registrationDTO.getUsername()) != null) {
            System.out.println("Registration failed: Username already exists.");
            return false;
        }

        // Check for unique email
        if (userDao.findByEmail(registrationDTO.getEmail()) != null) {
            System.out.println("Registration failed: Email already exists.");
            return false;
        }

        return userDao.addUser(convertToUserEntity(registrationDTO));
    }


   @Override
    public User loginUser(UserLoginDTO userLoginDTO) {
        User user = userDao.findByUsername(userLoginDTO.getUsername());
        if (user != null && user.getPassword().equals(userLoginDTO.getPassword())) { // Ensure passwords are hashed and compared securely
            // Return the user's role on successful login
            return user;
        } else {
            // Return a value indicating login failure, could be null or a specific failure role
            return null;
        }
    }
    @Override
    public boolean forgotPassword(UserPasswordUpdateDTO passwordUpdateDTO) {
        // validating if security answer is correct
        if (!userDao.validateSecurityAnswerByEmail(passwordUpdateDTO.getEmail(), passwordUpdateDTO.getSecurityAnswer())) {
            return false; // Incorrect security answer
        }
        return userDao.updatePassword(passwordUpdateDTO.getEmail(), passwordUpdateDTO.getNewPassword());
    }

    @Override
    public String getSecurityQuestion(String email){
        return userDao.getSecurityQuestion(email);
    }

    @Override
    public boolean updateUserBalance(int userId, BigDecimal amount) {
        if(userDao.findByUserId(userId)==null){
            System.out.println(" Balance could not be updated, User dosen't exist!!");
            return false;
        }
        return userDao.updateUserBalance(userId, amount);
    }

    @Override
    public User getUserDetails(int userId){
        return userDao.findByUserId(userId);
    }

    @Override
    public boolean updateUserDetails(User user) {
        if (!EmailValidator.validate(user.getEmail())) {
            System.out.println("Please enter valid email, Invalid email format!!");
            return false;
        }

        User userByUsername = userDao.findByUsername(user.getUsername());
        // Check for unique username
        if (userByUsername != null && user.getUserId()!= userByUsername.getUserId()) {
            System.out.println("Please enter different username, Username already exists!!");
            return false;
        }

        User userByEmail = userDao.findByEmail(user.getEmail());

        // Check for unique email
        if (userByEmail != null  && user.getUserId()!= userByEmail.getUserId()) {
            System.out.println("Please enter different email, Email already exists!!");
            return false;
        }
        return userDao.updateUserDetails(user);
    }

}