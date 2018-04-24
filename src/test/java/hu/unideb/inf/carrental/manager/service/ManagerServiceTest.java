package hu.unideb.inf.carrental.manager.service;

import hu.unideb.inf.carrental.commons.exception.EmailAlreadyInUseException;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.UsernameAlreadyInUseException;
import hu.unideb.inf.carrental.manager.resource.model.CreateManagerRequest;
import hu.unideb.inf.carrental.manager.resource.model.ManagerResponse;
import hu.unideb.inf.carrental.manager.resource.model.UpdateManagerRequest;
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
public class ManagerServiceTest {
    @Test
    public void saveShouldBeSuccess() throws Exception {
        CreateManagerRequest createManagerRequest = new CreateManagerRequest("newManager".toLowerCase(), "password", "newmanager@mail.com", "New Manager", "11111111111", 1111, "City", "Address");
        ManagerResponse managerResponse = new ManagerResponse(3L, 10L, "New Manager", "11111111111", 1111, "City", "Address");
        managerService.save(createManagerRequest);
        assert managerService.getById(3L).equals(managerResponse);
    }

    @Test(expected = UsernameAlreadyInUseException.class)
    public void saveShouldThrowUsernameAlreadyInUseException() throws Exception {
        CreateManagerRequest createManagerRequest = new CreateManagerRequest("manager".toLowerCase(), "password", "newmanager@mail.com", "New Manager", "11111111111", 1111, "City", "Address");
        managerService.save(createManagerRequest);
    }

    @Test(expected = EmailAlreadyInUseException.class)
    public void saveShouldThrowEmailAlreadyInUseException() throws Exception {
        CreateManagerRequest createManagerRequest = new CreateManagerRequest("newManager".toLowerCase(), "password", "manager@mail.com", "New Manager", "11111111111", 1111, "City", "Address");
        managerService.save(createManagerRequest);
    }

    @Test
    public void updateShouldBeSuccess() throws Exception {
        setAuth("manager");
        UpdateManagerRequest updateManagerRequest = new UpdateManagerRequest("Updated Manager", "11111111112", 1112, "City1", "Address1");
        ManagerResponse managerResponse = new ManagerResponse(1L, 8L, "Updated Manager", "11111111112", 1112, "City1", "Address1");
        managerService.update(updateManagerRequest);
        assert managerService.getById(1L).equals(managerResponse);
    }

    @Test
    public void deleteShouldBeSuccess() throws Exception {
        setAuth("manager");
        managerService.delete();
        setAuth("managerWithSite");
        managerService.delete();
    }

    @Test(expected = BadCredentialsException.class)
    public void deleteShouldThrowBadCredentialsException() throws Exception {
        setAuth("manager");
        managerService.delete();
        setAuth("manager");
    }

    @Test
    public void getShouldBeSuccess() {
        setAuth("manager");
        assert managerService.get().getFullName().equals("Manager User");
        setAuth("managerWithSite");
        assert managerService.get().getFullName().equals("Manager With Site");
    }

    @Test
    public void getByIdShouldBeSuccess() throws Exception {
        assert managerService.getById(1L).getFullName().equals("Manager User");
        assert managerService.getById(2L).getFullName().equals("Manager With Site");
    }

    @Test(expected = NotFoundException.class)
    public void getByIdShouldThrowNotFoundException() throws Exception {
        managerService.getById(100L);
    }

    @Test
    public void getByUserIdShouldBeSuccess() throws Exception {
        assert managerService.getByUserId(8L).getFullName().equals("Manager User");
        assert managerService.getByUserId(9L).getFullName().equals("Manager With Site");
    }

    @Test(expected = NotFoundException.class)
    public void getByUserIdShouldThrowNotFoundException() throws Exception {
        managerService.getByUserId(1L);
    }

    @Test
    public void getByUsernameShouldBeSuccess() throws Exception {
        assert managerService.getByUsername("manager").getFullName().equals("Manager User");
        assert managerService.getByUsername("managerWithSite").getFullName().equals("Manager With Site");
    }

    @Test(expected = NotFoundException.class)
    public void getByUsernameShouldThrowNotFoundException() throws Exception {
        managerService.getByUsername("company");
    }

    private void setAuth(String username) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username.toLowerCase(), "password");
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Autowired
    private ManagerService managerService;

    @Autowired
    private AuthenticationManager authenticationManager;
}