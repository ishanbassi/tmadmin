package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.Lead;
import com.bassi.tmapp.repository.LeadRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.Lead}.
 */
@Service
@Transactional
public class LeadService {

    private static final Logger log = LoggerFactory.getLogger(LeadService.class);

    private final LeadRepository leadRepository;

    public LeadService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    /**
     * Save a lead.
     *
     * @param lead the entity to save.
     * @return the persisted entity.
     */
    public Lead save(Lead lead) {
        log.debug("Request to save Lead : {}", lead);
        return leadRepository.save(lead);
    }

    /**
     * Update a lead.
     *
     * @param lead the entity to save.
     * @return the persisted entity.
     */
    public Lead update(Lead lead) {
        log.debug("Request to update Lead : {}", lead);
        return leadRepository.save(lead);
    }

    /**
     * Partially update a lead.
     *
     * @param lead the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Lead> partialUpdate(Lead lead) {
        log.debug("Request to partially update Lead : {}", lead);

        return leadRepository
            .findById(lead.getId())
            .map(existingLead -> {
                if (lead.getFullName() != null) {
                    existingLead.setFullName(lead.getFullName());
                }
                if (lead.getPhoneNumber() != null) {
                    existingLead.setPhoneNumber(lead.getPhoneNumber());
                }
                if (lead.getEmail() != null) {
                    existingLead.setEmail(lead.getEmail());
                }
                if (lead.getCity() != null) {
                    existingLead.setCity(lead.getCity());
                }
                if (lead.getBrandName() != null) {
                    existingLead.setBrandName(lead.getBrandName());
                }
                if (lead.getSelectedPackage() != null) {
                    existingLead.setSelectedPackage(lead.getSelectedPackage());
                }
                if (lead.getTmClass() != null) {
                    existingLead.setTmClass(lead.getTmClass());
                }
                if (lead.getComments() != null) {
                    existingLead.setComments(lead.getComments());
                }
                if (lead.getContactMethod() != null) {
                    existingLead.setContactMethod(lead.getContactMethod());
                }
                if (lead.getCreatedDate() != null) {
                    existingLead.setCreatedDate(lead.getCreatedDate());
                }
                if (lead.getModifiedDate() != null) {
                    existingLead.setModifiedDate(lead.getModifiedDate());
                }
                if (lead.getDeleted() != null) {
                    existingLead.setDeleted(lead.getDeleted());
                }
                if (lead.getStatus() != null) {
                    existingLead.setStatus(lead.getStatus());
                }
                if (lead.getLeadSource() != null) {
                    existingLead.setLeadSource(lead.getLeadSource());
                }

                return existingLead;
            })
            .map(leadRepository::save);
    }

    /**
     * Get one lead by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Lead> findOne(Long id) {
        log.debug("Request to get Lead : {}", id);
        return leadRepository.findById(id);
    }

    /**
     * Delete the lead by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Lead : {}", id);
        leadRepository.deleteById(id);
    }
}
