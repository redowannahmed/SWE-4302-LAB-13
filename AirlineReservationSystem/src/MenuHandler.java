import java.util.Scanner;

public class MenuHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService = new UserService();
    private final CustomerService customerService = new CustomerService();
    private final FlightService flightService = new FlightService();
    private final RolesAndPermissions roles = new RolesAndPermissions();
    private final FlightReservation reservation = new FlightReservation();
    private final Flight flight = new Flight();

    public void displayMainMenu() {
        int option;
        do {
            System.out.println("\nMENU:");
            System.out.println("1. Login as Admin");
            System.out.println("2. Register as Admin");
            System.out.println("3. Login as Passenger");
            System.out.println("4. Register as Passenger");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            option = scanner.nextInt();
            scanner.nextLine(); 

            String username = scanner.nextLine();
            String password = scanner.nextLine();

            switch (option) {
                case 1 -> handleAdminLogin(username, password);
                case 2 -> userService.registerAdmin();
                case 3 -> handlePassengerLogin();
                case 4 -> customerService.addNewCustomer(new Customer());
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid option. Try again.");
            }
        } while (option != 0);
    }

    private void handleAdminLogin(String username, String password) {
        int privilegeLevel = userService.authenticateAdmin(username, password);
        if (privilegeLevel == -1) {
            System.out.println("Error! Invalid credentials.");
        } else if (privilegeLevel == 0) {
            System.out.println("Logged in as Standard User.");
            customerService.displayCustomers();
        } else {
            System.out.println("Logged in as Admin.");
            displayAdminMenu();
        }
    }

    private void displayAdminMenu() { 
        int choice;
        do {
            System.out.println("\nADMIN MENU:");
            System.out.println("1. Add Customer");
            System.out.println("2. Search Customer");
            System.out.println("3. Update Customer");
            System.out.println("4. Delete Customer");
            System.out.println("5. View Customers");
            System.out.println("6. Book Flight");
            System.out.println("7. Cancel Flight");
            System.out.println("8. Delete Flight");
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1 -> customerService.addNewCustomer(new Customer());
                case 2 -> {
                    System.out.print("Enter Customer ID to search: ");
                    customerService.searchCustomer(scanner.nextLine());
                }
                case 3 -> {
                    System.out.print("Enter Customer ID to update: ");
                    customerService.updateCustomer(scanner.nextLine());
                }
                case 4 -> {
                    System.out.print("Enter Customer ID to delete: ");
                    customerService.deleteCustomer(scanner.nextLine());
                }
                case 5 -> customerService.displayCustomers();
                case 6 -> {
                    System.out.print("Enter Customer ID to book a flight: ");
                    flightService.bookFlight(reservation, scanner.nextLine());
                }
                case 7 -> {
                    System.out.print("Enter Customer ID to cancel a flight: ");
                    flightService.cancelFlight(reservation, scanner.nextLine());
                }
                case 8 -> flightService.deleteFlight(flight);
                case 0 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 0);
    }

    private void handlePassengerLogin() {
        System.out.print("\nEnter Email to Login: ");
        String email = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        String[] result = roles.isPassengerRegistered(email, password).split("-");
        if (Integer.parseInt(result[0]) == 1) {
            System.out.printf("\nLogged in Successfully as \"%s\".\n", email);
            displayPassengerMenu(result[1]);
        } else {
            System.out.println("Error! Invalid credentials.");
        }
    }

    private void displayPassengerMenu(String userId) {
        int choice;
        do {
            System.out.println("\nPASSENGER MENU:");
            System.out.println("1. Book Flight");
            System.out.println("2. Update Personal Info");
            System.out.println("3. Delete Account");
            System.out.println("4. View Flight Schedule");
            System.out.println("5. Cancel Flight");
            System.out.println("6. View My Flights");
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1 -> flightService.bookFlight(reservation, userId);
                case 2 -> customerService.updateCustomer(userId);
                case 3 -> deletePassengerAccount(userId);
                case 4 -> flightService.displayFlights(flight);
                case 5 -> flightService.cancelFlight(reservation, userId);
                case 6 -> reservation.displayFlightsRegisteredByOneUser(userId);
                case 0 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 0);
    }

    private void deletePassengerAccount(String userId) {
        System.out.print("Are you sure you want to delete your account? (Y/N): ");
        char confirmation = scanner.nextLine().charAt(0);
        if (confirmation == 'Y' || confirmation == 'y') {
            customerService.deleteCustomer(userId);
            System.out.println("Account deleted successfully.");
        } else {
            System.out.println("Account deletion cancelled.");
        }
    }
}
