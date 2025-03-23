import java.util.*;

class CustomerFlightInfo implements DisplayClass {
    private final List<Flight> flightsRegistered;
    private final List<Integer> ticketsBooked;

    CustomerFlightInfo() {
        this.flightsRegistered = new ArrayList<>();
        this.ticketsBooked = new ArrayList<>();
    }

    void addNewFlight(Flight flight) {
        flightsRegistered.add(flight);
        ticketsBooked.add(1);
    }

    void updateExistingFlightTickets(int index, int additionalTickets) {
        int updatedTickets = ticketsBooked.get(index) + additionalTickets;
        ticketsBooked.set(index, updatedTickets);
    }

    public List<Flight> getFlightsRegistered() {
        return Collections.unmodifiableList(flightsRegistered);
    }

    public List<Integer> getTicketsBooked() {
        return Collections.unmodifiableList(ticketsBooked);
    }

    @Override
    public void displayRegisteredUsersForAllFlight() {
        // Implementation for displaying all flights.
    }

    @Override
    public void displayRegisteredUsersForASpecificFlight(String flightNum) {
        // Implementation for displaying users for a specific flight.
    }

    @Override
    public void displayHeaderForUsers(Flight flight, List<Customer> c) {
        // Implementation for displaying headers.
    }

    @Override
    public void displayFlightsRegisteredByOneUser(String userID) {
        // Implementation for displaying flights registered by a single user.
    }
}
