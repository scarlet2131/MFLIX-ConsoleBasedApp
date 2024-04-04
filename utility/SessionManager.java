package utility;

import model.User;

import java.math.BigDecimal;

public class SessionManager {
    private static User currentUser = null;

    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static int getCurrentUserId() {
        return currentUser != null ? currentUser.getUserId() : -1;
    }
    public static User getCurrentUser() {
        return currentUser;
    }
    public static void updateBalance(BigDecimal balance){
        BigDecimal newBalance = currentUser.getBalance().add(balance);
        currentUser.setBalance(newBalance);
    }

}
