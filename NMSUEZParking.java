import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Start of main NMSUEZParking
public class NMSUEZParking {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, User> userDatabase = new HashMap<>();
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Start of main NMSUEZParking
public class NMSUEZParking {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, User> userDatabase = new HashMap<>();

        // Load existing user data from file (if any)
        loadUserData(userDatabase);

        System.out.println("Welcome to NMSU EZ Parking!");

        while (true) {
            // Prompting the user for action
            System.out.println("Are you a new user? (yes/no)");
            String newUserResponse = scanner.nextLine().trim();

            if (newUserResponse.equalsIgnoreCase("yes")) {
                // New user registration
                registerNewUser(scanner, userDatabase);
            } else if (newUserResponse.equalsIgnoreCase("no")) {
                // Returning user login
                String aggieID = loginUser(scanner, userDatabase);
                if (aggieID == "back") {
                     System.out.println();
                } else if (aggieID != null) {
                    System.out.println("Login Successful!");
                    // After successful login, show the user menu
                    showUserMenu(scanner, userDatabase, aggieID);
                    break; // Exit the loop if a valid user is found
                } else {
                    // Invalid login attempt
                    System.out.println("User not found. Please try again.");
                }
            } else {
                // Invalid input
                System.out.println("Sorry, that wasn't one of the choices. Please try again.");
            }
        }

        scanner.close();
    }

