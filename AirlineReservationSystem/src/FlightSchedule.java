import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

class FlightSchedule {
    private final String flightSchedule;
    private final String gate;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy, HH:mm a");

    FlightSchedule(String schedule, String gate) {
        this.flightSchedule = schedule;
        this.gate = gate;
    }

    public String getFlightSchedule() {
        return flightSchedule;
    }

    public String getGate() {
        return gate;
    }

    public static String createNewFlightTime() {
        Calendar c = Calendar.getInstance();
        
        int randomDays = (int)(Math.random() * 7);
        int randomHours = (int)(Math.random() * 24);

        c.add(Calendar.DATE, randomDays);
        c.add(Calendar.HOUR, randomHours);
        
        return LocalDateTime.ofInstant(c.getTime().toInstant(), ZoneId.systemDefault()).format(FORMATTER);
    }
}
