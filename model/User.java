package model;

import enums.Role;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private int userId; 
    private String username; 
    private String email; 
    private String password; 
    private Role role; 
    private boolean isActive; 
    private String contactDetails;
    private String  securityQuestion ;
    private String securityAnswer;
    private BigDecimal balance;

    // Constructor
    public User() {}
    public User(String username, String password, String email, Role role, boolean isActive, String contactDetails, String securityQuestion, String securityAnswer, BigDecimal balance) {
        this.username = username;
        this.password = password; 
        this.email = email;
        this.role = role; 
        this.isActive = isActive;
        this.contactDetails = contactDetails;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.balance = balance;
    }

    // Constructor that takes a ResultSet
    public User(ResultSet rs) throws SQLException {
        // Assuming column names in the ResultSet match the field names
        // Adjust the column names as necessary based on your database schema
        this.userId = rs.getInt("user_id");
        this.username = rs.getString("username");
        this.email = rs.getString("email");
        this.password = rs.getString("password");
        // Convert the string role to enum (assuming the Role enum has a valueOf method that matches the database role string)
        this.role = Role.valueOf(rs.getString("role").toUpperCase());
        this.isActive = rs.getBoolean("is_active");
        this.contactDetails = rs.getString("contactDetails");
        this.securityQuestion = rs.getString("security_question");
        this.securityAnswer = rs.getString("security_answer");
        this.balance = rs.getBigDecimal("balance");
    }

    // Getters and Setters

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public String getSecurityQuestion(){ return securityQuestion; }

    public void setSecurityQuestion(String securityQuestion) { this.securityQuestion = securityQuestion;}

    public String getSecurityAnswer(){ return securityAnswer; }

    public void setSecurityAnswer(String securityAnswer) { this.securityAnswer = securityAnswer; }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }
}

