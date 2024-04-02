package ui;

import dto.MovieDTO;
import dto.UserLoginDTO;
import dto.UserPasswordUpdateDTO;
import dto.UserRegistrationDTO;
import enums.Role;
import model.Movie;
import service.AdminService;
import service.AdminServiceImpl;
import service.UserService;
import service.UserServiceImpl;
import utility.ConsoleUtil;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ConsoleInterface {
    private static final Scanner scanner = new Scanner(System.in);
    private static UserService userService = new UserServiceImpl();
    private static AdminService adminService = new AdminServiceImpl();

    public static void startApplication() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== MFlix Movie Rental Application ===");
            System.out.println("1. Register User");
            System.out.println("2. Login");
            System.out.println("3. Forgot Password");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = 0;
            boolean validInput = false;
            while (!validInput) {
                try {
                    choice = scanner.nextInt();
                    validInput = true; // Will not get here if an exception is thrown
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 4.");
                    scanner.next(); // Consume the invalid input
                }
            }
            scanner.nextLine(); // Consume newline left-over

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    forgotPassword();
                    break;
                case 4:
                    System.out.println("Exiting application...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please choose again.");
            }
        }

    }
//--------------------------------------User actions----------------------------------------
    private static void registerUser() {
        System.out.println("\nRegister a new user:");
        String username = ConsoleUtil.getInput("Username: ");
        String email = ConsoleUtil.getInput("Email: ");
        String password = ConsoleUtil.getInput("Password: ");
        String securityQuestion = ConsoleUtil.getInput("Security Question (e.g., Your favorite movie?): ");
        String securityAnswer = ConsoleUtil.getInput("Security Answer: ");
        String contactDetails = ConsoleUtil.getInput("Contact details: ");

        // Populate the DTO with data collected from the user
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setUsername(username);
        registrationDTO.setEmail(email);
        registrationDTO.setPassword(password);
        registrationDTO.setSecurityQuestion(securityQuestion);
        registrationDTO.setSecurityAnswer(securityAnswer);
        registrationDTO.setContactDetails(contactDetails);

        // Pass the DTO to the service layer
        boolean success = userService.registerUser(registrationDTO);

        if (success) {
            System.out.println("User registered successfully!");
        } else {
            System.out.println("Registration failed. User might already exist or there was an error.");
        }
    }

    private static void loginUser() {
        System.out.println("\nLogin");
        String username = ConsoleUtil.getInput("Username: ");
        String password = ConsoleUtil.getInput("Password: ");

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername(username);
        userLoginDTO.setPassword(password);

        // This now expects a UserRole or similar object, not just a boolean
        Role role = userService.loginUser(userLoginDTO);

        if (role == Role.ADMIN) {
            System.out.println("Admin login successful!");
            showAdminMenu();
        } else if (role == Role.USER) {
            System.out.println("Login successful!");
            // Show regular user menu or further actions
        } else {
            System.out.println("Login failed. Please check your credentials and role.");
        }
    }


    public static void forgotPassword() {
        System.out.println("Forgot Password:");
        String email = ConsoleUtil.getInput("Email address: ");

        // Fetch and display the security question for the given email
        // Note: This part might need to adjust if you prefer fetching the question directly in the service layer
        String securityQuestion = userService.getSecurityQuestion(email); // Assume this method exists in UserService
        if (securityQuestion == null) {
            System.out.println("Email not found.");
            return;
        }

        System.out.println("Security question: " + securityQuestion);
        String securityAnswer = ConsoleUtil.getInput("Your answer: ");
        String newPassword = ConsoleUtil.getInput("New password: ");
        String confirmPassword = ConsoleUtil.getInput("Confirm new password: ");

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return;
        }

        // Populate the DTO with data collected from the user
        UserPasswordUpdateDTO userPasswordUpdateDTO = new UserPasswordUpdateDTO();
        userPasswordUpdateDTO.setEmail(email);
        userPasswordUpdateDTO.setNewPassword(newPassword);
        userPasswordUpdateDTO.setSecurityAnswer(securityAnswer);
        boolean passwordUpdated = userService.forgotPassword(userPasswordUpdateDTO);
        if (passwordUpdated) {
            System.out.println("Password updated successfully.");
        } else {
            System.out.println("Failed to update password. Incorrect security answer or technical issue.");
        }
    }