    // Method to loadUserData
    private static void loadUserData(Map<String, User> userDatabase) {
        File file = new File("user_data.txt");
        if (file.exists()) {
            try (Scanner fileScanner = new Scanner(file)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        String aggieID = parts[0];
                        String fullName = parts[1];
                        boolean hasParkingPermit = Boolean.parseBoolean(parts[2]);
                        String parkingPermitType = parts[3];
                        userDatabase.put(aggieID, new User(fullName, hasParkingPermit, parkingPermitType));
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to saveUserData
    private static void saveUserData(Map<String, User> userDatabase) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("user_data.txt"))) {
            for (Map.Entry<String, User> entry : userDatabase.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method for registerNewUser
    private static void registerNewUser(Scanner scanner, Map<String, User> userDatabase) {
        String aggieID;
        String fullName = null;
        boolean hasParkingPermit = false;
        String parkingPermitType = null;

        do {
            System.out.print("Please enter your Aggie ID (9 digits long), or type 'back' to return to the main menu: ");
            aggieID = scanner.nextLine().trim();
            if (aggieID.equalsIgnoreCase("back")) {
                return; // Go back to the main menu
            }
            if (!aggieID.matches("\\d{9}")) {
                System.out.println("Invalid input. Aggie ID must be 9 digits long. Please try again.");
            } else if (userDatabase.containsKey(aggieID)) {
                User existingUser = userDatabase.get(aggieID);
                System.out.println("User with this Aggie ID is already been registered:");
                String response;
                do {
                    System.out.print("Do you want to register again? (yes/no): ");
                    response = scanner.nextLine().trim();
                    if (!response.equalsIgnoreCase("no") && !response.equalsIgnoreCase("yes")) {
                        System.out.println("Sorry, invalid input. Please try again.");
                    } else {
                        break;
                    }
                } while (true);
                if (response.equalsIgnoreCase("no")) {
                    fullName = existingUser.getFullName();
                    hasParkingPermit = existingUser.hasParkingPermit();
                    parkingPermitType = existingUser.getParkingPermitType();
                    break; // Break the do-while loop if user confirms identity
                }
            } else {
                break; // If ID is new and valid, break the loop to proceed with registration
            }
        } while (true);

        if (fullName == null) {
            System.out.print("Please enter your full name (characters only): ");
            fullName = scanner.nextLine().trim();

            String permitResponse;
            do {
                System.out.print("Do you have a parking permit? (yes/no): ");
                permitResponse = scanner.nextLine().trim();
                if (!permitResponse.equalsIgnoreCase("yes") && !permitResponse.equalsIgnoreCase("no")) {
                    System.out.println("Sorry, invalid input. Please try again.");
                } else {
                    break; // Break the loop only if a valid response ("yes" or "no") is given
                }
            } while (true);

            if (permitResponse.equalsIgnoreCase("yes")) {
                hasParkingPermit = true;
                int permitChoice;
                do {
                    System.out.println("Which parking permit do you have?");
                    System.out.println("1. Commuter Student Permit (Green)");
                    System.out.println("2. North Residential Student Parking (Yellow)");
                    System.out.println("3. Faculty/Staff Parking (Maroon)");
                    System.out.print("Enter the number corresponding to your parking permit: ");
                    try {
                        permitChoice = Integer.parseInt(scanner.nextLine().trim());
                        if (permitChoice >= 1 && permitChoice <= 3) {
                            switch (permitChoice) {
                                case 1:
                                    parkingPermitType = "Commuter Permit (Green)";
                                    break;
                                case 2:
                                    parkingPermitType = "North Residential Parking (Yellow)";
                                    break;
                                case 3:
                                    parkingPermitType = "Faculty Parking (Maroon)";
                                    break;
                            }

                            // Input data into .txt file
                            userDatabase.put(aggieID, new User(fullName, hasParkingPermit, parkingPermitType));
                            saveUserData(userDatabase);
                            
                            System.out.println("Thank you for registering! Please sign back in with your info.");
                            
                            break; // Valid permit choice made, exit the loop
                        } else {
                            System.out.println("Invalid choice. Please select a valid option.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Sorry, invalid input. Please enter a number.");
                    }
                } while (true);
            } else {
                hasParkingPermit = false;
                System.out.println("Please visit the Parking Department to get a Parking Permit.");
                System.out.println("Parking Department Phone Number: 575-646-2133");
                System.out.println("Parking Department Location: 1400 E University Ave, Las Cruces, NM 88001");
            } return;
        }
        // After successful registration, show the user menu
        showUserMenu(scanner, userDatabase, aggieID);
    }

    // Method for loginUser
    private static String loginUser(Scanner scanner, Map<String, User> userDatabase) {
        String aggieID;
        do {
            System.out.print("Please enter your Aggie ID, or type 'back' to return to the main menu: ");
            aggieID = scanner.nextLine().trim();
            if (aggieID.equalsIgnoreCase("back")) {
                return "back"; // Go back to the main menu
            }
            if (!aggieID.matches("\\d{9}")) {
                System.out.println("Invalid input. Aggie ID must be 9 digits long. Please try again.");
                continue; 
            }
            User user = userDatabase.get(aggieID);
            if (user != null) {
               return user.getFullName(); // Found user, return full name
            } else {
               System.out.println("User not found. Please try again or type 'back' to return to the main menu.");
            }
         } while (true);
    }

    static class User {
        private String fullName;
        private boolean hasParkingPermit;
        private String parkingPermitType;

        public User(String fullName, boolean hasParkingPermit, String parkingPermitType) {
            this.fullName = fullName;
            this.hasParkingPermit = hasParkingPermit;
            this.parkingPermitType = parkingPermitType;
        }

        public String getFullName() {
            return fullName;
        }

        public boolean hasParkingPermit() {
            return hasParkingPermit;
        }

        public String getParkingPermitType() {
            return parkingPermitType;
        }

        @Override
        public String toString() {
            return fullName + "," + hasParkingPermit + "," + parkingPermitType;
        }
    }

    // Method for showUserMenu
    private static void showUserMenu(Scanner scanner, Map<String, User> userDatabase, String aggieID) {
        while (true) {
            System.out.println("\nThank you for Logging in, How can we help you?");
            System.out.println("1. Find Parking");
            System.out.println("2. Leave Parking");
            System.out.println("3. Search for Parking");
            System.out.println("4. Report Parking");
            System.out.println("5. Exit to Main Menu");
            System.out.print("Please select an option (1-5): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    // Implement the finding parking code here:
                    System.out.println("Finding Parking...");
                    break;
                case "2":
                    // Implement the leaving parking code here:
                    System.out.println("Leaving Parking...");
                    break;
                case "3":
                    // Implement the searching for parking code here:
                    System.out.println("Searching for Parking...");
                    break;
                case "4":
                    // Call the reportParking method
                    reportParking(scanner, userDatabase, aggieID);
                    break;
                case "5":
                    System.out.println("Exiting to main menu...");
                    return;
                default:
                    System.out.println("Invalid option. Please select a number between 1 and 5.");
                    break;
            }
        }
    }

    // Method to report parking
    private static void reportParking(Scanner scanner, Map<String, User> userDatabase, String aggieID) {
        System.out.println("\nReporting Parking...");

        // Prompt the user for details about the vehicle
        System.out.println("Please provide details of the vehicle to report:");
        System.out.print("Make: ");
        String make = scanner.nextLine();
        System.out.print("Model: ");
        String model = scanner.nextLine();
        System.out.print("Color: ");
        String color = scanner.nextLine();
        System.out.print("Parking Lot: ");
        String parkingLot = scanner.nextLine();
        System.out.print("Reason for report: ");
        String reason = scanner.nextLine();

        // Save the report details to a file (for parking department to review)
        saveReport(make, model, color, parkingLot, reason);
        System.out.println("Thank you for reporting. We will handle the situation.");
    }

    // Method to save report
    private static void saveReport(String make, String model, String color, String parkingLot, String reason) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("report_data.txt", true))) {
            writer.println(make + "," + model + "," + color + "," + parkingLot + "," + reason);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} // End of Main
        // Load existing user data from file (if any)
        loadUserData(userDatabase);

        System.out.println("Welcome to NMSU EZ Parking!");

        while (true) {
            // Prompting the user for action
            System.out.println("Are you a new user? (yes/no)");
            String newUserResponse = scanner.nextLine().trim();

            if (newUserResponse.equalsIgnoreCase("yes")) {
                // New user registration
                registerNewUser(scanner, userDatabase);
            } else if (newUserResponse.equalsIgnoreCase("no")) {
                // Returning user login
                String aggieID = loginUser(scanner, userDatabase);
                if (aggieID == "back") {
                     System.out.println();
                } else if (aggieID != null) {
                    System.out.println("Welcome back!");
                    // After successful login, show the user menu
                    showUserMenu(scanner, userDatabase, aggieID);
                    break; // Exit the loop if a valid user is found
                } else {
                    // Invalid login attempt
                    System.out.println("User not found. Please try again.");
                }
            } else {
                // Invalid input
                System.out.println("Sorry, that wasn't one of the choices. Please try again.");
            }
        }

        scanner.close();
    }

    // Method to loadUserData
    private static void loadUserData(Map<String, User> userDatabase) {
        File file = new File("user_data.txt");
        if (file.exists()) {
            try (Scanner fileScanner = new Scanner(file)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        String aggieID = parts[0];
                        String fullName = parts[1];
                        boolean hasParkingPermit = Boolean.parseBoolean(parts[2]);
                        String parkingPermitType = parts[3];
                        userDatabase.put(aggieID, new User(fullName, hasParkingPermit, parkingPermitType));
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to saveUserData
    private static void saveUserData(Map<String, User> userDatabase) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("user_data.txt"))) {
            for (Map.Entry<String, User> entry : userDatabase.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method for registerNewUser
    private static void registerNewUser(Scanner scanner, Map<String, User> userDatabase) {
        String aggieID;
        String fullName = null;
        boolean hasParkingPermit = false;
        String parkingPermitType = null;

        do {
            System.out.print("Please enter your Aggie ID (9 digits long), or type 'back' to return to the main menu: ");
            aggieID = scanner.nextLine().trim();
            if (aggieID.equalsIgnoreCase("back")) {
                return; // Go back to the main menu
            }
            if (!aggieID.matches("\\d{9}")) {
                System.out.println("Invalid input. Aggie ID must be 9 digits long. Please try again.");
            } else if (userDatabase.containsKey(aggieID)) {
                User existingUser = userDatabase.get(aggieID);
                System.out.println("User with this Aggie ID is already been registered:");
                String response;
                do {
                    System.out.print("Do you want to register again? (yes/no): ");
                    response = scanner.nextLine().trim();
                    if (!response.equalsIgnoreCase("no") && !response.equalsIgnoreCase("yes")) {
                        System.out.println("Sorry, invalid input. Please try again.");
                    } else {
                        break;
                    }
                } while (true);
                if (response.equalsIgnoreCase("no")) {
                    fullName = existingUser.getFullName();
                    hasParkingPermit = existingUser.hasParkingPermit();
                    parkingPermitType = existingUser.getParkingPermitType();
                    break; // Break the do-while loop if user confirms identity
                }
            } else {
                break; // If ID is new and valid, break the loop to proceed with registration
            }
        } while (true);

        if (fullName == null) {
            System.out.print("Please enter your full name (characters only): ");
            fullName = scanner.nextLine().trim();

            String permitResponse;
            do {
                System.out.print("Do you have a parking permit? (yes/no): ");
                permitResponse = scanner.nextLine().trim();
                if (!permitResponse.equalsIgnoreCase("yes") && !permitResponse.equalsIgnoreCase("no")) {
                    System.out.println("Sorry, invalid input. Please try again.");
                } else {
                    break; // Break the loop only if a valid response ("yes" or "no") is given
                }
            } while (true);

            if (permitResponse.equalsIgnoreCase("yes")) {
                hasParkingPermit = true;
                int permitChoice;
                do {
                    System.out.println("Which parking permit do you have?");
                    System.out.println("1. Commuter Student Permit (Green)");
                    System.out.println("2. North Residential Student Parking (Yellow)");
                    System.out.println("3. Faculty/Staff Parking (Maroon)");
                    System.out.print("Enter the number corresponding to your parking permit: ");
                    try {
                        permitChoice = Integer.parseInt(scanner.nextLine().trim());
                        if (permitChoice >= 1 && permitChoice <= 3) {
                            switch (permitChoice) {
                                case 1:
                                    parkingPermitType = "Commuter Permit (Green)";
                                    break;
                                case 2:
                                    parkingPermitType = "North Residential Parking (Yellow)";
                                    break;
                                case 3:
                                    parkingPermitType = "Faculty Parking (Maroon)";
                                    break;
                            }

                            // Input data into .txt file
                            userDatabase.put(aggieID, new User(fullName, hasParkingPermit, parkingPermitType));
                            saveUserData(userDatabase);

                            break; // Valid permit choice made, exit the loop
                        } else {
                            System.out.println("Invalid choice. Please select a valid option.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Sorry, invalid input. Please enter a number.");
                    }
                } while (true);
            } else {
                hasParkingPermit = false;
                System.out.println("Please visit the Parking Department to get a Parking Permit.");
                System.out.println("Parking Department Phone Number: 575-646-2133");
                System.out.println("Parking Department Location: 1400 E University Ave, Las Cruces, NM 88001");
            }
        }
        // After successful registration, show the user menu
        showUserMenu(scanner, userDatabase, aggieID);
    }

    // Method for loginUser
    private static String loginUser(Scanner scanner, Map<String, User> userDatabase) {
        String aggieID;
        do {
            System.out.print("Please enter your Aggie ID, or type 'back' to return to the main menu: ");
            aggieID = scanner.nextLine().trim();
            if (aggieID.equalsIgnoreCase("back")) {
                return "back"; // Go back to the main menu
            }
            if (!aggieID.matches("\\d{9}")) {
                System.out.println("Invalid input. Aggie ID must be 9 digits long. Please try again.");
            }
        } while (!aggieID.matches("\\d{9}") && !aggieID.equalsIgnoreCase("back"));

        if (aggieID.equalsIgnoreCase("back")) {
            return "back"; // Go back to the main menu
        }

        User user = userDatabase.get(aggieID);
        return (user != null) ? user.getFullName() : null;
    }

    static class User {
        private String fullName;
        private boolean hasParkingPermit;
        private String parkingPermitType;

        public User(String fullName, boolean hasParkingPermit, String parkingPermitType) {
            this.fullName = fullName;
            this.hasParkingPermit = hasParkingPermit;
            this.parkingPermitType = parkingPermitType;
        }

        public String getFullName() {
            return fullName;
        }

        public boolean hasParkingPermit() {
            return hasParkingPermit;
        }

        public String getParkingPermitType() {
            return parkingPermitType;
        }

        @Override
        public String toString() {
            return fullName + "," + hasParkingPermit + "," + parkingPermitType;
        }
    }

    // Method for showUserMenu
    private static void showUserMenu(Scanner scanner, Map<String, User> userDatabase, String aggieID) {
        while (true) {
            System.out.println("\nThank you for Logging in, How can we help you?");
            System.out.println("1. Find Parking");
            System.out.println("2. Leave Parking");
            System.out.println("3. Search for Parking");
            System.out.println("4. Report Parking");
            System.out.println("5. Exit to Main Menu");
            System.out.print("Please select an option (1-5): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    // Implement the finding parking code here:
                    System.out.println("Finding Parking...");
                    break;
                case "2":
                    // Implement the leaving parking code here:
                    System.out.println("Leaving Parking...");
                    break;
                case "3":
                    // Implement the searching for parking code here:
                    System.out.println("Searching for Parking...");
                    break;
                case "4":
                    // Call the reportParking method
                    reportParking(scanner, userDatabase, aggieID);
                    break;
                case "5":
                    System.out.println("Exiting to main menu...");
                    return;
                default:
                    System.out.println("Invalid option. Please select a number between 1 and 5.");
                    break;
            }
        }
    }

    // Method to report parking
    private static void reportParking(Scanner scanner, Map<String, User> userDatabase, String aggieID) {
        System.out.println("\nReporting Parking...");

        // Prompt the user for details about the vehicle
        System.out.println("Please provide details of the vehicle to report:");
        System.out.print("Make of the vehicle: ");
        String make = scanner.nextLine();
        System.out.print("Model of the vehicle: ");
        String model = scanner.nextLine();
        System.out.print("Color of the vehicle: ");
        String color = scanner.nextLine();
        System.out.print("Parking Lot the vehicle is located in: ");
        String parkingLot = scanner.nextLine();
        System.out.print("Reason for report: ");
        String reason = scanner.nextLine();

        // Save the report details to a file (for parking department to review)
        saveReport(make, model, color, parkingLot, reason);
        System.out.println("Thank you for reporting. We will handle the situation.");
    }

    // Method to save report
    private static void saveReport(String make, String model, String color, String parkingLot, String reason) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("report_data.txt", true))) {
            writer.println(make + "," + model + "," + color + "," + parkingLot + "," + reason);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} // End of Main
