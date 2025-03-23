class FlightDetails {
    private final String flightNumber;
    private final int numOfSeats;
    private final String fromCity;
    private final String toCity;
    private final double distanceInMiles;
    private final double distanceInKm;

    FlightDetails(String flightNumber, int seats, String[][] cities, String[] distances) {
        this.flightNumber = flightNumber;
        this.numOfSeats = seats;
        this.fromCity = cities[0][0];
        this.toCity = cities[1][0];
        this.distanceInMiles = Double.parseDouble(distances[0]);
        this.distanceInKm = Double.parseDouble(distances[1]);
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public int getNumOfSeats() {
        return numOfSeats;
    }

    public String getDestination() {
        return fromCity + " -> " + toCity;
    }

    public double getDistanceInMiles() {
        return distanceInMiles;
    }
}
