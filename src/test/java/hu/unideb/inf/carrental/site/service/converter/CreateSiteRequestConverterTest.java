package hu.unideb.inf.carrental.site.service.converter;

import hu.unideb.inf.carrental.commons.domain.site.Site;
import hu.unideb.inf.carrental.site.resource.model.CreateSiteRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CreateSiteRequestConverterTest {
    @Autowired
    CreateSiteRequestConverter createSiteRequestConverter;

    @Test
    public void from() throws Exception {
        CreateSiteRequest createSiteRequest = new CreateSiteRequest("manager", "site@mail.com", "123", 123, "Debrecen", "Kassai", "0-24");
        Site site = createSiteRequestConverter.from(createSiteRequest);
        assert site.getId() == null;
        assert site.getCompany() == null;
        assert site.getManager().getUser().getUsername().equals(createSiteRequest.getManagerUserUsername());
        assert site.getEmail().equals(createSiteRequest.getEmail());
        assert site.getPhoneNumber().equals(createSiteRequest.getPhoneNumber());
        assert site.getZipCode().equals(createSiteRequest.getZipCode());
        assert site.getCity().equals(createSiteRequest.getCity());
        assert site.getAddress().equals(createSiteRequest.getAddress());
        assert site.getOpeningHours().equals(createSiteRequest.getOpeningHours());
    }
}
