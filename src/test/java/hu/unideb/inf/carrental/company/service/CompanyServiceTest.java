package hu.unideb.inf.carrental.company.service;

import hu.unideb.inf.carrental.commons.domain.user.enumeration.UserRole;
import hu.unideb.inf.carrental.commons.exception.*;
import hu.unideb.inf.carrental.company.resource.model.CompanyResponse;
import hu.unideb.inf.carrental.company.resource.model.CreateCompanyRequest;
import hu.unideb.inf.carrental.company.resource.model.UpdateCompanyRequest;
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
public class CompanyServiceTest {
    @Test
    public void saveShouldBeSuccess() throws Exception {
        CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest("newCompany".toLowerCase(), "password", "newcompany@mail.com", "New Company", "newcompany@mail.com", "New Company", "11111111111", 1111, "City", "Address");
        CompanyResponse companyResponse = new CompanyResponse(4L, 10L, "newCompany".toLowerCase(), "newcompany@mail.com", UserRole.ROLE_COMPANY.toString(), "New Company", "newcompany@mail.com", "New Company", "11111111111", 1111, "City", "Address");
        companyService.save(createCompanyRequest);
        assert companyService.getById(4L).equals(companyResponse);
    }

    @Test(expected = UsernameAlreadyInUseException.class)
    public void saveShouldThrowUsernameAlreadyInUseException() throws Exception {
        CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest("company".toLowerCase(), "password", "newcompany@mail.com", "New Company", "newcompany@mail.com", "New Company", "11111111111", 1111, "City", "Address");
        companyService.save(createCompanyRequest);
    }

    @Test(expected = EmailAlreadyInUseException.class)
    public void saveShouldThrowEmailAlreadyInUseException() throws Exception {
        CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest("newCompany".toLowerCase(), "password", "company@mail.com", "New Company", "newcompany@mail.com", "New Company", "11111111111", 1111, "City", "Address");
        companyService.save(createCompanyRequest);
    }

    @Test(expected = NameAlreadyInUseException.class)
    public void saveShouldThrowNameAlreadyInUseException() throws Exception {
        CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest("newCompany".toLowerCase(), "password", "newcompany@mail.com", "Company", "newcompany@mail.com", "New Company", "11111111111", 1111, "City", "Address");
        companyService.save(createCompanyRequest);
    }

    @Test(expected = CompanyEmailAlreadyInUseException.class)
    public void saveShouldThrowCompanyEmailAlreadyInUseException() throws Exception {
        CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest("newCompany".toLowerCase(), "password", "newcompany@mail.com", "New Company", "company@mail.com", "New Company", "11111111111", 1111, "City", "Address");
        companyService.save(createCompanyRequest);
    }

    @Test
    public void updateShouldBeSuccess() throws Exception {
        setAuth("company");
        UpdateCompanyRequest updateCompanyRequest = new UpdateCompanyRequest("New Company", "newcompany@mail.com", "New Company", "11111111111", 1111, "City", "Address");
        companyService.update(updateCompanyRequest);
    }

    @Test(expected = NameAlreadyInUseException.class)
    public void updateShouldThrowNameAlreadyInUseException() throws Exception {
        setAuth("companyWithSites");
        UpdateCompanyRequest updateCompanyRequest = new UpdateCompanyRequest("Company", "newcompany@mail.com", "New Company", "11111111111", 1111, "City", "Address");
        companyService.update(updateCompanyRequest);
    }

    @Test(expected = CompanyEmailAlreadyInUseException.class)
    public void updateShouldThrowCompanyEmailAlreadyInUseException() throws Exception {
        setAuth("companyWithSites");
        UpdateCompanyRequest updateCompanyRequest = new UpdateCompanyRequest("New Company", "company@mail.com", "New Company", "11111111111", 1111, "City", "Address");
        companyService.update(updateCompanyRequest);
    }

    @Test(expected = BadCredentialsException.class)
    public void deleteShouldThrowBadCredentialsException() throws Exception {
        setAuth("company");
        companyService.delete();
        setAuth("company");
    }

    @Test(expected = CollisionException.class)
    public void deleteShouldThrowCollisionException() throws Exception {
        setAuth("companyWithSites");
        companyService.delete();
    }

    @Test
    public void getShouldBeSuccess() {
        setAuth("company");
        assert companyService.get().getName().equals("Company");
        setAuth("companyWithSites");
        assert companyService.get().getName().equals("Company With Sites");
        setAuth("companyWithCars");
        assert companyService.get().getName().equals("Company With Cars");
    }

    @Test
    public void getByIdShouldBeSuccess() throws Exception {
        assert companyService.getById(1L).getName().equals("Company");
        assert companyService.getById(2L).getName().equals("Company With Sites");
        assert companyService.getById(3L).getName().equals("Company With Cars");
    }

    @Test(expected = NotFoundException.class)
    public void getByIdShouldThrowNotFoundException() throws Exception {
        companyService.getById(100L);
    }

    @Test
    public void getByNameShouldBeSuccess() throws Exception {
        assert companyService.getByName("Company").getId().equals(1L);
        assert companyService.getByName("Company With Sites").getId().equals(2L);
        assert companyService.getByName("Company With Cars").getId().equals(3L);
    }

    @Test(expected = NotFoundException.class)
    public void getByNameShouldThrowNotFoundException() throws Exception {
        companyService.getByName("Company Does Not Exists");
    }

    @Test
    public void getAllShouldBeSuccess() {
        assert companyService.getAll().size() == 3;
    }

    private void setAuth(String username) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username.toLowerCase(), "password");
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AuthenticationManager authenticationManager;
}