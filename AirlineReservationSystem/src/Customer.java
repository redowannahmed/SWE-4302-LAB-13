import java.util.*;

public class Customer {

    // ************************************************************ Fields
    // ************************************************************
    private final String userID;
    private String email;
    private String name;
    private String phone;
    private final String password;
    private String address;
    private int age;
    public List<Flight> flightsRegisteredByUser;
    public List<Integer> numOfTicketsBookedByUser;
    public static final List<Customer> customerCollection = User.getCustomersCollection();

    // Magic literal for spacing in randomIDDisplay refactored into constant
    private static final int ID_SPACE_POSITION = 3;

    // ************************************************************
    // Behaviours/Methods
    // ************************************************************

    Customer() {
        this.userID = null;
        this.name = null;
        this.email = null;
        this.password = null;
        this.phone = null;
        this.address = null;
        this.age = 0;
    }

    /**
     * Registers new customer to the program. Obj of RandomGenerator(Composition) is
     * being used to assign unique userID to the newly created customer.
     *
     * @param name     name of the customer
     * @param email    customer's email
     * @param password customer's account password
     * @param phone    customer's phone-number
     * @param address  customer's address
     * @param age      customer's age
     */
    Customer(String name, String email, String password, String phone, String address, int age) {
        this.name = name;
        this.userID = generateUserID(); //// [Technique: Extract Method] Generate the unique userID using a dedicated method.
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.age = age;
        this.flightsRegisteredByUser = new ArrayList<>();
        this.numOfTicketsBookedByUser = new ArrayList<>();
    }

    private String generateUserID() {
        RandomGenerator random = new RandomGenerator();
        random.randomIDGen();
        return random.getRandomNumber(); // [Technique: Extract Method]
    }

    /**
     * Takes input for the new customer and adds them to programs memory.
     * isUniqueData() validates the entered email
     * and returns true if the entered email is already registered. If email is
     * already registered, program will ask the user
     * to enter new email address to get himself register.
     */
    public void addNewCustomer() {
        System.out.printf("\n\n\n%60s ++++++++++++++ Welcome to the Customer Registration Portal ++++++++++++++", "");
        Scanner read = new Scanner(System.in);
        
        // [Technique: Extract Method] Extract input prompting to a dedicated method.
        CustomerData data = inputCustomerData(read);
        customerCollection.add(new Customer(data.name, data.email, data.password, data.phone, data.address, data.age));
    }

    private CustomerData inputCustomerData(Scanner read) {
        String name, email, password, phone, address;
        int age;
        
        System.out.print("\nEnter your name :\t");
        name = read.nextLine();
        
        System.out.print("Enter your email address :\t");
        email = read.nextLine();
        while (isEmailAlreadyRegistered(email)) {
            System.out.println("ERROR!!! User with the same email already exists... Use new email or login using the previous credentials....");
            System.out.print("Enter your email address :\t");
            email = read.nextLine();
        }
        
        System.out.print("Enter your Password :\t");
        password = read.nextLine();
        
        System.out.print("Enter your Phone number :\t");
        phone = read.nextLine();
        
        System.out.print("Enter your address :\t");
        address = read.nextLine();
        
        System.out.print("Enter your age :\t");
        age = read.nextInt();
        return new CustomerData(name, email, password, phone, address, age); // [Technique: Introduce Parameter Object]
    }

    private static class CustomerData {
        String name;
        String email;
        String password;
        String phone;
        String address;
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

    /**
     * Returns String consisting of customers data(name, age, email etc...) for
     * displaying.
     * randomIDDisplay() adds space between the userID for easy readability.
     *
     * @param i for serial numbers.
     * @return customers data in String
     */
    private String toString(int i) {
        return String.format("%10s| %-10d | %-10s | %-32s | %-7s | %-27s | %-35s | %-23s |", "", i,
                randomIDDisplay(userID), name, age, email, address, phone);
    }

