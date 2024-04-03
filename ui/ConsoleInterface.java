package ui;

import dto.MovieDTO;
import dto.UserLoginDTO;
import dto.UserPasswordUpdateDTO;
import dto.UserRegistrationDTO;
import enums.Role;
import model.Movie;
import model.Rental;
import model.User;
import service.*;
import utility.ConsoleUtil;
import utility.SessionManager;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ConsoleInterface {
    private static final Scanner scanner = new Scanner(System.in);
    private static UserService userService = new UserServiceImpl();
    private static AdminService adminService = new AdminServiceImpl();
    private static MovieService movieService = new MovieServiceImpl();
    private static RentalService rentalService = new RentalServiceImpl();

    public static void startApplication() {
        int choice = 0;
        while (choice != 4) {
            System.out.println("\n=== MFlix Movie Rental Application ===");
            System.out.println("1. Register User");
            System.out.println("2. Login");
            System.out.println("3. Forgot Password");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            choice = ConsoleUtil.getIntInput(" ",1,4,false); // Assume this correctly consumes the line

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
        BigDecimal balance = ConsoleUtil.getBigDecimalInput("Balance (leave blank to start with 0 balance) : ",true);
        if (balance == null) {
            balance = BigDecimal.ZERO; // Default value or handle appropriately
        }

        // Populate the DTO with data collected from the user
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setUsername(username);
        registrationDTO.setEmail(email);
        registrationDTO.setPassword(password);
        registrationDTO.setSecurityQuestion(securityQuestion);
        registrationDTO.setSecurityAnswer(securityAnswer);
        registrationDTO.setContactDetails(contactDetails);
        registrationDTO.setBalance(balance);

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
        User user = userService.loginUser(userLoginDTO);

        if (user != null) {
            SessionManager.login(user);

            if (user.getRole() == Role.ADMIN) {
                System.out.println("Admin login successful!");
                showAdminMenu();
            } else if (user.getRole() == Role.USER) {
                System.out.println("Login successful!");
                showUserMenu();
            } else {
                // This branch might be unnecessary if you're confident all users have a valid role
                // But it's here to handle any unexpected cases
                System.out.println("Login failed. User role is unrecognized.");
            }
        } else {
            System.out.println("Login failed. Please check your credentials.");
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

    public static void viewPersonaldetails(){
        int userId = SessionManager.getCurrentUserId(); // Get current user's ID
        if (userId == -1) {
            System.out.println("No user is currently logged in.");
            return;
        }

        User user = userService.getUserDetails(userId); // Fetch user details
        if (user == null) {
            System.out.println("Failed to retrieve user details.");
            return;
        }

        // Display user details
        System.out.println("\nUser Personal Details:");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Contact Details: " + user.getContactDetails());
        System.out.println("Security Question: " + user.getSecurityQuestion());
        System.out.println("Balance: " + user.getBalance()); // Assuming User class has getBalance method
    }


    public static void updatePersonalDetails() {
        int userId = SessionManager.getCurrentUserId(); // Get current user's ID
        if (userId == -1) {
            System.out.println("No user is currently logged in.");
            return;
        }

        User user = userService.getUserDetails(userId); // Fetch current user details
        if (user == null) {
            System.out.println("Failed to retrieve user details.");
            return;
        }

        int choice = 0;
        while( choice !=  5 ){
            System.out.println("\n=== Update Personal Details ===");
            System.out.println("1. Email");
            System.out.println("2. Username");
            System.out.println("3. Contact Details");
            System.out.println("4. Password");
            System.out.println("5. Cancel");
            System.out.print("Choose an option to update: ");

            choice = ConsoleUtil.getIntInput(" ", 1, 5, false);

            switch (choice) {
                case 1:
                    String newEmail = ConsoleUtil.getInput("Enter new email: ");
                    user.setEmail(newEmail);
                    break;
                case 2:
                    String newUsername = ConsoleUtil.getInput("Enter new username: ");
                    user.setUsername(newUsername);
                    break;
                case 3:
                    String newContactDetails = ConsoleUtil.getInput("Enter new contact details: ");
                    user.setContactDetails(newContactDetails);
                    break;
                case 4:
                    String newPassword = ConsoleUtil.getInput("Enter new password: ");
                    // Assuming UserService can handle password hashing internally
                    user.setPassword(newPassword);
                    break;
                case 5:
                    System.out.println("Update cancelled.");
                    return;
                default:
                    System.out.println("Invalid choice. Please choose again.");
                    return;
            }

            // Update user details
            boolean success = userService.updateUserDetails(user);
            if (success) {
                System.out.println("User details updated successfully.");
            } else {
                System.out.println("Failed to update user details.");
            }
        }

    }

//--------------------------------------Movie CRUD Actions----------------------------------------

    private static void showAdminMenu() {
        int choice = 0;

        while(choice != 6){
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Add Movie");
            System.out.println("2. Update Movie");
            System.out.println("3. Delete Movie");
            System.out.println("4. View All Movies");
            System.out.println("5. View personal information");
            System.out.println("6. Update personal information");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");
            choice = ConsoleUtil.getIntInput("Please enter your choice : ",1, 7, false);


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
                    viewPersonaldetails();
                    break;
                case 6:
                    updatePersonalDetails();
                    break;
                case 7:
                    SessionManager.logout();
                    System.out.println("Logging out...");
                    startApplication();
                    break;
                default:
                    System.out.println("Invalid choice. Please choose again.");
                    showAdminMenu();
                    break;
            }
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

    private static Map<Integer, Movie> viewAllMovies() {
        System.out.println("\n=== View All Movies ===");
        List<Movie> movies = adminService.getAllMovies(); // Assuming adminService is your service layer instance
        Map<Integer, Movie> mapOfMovies = Optional.ofNullable(movies).orElseGet(Collections::emptyList) // Handles null list
                .stream()
                .filter(movie -> movie != null) // Filters out null Movie objects
                .collect(Collectors.toMap(Movie::getMovieId, movie -> movie, (existing, replacement) -> existing));
        if (movies.isEmpty()) {
            System.out.println("No movies found.");
            return mapOfMovies;
        }

        for (Movie movie : movies) {
            System.out.println("ID: " + movie.getMovieId() + ", Title: " + movie.getTitle() +
                    ", Genre: " + movie.getGenre() + ", Release Year: " + movie.getReleaseYear() +
                    ", Available: " + (movie.isAvailable() ? "Yes" : "No"));
        }
        return mapOfMovies;
    }

    //--------------------------------------User Menu Actions----------------------------------------

    public static void displayUserInfo(User user) {
        if (user != null) {
            System.out.println("Welcome, " + user.getUsername());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Balance: " + user.getBalance());
        } else {
            System.out.println("Failed to fetch user details. Please login ");
        }
    }
    public static void showUserMenu() {
        int option = 0;

        while (option != 8) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. View All Movies");
            System.out.println("2. Rent a Movie");
            System.out.println("3. View Rental History");
            System.out.println("4. View personal information");
            System.out.println("5. Update personal information");
            System.out.println("6. Search movie by title");
            System.out.println("7. Add Balance to wallet");
            System.out.println("8. Logout");

            System.out.print("Enter option: ");
            option = ConsoleUtil.getIntInput("Please enter your choice : ",1, 8, false);

            switch (option) {
                case 1:
                    viewAllMovies();  // Method to display all movies
                    break;
                case 2:
                    rentMovie();  // Method to handle movie rental
                    break;
                case 3:
                    viewRentalHistory();  // Method to handle movie rental
                    break;
                case 4:
                    viewPersonaldetails();  // Method to handle movie rental
                    break;
                case 5:
                    updatePersonalDetails();  // Method to handle movie rental
                    break;
                case 6:
                    searchMovieByTitle();  // Method to handle movie rental
                    break;
                // Handle other cases
                case 7:
                    addBalanceToWallet();  // Method to handle movie rental
                    break;

                case 8:
                    SessionManager.logout();
                    System.out.println("Logging out...");
                    startApplication();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }


    public static void rentMovie() {
        User user = SessionManager.getCurrentUser();
        if (user == null) {
            System.out.println("Please log in first.");
            return;
        }
        displayUserInfo(user);
        displayRentalPackages();

        Map<Integer, Movie> movies = viewAllMovies(); // Assumes implementation that lists available movies

        int movieId = ConsoleUtil.getIntInput("Enter Movie ID to rent: ",null,null,false);
        if (!movies.containsKey(movieId)) {
            System.out.println("Movie not found. Please enter a valid Movie ID.");
            return;
        }

        Movie movieToRent = movies.get(movieId);

        int packageChoice = ConsoleUtil.getIntInput("Select your rental package (1 to 4): ",1,4, false);
        int cost = calculatePackageCost(packageChoice);
        if (user.getBalance().compareTo(BigDecimal.valueOf(cost)) < 0) {
            System.out.println("Insufficient balance. Please recharge or select a different package.");
            return;
        }

        boolean confirm = ConsoleUtil.confirm("Confirm rental for $" + cost + "?");
        if (!confirm) {
            System.out.println("Rental cancelled.");
            return;
        }

        String result = rentalService.rentMovie(user.getUserId(), movieToRent, packageChoice, BigDecimal.valueOf(cost));
        System.out.println(result);
    }

    private static void displayRentalPackages() {
        System.out.println("Available Rental Packages per movie:");
        System.out.println("1 Month: $10");
        System.out.println("3 Months: $20");
        System.out.println("6 Months: $40");
        System.out.println("12 Months: $70");
    }

    private static int calculatePackageCost(int packageChoice) {
        switch (packageChoice) {
            case 1: return 10;
            case 2: return 20;
            case 3: return 40;
            case 4: return 70;
            default: return -1; // Invalid choice
        }
    }

    public static void viewRentalHistory() {
        int userId = SessionManager.getCurrentUserId();
        if (userId == -1) {
            System.out.println("No user is currently logged in.");
            return;
        }

        List<Rental> rentals = rentalService.getRentalHistory(userId);
        if (rentals.isEmpty()) {
            System.out.println("No rental history found.");
        } else {
            System.out.println("\nRental History:");
            for (Rental rental : rentals) {
                // Assuming you have a method to fetch movie title by ID
                Movie movie = movieService.getMovieDetails(rental.getMovieId());
                System.out.printf("Movie: %s, Rented On: %s, Due On: %s, Status: %s%n",
                        movie.getTitle(),
                        rental.getStartDate(),
                        rental.getDueDate(),
                        rental.getStatus());
            }
        }
    }

    public static void addBalanceToWallet() {
        int userId = SessionManager.getCurrentUserId();
        if (userId == -1) {
            System.out.println("No user is currently logged in.");
            return;
        }

        // Prompt user for the amount to add to the wallet
        System.out.print("Enter the amount to add to your wallet: ");
        BigDecimal amountToAdd = ConsoleUtil.getBigDecimalInput("Amount: ", false);

        // Ensure a positive amount is entered
        if (amountToAdd.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Please enter a positive amount.");
            return;
        }

        // Attempt to update the user's balance
        boolean success = userService.updateUserBalance(userId, amountToAdd);
        if (success) {
            System.out.println("Your wallet has been successfully updated.");
        } else {
            System.out.println("Failed to update your wallet. Please try again.");
        }
    }

    public static void searchMovieByTitle() {
        System.out.print("Enter movie title to search for: ");
        String title = ConsoleUtil.getInput(""); // Assuming ConsoleUtil.getInput() method exists for user input

        List<Movie> movies = movieService.searchMoviesByTitle(title);
        if (movies.isEmpty()) {
            System.out.println("No movies found matching \"" + title + "\".");
        } else {
            System.out.println("Movies found:");
            for (Movie movie : movies) {
                System.out.printf("ID: %d, Title: %s, Genre: %s, Release Year: %d, Available: %s\n",
                        movie.getMovieId(),
                        movie.getTitle(),
                        movie.getGenre(),
                        movie.getReleaseYear(),
                        movie.isAvailable() ? "Yes" : "No");
            }

            // Ask if the user wants to rent a movie from the search results
            if (ConsoleUtil.confirm("Would you like to rent a movie from these results?")) {
                rentMovieFromSearchResults(movies);
            } else {
                System.out.println("Returning to main menu.");
                return;
            }
        }
    }

    private static void rentMovieFromSearchResults(List<Movie> movies) {
        User currentUser = SessionManager.getCurrentUser();
        System.out.print("Enter the ID of the movie you want to rent (or 0 to cancel): ");
        int movieId = ConsoleUtil.getIntInput("Movie ID: ", 0, null, false);

        if (movieId == 0) {
            System.out.println("Rental cancelled.");
            return;
        }

        // Check if the selected movie ID is in the list of search results and is available
        Movie selectedMovie = movies.stream()
                .filter(movie -> movie.getMovieId() == movieId && movie.isAvailable())
                .findFirst()
                .orElse(null);

        if (selectedMovie == null) {
            System.out.println("Invalid selection or movie is not available for rental.");
            return;
        }

        int packageChoice = ConsoleUtil.getIntInput("Select your rental package (1 to 4): ",1,4, false);
        int cost = calculatePackageCost(packageChoice);
        if (currentUser.getBalance().compareTo(BigDecimal.valueOf(cost)) < 0) {
            System.out.println("Insufficient balance. Please recharge or select a different package.");
            return;
        }

        boolean confirm = ConsoleUtil.confirm("Confirm rental for $" + cost + "?");
        if (!confirm) {
            System.out.println("Rental cancelled.");
            return;
        }
        // Proceed with renting the selected movie
        String result = rentalService.rentMovie(SessionManager.getCurrentUserId(), selectedMovie,packageChoice, BigDecimal.valueOf(cost));
        System.out.println(result);
    }







}
