package hu.unideb.inf.carrental.customer.service;

import hu.unideb.inf.carrental.commons.domain.user.enumeration.UserRole;
import hu.unideb.inf.carrental.commons.exception.CarInRentException;
import hu.unideb.inf.carrental.commons.exception.EmailAlreadyInUseException;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.UsernameAlreadyInUseException;
import hu.unideb.inf.carrental.customer.resource.model.CreateCustomerRequest;
import hu.unideb.inf.carrental.customer.resource.model.CustomerResponse;
import hu.unideb.inf.carrental.customer.resource.model.UpdateCustomerRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTestRun.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql")
})
public class CustomerServiceTest {
    @Test
    public void saveShouldBeSuccess() throws Exception {
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest("newCustomer".toLowerCase(), "password", "newcustomer@mail.com", "New Customer", "11111111111", 1111, "City", "Address");
        CustomerResponse customerResponse = new CustomerResponse(10L, "newCustomer".toLowerCase(), "newcustomer@mail.com", UserRole.ROLE_CUSTOMER.toString(), "New Customer", "11111111111", 1111, "City", "Address");
        customerService.save(createCustomerRequest);
        assert customerService.getById(5L).equals(customerResponse);
    }

    @Test(expected = UsernameAlreadyInUseException.class)
    public void saveShouldThrowUsernameAlreadyInUseException() throws Exception {
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest("customer".toLowerCase(), "password", "newcustomer@mail.com", "New Customer", "11111111111", 1111, "City", "Address");
        customerService.save(createCustomerRequest);
    }

    @Test(expected = EmailAlreadyInUseException.class)
    public void saveShouldThrowEmailAlreadyInUseException() throws Exception {
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest("newCustomer".toLowerCase(), "password", "customer@mail.com", "New Customer", "11111111111", 1111, "City", "Address");
        customerService.save(createCustomerRequest);
    }

    @Test
    public void updateShouldBeSuccess() throws Exception {
        setAuth("customer");
        UpdateCustomerRequest updateCustomerRequest = new UpdateCustomerRequest("Updated Customer", "11111111112", 1112, "City1", "Address1");
        CustomerResponse customerResponse = new CustomerResponse(4L, "customer", "customer@mail.com", UserRole.ROLE_CUSTOMER.toString(), "Updated Customer", "11111111112", 1112, "City1", "Address1");
        customerService.update(updateCustomerRequest);
        assert customerService.getById(1L).equals(customerResponse);
    }

    @Test
    public void deleteShouldBeSuccess() throws Exception {
        setAuth("customer");
        customerService.delete();
        setAuth("customerClosedReservation");
        customerService.delete();
    }

    @Test(expected = BadCredentialsException.class)
    public void deleteShouldThrowBadCredentialsException() throws Exception {
        setAuth("customer");
        customerService.delete();
        setAuth("customer");
    }

    @Test(expected = CarInRentException.class)
    public void deleteShouldThrowCarInRentException() throws Exception {
        setAuth("customerActiveReservation");
        customerService.delete();
    }

    @Test
    public void getShouldBeSuccess() {
        setAuth("customer");
        assert customerService.get().getFullName().equals("Customer User");
        setAuth("customerActiveReservation");
        assert customerService.get().getFullName().equals("Customer Active Reservation");
        setAuth("customerClosedReservation");
        assert customerService.get().getFullName().equals("Customer Closed Reservation");
        setAuth("customerReservation");
        assert customerService.get().getFullName().equals("Customer Reservation");
    }

    @Test
    public void getByIdShouldBeSuccess() throws Exception {
        assert customerService.getById(1L).getFullName().equals("Customer User");
        assert customerService.getById(2L).getFullName().equals("Customer Active Reservation");
        assert customerService.getById(3L).getFullName().equals("Customer Closed Reservation");
        assert customerService.getById(4L).getFullName().equals("Customer Reservation");
    }

    @Test(expected = NotFoundException.class)
    public void getByIdShouldThrowNotFoundException() throws Exception {
        customerService.getById(100L);
    }

    @Test
    public void getByUserIdShouldBeSuccess() throws Exception {
        assert customerService.getByUserId(4L).getFullName().equals("Customer User");
        assert customerService.getByUserId(5L).getFullName().equals("Customer Active Reservation");
        assert customerService.getByUserId(6L).getFullName().equals("Customer Closed Reservation");
        assert customerService.getByUserId(7L).getFullName().equals("Customer Reservation");
    }

    @Test(expected = NotFoundException.class)
    public void getByUserIdShouldThrowNotFoundException() throws Exception {
        customerService.getByUserId(1L);
    }

    @Test
    public void getByUserNameShouldBeSuccess() throws Exception {
        assert customerService.getByUsername("customer").getFullName().equals("Customer User");
        assert customerService.getByUsername("customerActiveReservation").getFullName().equals("Customer Active Reservation");
        assert customerService.getByUsername("customerClosedReservation").getFullName().equals("Customer Closed Reservation");
        assert customerService.getByUsername("customerReservation").getFullName().equals("Customer Reservation");
    }

    @Test(expected = NotFoundException.class)
    public void getByUserNameShouldThrowNotFoundException() throws Exception {
        customerService.getByUsername("company");
    }

    private void setAuth(String username) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username.toLowerCase(), "password");
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AuthenticationManager authenticationManager;
}