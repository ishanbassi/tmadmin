package com.bassi.tmapp.repository.extended;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bassi.tmapp.domain.TmAgent;
import com.bassi.tmapp.repository.TmAgentRepository;


@Repository
public interface TmAgentRepositoryExtended extends JpaRepository<TmAgent, Long> {
	
	public Optional<TmAgent> findByEmail(String email);
	
	public Optional<TmAgent> findByNameAnd
	
}
