//Fatima Harrison

//Inserting imports
import java.io.*;
import java.util.*;
//Declaring main class for Library Management Systems
public class LMS {
    //Class variables to be used throughout the programming
    static class Patron {
        public static String txt;
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
    //A function to load data from the given data file to the assigned role.
    public static List<Patron> loadData(String PatronFile, String role) {
        List<Patron> patrons = new ArrayList<>();

        // Role-based access control determine if the user role is false. 
        if (!"User".equalsIgnoreCase(role)) {
            System.out.println("Access denied: User only.");
            return patrons;
        }

        File file = new File(PatronFile);
        if (!file.exists()) {
            System.out.println("File has not been found: " + Patron.txt);
            return patrons;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.strip().split(",");
                if (parts.length == 5) {
                    try {
                        double overdue = Double.parseDouble(parts[4]);
                        patrons.add(new Patron(parts[0], parts[1], parts[2], parts[3], overdue));
                    } catch (NumberFormatException e) {
                        System.out.println("⚠️ Skipping invalid overdue amount: " + parts[4]);
                    }
                } else {
                    System.out.println("⚠️ Skipping malformed line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to read file: " + e.getMessage());
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

    public static boolean isValidId(String id, List<Patron> patrons) {
        if (id.length() != 7 || !id.matches("\\d{7}")) return false;
        for (Patron p : patrons) {
            if (p.id.equals(id)) return false;
        }
        return true;
    }

    public static double getValidOverdue(Scanner scanner) {
        while (true) {
            System.out.print("Enter overdue amount ($0 - $250): ");
            try {
                double amount = Double.parseDouble(scanner.nextLine());
                if (amount >= 0 && amount <= 250) {
                    return amount;
                } else {
                    System.out.println("Amount must be between $0 and $250.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
        //Method function to create the ID for each added patron.
    public static void addPatron(List<Patron> patrons, Scanner scanner) {
        String id;
        //System functions to prompt and store data.
        while (true) {
            System.out.print("Enter 7-digit unique Patron ID: ");
            id = scanner.nextLine();
            if (isValidId(id, patrons)) break;
            System.out.println("Invalid or duplicate ID.");
        }
        //Prompting the user to enter the patron's details
        System.out.print("Enter Patron Name: ");
        String name = scanner.nextLine();
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

    public static void removePatron(List<Patron> patrons, Scanner scanner) {
        System.out.print("Enter Patron ID to remove: ");
        String removeId = scanner.nextLine();
        boolean removed = patrons.removeIf(p -> p.id.equals(removeId));
        if (removed) {
            System.out.println(" Patron removed successfully.");
        } else {
            System.out.println("Patron ID not found.");
        }
        displayPatrons(patrons);
    }

    public static void updateAddress(List<Patron> patrons, Scanner scanner, String role) {
        if (!role.equalsIgnoreCase("User")) {
            System.out.println("Access Denied🚫");
            return;
        }

        System.out.print("Enter Patron Name and ID to update address: ");
        String name = scanner.nextLine();
        String id = scanner.nextLine();
        for (Patron p : patrons) {
            if (p.name.equals(name) || p.id.equals(id)) {
                System.out.println("Current Address: " + p.address);
                System.out.print("New address: ");
                String newAddress = scanner.nextLine();

                if (newAddress.isEmpty()) {
                    System.out.println("Address cannot be empty");
                    return;
                }

                p.address = newAddress;
                System.out.println("Address updated successfully ✅");
                return;
            }
        }
        System.out.println("Patron not found.");
    }
    //Allows the user to login in with username.
    public static String login(Scanner scanner) {
        System.out.print("Enter your role (User/Patron): ");//Prompts user to enter their role.
        String role = scanner.nextLine().trim();//Scans into the system
        // If the person inputs a invalid entry, The system will redirect to guest mode.
        if (!role.equalsIgnoreCase("User") && !role.equalsIgnoreCase("Patron")) {
            System.out.println(" Access Denied.");
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
        String filePath = scanner.nextLine();
        List<Patron> patrons = loadData(filePath);
        //Prompting only the user to enter the data file path.
        if (role.equalsIgnoreCase("User")) {
            System.out.print("📂 Enter the path to the patrons data file: ");
            filePath = scanner.nextLine();
        } else {
            //For Patron or Guest, use a default path or skip loading
            filePath = "Denied.txt"; // Random file path
            System.out.println("\n Redirecting");
        }
        patrons = loadData(filePath);
        displayPatrons(patrons);

        //A while loop to give each role a functional option.
        while (true) {
            //Section for admin user's only
            if (role.equalsIgnoreCase("User")) {
                System.out.println("\n**** Menu ****");
                System.out.println("1. Add New Patron");
                System.out.println("2. Remove Patron");
                System.out.println("3. Print All Patrons");
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
                System.out.println("Exit");
            }
            //Prompts the user to choose an option that will be scanned into the system.
            System.out.print("Please choose an option**");
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
                default: //Message give if the user does not choose a valid option.
                    System.out.println("**Please select an option**");
            }
        }
    }
}
