package hu.unideb.inf.carrental.site.service;

import hu.unideb.inf.carrental.commons.exception.CollisionException;
import hu.unideb.inf.carrental.commons.exception.ManagerCollisionException;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.UnauthorizedAccessException;
import hu.unideb.inf.carrental.site.resource.model.CreateSiteRequest;
import hu.unideb.inf.carrental.site.resource.model.SiteResponse;
import hu.unideb.inf.carrental.site.resource.model.UpdateSiteRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
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
public class SiteServiceTest {

    @DirtiesContext
    @Test
    public void saveShouldBeSuccessWhenManagerExists() throws Exception {
        setAuth("company");
        CreateSiteRequest createSiteRequest = new CreateSiteRequest("manager", "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");
        SiteResponse siteResponse = new SiteResponse(5L, 1L, 1L, "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");
        siteService.save(createSiteRequest);

        assert siteService.getByCompany().size() == 1;
        assert siteService.getByCompany().get(0).equals(siteResponse);
    }

    @DirtiesContext
    @Test
    public void saveShouldBeSuccessWhenManagerNull() throws Exception {
        setAuth("company");
        CreateSiteRequest createSiteRequest = new CreateSiteRequest(null, "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");
        SiteResponse siteResponse = new SiteResponse(5L, 1L, null, "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");
        siteService.save(createSiteRequest);

        assert siteService.getByCompany().size() == 1;
        assert siteService.getByCompany().get(0).equals(siteResponse);
    }


    @Test(expected = NotFoundException.class)
    public void saveShouldThrowNotFoundException() throws Exception {
        setAuth("company");
        CreateSiteRequest createSiteRequest = new CreateSiteRequest("invalidManager".toLowerCase(), "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");
        siteService.save(createSiteRequest);
    }

    @Test
    public void updateByCompanyShouldBeSuccess() throws Exception {
        setAuth("companyWithSites");
        UpdateSiteRequest updateSiteRequest = new UpdateSiteRequest(1L, "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");
        SiteResponse siteResponse = new SiteResponse(1L, 2L, null, "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");
        siteService.update(updateSiteRequest);

        assert siteService.getById(1L).equals(siteResponse);
    }

    @Test
    public void updateByManagerShouldBeSuccess() throws Exception {
        setAuth("managerWithSite");
        UpdateSiteRequest updateSiteRequest = new UpdateSiteRequest(3L, "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");
        SiteResponse siteResponse = new SiteResponse(3L, 3L, 2L, "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");
        siteService.update(updateSiteRequest);

        assert siteService.getById(3L).equals(siteResponse);
    }

    @Test(expected = NotFoundException.class)
    public void updateByCompanyShouldThrowNotFoundException() throws Exception {
        setAuth("companyWithSites");
        UpdateSiteRequest updateSiteRequest = new UpdateSiteRequest(100L, "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");
        siteService.update(updateSiteRequest);
    }

    @Test(expected = NotFoundException.class)
    public void updateByManagerShouldThrowNotFoundException() throws Exception {
        setAuth("managerWithSite");
        UpdateSiteRequest updateSiteRequest = new UpdateSiteRequest(100L, "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");
        siteService.update(updateSiteRequest);
    }

    @Test(expected = UnauthorizedAccessException.class)
    public void updateByManagerShouldThrowUnauthorizedAccessException() throws Exception {
        setAuth("managerWithSite");
        UpdateSiteRequest updateSiteRequest = new UpdateSiteRequest(1L, "new_site@mail.com", "99999999999", 9999, "City", "Address", "OpeningHours");
        siteService.update(updateSiteRequest);
    }

    @Test
    public void deleteShouldBeSuccess() throws Exception {
        setAuth("companyWithSites");
        siteService.delete(1L);
    }

    @Test(expected = NotFoundException.class)
    public void deleteShouldThrowNotFoundException() throws Exception {
        setAuth("company");
        siteService.delete(100L);
    }

    @Test(expected = UnauthorizedAccessException.class)
    public void deleteShouldThrowUnauthorizedAccessException() throws Exception {
        setAuth("company");
        siteService.delete(3L);
    }

    @Test(expected = CollisionException.class)
    public void deleteShouldThrowCollisionException() throws Exception {
        setAuth("companyWithCars");
        siteService.delete(3L);
    }

    @Test
    public void setManagerByIdShouldBeSuccess() throws Exception {
        setAuth("companyWithSites");
        siteService.setManagerById(1L, 1L);

        assert siteService.getById(1L).getManagerId().equals(1L);
    }

