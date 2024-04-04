package ui;

import dto.*;
import enums.Role;
import model.Movie;
import model.Rating;
import model.Rental;
import model.User;
import service.*;
import utility.ConsoleUtil;
import utility.RentalUtils;
import utility.SessionManager;

import java.math.BigDecimal;
import java.time.LocalDate;
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
                    ", Available: " + (movie.isAvailable() ? "Yes" : "No") +
                    ", Rating: "+ movie.getAverageRating());
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

        while (option != 10) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. View All Movies");
            System.out.println("2. Rent a Movie");
            System.out.println("3. Rate a Movie");
            System.out.println("4. View Rental History");
            System.out.println("5. View personal information");
            System.out.println("6. Update personal information");
            System.out.println("7. Search movie by title");
            System.out.println("8. Add Balance to wallet");
            System.out.println("9. Search and filter movies");
            System.out.println("10. Logout");

            System.out.print("Enter option: ");
            option = ConsoleUtil.getIntInput("Please enter your choice : ",1, 10, false);

            switch (option) {
                case 1:
                    viewAllMovies();  // Method to display all movies
                    break;
                case 2:
                    rentMovie();  // Method to handle movie rental
                    break;
                case 3:
                    rateMovie();  // Method to handle movie rental
                    break;
                case 4:
                    viewRentalHistory();  // Method to handle movie rental
                    break;
                case 5:
                    viewPersonaldetails();  // Method to handle movie rental
                    break;
                case 6:
                    updatePersonalDetails();  // Method to handle movie rental
                    break;
                case 7:
                    searchMovieByTitle();  // Method to handle movie rental
                    break;
                // Handle other cases
                case 8:
                    addBalanceToWallet();  // Method to handle movie rental
                    break;
                case 9:
                    searchAndFilterMovies();  // Method to handle movie rental
                    break;
                case 10:
                    SessionManager.logout();
                    System.out.println("Logging out...");
                    startApplication();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void rentSelectedMovie(Movie movie, User user) {
        if (movie == null || user == null) {
            System.out.println("Invalid movie or user.");
            return;
        }

        displayRentalPackages();
        int packageChoice = ConsoleUtil.getIntInput("Select your rental package (1 to 4): ", 1, 4, false);
        BigDecimal cost = RentalUtils.calculatePackageCost(packageChoice);
        if (user.getBalance().compareTo(cost) < 0) {
            System.out.println("Insufficient balance. Please recharge or select a different package.");
            return;
        }

        boolean confirm = ConsoleUtil.confirm("Confirm rental for $" + cost + "?");
        if (!confirm) {
            System.out.println("Rental cancelled.");
            return;
        }

        String result = rentalService.rentMovie(new RentalDTO(
                user.getUserId(),
                movie.getMovieId(),
                LocalDate.now(),
                RentalUtils.calculateDueDate(packageChoice),
                cost
        ));
        System.out.println(result);
    }

    public static void rentMovie() {
        User user = SessionManager.getCurrentUser();
        if (user == null) {
            System.out.println("Please log in first.");
            return;
        }
        displayUserInfo(user);

        Map<Integer, Movie> movies = viewAllMovies(); // Assumes implementation that lists available movies

        if(movies==null || movies.isEmpty()){
            return;
        }
        int movieId = ConsoleUtil.getIntInput("Enter Movie ID to rent: ", null, null, false);
        Movie movieToRent = movies.get(movieId);
        if (movieToRent == null) {
            System.out.println("Movie not found. Please enter a valid Movie ID.");
            return;
        }

        rentSelectedMovie(movieToRent, user);
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
                Movie movie = movieService.getMovieDetails(rental.getMovieId(),false);
                System.out.printf("Movie: %s, Rented On: %s, Due On: %s, Status: %s, Movie Availability: %s%n",
                        movie.getTitle(),
                        rental.getStartDate(),
                        rental.getDueDate(),
                        rental.getStatus(),
                        movie.isAvailable() ? "Yes" : "No");
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
            SessionManager.updateBalance(amountToAdd);
            System.out.println("Your wallet has been successfully updated.");
        } else {
            System.out.println("Failed to update your wallet. Please try again.");
        }
    }

    public static void searchMovieByTitle() {
        System.out.print("Enter movie title to search for: ");
        String title = ConsoleUtil.getInput(""); // Assuming ConsoleUtil.getInput() method exists for user input

        List<Movie> movies = movieService.searchMoviesByTitle(title);
        boolean movieAvailability = false;
        if (movies.isEmpty()) {
            System.out.println("No movies found matching \"" + title + "\".");
        } else {
            System.out.println("Movies found:");
            for (Movie movie : movies) {
                if(movie.isAvailable()){
                    movieAvailability = true;
                }
                System.out.printf("ID: %d, Title: %s, Genre: %s, Release Year: %d, Available: %s\n",
                        movie.getMovieId(),
                        movie.getTitle(),
                        movie.getGenre(),
                        movie.getReleaseYear(),
                        movie.isAvailable() ? "Yes" : "No");

            }

            if(!movieAvailability){
                System.out.println("Sorry, No movies available to rent");
                return;
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
        if (currentUser == null) {
            System.out.println("Rental cancelled. No user logged in.");
            return;
        }

        int movieId = ConsoleUtil.getIntInput("Enter the ID of the movie you want to rent (or 0 to cancel): ", 0, null, false);
        if (movieId == 0) {
            System.out.println("Rental cancelled.");
            return;
        }

        Movie selectedMovie = movies.stream()
                .filter(movie -> movie.getMovieId() == movieId && movie.isAvailable())
                .findFirst()
                .orElse(null);

        if (selectedMovie == null) {
            System.out.println("Invalid selection or movie is not available for rental.");
            return;
        }

        rentSelectedMovie(selectedMovie, currentUser);
    }


    public static void searchAndFilterMovies() {
        // Show all movies first
        Map<Integer, Movie> movieMap = viewAllMovies();
        if(movieMap==null || movieMap.isEmpty()){
            return;
        }

        if (!ConsoleUtil.confirm("Would you like to apply filters or sorting to the movie list?")) {
            return; // Exit if the user does not want to filter or sort
        }

        // Proceed with filtering and sorting
        System.out.println("\nEnter filter options (leave blank for no filter):");
        String genre = ConsoleUtil.getOptionalInput("Genre: ");
        Integer year = ConsoleUtil.getIntInput("Year (YYYY): ", 1870, LocalDate.now().getYear(), true);
        Double rating = ConsoleUtil.getDoubleInput("Minimum rating: ", 0.0, 10.0, true);

        System.out.println("\nSort options:");
        System.out.println("1. Title");
        System.out.println("2. Genre");
        System.out.println("3. Release Year");
        System.out.println("4. Rating");
        System.out.println("5. No Sorting");
        int sortOption = ConsoleUtil.getIntInput("Choose sort option (1-5): ", 1, 5, false);

        String sortBy = mapSortOptionToColumn(sortOption);
        MovieSearchDTO searchCriteria = new MovieSearchDTO(
                genre.isEmpty() ? null : genre,
                year,
                rating,
                sortBy.isEmpty() ? null : sortBy
        );

        List<Movie> filteredMovies = movieService.searchMovies(searchCriteria);
        if (filteredMovies.isEmpty()) {
            System.out.println("No movies found with the given criteria.");
        } else {
            filteredMovies.forEach(movie -> System.out.printf("ID: %s, Title: %s, Genre: %s, Year: %d%n",
                    movie.getMovieId(), movie.getTitle(), movie.getGenre(), movie.getReleaseYear()));
            // Optionally, include rating in the output if implemented
        }
    }
    private static String mapSortOptionToColumn(int sortOption) {
        switch (sortOption) {
            case 1: return "title";
            case 2: return "genre";
            case 3: return "releaseYear";
            case 4: return "rating"; // Make sure this corresponds with how you handle ratings aggregation in the query
            default: return null; // No sorting
        }
    }


    public static void rateMovie() {
        int userId = SessionManager.getCurrentUserId();
        if (userId <= 0) {
            System.out.println("Please log in first.");
            return;
        }

        Map<Integer, Movie> movieMap = viewAllMovies();
        if(movieMap==null || movieMap.isEmpty()){
            return;
        }

        int movieId = ConsoleUtil.getIntInput("Enter Movie ID to rate: ", null, null, false);
        int rating = ConsoleUtil.getIntInput("Enter your rating (1-10): ", 1, 10, false);


        boolean success = movieService.addRating(new Rating(movieId, userId, rating));
        if (success) {
            System.out.println("Thank you for rating the movie!");
        } else {
            System.out.println("Failed to add rating.");
        }
    }


}
