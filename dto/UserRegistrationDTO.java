package dto;

public class UserRegistrationDTO {
    private String username;
    private String email;
    private String password;
    private String contactDetails;
    private String securityQuestion;
    private String securityAnswer;

    // Constructors
    public UserRegistrationDTO() {}

    public UserRegistrationDTO(String username, String email, String password, String contactDetails, String securityQuestion, String securityAnswer) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.contactDetails = contactDetails;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }
}
