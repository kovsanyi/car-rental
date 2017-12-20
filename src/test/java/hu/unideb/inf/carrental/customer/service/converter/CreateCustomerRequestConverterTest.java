package hu.unideb.inf.carrental.customer.service.converter;

import hu.unideb.inf.carrental.commons.domain.customer.Customer;
import hu.unideb.inf.carrental.customer.resource.model.CreateCustomerRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CreateCustomerRequestConverterTest {
    @Autowired
    private CreateCustomerRequestConverter createCustomerRequestConverter;

    @Test
    public void from() throws Exception {
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest("customer", "user", "customer@mail.com", "Customer User", "123", 123, "Debrecen", "Kassai");
        Customer customer = createCustomerRequestConverter.from(createCustomerRequest);
        assert customer.getUser().getId() == null;
        assert customer.getUser().getUsername().equals(createCustomerRequest.getUserUsername());
        assert customer.getUser().getEmail().equals(createCustomerRequest.getUserEmail());
        assert customer.getId() == null;
        assert customer.getFullName().equals(createCustomerRequest.getFullName());
        assert customer.getPhoneNumber().equals(createCustomerRequest.getPhoneNumber());
        assert customer.getZipCode().equals(createCustomerRequest.getZipCode());
        assert customer.getCity().equals(createCustomerRequest.getCity());
        assert customer.getAddress().equals(createCustomerRequest.getAddress());
    }

}