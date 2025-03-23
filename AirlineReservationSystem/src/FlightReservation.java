import java.util.List;
import java.util.Scanner;
import java.util.Iterator;

public class FlightReservation implements DisplayClass {

    // Field: Using a Flight instance to access the flight list.
    private Flight flight = new Flight();

    // ========================
    // Public Booking Methods
    // ========================

    /**
     * Books a specified number of tickets for a flight.
     * 
     * Refactoring:
     * - Extracted helper methods for finding flight and customer.
     * - Extracted ticket update operations.
     * - Simplified nested loops with early returns.
     *
     * @param flightNo     Flight ID of the flight to be booked.
     * @param numOfTickets Number of tickets to be booked.
     * @param userID       User ID of the customer booking the flight.
     */
    public void bookFlight(String flightNo, int numOfTickets, String userID) {
        Flight foundFlight = findFlightByNumber(flightNo);
        if (foundFlight == null) {
            System.out.println("Invalid Flight Number...! No flight with the ID \"" + flightNo + "\" was found...");
            return;
        }
        
        Customer customer = findCustomerByUserId(userID);
        if (customer == null) {
            System.out.println("Invalid User ID...! No user with the ID \"" + userID + "\" was found...");
            return;
        }
        
        // Update available seats in the flight.
        foundFlight.setNoOfSeatsInTheFlight(foundFlight.getNoOfSeats() - numOfTickets);

        // If customer is not already registered in flight, add them.
        if (!foundFlight.isCustomerAlreadyAdded(foundFlight.getListOfRegisteredCustomersInAFlight(), customer)) {
            foundFlight.addNewCustomerToFlight(customer);
        }

        // Check if flight is already in the customer's registered flights list.
        int customerFlightIndex = getFlightIndex(customer.getFlightsRegisteredByUser(), foundFlight);
        if (customerFlightIndex != -1) {
            addNumberOfTicketsToAlreadyBookedFlight(customer, numOfTickets, customerFlightIndex);
            customer.addExistingFlightToCustomerList(customerFlightIndex, numOfTickets);
        } else {
            customer.addNewFlightToCustomerList(foundFlight);
            addNumberOfTicketsForNewFlight(customer, numOfTickets);
        }
        
        System.out.printf("\n %50s You've booked %d tickets for Flight \"%5s\"...", "", numOfTickets, flightNo.toUpperCase());
    }
    
    /**
     * Cancels a flight booking for a given user.
     * 
     * Refactoring:
     * - Extracted customer lookup using findCustomerByUserId.
     * - Simplified iteration over customer's registered flights.
     *
     * @param userID ID of the user canceling the flight.
     */
    public void cancelFlight(String userID) {
        Customer customer = findCustomerByUserId(userID);
        if (customer == null) {
            System.out.println("Invalid User ID. No customer found with ID \"" + userID + "\".");
            return;
        }
        
        if (customer.getFlightsRegisteredByUser().isEmpty()) {
            System.out.println("No Flight has been registered by you.");
            return;
        }
        
        // Display the flights registered by the customer.
        System.out.printf("%50s %s Here is the list of all the Flights registered by you %s\n", " ", "++++++++++++++", "++++++++++++++");
        displayFlightsRegisteredByOneUser(userID);
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Flight Number of the Flight you want to cancel: ");
        String flightNum = scanner.nextLine();
        System.out.print("Enter the number of tickets to cancel: ");
        int numOfTickets = scanner.nextInt();
        
        boolean flightFound = false;
        int index = 0;
        Iterator<Flight> flightIterator = customer.getFlightsRegisteredByUser().iterator();
        while (flightIterator.hasNext()) {
            Flight registeredFlight = flightIterator.next();
            if (flightNum.equalsIgnoreCase(registeredFlight.getFlightNumber())) {
                flightFound = true;
                int numOfTicketsForFlight = customer.getNumOfTicketsBookedByUser().get(index);
                // Validate that the number of tickets to cancel is not more than booked.
                while (numOfTickets > numOfTicketsForFlight) {
                    System.out.print("ERROR!!! Number of tickets cannot be greater than " + numOfTicketsForFlight + " for this flight. Please enter the number of tickets again: ");
                    numOfTickets = scanner.nextInt();
                }
                int ticketsToBeReturned;
                if (numOfTicketsForFlight == numOfTickets) {
                    ticketsToBeReturned = registeredFlight.getNoOfSeats() + numOfTicketsForFlight;
                    customer.getNumOfTicketsBookedByUser().remove(index);
                    flightIterator.remove();
                } else {
                    ticketsToBeReturned = registeredFlight.getNoOfSeats() + numOfTickets;
                    customer.getNumOfTicketsBookedByUser().set(index, (numOfTicketsForFlight - numOfTickets));
                }
                registeredFlight.setNoOfSeatsInTheFlight(ticketsToBeReturned);
                break;
            }
            index++;
        }
        
        if (!flightFound) {
            System.out.println("ERROR!!! Couldn't find Flight with ID \"" + flightNum.toUpperCase() + "\".....");
        }
    }
    
