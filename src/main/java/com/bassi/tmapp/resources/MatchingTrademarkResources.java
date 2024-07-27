package com.bassi.tmapp.resources;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bassi.tmapp.service.MatchingTrademarkService;
import com.bassi.tmapp.service.dto.MatchingTrademarktDto;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/matching/trademarks")
public class MatchingTrademarkResources {
	
	private MatchingTrademarkService matchingTrademarkService;
	MatchingTrademarkResources(
			MatchingTrademarkService matchingTrademarkService ){
		this.matchingTrademarkService  = matchingTrademarkService;
	}
	
	
	
	
	@GetMapping(path = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> getMethodName(@RequestParam Integer journalNo, @RequestBody List<MatchingTrademarktDto> matchingTrademarkExportDtoList) {
		String fileName = "Trademark-Journal-No-" + journalNo + ".csv";
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        byte[] csvBytes = matchingTrademarkService.exportTrademarks(matchingTrademarkExportDtoList);
        return ResponseEntity.ok().headers(headers).body(csvBytes);
	}
}
