public class RolesAndPermissions {
    private final UserService userService = new UserService();

    /**
     * Checks if the admin with specified credentials is registered.
     * @param username of the admin
     * @param password of the admin
     * @return -1 if admin not found, else 1
     */
    public int isPrivilegedUserOrNot(String username, String password) {
        return userService.isAdminRegistered(username, password) ? 1 : -1;
    }

    /**
     * Checks if the passenger with specified credentials is registered.
     * @param email of the specified passenger
     * @param password of the specified passenger
     * @return "1-userID" if the passenger is registered, else "0"
     */
    public String isPassengerRegistered(String email, String password) {
        for (Customer c : CustomerService.getCustomersCollection()) {
            if (email.equals(c.getEmail()) && password.equals(c.getPassword())) {
                return "1-" + c.getUserID();
            }
        }
        return "0";
    }
}
