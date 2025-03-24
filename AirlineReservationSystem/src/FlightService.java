import java.util.Scanner;

public class FlightService {
    private final Scanner scanner = new Scanner(System.in);

    public void bookFlight(FlightReservation reservation, String userID) {
        System.out.print("Enter flight number: ");
        String flightNumber = scanner.nextLine();
        System.out.print("Enter number of tickets: ");
        int numTickets = scanner.nextInt();
        scanner.nextLine(); 
        if (numTickets > 10) {
            System.out.println("Error! Cannot book more than 10 tickets.");
            return;
        }

        reservation.bookFlight(flightNumber, numTickets, userID);
    }

    public void cancelFlight(FlightReservation reservation, String userID) {
        reservation.cancelFlight(userID);
    }

    public void displayFlights(Flight flight) {
        flight.displayFlightSchedule();
    }

    public void deleteFlight(Flight flight) {
        System.out.print("Enter flight number to delete: ");
        String flightNum = scanner.nextLine();
        flight.deleteFlight(flightNum);
    }
}