    // ====================================
    // Helper Methods for Booking/Cancellation
    // ====================================

    /**
     * Finds a flight by its flight number from the flight list.
     *
     * @param flightNo Flight ID to search for.
     * @return The Flight object if found; null otherwise.
     */
    private Flight findFlightByNumber(String flightNo) {
        for (Flight f : flight.getFlightList()) {
            if (flightNo.equalsIgnoreCase(f.getFlightNumber())) {
                return f;
            }
        }
        return null;
    }
    
    /**
     * Finds a customer by their user ID from the customer collection.
     *
     * @param userID User ID to search for.
     * @return The Customer object if found; null otherwise.
     */
    private Customer findCustomerByUserId(String userID) {
        for (Customer customer : Customer.customerCollection) {
            if (userID.equals(customer.getUserID())) {
                return customer;
            }
        }
        return null;
    }
    
    /**
     * Returns the index of a flight in a list of flights; returns -1 if not found.
     * This replaces the mutable flightIndexInFlightList field.
     *
     * @param flightList   List of flights.
     * @param targetFlight Flight to locate.
     * @return Index of targetFlight in flightList, or -1 if not found.
     */
    private int getFlightIndex(List<Flight> flightList, Flight targetFlight) {
        return flightList.indexOf(targetFlight);
    }
    
    /**
     * Updates the ticket count for an already booked flight.
     *
     * @param customer      The customer who booked the flight.
     * @param numOfTickets  Number of tickets to add.
     * @param flightIndex   Index of the flight in the customer's flight list.
     */
    private void addNumberOfTicketsToAlreadyBookedFlight(Customer customer, int numOfTickets, int flightIndex) {
        int newNumOfTickets = customer.getNumOfTicketsBookedByUser().get(flightIndex) + numOfTickets;
        customer.getNumOfTicketsBookedByUser().set(flightIndex, newNumOfTickets);
    }
    
    /**
     * Adds the number of tickets for a new flight booking.
     *
     * @param customer     The customer booking the flight.
     * @param numOfTickets Number of tickets booked.
     */
    private void addNumberOfTicketsForNewFlight(Customer customer, int numOfTickets) {
        customer.getNumOfTicketsBookedByUser().add(numOfTickets);
    }
    
    /**
     * Determines the current status of a flight.
     *
     * @param flightToCheck The flight to check.
     * @return "As Per Schedule" if the flight is found; "   Cancelled   " otherwise.
     */
    private String flightStatus(Flight flightToCheck) {
        boolean isFlightAvailable = false;
        for (Flight f : flight.getFlightList()) {
            if (f.getFlightNumber().equalsIgnoreCase(flightToCheck.getFlightNumber())) {
                isFlightAvailable = true;
                break;
            }
        }
        return isFlightAvailable ? "As Per Schedule" : "   Cancelled   ";
    }
    
    // ====================================
    // Formatting Methods for Display
    // ====================================
    
