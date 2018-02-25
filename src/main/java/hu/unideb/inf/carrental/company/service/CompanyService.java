package hu.unideb.inf.carrental.company.service;

import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.company.Company;
import hu.unideb.inf.carrental.commons.domain.company.CompanyRepository;
import hu.unideb.inf.carrental.commons.domain.user.User;
import hu.unideb.inf.carrental.commons.domain.user.enumeration.UserRole;
import hu.unideb.inf.carrental.commons.exception.*;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.company.resource.model.CompanyResponse;
import hu.unideb.inf.carrental.company.resource.model.CreateCompanyRequest;
import hu.unideb.inf.carrental.company.resource.model.UpdateCompanyRequest;
import hu.unideb.inf.carrental.company.service.converter.CompanyResponseConverter;
import hu.unideb.inf.carrental.company.service.converter.CreateCompanyRequestConverter;
import hu.unideb.inf.carrental.company.service.converter.UpdateCompanyRequestConverter;
import hu.unideb.inf.carrental.company.service.delete.DeleteCompany;
import hu.unideb.inf.carrental.company.service.validator.CompanyValidator;
import hu.unideb.inf.carrental.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for registering and managing a company (owner) as a user.
 *
 * @see CreateCompanyRequest
 * @see UpdateCompanyRequest
 * @see CompanyResponse
 */
@Service
public class CompanyService {
    /**
     * Registers a new company (owner) as a user. If the registration is successful, it returns the ID of the
     * registered company.
     *
     * @param createCompanyRequest a request object to register a new company
     * @return the ID of the registered company
     * @throws UsernameAlreadyInUseException     if the given username is already in use
     * @throws EmailAlreadyInUseException        if the given user email is already in use
     * @throws NameAlreadyInUseException         if the given company name is already in use
     * @throws CompanyEmailAlreadyInUseException if the given company email is already in use
     */
    @Transactional
    public long save(CreateCompanyRequest createCompanyRequest) throws UsernameAlreadyInUseException,
            EmailAlreadyInUseException, NameAlreadyInUseException, CompanyEmailAlreadyInUseException {
        LOGGER.info("Saving company");
        Company company = createCompanyRequestConverter.from(createCompanyRequest);
        companyValidator.validate(company);
        company.getUser().setRole(UserRole.ROLE_COMPANY);
        userService.save(company.getUser());
        return companyRepository.save(company).getId();
    }

    /**
     * Updates the details of the logged in company (owner) using the given {@code UpdateCompanyRequest}.<br>
     * <b>This method can only be called by a company owner.</b>
     *
     * @param updateCompanyRequest a request object to update the details of the logged in company (owner)
     * @throws NameAlreadyInUseException         if the given company name is already in use
     * @throws CompanyEmailAlreadyInUseException if the given company email is already in use
     */
    @Secured({"ROLE_COMPANY"})
    public void update(UpdateCompanyRequest updateCompanyRequest)
            throws NameAlreadyInUseException, CompanyEmailAlreadyInUseException {
        LOGGER.info("Updating company");
        Company update = updateCompanyRequestConverter.from(updateCompanyRequest);
        Company company = companyRepository.findByUser(getUser()).get();
        company.setName(update.getName());
        company.setEmail(update.getEmail());
        company.setPhoneNumber(update.getPhoneNumber());
        companyValidator.validate(company);
        companyRepository.save(company);
    }

    /**
     * Deletes the logged in company (owner). All registered site must be deleted before company deletion.<br>
     * <b>This method can only be called by a company owner.</b>
     *
     * @throws CollisionException if the company has registered site(s)
     * @throws CarInRentException if the company can not be deleted due to it has active reservation
     * @see hu.unideb.inf.carrental.site.service.SiteService
     */
    @Secured({"ROLE_COMPANY"})
    public void delete() throws CollisionException, CarInRentException {
        LOGGER.info("Deleting company");
        deleteCompany.delete(getCompany());
    }

    /**
     * Returns the details of the logged in company (owner).<br>
     * <b>This method can only be called by a company owner.</b>
     *
     * @return the details of the logged in company (owner)
     */
    @Secured({"ROLE_COMPANY"})
    public CompanyResponse get() {
        LOGGER.info("Providing company details of logged in user");
        return companyResponseConverter.from(companyRepository.findByUser(getUser()).get());
    }

    /**
     * Returns the company details by <b>company ID</b>.<br>
     * <b>Company ID does not equal to user ID!</b> The user ID is unique to the whole system including company,
     * manager and customer IDs, whereas the company ID is unique only among the company owners.
     *
     * @param id ID of the company owner
     * @return the company details by company ID
     * @throws NotFoundException if the company ID does not identify a company
     */
    public CompanyResponse getById(long id) throws NotFoundException {
        LOGGER.info("Providing company details by ID {}", id);
        return companyResponseConverter.from(companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.INVALID_COMPANY_ID)));
    }

    /**
     * Returns the company details by company name.
     *
     * @param name name of the company
     * @return the company details by company name
     * @throws NotFoundException if the name does not identify a company
     */
    public CompanyResponse getByName(String name) throws NotFoundException {
        LOGGER.info("Providing company details by name {}", name);
        return companyResponseConverter.from(companyRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(Constants.COMPANY_NOT_FOUND)));
    }

    /**
     * Returns the details of all registered companies.
     *
     * @return the details of all registered companies
     */
    public List<CompanyResponse> getAll() {
        LOGGER.info("Providing details of all company");
        return companyRepository.findAll().stream().map(companyResponseConverter::from).collect(Collectors.toList());
    }

    private Company getCompany() {
        return companyRepository.findByUser(getUser()).get();
    }

    private User getUser() {
        return SecurityUtils.getLoggedInUser();
    }

    @Autowired
    public CompanyService(UserService userService, CompanyValidator companyValidator,
                          CompanyRepository companyRepository, DeleteCompany deleteCompany,
                          CreateCompanyRequestConverter createCompanyRequestConverter,
                          UpdateCompanyRequestConverter updateCompanyRequestConverter,
                          CompanyResponseConverter companyResponseConverter) {
        this.userService = userService;
        this.companyValidator = companyValidator;
        this.companyRepository = companyRepository;
        this.deleteCompany = deleteCompany;
        this.createCompanyRequestConverter = createCompanyRequestConverter;
        this.updateCompanyRequestConverter = updateCompanyRequestConverter;
        this.companyResponseConverter = companyResponseConverter;
    }

    private final UserService userService;
    private final CompanyValidator companyValidator;
    private final CompanyRepository companyRepository;
    private final DeleteCompany deleteCompany;

    private final CreateCompanyRequestConverter createCompanyRequestConverter;
    private final UpdateCompanyRequestConverter updateCompanyRequestConverter;
    private final CompanyResponseConverter companyResponseConverter;

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyService.class);
}
