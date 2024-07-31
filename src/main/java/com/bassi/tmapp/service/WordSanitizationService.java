package com.bassi.tmapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.bassi.tmapp.service.constants.StopWords;

@Service
public class WordSanitizationService {
	
	private List<String> stopWords = StopWords.STOP_WORDS_LIST;
	
	
	
	public String sanitizeWord(String word) {
		return removeStopWords(removeSpecialCharacters(word));
	}
	
	private String removeStopWords(String word) {
		ArrayList<String> allWords = Stream.of(word.toLowerCase().split(" "))
				.collect(Collectors.toCollection(ArrayList<String>::new));
		
		allWords.removeAll(stopWords);
		return allWords.stream().collect(Collectors.joining(" "));
		
				
		
	}
	
	private String removeSpecialCharacters(String word) {
		return word.replaceAll("[–~`!@#$%^&*(){}\\[\\];:\"'<,.>?\\/\\\\|_+=-]", " ");
	}
}
