package com.bassi.tmapp.service.extended;

import com.bassi.tmapp.domain.Lead;
import com.bassi.tmapp.repository.LeadRepository;
import com.bassi.tmapp.service.LeadService;
import com.bassi.tmapp.service.dto.LeadDTO;
import com.bassi.tmapp.service.mapper.LeadMapper;
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
    private final LeadMapper leadMapper;

    public LeadServiceExtended(LeadRepository leadRepository, MailServiceExtended mailServiceExtended, LeadMapper leadMapper) {
        this.leadRepository = leadRepository;
        this.mailServiceExtended = mailServiceExtended;
        this.leadMapper = leadMapper;
    }

    public LeadDTO save(LeadDTO leadDTO) {
        LOG.debug("Request to save Lead : {}", leadDTO);
        Lead lead = leadMapper.toEntity(leadDTO);
        lead = leadRepository.save(lead);
        //        this.mailServiceExtended.sendNewLeadMailToAdmin(lead);
        return leadMapper.toDto(lead);
    }
}
