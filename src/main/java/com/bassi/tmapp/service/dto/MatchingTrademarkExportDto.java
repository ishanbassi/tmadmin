package com.bassi.tmapp.service.dto;



public class MatchingTrademarkExportDto {
	private PublishedTmDTO publishedTmDTO;
	
	private TrademarkDTO trademarkDto;

	public PublishedTmDTO getPublishedTmDTO() {
		return publishedTmDTO;
	}

	public void setPublishedTmDTO(PublishedTmDTO publishedTmDTO) {
		this.publishedTmDTO = publishedTmDTO;
	}

	public TrademarkDTO getTrademarkDto() {
		return trademarkDto;
	}

	public void setTrademarkDto(TrademarkDTO trademarkDto) {
		this.trademarkDto = trademarkDto;
	}
	
	
}