    /**
     * Formats flight and booking details for display (for a specific flight booked by a customer).
     * Renamed from the overloaded toString method for clarity.
     *
     * @param serialNum  Serial number of the flight in the list.
     * @param flightInfo Flight details.
     * @param customer   Customer booking the flight.
     * @return Formatted string containing flight details.
     */
    public String formatFlightInfo(int serialNum, Flight flightInfo, Customer customer) {
        return String.format("| %-5d| %-41s | %-9s | \t%-9d | %-21s | %-22s | %-10s  |   %-6sHrs |  %-4s  | %-10s |", 
                serialNum, 
                flightInfo.getFlightSchedule(), 
                flightInfo.getFlightNumber(), 
                customer.getNumOfTicketsBookedByUser().get(serialNum - 1), 
                flightInfo.getFromWhichCity(), 
                flightInfo.getToWhichCity(), 
                flightInfo.fetchArrivalTime(), 
                flightInfo.getFlightTime(), 
                flightInfo.getGate(), 
                flightStatus(flightInfo));
    }
    
    /**
     * Formats customer details for display (for customers registered in a flight).
     * Renamed from the overloaded toString method for clarity.
     *
     * @param serialNum Serial number for the display.
     * @param customer  Customer details.
     * @param index     Index for the booked tickets in the customer's list.
     * @return Formatted string containing customer details.
     */
    public String formatCustomerInfo(int serialNum, Customer customer, int index) {
        return String.format("%10s| %-10d | %-10s | %-32s | %-7s | %-27s | %-35s | %-23s |       %-7s  |", 
                "", 
                (serialNum + 1), 
                customer.randomIDDisplay(customer.getUserID()), 
                customer.getName(),
                customer.getAge(), 
                customer.getEmail(), 
                customer.getAddress(), 
                customer.getPhone(), 
                customer.getNumOfTicketsBookedByUser().get(index));
    }
    
    // ====================================
    // Display Methods (implements DisplayClass)
    // ====================================
    
    @Override
    public void displayFlightsRegisteredByOneUser(String userID) {
        System.out.println();
        System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+---------------------------+-------------+--------+-----------------+\n");
        System.out.printf("| Num  | FLIGHT SCHEDULE\t\t\t   | FLIGHT NO |  Booked Tickets  | \tFROM ====>>       | \t====>> TO\t   | \t    ARRIVAL TIME       | FLIGHT TIME |  GATE  |  FLIGHT STATUS  |%n");
        System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+---------------------------+-------------+--------+-----------------+\n");
        
        for (Customer customer : Customer.customerCollection) {
            if (userID.equals(customer.getUserID())) {
                List<Flight> flights = customer.getFlightsRegisteredByUser();
                int size = flights.size();
                for (int i = 0; i < size; i++) {
                    System.out.println(formatFlightInfo((i + 1), flights.get(i), customer));
                    System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+---------------------------+-------------+--------+-----------------+\n");
                }
            }
        }
    }
    
    @Override
    public void displayHeaderForUsers(Flight flightObj, List<Customer> customers) {
        System.out.printf("\n%65s Displaying Registered Customers for Flight No. \"%-6s\" %s \n\n", "+++++++++++++", flightObj.getFlightNumber(), "+++++++++++++");
        System.out.printf("%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+----------------+\n", "");
        System.out.printf("%10s| SerialNum  |   UserID   | Passenger Names                  | Age     | EmailID\t\t       | Home Address\t\t\t     | Phone Number\t       | Booked Tickets |%n", "");
        System.out.printf("%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+----------------+\n", "");
        int size = flightObj.getListOfRegisteredCustomersInAFlight().size();
        for (int i = 0; i < size; i++) {
            // Get the customer's flight index for this flight.
            int custFlightIndex = getFlightIndex(customers.get(i).getFlightsRegisteredByUser(), flightObj);
            System.out.println(formatCustomerInfo(i, customers.get(i), custFlightIndex));
            System.out.printf("%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+----------------+\n", "");
        }
    }
    
    @Override
    public void displayRegisteredUsersForAllFlight() {
        System.out.println();
        for (Flight flightObj : flight.getFlightList()) {
            List<Customer> customers = flightObj.getListOfRegisteredCustomersInAFlight();
            if (!customers.isEmpty()) {
                displayHeaderForUsers(flightObj, customers);
            }
        }
    }
    
    @Override
    public void displayRegisteredUsersForASpecificFlight(String flightNum) {
        System.out.println();
        for (Flight flightObj : flight.getFlightList()) {
            if (flightObj.getFlightNumber().equalsIgnoreCase(flightNum)) {
                displayHeaderForUsers(flightObj, flightObj.getListOfRegisteredCustomersInAFlight());
            }
        }
    }
}