package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.Company;
import com.bassi.tmapp.repository.CompanyRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.Company}.
 */
@Service
@Transactional
public class CompanyService {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    /**
     * Save a company.
     *
     * @param company the entity to save.
     * @return the persisted entity.
     */
    public Company save(Company company) {
        LOG.debug("Request to save Company : {}", company);
        return companyRepository.save(company);
    }

    /**
     * Update a company.
     *
     * @param company the entity to save.
     * @return the persisted entity.
     */
    public Company update(Company company) {
        LOG.debug("Request to update Company : {}", company);
        return companyRepository.save(company);
    }

    /**
     * Partially update a company.
     *
     * @param company the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Company> partialUpdate(Company company) {
        LOG.debug("Request to partially update Company : {}", company);

        return companyRepository
            .findById(company.getId())
            .map(existingCompany -> {
                if (company.getType() != null) {
                    existingCompany.setType(company.getType());
                }
                if (company.getName() != null) {
                    existingCompany.setName(company.getName());
                }
                if (company.getCin() != null) {
                    existingCompany.setCin(company.getCin());
                }
                if (company.getGstin() != null) {
                    existingCompany.setGstin(company.getGstin());
                }
                if (company.getNatureOfBusiness() != null) {
                    existingCompany.setNatureOfBusiness(company.getNatureOfBusiness());
                }
                if (company.getAddress() != null) {
                    existingCompany.setAddress(company.getAddress());
                }
                if (company.getState() != null) {
                    existingCompany.setState(company.getState());
                }
                if (company.getPincode() != null) {
                    existingCompany.setPincode(company.getPincode());
                }
                if (company.getCity() != null) {
                    existingCompany.setCity(company.getCity());
                }
                if (company.getCreatedDate() != null) {
                    existingCompany.setCreatedDate(company.getCreatedDate());
                }
                if (company.getModifiedDate() != null) {
                    existingCompany.setModifiedDate(company.getModifiedDate());
                }
                if (company.getDeleted() != null) {
                    existingCompany.setDeleted(company.getDeleted());
                }

                return existingCompany;
            })
            .map(companyRepository::save);
    }

    /**
     * Get one company by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Company> findOne(Long id) {
        LOG.debug("Request to get Company : {}", id);
        return companyRepository.findById(id);
    }

    /**
     * Delete the company by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Company : {}", id);
        companyRepository.deleteById(id);
    }
}
