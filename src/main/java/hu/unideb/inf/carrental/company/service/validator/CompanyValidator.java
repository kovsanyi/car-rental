package hu.unideb.inf.carrental.company.service.validator;

import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.company.Company;
import hu.unideb.inf.carrental.commons.domain.company.CompanyRepository;
import hu.unideb.inf.carrental.commons.exception.CompanyEmailAlreadyInUseException;
import hu.unideb.inf.carrental.commons.exception.NameAlreadyInUseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyValidator {
    private final static Logger logger = LoggerFactory.getLogger(CompanyValidator.class);

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyValidator(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void validate(Company company) throws NameAlreadyInUseException, CompanyEmailAlreadyInUseException {
        if (company.getId() == null) {
            logger.info("Validating new company");
            if (companyRepository.existsByName(company.getName())) {
                throw new NameAlreadyInUseException(Constants.COMPANY_NAME_ALREADY_EXISTS);
            }
            if (companyRepository.existsByEmail(company.getEmail())) {
                throw new CompanyEmailAlreadyInUseException(Constants.COMPANY_EMAIL_ALREADY_EXISTS);
            }
        } else {
            logger.info("Validating existing company");
            Company original = companyRepository.findOne(company.getId());
            Company foundedByName = companyRepository.findByName(company.getName()).orElse(original);
            Company foundedByEmail = companyRepository.findByEmail(company.getEmail()).orElse(original);
            if (!foundedByName.equals(original)) {
                throw new NameAlreadyInUseException(Constants.COMPANY_NAME_ALREADY_EXISTS);
            }
            if (!foundedByEmail.equals(original)) {
                throw new CompanyEmailAlreadyInUseException(Constants.COMPANY_EMAIL_ALREADY_EXISTS);
            }
        }
    }
}
