import java.util.*;

public class Customer {

    // ************************************************************
    // Fields – Encapsulated & Organized
    // [Encapsulate Collection, Self Encapsulate Field, Replace Magic Literal]
    // ************************************************************
    private final String userID;
    private String email;
    private String name;
    private String phone;
    private final String password;
    private String address;
    private int age;
    private final CustomerFlightInfo flightInfo; // [Extract Class]
    public List<Integer> numOfTicketsBookedByUser;
    public List<Flight> flightsRegisteredByUser;
    public static final List<Customer> customerCollection = User.getCustomersCollection();

    private static final int ID_SPACE_POSITION = 3; // [Replace Magic Literal]

    // ************************************************************
    // Constructors
    // ************************************************************

    Customer() {
        this(null, null, null, null, null, 0); // [Inline Method]
    }

    Customer(String name, String email, String password, String phone, String address, int age) {
        this.userID = generateUserID(); // [Extract Method]
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.age = age;
        this.flightInfo = new CustomerFlightInfo(); // [Extract Class]
    }

    private String generateUserID() {
        RandomGenerator random = new RandomGenerator();
        random.randomIDGen();
        return random.getRandomNumber(); // [Extract Method]
    }

    // ************************************************************
    // Customer Registration
    // ************************************************************

    public void addNewCustomer() {
        Scanner read = new Scanner(System.in);
        CustomerData data = inputCustomerData(read); // [Extract Method, Introduce Parameter Object]
        customerCollection.add(new Customer(data.name, data.email, data.password, data.phone, data.address, data.age));
    }

    private CustomerData inputCustomerData(Scanner read) {
        System.out.print("\nEnter your name: ");
        String name = read.nextLine();

        System.out.print("Enter your email: ");
        String email = read.nextLine();
        while (isEmailAlreadyRegistered(email)) { // [Rename Method]
            System.out.println("ERROR: Email already exists. Enter a new email.");
            email = read.nextLine();
        }

        System.out.print("Enter your password: ");
        String password = read.nextLine();

        System.out.print("Enter your phone number: ");
        String phone = read.nextLine();

        System.out.print("Enter your address: ");
        String address = read.nextLine();

        System.out.print("Enter your age: ");
        int age = read.nextInt();
        return new CustomerData(name, email, password, phone, address, age); // [Introduce Parameter Object]
    }

    private static class CustomerData {
        String name, email, password, phone, address;
        int age;

        CustomerData(String name, String email, String password, String phone, String address, int age) {
            this.name = name;
            this.email = email;
            this.password = password;
            this.phone = phone;
            this.address = address;
            this.age = age;
        }
    }

    // ************************************************************
    // Display Methods
    // ************************************************************

    public void displayCustomersData(boolean showHeader) {
        if (showHeader) {
            displayHeader();
        }
        int i = 0;
        for (Customer c : customerCollection) {
            i++;
            System.out.println(c.formatForDisplay(i)); // [Rename Method]
        }
    }

    public String toString(int flag) {
        // If flag == 1, return detailed information
        if (flag == 1) {
            return String.format("Customer ID: %s\nName: %s\nEmail: %s\nPhone: %s\nAddress: %s\nAge: %d",
                    userID, name, email, phone, address, age);
        }
        // Otherwise, return basic information
        return toString();  // Fallback to default toString
    }

    public void editUserInfo(String ID) {
        boolean isFound = false;
        Scanner read = new Scanner(System.in);
        for (Customer c : customerCollection) {
            if (ID.equals(c.getUserID())) {
                isFound = true;
                System.out.print("\nEnter the new name of the Passenger:\t");
                String name = read.nextLine();
                c.setName(name);
                System.out.print("Enter the new email address of Passenger " + name + ":\t");
                c.setEmail(read.nextLine());
                System.out.print("Enter the new Phone number of Passenger " + name + ":\t");
                c.setPhone(read.nextLine());
                System.out.print("Enter the new address of Passenger " + name + ":\t");
                c.setAddress(read.nextLine());
                System.out.print("Enter the new age of Passenger " + name + ":\t");
                c.setAge(read.nextInt());
                displayCustomersData(false);
                break;
            }
        }
        if (!isFound) {
            System.out.printf("%-50sNo Customer with the ID %s Found...!!!\n", " ", ID);
        }
    }