    /**
     * Searches for customer with the given ID and displays the customers' data if
     * found.
     *
     * @param ID of the searching/required customer
     */
    public void searchUser(String ID) {
        Customer foundCustomer = customerCollection.stream()
                .filter(c -> c.getUserID().equals(ID))
                .findFirst()
                .orElse(null);

        if (foundCustomer != null) { // [Replace Nested Conditional with Guard Clause]
            displayHeader();
            System.out.println(foundCustomer.formatForDisplay(1));
        } else {
            System.out.printf("No Customer with the ID %s Found.\n", ID);
        }
    }

    public boolean isEmailAlreadyRegistered(String emailID) { // [Rename Method]
        return customerCollection.stream().anyMatch(c -> c.getEmail().equals(emailID));
    }

    /**
     * Returns true if the given emailID is already registered, false otherwise
     *
     * @param emailID to be checked in the list
     */
    public boolean isUniqueData(String emailID) {
        boolean isUnique = false;
        for (Customer c : customerCollection) {
            if (emailID.equals(c.getEmail())) {
                isUnique = true;
                break;
            }
        }
        return isUnique;
    }

    public void editUserInfo(String ID) {
        Scanner read = new Scanner(System.in);
        Customer targetCustomer = customerCollection.stream()
                .filter(c -> c.getUserID().equals(ID))
                .findFirst()
                .orElse(null);

        if (targetCustomer != null) { // [Replace Nested Conditional with Guard Clause]
            updateCustomerInfo(targetCustomer, read); // [Extract Method]
            displayCustomersData(false);
        } else {
            System.out.printf("No Customer with the ID %s Found.\n", ID);
        }
    }

    private void updateCustomerInfo(Customer customer, Scanner read) { // [Extract Method]
        System.out.print("\nEnter the new name: ");
        customer.setName(read.nextLine());

        System.out.print("Enter the new email: ");
        customer.setEmail(read.nextLine());

        System.out.print("Enter the new phone: ");
        customer.setPhone(read.nextLine());

        System.out.print("Enter the new address: ");
        customer.setAddress(read.nextLine());

        System.out.print("Enter the new age: ");
        customer.setAge(read.nextInt());
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

    /**
     * Shows the customers' data in formatted way.
     * 
     * @param showHeader to check if we want to print ascii art for the customers'
     *                   data.
     */
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

    private String formatForDisplay(int serialNumber) {
        return String.format("%10s| %-10d | %-10s | %-32s | %-7d | %-27s | %-35s | %-23s |",
                "", serialNumber, randomIDDisplay(userID), name, age, email, address, phone);
    }

    /**
     * Shows the header for printing customers data
     */
    void displayHeader() {
        System.out.println();
        System.out.printf(
                "%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+\n",
                "");
        System.out.printf(
                "%10s| SerialNum  |   UserID   | Passenger Names                  | Age     | EmailID\t\t       | Home Address\t\t\t     | Phone Number\t       |%n",
                "");
        System.out.printf(
                "%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+\n",
                "");
        System.out.println();

    }

    /**
     * Adds space between userID to increase its readability
     * <p>
     * Example:
     * </p>
     * <b>"920 191" is much more readable than "920191"</b>
     *
     * @param randomID id to add space
     * @return randomID with added space
     */
    private String randomIDDisplay(String randomID) { // [Extract Method, Replace Magic Literal]
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


    /**
     * Associates a new flight with the specified customer
     *
     * @param f flight to associate
     */
    void addNewFlightToCustomerList(Flight f) {
        this.flightsRegisteredByUser.add(f);
        // numOfFlights++;
    }

    /**
     * Adds numOfTickets to already booked flights
     * 
     * @param index        at which flight is registered in the arraylist
     * @param numOfTickets how many tickets to add
     */
    void addExistingFlightToCustomerList(int index, int numOfTickets) {
        int newNumOfTickets = numOfTicketsBookedByUser.get(index) + numOfTickets;
        this.numOfTicketsBookedByUser.set(index, newNumOfTickets);
    }

    // ************************************************************ Setters &
    // Getters ************************************************************

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