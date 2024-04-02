package service;

import dao.UserDao;
import dao.UserDaoImpl;
import dto.UserLoginDTO;
import dto.UserPasswordUpdateDTO;
import dto.UserRegistrationDTO;
import enums.Role;
import model.User;
import utility.EmailValidator;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();


    public User convertToUserEntity(UserRegistrationDTO registrationDTO) {
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(registrationDTO.getPassword()); // Ensure to hash the password here
        user.setContactDetails(registrationDTO.getContactDetails());
        user.setSecurityQuestion(registrationDTO.getSecurityQuestion());
        user.setSecurityAnswer(registrationDTO.getSecurityAnswer());

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
    public Role loginUser(UserLoginDTO userLoginDTO) {
        User user = userDao.findByUsername(userLoginDTO.getUsername());
        if (user != null && user.getPassword().equals(userLoginDTO.getPassword())) { // Ensure passwords are hashed and compared securely
            // Return the user's role on successful login
            return user.getRole();
        } else {
            // Return a value indicating login failure, could be null or a specific failure role
            return Role.INVALID; // Assuming UserRole is an enum that includes INVALID
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

}