package com.bassi.tmapp.service.dto;

public interface MatchingTrademarkView {
	String getMatchingTrademark();
	String getRegisteredTrademark();
	Integer getTmClass();
	Long getApplicationNo();
	String getDetails();
	Integer getJournalNo();
	String getProprietorName();
	String getProprietorAddress();
	String getAgentName();
	String getAgentAddress();
}