//--------------------------------------Movie CRUD Actions----------------------------------------

    private static void showAdminMenu() {
        System.out.println("\n=== Admin Menu ===");
        System.out.println("1. Add Movie");
        System.out.println("2. Update Movie");
        System.out.println("3. Delete Movie");
        System.out.println("4. View All Movies");
        System.out.println("5. Logout");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                addMovie();
                break;
            case 2:
                updateMovie();
                break;
            case 3:
                deleteMovie();
                break;
            case 4:
                viewAllMovies();
                break;
            case 5:
                startApplication(); // Or another way to log out
                break;
            default:
                System.out.println("Invalid choice. Please choose again.");
                showAdminMenu();
                break;
        }
    }

    private static void addMovie() {
        System.out.println("\n=== Add a New Movie ===");
        String title = ConsoleUtil.getInput("Title: ");
        String genre = ConsoleUtil.getInput("Genre: ");
        int releaseYear = ConsoleUtil.getIntInput("Release Year (YYYY): ", 1878, java.time.LocalDate.now().getYear(),false); // Assuming the oldest movie is from 1878
        String availabilityInput = ConsoleUtil.getInput("Is Available (true/false): ");
        boolean isAvailable = ConsoleUtil.getBooleanInput(availabilityInput,false);

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTitle(title);
        movieDTO.setGenre(genre);
        movieDTO.setReleaseYear(releaseYear);
        movieDTO.setAvailable(isAvailable);

        if (adminService.addMovie(movieDTO)) {
            System.out.println("Movie added successfully!");
        } else {
            System.out.println("Failed to add the movie.");
        }
    }


    private static void updateMovie() {
        System.out.println("\n=== Update Existing Movie ===");
        int movieId = ConsoleUtil.getIntInput("Enter the Movie ID to update: ", null, null, false); // Movie ID is required

        String title = ConsoleUtil.getOptionalInput("New Title (leave blank to keep unchanged): ");
        String genre = ConsoleUtil.getOptionalInput("New Genre (leave blank to keep unchanged): ");

        // Optional inputs with validation
        Integer releaseYear = ConsoleUtil.getIntInput("New Release Year (YYYY or leave blank to keep unchanged): ", 1878, java.time.LocalDate.now().getYear(), true);
        Boolean isAvailable = ConsoleUtil.getBooleanInput("Is Available (true/false or leave blank to keep unchanged): ", true);

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTitle(title.isEmpty() ? null : title);
        movieDTO.setGenre(genre.isEmpty() ? null : genre);
        if (releaseYear != null) movieDTO.setReleaseYear(releaseYear);
        if (isAvailable != null) movieDTO.setAvailable(isAvailable);

        // Assuming adminService has an updateMovie method that accepts movieId and movieDTO
        if (adminService.updateMovie(movieId, movieDTO)) {
            System.out.println("Movie updated successfully!");
        } else {
            System.out.println("Failed to update the movie. Ensure the movie ID is correct.");
        }
    }


    private static void deleteMovie() {
        System.out.println("\n=== Delete Movie ===");
        int movieId = ConsoleUtil.getIntInput("Enter the Movie ID to delete: ", null, null, false); // Fetch the movie ID
        String confirmation = ConsoleUtil.getInput("Are you sure you want to delete this movie? (yes/no): "); // Confirm action

        if ("yes".equalsIgnoreCase(confirmation)) {
            if (adminService.deleteMovie(movieId)) {
                System.out.println("Movie deleted successfully.");
            } else {
                System.out.println("Failed to delete the movie. Ensure the movie ID is correct.");
            }
        } else {
            System.out.println("Movie deletion canceled.");
        }
    }

    private static void viewAllMovies() {
        System.out.println("\n=== View All Movies ===");
        List<Movie> movies = adminService.getAllMovies(); // Assuming adminService is your service layer instance

        if (movies.isEmpty()) {
            System.out.println("No movies found.");
            return;
        }

        for (Movie movie : movies) {
            System.out.println("ID: " + movie.getMovieId() + ", Title: " + movie.getTitle() +
                    ", Genre: " + movie.getGenre() + ", Release Year: " + movie.getReleaseYear() +
                    ", Available: " + (movie.isAvailable() ? "Yes" : "No"));
        }
    }


}
