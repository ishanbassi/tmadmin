package com.bassi.tmapp.service.extended;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bassi.tmapp.domain.TmAgent;
import com.bassi.tmapp.repository.TmAgentRepository;
import com.bassi.tmapp.repository.extended.TmAgentRepositoryExtended;
import com.bassi.tmapp.security.SecurityUtils;
import com.bassi.tmapp.service.extended.dto.AccountDto;
import com.bassi.tmapp.web.rest.errors.InternalServerAlertException;

@Service
public class AccountServiceExtended {
	
	private final TmAgentRepositoryExtended agentRepositoryExtended;
	
	AccountServiceExtended(TmAgentRepositoryExtended agentRepositoryExtended) {
		this.agentRepositoryExtended = agentRepositoryExtended;
	}
	
	public AccountDto getAgent() {
		String email = SecurityUtils.getCurrentUserLogin()
				.orElseThrow(() -> new InternalServerAlertException("User could not be found"));
		
		TmAgent agent = agentRepositoryExtended.findByEmail(email).orElseThrow(
				() -> new InternalServerAlertException("Currently no agent is linked with the email : " + email));;
		
		AccountDto account = new AccountDto();
		account.setTmAgent(agent);
		return account;
		
		
		
	}

}
