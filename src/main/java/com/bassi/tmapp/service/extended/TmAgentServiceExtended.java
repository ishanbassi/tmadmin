package com.bassi.tmapp.service.extended;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bassi.tmapp.config.Constants;
import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.domain.TmAgent;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.repository.TrademarkRepository;
import com.bassi.tmapp.repository.extended.PublishedTmRepositoryExtended;
import com.bassi.tmapp.repository.extended.TmAgentRepositoryExtended;
import com.bassi.tmapp.service.dto.PublishedTmDTO;
import com.bassi.tmapp.service.dto.TmAgentDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.extended.pdfService.ITextPdfReaderService;
import com.bassi.tmapp.service.mapper.PublishedTmMapper;
import com.bassi.tmapp.service.mapper.TmAgentMapper;
import com.bassi.tmapp.service.mapper.TrademarkMapper;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class TmAgentServiceExtended {
	
	private static final Logger log = LoggerFactory.getLogger(TmAgentServiceExtended.class);
	
	private final TmAgentRepositoryExtended tmAgentRepositoryExtended;
	private final TrademarkRepository trademarkRepository;
	private final TrademarkMapper trademarkMapper;
	private final TmAgentMapper tmAgentMapper;
	private final PublishedTmRepositoryExtended publishedTmRepositoryExtended;
	private final PublishedTmMapper publishedTmMapper;

	TmAgentServiceExtended(TmAgentRepositoryExtended tmAgentRepositoryExtended, TrademarkRepository trademarkRepository,
			TrademarkMapper trademarkMapper, TmAgentMapper tmAgentMapper,
			PublishedTmRepositoryExtended publishedTmRepositoryExtended,PublishedTmMapper publishedTmMapper) {
		this.tmAgentRepositoryExtended = tmAgentRepositoryExtended;
		this.trademarkRepository = trademarkRepository;
		this.trademarkMapper = trademarkMapper;
		this.tmAgentMapper  = tmAgentMapper;
		this.publishedTmRepositoryExtended  = publishedTmRepositoryExtended;
		this.publishedTmMapper = publishedTmMapper;
	}
	
	
	private Map<String, List<PublishedTmDTO>> getPublishedTrademarkByAgentName(List<PublishedTmDTO> trademarks) {
		return trademarks.stream().collect(Collectors.groupingBy(tm -> Objects.requireNonNullElse(tm.getAgentName(), Constants.AGENT_MISSING)));
		
	}
	
	public void saveTmAgents(List<PublishedTmDTO> trademarks) {
		log.info("Going to process published trademarks to extract agents and trademarks");
		Map<String, List<PublishedTmDTO>> trademarkByAgentName = getPublishedTrademarkByAgentName(trademarks);
		Set<String> agentNames = trademarkByAgentName.keySet();
		List<TmAgent> agents  = tmAgentRepositoryExtended.findByFullNameIn(agentNames);
		log.info("Found {} agents", agents.size());
		List<TmAgent> newAgents = agentNames.stream()
		.filter(agentName -> {
			return agents.stream().noneMatch(agent -> agent.getFullName().equalsIgnoreCase(agentName));
		})
		.map(agentName -> {
			TmAgent newAgent  = new TmAgent();
			newAgent.setFullName(agentName);
			trademarkByAgentName.get(agentName).stream().findFirst().
			ifPresent(tm -> newAgent.setAddress(tm.getAgentAddress()));
			return newAgent;
		}).toList();
		newAgents  = tmAgentRepositoryExtended.saveAll(newAgents);
		log.info("Found {} new agents", newAgents.size());
		agents.addAll(newAgents);
		
//		saveTrademarks(trademarkByAgentName, agents);
		assignAgentsToPublishedTrademarks(trademarkByAgentName,agents);
	}


	private void saveTrademarks(Map<String, List<PublishedTmDTO>> trademarkByAgentName, List<TmAgent> newAgents) {
		log.info("Going to extract trademarks from published trademark");
		List<TrademarkDTO> trademarkDtos =  trademarkByAgentName.values()
				.stream()
				.flatMap(List::stream)
				.map(tm -> {
					TrademarkDTO dto  = new TrademarkDTO(tm);
					TmAgentDTO agentDto = newAgents.stream()
							.filter(newAgent -> newAgent.getFullName().equalsIgnoreCase(tm.getAgentName())).findFirst()
							.map(newAgent -> tmAgentMapper.toDto(newAgent))
							.orElse(null);
					dto.setTmAgent(agentDto);
					return dto;
				})
				.toList();
		
		List<Trademark> trademarks = trademarkMapper.toEntity(trademarkDtos);
		trademarkRepository.saveAll(trademarks);
		
	}
	
	private void assignAgentsToPublishedTrademarks(Map<String, List<PublishedTmDTO>> trademarkByAgentName,
			List<TmAgent> agents) {
		for (TmAgent agent : agents) {
			if (trademarkByAgentName.containsKey(agent.getFullName())) {
				for (PublishedTmDTO tm : trademarkByAgentName.get(agent.getFullName())) {
					tm.setAgent(agent);
				}
				
				List<PublishedTm> publishedTrademarks = publishedTmMapper.toEntity(trademarkByAgentName.get(agent.getFullName()));
				publishedTmRepositoryExtended.saveAll(publishedTrademarks);
				return;
				
			}
			log.info("No published trademark found that matches with the agent: {}", agent);
			
		}

	}
	
	public void migrateAgentsFromExistingTrademarks() {
		log.info("Going to migrate Agents from existing trademarks");
		int pageSize = 500;
		Pageable page = PageRequest.of(0, pageSize);
		Page<PublishedTm> trademarkPage  = publishedTmRepositoryExtended.findAll(page);
		while(trademarkPage.hasContent()) {
			List<PublishedTmDTO> tmDto = publishedTmMapper.toDto(trademarkPage.getContent());
			saveTmAgents(tmDto);
			
			if(trademarkPage.hasNext()) {
				trademarkPage = publishedTmRepositoryExtended.findAll(trademarkPage.nextPageable()); 
			}
			else {
				break;
			}
			
		}
		
	}
	
	
}