    @Test(expected = NotFoundException.class)
    public void setManagerByIdShouldThrowNotFoundException() throws Exception {
        setAuth("companyWithSites");
        siteService.setManagerById(1L, 100L);
    }

    @Test(expected = UnauthorizedAccessException.class)
    public void setManagerByIdShouldThrowUnauthorizedAccessException() throws Exception {
        setAuth("company");
        siteService.setManagerById(1L, 1L);
    }

    @Test(expected = ManagerCollisionException.class)
    public void setManagerByIdShouldThrowManagerCollisionException() throws Exception {
        setAuth("companyWithSites");
        siteService.setManagerById(1L, 2L);
    }

    @Test
    public void setManagerByUserIdShouldBeSuccess() throws Exception {
        setAuth("companyWithSites");
        siteService.setManagerByUserId(1L, 8L);

        assert siteService.getById(1L).getManagerId().equals(1L);
    }

    @Test(expected = NotFoundException.class)
    public void setManagerByUserIdShouldThrowNotFoundException() throws Exception {
        setAuth("companyWithSites");
        siteService.setManagerByUserId(1L, 100L);
    }

    @Test(expected = UnauthorizedAccessException.class)
    public void setManagerByUserIdShouldThrowUnauthorizedAccessException() throws Exception {
        setAuth("company");
        siteService.setManagerByUserId(1L, 8L);
    }

    @Test(expected = ManagerCollisionException.class)
    public void setManagerByUserIdShouldThrowManagerCollisionException() throws Exception {
        setAuth("companyWithSites");
        siteService.setManagerByUserId(1L, 9L);
    }

    @Test
    public void setManagerByUsernameShouldBeSuccess() throws Exception {
        setAuth("companyWithSites");
        siteService.setManagerByUsername(1L, "manager");

        assert siteService.getById(1L).getManagerId().equals(1L);
    }

    @Test(expected = NotFoundException.class)
    public void setManagerByUsernameShouldThrowNotFoundException() throws Exception {
        setAuth("companyWithSites");
        siteService.setManagerByUsername(1L, "invalidManager".toLowerCase());
    }

    @Test(expected = UnauthorizedAccessException.class)
    public void setManagerByUsernameShouldThrowUnauthorizedAccessException() throws Exception {
        setAuth("company");
        siteService.setManagerByUsername(1L, "manager");
    }

    @Test(expected = ManagerCollisionException.class)
    public void setManagerByUsernameShouldThrowManagerCollisionException() throws Exception {
        setAuth("companyWithSites");
        siteService.setManagerByUsername(1L, "managerWithSite".toLowerCase());
    }

    @Test
    public void getByCompanyShouldBeSuccess() {
        setAuth("companyWithSites");
        assert siteService.getByCompany().size() == 2;
        setAuth("companyWithCars");
        assert siteService.getByCompany().size() == 2;
    }

    @Test
    public void getByManagerShouldBeSuccess() throws Exception {
        setAuth("managerWithSite");
        assert siteService.getByManager().getManagerId().equals(2L);
    }

    @Test(expected = NotFoundException.class)
    public void getByManagerShouldThrowNotFoundException() throws Exception {
        setAuth("manager");
        siteService.getByManager();
    }

    @Test
    public void getByIdShouldBeSuccess() throws Exception {
        assert siteService.getById(1L).getCity().equals("City1");
        assert siteService.getById(2L).getCity().equals("City2");
        assert siteService.getById(3L).getCity().equals("City3");
        assert siteService.getById(4L).getCity().equals("City4");
    }

    @Test(expected = NotFoundException.class)
    public void getByIdShouldThrowNotFoundException() throws Exception {
        siteService.getById(100L);
    }

    @Test
    public void getByCompanyNameShouldBeSuccess() throws Exception {
        assert siteService.getByCompanyName("Company").size() == 0;
        assert siteService.getByCompanyName("Company With Sites").size() == 2;
        assert siteService.getByCompanyName("Company With Cars").size() == 2;
    }

    @Test(expected = NotFoundException.class)
    public void getByCompanyNameShouldThrowNotFoundException() throws Exception {
        siteService.getByCompanyName("Invalid Company");
    }

    private void setAuth(String username) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username.toLowerCase(), "password");
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Autowired
    private SiteService siteService;

    @Autowired
    private AuthenticationManager authenticationManager;
}