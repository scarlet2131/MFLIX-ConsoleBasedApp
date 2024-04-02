package utility;

import java.util.Scanner;

public class ConsoleUtil {
    private static final Scanner scanner = new Scanner(System.in);

    // Utility method to get non-empty input
    public static String getInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine();
        } while (input.trim().isEmpty());
        return input;
    }

    // New method for optional input
    public static String getOptionalInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine(); // Return the input directly, even if it's empty
    }

    // Utility method to get validated integer input
    // Enhanced to handle optional inputs and validate integers
    public static Integer getIntInput(String prompt, Integer min, Integer max, boolean allowEmpty) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input.isEmpty() && allowEmpty) {
                return null; // Return null for optional fields
            }
            try {
                int value = Integer.parseInt(input);
                if ((min == null || value >= min) && (max == null || value <= max)) {
                    return value;
                }
                System.out.println("Input must be between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    // Updated to handle optional boolean inputs
    public static Boolean getBooleanInput(String prompt, boolean allowEmpty) {
        while (true) {
            System.out.print(prompt + " (true/false): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.isEmpty() && allowEmpty) {
                return null; // Return null for optional fields
            }
            if (input.equals("true")) {
                return true;
            } else if (input.equals("false")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'true' or 'false'.");
            }
        }
    }

    // Utility method to get validated double input
    public static Double getDoubleInput(String prompt, Double min, Double max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                double value = Double.parseDouble(input);
                if ((min == null || value >= min) && (max == null || value <= max)) {
                    return value;
                }
                System.out.println("Input must be between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    // Utility method to get validated boolean input
//    public static boolean getBooleanInput(String prompt) {
//        while (true) {
//            System.out.print(prompt + " (true/false): ");
//            String input = scanner.nextLine().trim().toLowerCase();
//            if (input.equals("true")) {
//                return true;
//            } else if (input.equals("false")) {
//                return false;
//            } else {
//                System.out.println("Invalid input. Please enter true or false.");
//            }
//        }
//    }
}
