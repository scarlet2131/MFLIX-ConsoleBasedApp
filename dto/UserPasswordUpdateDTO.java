package dto;

public class UserPasswordUpdateDTO {
    private String email;
    private String newPassword;

    private String securityAnswer;

    // Constructors, Getters, and Setters
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    public String getSecurityAnswer(){ return securityAnswer; }

    public void setSecurityAnswer(String securityAnswer){ this.securityAnswer = securityAnswer; }
}
