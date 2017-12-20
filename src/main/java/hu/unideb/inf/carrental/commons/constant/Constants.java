package hu.unideb.inf.carrental.commons.constant;

public final class Constants {
    //Path
    public final static String PATH_USER = "/api/user";
    public final static String PATH_MANAGER = "/api/manager";
    public final static String PATH_CUSTOMER = "/api/customer";
    public final static String PATH_COMPANY = "/api/company";

    //User
    public final static String USER_NOT_FOUND = "User not found";
    public final static String USERNAME_ALREADY_EXISTS = "Username already in use!";
    public final static String EMAIL_ALREADY_EXISTS = "Email already in use!";
    public final static String ERROR_ROLE_NOT_SET = "Role not set for new user!";

    //Customer
    public final static String CUSTOMER_NOT_FOUND = "Customer not found";

    //Manager
    public final static String MANAGER_NOT_FOUND = "Manager not found";

    //Company
    public final static String COMPANY_NOT_FOUND = "Company not found";
    public final static String COMPANY_HAS_SITES = "To delete a company remove all sites";
    public final static String INVALID_COMPANY_ID = "Company ID is invalid";
    public final static String COMPANY_NAME_ALREADY_EXISTS = "Company name already in use";
    public final static String COMPANY_EMAIL_ALREADY_EXISTS = "Company email already in use";

    //Site
    public final static String SITE_NOT_FOUND = "Site not found";
    public final static String INVALID_SITE_ID = "Site ID is invalid";
    public final static String SITE_HAS_CARS = "To delete a site remove all cars";
    public final static String MANAGER_ALREADY_SITE_MANAGER = "Manager already a site manager";
    public final static String NO_MANAGED_SITE = "No managed site";

    //Car
    public final static String CAR_NOT_FOUND = "Car not found";
    public final static String CAR_STILL_IN_RENT = "Car is still under renting";
    public final static String CAR_ALREADY_IN_RENT = "Car is already under renting";
    public final static int CAR_MAX_PRICE = 100000;

    //Car image
    public final static String CAR_IMAGE_NOT_FOUND = "Car image not found";

    //Reservation
    public final static String RESERVATION_NOT_FOUND = "Reservation not found";
    public final static String NO_ACTIVE_RESERVATION = "No active reservation";
    public final static String CUSTOMER_HAS_RESERVATION = "Customer already has a reservation";

    //Commons
    public final static String INVALID_DATE = "Date is invalid";
    public final static String NO_RIGHTS = "No rights to the operation";
}
