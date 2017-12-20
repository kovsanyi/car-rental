package hu.unideb.inf.carrental.customer.service.converter;

import hu.unideb.inf.carrental.commons.domain.customer.Customer;
import hu.unideb.inf.carrental.customer.resource.model.UpdateCustomerRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UpdateCustomerRequestConverterTest {
    @Autowired
    private UpdateCustomerRequestConverter updateCustomerRequestConverter;

    @Test
    public void from() throws Exception {
        UpdateCustomerRequest updateCustomerRequest = new UpdateCustomerRequest("Customer User", "123", 123, "Debrecen", "Kassai");
        Customer customer = updateCustomerRequestConverter.from(updateCustomerRequest);
        assert customer.getUser() == null;
        assert customer.getId() == null;
        assert customer.getFullName().equals(updateCustomerRequest.getFullName());
        assert customer.getPhoneNumber().equals(updateCustomerRequest.getPhoneNumber());
        assert customer.getZipCode().equals(updateCustomerRequest.getZipCode());
        assert customer.getCity().equals(updateCustomerRequest.getCity());
        assert customer.getAddress().equals(updateCustomerRequest.getAddress());
    }

}
