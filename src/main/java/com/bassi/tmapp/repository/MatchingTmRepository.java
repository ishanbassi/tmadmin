package com.bassi.tmapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bassi.tmapp.domain.Phonetics;
import com.bassi.tmapp.service.dto.MatchingTrademarktDto;

@Repository
public interface MatchingTmRepository{
	
//	@Query(value ="SELECT tm.*, t.* FROM published_tm tm inner join published_tm_phonetics ph on tm.id = ph.tm_id"
//			+ "inner join phonetics p on ph.pk = p.pk inner join trademark t on t.id = p.tm_id and tm.tm_class = t.tm_class"
//			+ "and tm.journal_no = ?1 and t.journal_no=?1", nativeQuery=true)
//	List<MatchingTrademarktDto> findAllMatchingTrademarksByClass(int journalNo);
//	
//	@Query(value ="SELECT tm.*, t.* FROM published_tm tm inner join published_tm_phonetics ph on tm.id = ph.tm_id"
//			+ "inner join phonetics p on ph.pk = p.pk inner join trademark t on t.id = p.tm_id"
//			+ "and tm.journal_no = ?2 and t.journal_no=?2"
//			+ "and tm.tmClass = ?1 and t.tmClass = ?1",nativeQuery=true)
//	List<MatchingTrademarktDto> findMatchingTrademarksByClassAndJournalNo(int tmClass, int journalNo);
//	
	
	
	
	
	
}
