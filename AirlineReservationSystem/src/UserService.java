import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserService {
    private static final Map<String, String> adminCredentials = new HashMap<>();
    private final Scanner scanner = new Scanner(System.in);

    public UserService() {
        // Default admin credentials
        adminCredentials.put("root", "root");
    }

    public int authenticateAdmin(String username, String password) {
        return isAdminRegistered(username, password) ? 1 : -1;
    }

    public void registerAdmin() {
        System.out.print("\nEnter the UserName to Register: ");
        String username = scanner.nextLine();
        System.out.print("Enter the Password to Register: ");
        String password = scanner.nextLine();

        if (adminCredentials.containsKey(username)) {
            System.out.println("ERROR! Admin username already exists. Try again.");
            return;
        }

        adminCredentials.put(username, password);
        System.out.println("Admin registered successfully!");
    }

    public boolean isAdminRegistered(String username, String password) {
        return adminCredentials.containsKey(username) && adminCredentials.get(username).equals(password);
    }
}
