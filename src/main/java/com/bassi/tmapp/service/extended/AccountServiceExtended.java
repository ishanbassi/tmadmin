package com.bassi.tmapp.service.extended;

import com.bassi.tmapp.repository.extended.TmAgentRepositoryExtended;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceExtended {

    private final TmAgentRepositoryExtended agentRepositoryExtended;

    AccountServiceExtended(TmAgentRepositoryExtended agentRepositoryExtended) {
        this.agentRepositoryExtended = agentRepositoryExtended;
    }
}
