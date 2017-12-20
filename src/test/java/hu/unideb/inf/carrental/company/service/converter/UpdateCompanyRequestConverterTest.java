package hu.unideb.inf.carrental.company.service.converter;

import hu.unideb.inf.carrental.commons.domain.company.Company;
import hu.unideb.inf.carrental.company.resource.model.UpdateCompanyRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UpdateCompanyRequestConverterTest {
    @Autowired
    UpdateCompanyRequestConverter updateCompanyRequestConverter;

    @Test
    public void from() throws Exception {
        UpdateCompanyRequest createCompanyRequest = new UpdateCompanyRequest("Company", "company@mail.com", "123", "Company Owner", 123, "Debrecen", "Kassai");
        Company company = updateCompanyRequestConverter.from(createCompanyRequest);
        assert company.getUser() == null;
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
