import java.util.*;

class CustomerFlightInfo {
    private List<Flight> flightsRegistered;
    private List<Integer> ticketsBooked;

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
}
