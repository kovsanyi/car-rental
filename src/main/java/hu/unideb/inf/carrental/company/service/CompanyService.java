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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyService.class);

    private final UserService userService;
    private final CompanyValidator companyValidator;
    private final CompanyRepository companyRepository;
    private final DeleteCompany deleteCompany;

    private final CreateCompanyRequestConverter createCompanyRequestConverter;
    private final UpdateCompanyRequestConverter updateCompanyRequestConverter;
    private final CompanyResponseConverter companyResponseConverter;

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

    public long save(CreateCompanyRequest createCompanyRequest) throws UsernameAlreadyInUseException,
            EmailAlreadyInUseException, NameAlreadyInUseException, CompanyEmailAlreadyInUseException {
        LOGGER.info("Saving company");
        Company company = createCompanyRequestConverter.from(createCompanyRequest);
        companyValidator.validate(company);
        company.getUser().setRole(UserRole.ROLE_COMPANY);
        userService.save(company.getUser());
        return companyRepository.save(company).getId();
    }

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

    @Secured({"ROLE_COMPANY"})
    public void delete() throws CollisionException, CarInRentException {
        LOGGER.info("Deleting company");
        deleteCompany.delete(getCompany());
    }

    @Secured({"ROLE_COMPANY"})
    public CompanyResponse get() {
        LOGGER.info("Providing company details of logged in user");
        return companyResponseConverter.from(companyRepository.findByUser(getUser()).get());
    }

    public CompanyResponse getById(long id) throws NotFoundException {
        LOGGER.info("Providing company details by ID {}", id);
        return companyResponseConverter.from(companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.INVALID_COMPANY_ID)));
    }

    public CompanyResponse getByName(String name) throws NotFoundException {
        LOGGER.info("Providing company details by name {}", name);
        return companyResponseConverter.from(companyRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(Constants.COMPANY_NOT_FOUND)));
    }

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
}
