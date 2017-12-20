package hu.unideb.inf.carrental.company.service.converter;

import hu.unideb.inf.carrental.commons.domain.company.Company;
import hu.unideb.inf.carrental.company.resource.model.CreateCompanyRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CreateCompanyRequestConverterTest {
    @Autowired
    private CreateCompanyRequestConverter createCompanyRequestConverter;

    @Test
    public void from() throws Exception {
        CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest("user", "user", "user@mail.com", "Company", "company@mail.com", "123", "Company Owner", 123, "Debrecen", "Kassai");
        Company company = createCompanyRequestConverter.from(createCompanyRequest);
        assert company.getUser().getId() == null;
        assert company.getUser().getUsername().equals(createCompanyRequest.getUserUsername());
        assert company.getUser().getEmail().equals(createCompanyRequest.getUserEmail());
        assert company.getId() == null;
        assert company.getName().equals(createCompanyRequest.getName());
        assert company.getEmail().equals(createCompanyRequest.getEmail());
        assert company.getPhoneNumber().equals(createCompanyRequest.getPhoneNumber());
        assert company.getFullName().equals(createCompanyRequest.getFullName());
        assert company.getZipCode().equals(createCompanyRequest.getZipCode());
        assert company.getCity().equals(createCompanyRequest.getCity());
        assert company.getAddress().equals(createCompanyRequest.getAddress());
    }
}
