package com.bassi.tmapp.web.rest;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bassi.tmapp.service.MatchingTrademarkService;

@RestController
@RequestMapping("/api/matching/trademarks")
public class MatchingTrademarkResources {
	
	private MatchingTrademarkService matchingTrademarkService;
	MatchingTrademarkResources(
			MatchingTrademarkService matchingTrademarkService ){
		this.matchingTrademarkService  = matchingTrademarkService;
	}
	
	
	
	
	@GetMapping(path = "/download/{journalNo}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> download (@PathVariable("journalNo") int journalNo) {
		String fileName = "Trademark-Journal-No-" + journalNo + ".csv";
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        byte[] csvBytes = matchingTrademarkService.exportTrademarks(journalNo);
        return ResponseEntity.ok().headers(headers).body(csvBytes);
	}
	
	@GetMapping(path="/find/{class}/{journalNo}")
	public ResponseEntity<List<Object>> findAllMatchingTrademarksByClass(
			@PathVariable("class") int tmClass, @PathVariable("journalNo") int journalNo) {
		List<Object> matchingTrademarks = matchingTrademarkService.findAllMatchingTrademarksByClass(tmClass, journalNo);
		return ResponseEntity.ok(matchingTrademarks);
	}
}
