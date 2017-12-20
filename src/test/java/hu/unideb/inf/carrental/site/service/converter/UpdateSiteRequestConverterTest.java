package hu.unideb.inf.carrental.site.service.converter;

import hu.unideb.inf.carrental.commons.domain.site.Site;
import hu.unideb.inf.carrental.site.resource.model.UpdateSiteRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UpdateSiteRequestConverterTest {
    @Autowired
    private UpdateSiteRequestConverter updateSiteRequestConverter;

    @Test
    public void from() throws Exception {
        UpdateSiteRequest updateSiteRequest = new UpdateSiteRequest(1L, "site@mail.com", "123", 123, "Debrecen", "Kassai", "0-24");
        Site site = updateSiteRequestConverter.from(updateSiteRequest);
        assert site.getId().equals(updateSiteRequest.getId());
        assert site.getCompany() == null;
        assert site.getManager() == null;
        assert site.getEmail().equals(updateSiteRequest.getEmail());
        assert site.getPhoneNumber().equals(updateSiteRequest.getPhoneNumber());
        assert site.getZipCode().equals(updateSiteRequest.getZipCode());
        assert site.getCity().equals(updateSiteRequest.getCity());
        assert site.getAddress().equals(updateSiteRequest.getAddress());
        assert site.getOpeningHours().equals(updateSiteRequest.getOpeningHours());
    }

}
