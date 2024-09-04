package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.service.dto.MatchingTrademarktDto;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PublishedTm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PublishedTmRepository extends JpaRepository<PublishedTm, Long>, JpaSpecificationExecutor<PublishedTm> {
	
//	@Query("SELECT tm.*, t.* FROM published_tm tm inner join published_tm_phonetics ph on tm.id = ph.tm_id"
//			+ "inner join phonetics p on ph.pk = p.pk inner join trademark t on t.id = p.tm_id and tm.tm_class = t.tm_class"
//			+ "and tm.journal_no = ?1 and t.journal_no=?1")
//	List<MatchingTrademarktDto> findAllMatchingTrademarksByClass(int journalNo);
//	
//	@Query("SELECT tm.*, t.* FROM published_tm tm inner join published_tm_phonetics ph on tm.id = ph.tm_id"
//			+ "inner join phonetics p on ph.pk = p.pk inner join trademark t on t.id = p.tm_id"
//			+ "and tm.journal_no = ?2 and t.journal_no=?2"
//			+ "and tm.tmClass = ?1 and t.tmClass = ?1")
//	List<MatchingTrademarktDto> findMatchingTrademarksByClassAndJournalNo(int tmClass, int journalNo);
	
	@Query(value = "WITH published AS"
			+ "(SELECT tm.name, ph.phonetic_pk, ph.complete FROM published_tm tm INNER JOIN published_tm_phonetics ph on tm.id = ph.published_tm_id WHERE journal_no = ?1),"
			+ "registered AS"
			+ "(SELECT tm.name , ph.phonetic_pk, ph.complete FROM trademark tm INNER JOIN phonetics ph on tm.id = ph.trademark_id)"
			+ "SELECT tm.name as published, t.name as registeredTrademark FROM published tm  INNER JOIN registered t on tm.phonetic_pk = t.phonetic_pk", nativeQuery=true)
	List<Object> findAllMatchingTrademarksByJournal(int journalNo);
	
	@Query(value = "WITH published AS"
			+ "(SELECT tm.name, ph.phonetic_pk, ph.complete FROM published_tm tm INNER JOIN published_tm_phonetics ph on tm.id = ph.published_tm_id WHERE journal_no = ?1 and tm_class = ?2),"
			+ "registered AS"
			+ "(SELECT tm.name , ph.phonetic_pk, ph.complete FROM trademark tm INNER JOIN phonetics ph on tm.id = ph.trademark_id WHERE tm_class = ?2)"
			+ "SELECT tm.name as published, t.name as registeredTrademark FROM published tm  INNER JOIN registered t on tm.phonetic_pk = t.phonetic_pk", nativeQuery = true)
	List<Object> findAllMatchingTrademarksByJournalAndClass(int journalNo, int tmClass);

	

}
