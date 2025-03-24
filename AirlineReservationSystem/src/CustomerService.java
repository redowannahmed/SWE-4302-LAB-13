import java.util.ArrayList;
import java.util.List;

public class CustomerService {
    private static final List<Customer> customersCollection = new ArrayList<>();

    public void addNewCustomer(Customer customer) {
        customersCollection.add(customer);
        System.out.println("New customer added successfully!");
    }

    public void displayCustomers() {
        for (Customer customer : customersCollection) {
            System.out.println(customer);
        }
    }

    public void searchCustomer(String customerID) {
        for (Customer customer : customersCollection) {
            if (customer.getUserID().equals(customerID)) {
                System.out.println("Customer found: " + customer);
                return;
            }
        }
        System.out.println("Customer not found!");
    }

    public void updateCustomer(String customerID) {
        for (Customer customer : customersCollection) {
            if (customer.getUserID().equals(customerID)) {
                System.out.print("Enter new details: ");
                customer.setName(System.console().readLine());
                System.out.println("Customer updated successfully!");
                return;
            }
        }
        System.out.println("Customer not found!");
    }

    public void deleteCustomer(String customerID) {
        customersCollection.removeIf(customer -> customer.getUserID().equals(customerID));
        System.out.println("Customer deleted successfully!");
    }

    // ✅ Added getCustomersCollection() method
    public static List<Customer> getCustomersCollection() {
        return customersCollection;
    }
}
