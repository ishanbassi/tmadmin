package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.Lead;
import com.bassi.tmapp.domain.enumeration.LeadStatus;
import com.bassi.tmapp.repository.LeadRepository;
import com.bassi.tmapp.service.dto.AdminUserDTO;
import com.bassi.tmapp.service.dto.LeadDTO;
import com.bassi.tmapp.service.extended.MailServiceExtended;
import com.bassi.tmapp.service.mapper.LeadMapper;
import java.util.List;
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

    private static final Logger LOG = LoggerFactory.getLogger(LeadService.class);

    private final LeadRepository leadRepository;

    private final LeadMapper leadMapper;

    private final MailServiceExtended mailServiceExtended;

    public LeadService(LeadRepository leadRepository, LeadMapper leadMapper, MailServiceExtended mailServiceExtended) {
        this.leadRepository = leadRepository;
        this.leadMapper = leadMapper;
        this.mailServiceExtended = mailServiceExtended;
    }

    /**
     * Save a lead.
     *
     * @param leadDTO the entity to save.
     * @return the persisted entity.
     */
    public LeadDTO save(LeadDTO leadDTO) {
        LOG.debug("Request to save Lead : {}", leadDTO);
        Lead lead = leadMapper.toEntity(leadDTO);
        lead = leadRepository.save(lead);
        mailServiceExtended.sendNewLeadMailToAdmin(lead);
        return leadMapper.toDto(lead);
    }

    /**
     * Update a lead.
     *
     * @param leadDTO the entity to save.
     * @return the persisted entity.
     */
    public LeadDTO update(LeadDTO leadDTO) {
        LOG.debug("Request to update Lead : {}", leadDTO);
        Lead lead = leadMapper.toEntity(leadDTO);
        lead = leadRepository.save(lead);
        return leadMapper.toDto(lead);
    }

    /**
     * Partially update a lead.
     *
     * @param leadDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LeadDTO> partialUpdate(LeadDTO leadDTO) {
        LOG.debug("Request to partially update Lead : {}", leadDTO);

        return leadRepository
            .findById(leadDTO.getId())
            .map(existingLead -> {
                leadMapper.partialUpdate(existingLead, leadDTO);

                return existingLead;
            })
            .map(leadRepository::save)
            .map(leadMapper::toDto);
    }

    /**
     * Get one lead by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LeadDTO> findOne(Long id) {
        LOG.debug("Request to get Lead : {}", id);
        return leadRepository.findById(id).map(leadMapper::toDto);
    }

    /**
     * Delete the lead by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Lead : {}", id);
        leadRepository.deleteById(id);
    }

    public List<LeadDTO> saveAll(List<LeadDTO> leadDTOList) {
        List<Lead> leads = leadMapper.toEntity(leadDTOList);
        leads = leadRepository.saveAll(leads);
        return leadMapper.toDto(leads);
    }

    public void sendOfferMail() {
        List<Lead> leads = leadRepository.findByLeadSourceAndStatus("Pharmexcil_Members_Directory_2008.pdf", LeadStatus.NEW);
        int count = 0;
        for (Lead lead : leads) {
            if (count >= 1000) {
                LOG.info("Ending the process because email limit reached.");
                break;
            }
            if (lead.getStatus() != LeadStatus.NEW) {
                continue;
            }
            count++;
            mailServiceExtended.sendOfferMailToPharamas(lead);
            lead.setStatus(LeadStatus.FOLLOW_UP);
            LOG.info("Mail sent to: {}", lead.getEmail());
            LOG.info("{} emails sent", count);
            leadRepository.save(lead);
        }
    }

    public AdminUserDTO createAdminDtoFromLead(LeadDTO leadDTO) {
        AdminUserDTO adminUserDTO = new AdminUserDTO();
        String[] firstLastName = splitName(leadDTO.getFullName());
        adminUserDTO.setLogin(leadDTO.getEmail());
        adminUserDTO.setEmail(leadDTO.getEmail());
        adminUserDTO.setFirstName(firstLastName[0]);
        adminUserDTO.setLastName(firstLastName[1]);
        return adminUserDTO;
    }

    public static String[] splitName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return new String[] { "", "" };
        }

        String[] parts = fullName.trim().split("\\s+");

        String firstName = parts[0];
        String lastName = "";

        if (parts.length > 1) {
            lastName = parts[parts.length - 1];
        }

        return new String[] { firstName, lastName };
    }
}
