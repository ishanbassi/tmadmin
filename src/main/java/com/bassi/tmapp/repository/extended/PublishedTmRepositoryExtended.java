package com.bassi.tmapp.repository.extended;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bassi.tmapp.domain.PublishedTm;

/**
 * Spring Data JPA repository for the PublishedTm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PublishedTmRepositoryExtended extends JpaRepository<PublishedTm, Long>, JpaSpecificationExecutor<PublishedTm> {
	
	@Query(value = "WITH published AS"
			+ "(SELECT tm.*, ph.phonetic_pk, ph.complete FROM published_tm tm INNER JOIN published_tm_phonetics ph on tm.id = ph.published_tm_id WHERE journal_no = ?1),"
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

	
	@Query(value="select tm.* FROM published_tm tm where name is not  null and journal_no = ?1 "
			+ "and tm.id NOT IN (SELECT published_tm_id FROM published_tm_phonetics)", nativeQuery=true)
	List<PublishedTm> findTrademarksWherePhoneticsMissing(int journalNo);

	
	@Modifying
	@Query(value="UPDATE PublishedTm tm SET tm.deleted=true WHERE tm.journalNo = ?1")
	void softDeleteByJournalNo(Integer journalNo);
	
	@Modifying
	@Query(value="DELETE FROM PublishedTm tm  WHERE tm.journalNo = ?1")
	void deleteByJournalNo(Integer journalNo);

	
	
	
	

	

}