    private String formatForDisplay(int serialNumber) {
        return String.format("%10s| %-10d | %-10s | %-32s | %-7d | %-27s | %-35s | %-23s |",
                "", serialNumber, randomIDDisplay(userID), name, age, email, address, phone);
    }

    private void displayHeader() { // [Extract Method]
        String separator = "+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+";
        System.out.println();
        System.out.printf("%10s%s\n", "", separator);
        System.out.printf("%10s| SerialNum  |   UserID   | Passenger Names                  | Age     | EmailID\t       | Home Address\t\t     | Phone Number\t       |\n", "");
        System.out.printf("%10s%s\n", "", separator);
        System.out.println();
    }

    public String randomIDDisplay(String randomID) { // [Extract Method, Replace Magic Literal]
        StringBuilder newString = new StringBuilder();
        for (int i = 0; i < randomID.length(); i++) {
            if (i == ID_SPACE_POSITION) {
                newString.append(" ").append(randomID.charAt(i));
            } else {
                newString.append(randomID.charAt(i));
            }
        }
        return newString.toString();
    }

    // ************************************************************
    // Flight Management (Delegated to Extracted Class)
    // ************************************************************

    public void addFlight(Flight f) {
        flightInfo.addNewFlight(f); // [Extract Class]
    }

    public void updateTickets(int index, int numOfTickets) {
        flightInfo.updateExistingFlightTickets(index, numOfTickets); // [Extract Class]
    }
    
    void addExistingFlightToCustomerList(int index, int numOfTickets) {
        int newNumOfTickets = numOfTicketsBookedByUser.get(index) + numOfTickets;
        this.numOfTicketsBookedByUser.set(index, newNumOfTickets);
    }

    void addNewFlightToCustomerList(Flight f) {
        this.flightsRegisteredByUser.add(f);
        // numOfFlights++;
    }


    public boolean isEmailAlreadyRegistered(String emailID) {
        boolean isUnique = false;
        for (Customer c : customerCollection) {
            if (emailID.equals(c.getEmail())) {
                isUnique = true;
                break;
            }
        }
        return isUnique;
    }

    public void deleteUser(String ID) {
        boolean isFound = false;
        Iterator<Customer> iterator = customerCollection.iterator();
        while (iterator.hasNext()) {
            Customer customer = iterator.next();
            if (ID.equals(customer.getUserID())) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            iterator.remove();
            System.out.printf("\n%-50sPrinting all  Customer's Data after deleting Customer with the ID %s.....!!!!\n",
                    "", ID);
            displayCustomersData(false);
        } else {
            System.out.printf("%-50sNo Customer with the ID %s Found...!!!\n", " ", ID);
        }
    }

    public void searchUser(String ID) {
        boolean isFound = false;
        Customer customerWithTheID = customerCollection.get(0);
        for (Customer c : customerCollection) {
            if (ID.equals(c.getUserID())) {
                System.out.printf("%-50sCustomer Found...!!!Here is the Full Record...!!!\n\n\n", " ");
                displayHeader();
                isFound = true;
                customerWithTheID = c;
                break;
            }
        }
        if (isFound) {
            System.out.println(customerWithTheID.toString(1));
            System.out.printf(
                    "%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+\n",
                    "");
        } else {
            System.out.printf("%-50sNo Customer with the ID %s Found...!!!\n", " ", ID);
        }
    }

    // Getters & Setters...

    public List<Flight> getFlightsRegisteredByUser() {
        return flightsRegisteredByUser;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getNumOfTicketsBookedByUser() {
        return numOfTicketsBookedByUser;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
