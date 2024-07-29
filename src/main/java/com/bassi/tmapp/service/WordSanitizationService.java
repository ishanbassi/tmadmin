package com.bassi.tmapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.bassi.tmapp.service.constants.StopWords;

@Service
public class WordSanitizationService {
	
	public String removeStopWords(String word) {
		List<String> = Stream.of(word.split(" "))
				.collect(Collectors.toCollection(ArrayList<String>::new));
				
		
	}
	
	public String removeSpecialCharacters(String word) {
		return null;
	}
}
