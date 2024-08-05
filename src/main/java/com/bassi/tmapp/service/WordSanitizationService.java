package com.bassi.tmapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.bassi.tmapp.service.constants.StopWords;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

@Service
public class WordSanitizationService {
	
	private List<String> stopWords = StopWords.STOP_WORDS_LIST;
	Properties props;
	StanfordCoreNLP pipeline; 
	
	public WordSanitizationService() {
//		props = new Properties();
//	    props.setProperty("annotators", "tokenize,ssplit,pos");
//	    pipeline = new StanfordCoreNLP(props);
	}
	
	
	public String sanitizeWord(String word) {
		removeSpecialCharacters(word);
		removeStopWords(word);
//		persistProperNounsOnly(word);
		return word;
	}
	
	private String persistProperNounsOnly(String word) {
		List<String> properNouns = new ArrayList<>();
        CoreDocument document = pipeline.processToCoreDocument(word);
        for(CoreLabel token:document.tokens()) {
        	String pos = token.tag();
        	if(pos.equals("NNP") || pos.equals("NNPS")) {
        		properNouns.add(token.word());
        	}
        }
        return properNouns.stream().collect(Collectors.joining(" "));
        
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
