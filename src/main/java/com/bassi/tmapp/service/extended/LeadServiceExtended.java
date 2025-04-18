package com.bassi.tmapp.service.extended;

import com.bassi.tmapp.domain.Lead;
import com.bassi.tmapp.repository.LeadRepository;
import com.bassi.tmapp.service.LeadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LeadServiceExtended {

    private static final Logger LOG = LoggerFactory.getLogger(LeadService.class);
    private final LeadRepository leadRepository;
    private final MailServiceExtended mailServiceExtended;

    public LeadServiceExtended(LeadRepository leadRepository, MailServiceExtended mailServiceExtended) {
        this.leadRepository = leadRepository;
        this.mailServiceExtended = mailServiceExtended;
    }

    /**
     * Save a lead.
     *
     * @param lead the entity to save.
     * @return the persisted entity.
     */
    public Lead save(Lead lead) {
        LOG.debug("Request to save Lead : {}", lead);
        lead = leadRepository.save(lead);
        this.mailServiceExtended.sendNewLeadMailToAdmin(lead);
        return lead;
    }
}
