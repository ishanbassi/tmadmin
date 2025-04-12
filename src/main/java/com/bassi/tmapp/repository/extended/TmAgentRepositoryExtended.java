package com.bassi.tmapp.repository.extended;

import com.bassi.tmapp.domain.TmAgent;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TmAgentRepositoryExtended extends JpaRepository<TmAgent, Long> {
    public Optional<TmAgent> findByEmail(String email);

    public List<TmAgent> findByFullNameIn(Set<String> agentNames);
}
