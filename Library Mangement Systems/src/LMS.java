//Fatima Harrison, CEN 3024C, September 5th 2025
//SDLC Assignment Part 2
//This is the development of a library management system (LMS) that will be taken place at locals libraries.
//It's an upgrade for technology to be used in these libraries in order for an authorized user to store each patron's data to be stored paperless.
//A file path is distributed for authorized user to have access to review details. The system provides functional features giving the user options to add, review, and remove a patron to keep in track of their current members.
//The main method requires the patron class the carries the main objective for authorized users to add, remove, and review the
//- list within the menu options. The objective is to successfully store in members of library using a upgraded version with technology and without using paper and
//- to keep in track of their data.

//Inserting imports
import java.io.*;
import java.util.*;
//Declaring main class for Library Management Systems
public class LMS {
    //Class variables to be used throughout the programming
    private static class Patron {
        String id; //Patron's details
        String name; //Patron's name
        String email;//Patron's email
        String address;//Patron's address
        double overdue;//Item rental pricing
        //Declaring Parameterized contributors.
        Patron(String id, String name, String email, String address, double overdue) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.address = address;
            this.overdue = overdue;
        }
        //To give the toString a proper format seperated by commas.
        //The %s holds the string value.
        @Override
        public String toString() {
            return String.format("ID: %s, Name: %s, Email: %s, Address: %s, Overdue: $%.2f",
                    id, name, email, address, overdue);
        }
    }
    //A function to load data from the given data file path.
    public static List<Patron> loadData(String PatronFile) {
        //To list each Patron in a array list.
        List<Patron> patrons = new ArrayList<>();
        File file = new File(PatronFile);
        //Give the user a message if the file has not been found.
        if (!file.exists()) {
            System.out.println("File has not been found");
            return patrons;
        }
        // BufferReader creates a buffered stream that wraps around a file stream
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.strip().split(","); //Identified in the spacing of a ','.
                if (parts.length == 5) { //Length of array
                    //Method to read each array in the given file path.
                    try {
                        //Double method to read over the decimal
                        double overdue = Double.parseDouble(parts[4]);
                        patrons.add(new Patron(parts[0], parts[1], parts[2], parts[3], overdue)); //File reader format
                    } catch (NumberFormatException e) { //A message if error occurs.
                        System.out.println("Skipping invalid overdue amount!");
                    }
                }
            }
            //Gives message if an error occurs
        } catch (IOException e) {
            System.out.println("Unable to read file.");
        }

        return patrons;
    }
    //A function to display the list of Patrons within the file.
    public static void displayPatrons(List<Patron> patrons) {
        //If the file has no data, the user will be given a message.
        if (patrons.isEmpty()) {
            System.out.println("No records found.");
            //Otherwise, it will print out the list of Patrons.
        } else {
            System.out.println("\n Patron List:");
            for (Patron patron : patrons) {
                System.out.println(patron);
            }
        }
    }
    //Validates a patron ID based on length and already not exist.
    public static boolean isValidId(String id, List<Patron> patrons) {
        if (id.length() != 7 || !id.matches("\\d{7}")) return false;
        //Reject if it's not meet.
        for (Patron p : patrons) {
            if (p.id.equals(id)) return false;
        }
        return true; //returns if both meets requirement.
    }
    //Method to set the outstanding balance.
    public static double getValidOverdue(Scanner scanner) {
        while (true) {
            System.out.print("Enter overdue amount ($0 - $250): ");
            try {
                //Method to determine if the inserted amount meets the requirements
                double amount = Double.parseDouble(scanner.nextLine());
                if (amount >= 0 && amount <= 250) {
                    return amount; //Stored into the system
                } else {
                    //shared message if not
                    System.out.println("Amount must be between $0 and $250.");
                }
                //Catches exception if a letter or invalid character was entered.
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
        //Method function to create the ID for each added patron.
    public static void addPatron(List<Patron> patrons, Scanner scanner) {
        String id;
        patrons = new ArrayList<>();
        //System functions to prompt and store data.
        while (true) {
            System.out.print("Enter 7-digit unique Patron ID: ");
            id = scanner.nextLine();
            if (isValidId(id, patrons)) break;
            System.out.println("Invalid entry or duplicate ID.");
        }
        //Prompting the user to enter the patron's details
        System.out.print("Enter Patron Name: ");
        String name = scanner.nextLine();// Enter in new line
        System.out.print("Enter Patron Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Patron Address: ");
        String address = scanner.nextLine();
        double overdue = getValidOverdue(scanner); //Using the double since the price requires a decimal.
        //Notify user once detail are completed correctly.
        patrons.add(new Patron(id, name, email, address, overdue));
        System.out.println("Patron added successfully✅");
        displayPatrons(patrons);
    }
    //Method to remove a patron by prompting the patron's ID #.
    public static void removePatron(List<Patron> patrons, Scanner scanner) {
        System.out.print("Enter Patron ID to remove: ");
        String removeId = scanner.nextLine();
        //Checks whether a given patron ID is valid
        boolean removed = patrons.removeIf(p -> p.id.equals(removeId));
        if (removed) {
            System.out.println(" Patron removed successfully.");
        } else {
            System.out.println("Patron ID not found.");
        }
        displayPatrons(patrons);
    }
    //Method for the patron to update their address.
    public static void updateAddress(List<Patron> patrons, Scanner scanner, String role) {
        if (!role.equalsIgnoreCase("Patron")) { //Only Patrons if not access denied.
            System.out.println("Access Denied🚫");
            return;
        }
       //Prompting the patron to be verified based from their name and ID number.
        System.out.print("Enter Patron Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Patron ID: ");
        String id = scanner.nextLine();
        for (Patron p : patrons) { //If it's been matched
            if (p.name.equals(name) || p.id.equals(id)) { //Prompting them to...
                System.out.println("Current Address: " + p.address);
                System.out.print("New address: ");
                String newAddress = scanner.nextLine();
                //Restricting an empty insert.
                if (newAddress.isEmpty()) {
                    System.out.println("Address cannot be empty");
                    return;
                }

                p.address = newAddress;
                System.out.println("Address updated successfully ✅");
                return;
            }
        }//Error message if not found
        System.out.println("Patron not found🚫");
    }
    //Allows the user to login in with username.
    public static String login(Scanner scanner) {
        System.out.print("Enter your role (User/Patron): ");//Prompts user to enter their role.
        String role = scanner.nextLine().trim();//Scans into the system
        // If the person inputs a invalid entry, The system will redirect to guest mode.
        if (!role.equalsIgnoreCase("User") && !role.equalsIgnoreCase("Patron")) {
            System.out.println(" Access Denied🚫");
            role = "Guest";
        }
        //Given message of current login role.
        System.out.println("Logged in as " + role);
        return role;
    }
    //Main file scanner
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] credentials = new String[]{login(scanner)};
        String role = credentials[0];
        String filePath = "E";
        List<Patron> patrons = loadData(filePath);
        //Prompting only the user to enter the data file path.
        if (role.equalsIgnoreCase("User")) {
            System.out.print("📂 Enter the file path: ");
            filePath = scanner.nextLine();
        } else {
            //For Patron or Guest, use a default path or skip loading
            filePath = "Patron.txt"; //File path
            System.out.println("\nYou have no access to a file🚫");
        }
        patrons = loadData(filePath); //Loads the data for patron within filepath
        displayPatrons(patrons);//Playing the data on screen

        //A while loop to give each role a functional option.
        while (true) {
            //Section for admin user's only
            if (role.equalsIgnoreCase("User")) {
                System.out.println("\n**** Menu ****");
                System.out.println("1. Add New Patron");
                System.out.println("2. Remove Patron");
                System.out.println("3. Print All Patrons");
                System.out.println("6. Exit");
            }//Section for Patron's users only
            if (role.equalsIgnoreCase("Patron")) {
                System.out.println("\n**** Menu ****");
                System.out.println("4. Update Your Address");
                System.out.println("5. Exit");
                //Section for invalid guest
            } if (role.equalsIgnoreCase("Guest")) {
                System.out.println("\n**** Menu ****");
                System.out.println("5. Exit");
            } else {
                System.out.println("*********************");
            }
            //Prompts the user to choose an option that will be scanned into the system.
            System.out.print("Please choose an option:");
            String choice = scanner.nextLine();
            //Using a switch expression function to be evaluated as cases.
            switch (choice) {
                case "1": //Option to add a Patron
                    addPatron(patrons, scanner);
                    break; //Method for breaking onto the next case
                case "2":
                    removePatron(patrons, scanner);
                    break;//Option to remove
                case "3":
                    displayPatrons(patrons);
                    break;
                case "4": //Only used if the user is a Patron to update their address.
                    if (role.equalsIgnoreCase("Patron")) {
                        updateAddress(patrons, scanner, role);
                    } else {
                        System.out.println("System logged out");
                        return;
                    }
                    break;
                case "5": //Option to exit the options and successfully log out.
                    if (role.equalsIgnoreCase("Patron" ) || role.equalsIgnoreCase("Guest")) {
                        System.out.println("System logged out");
                        return;
                    }
                case "6": //Option to exit the options and successfully log out.
                    if (role.equalsIgnoreCase("User" )){
                        System.out.println("System logged out");
                        return;
                        }
                default: //Message give if the user does not choose a valid option.
                    System.out.println("**Option is required to continue**");
            }
        }
    }
}
