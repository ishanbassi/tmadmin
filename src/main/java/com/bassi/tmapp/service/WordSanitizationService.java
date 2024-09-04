package com.bassi.tmapp.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.aspectj.apache.bcel.util.ClassPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.bassi.tmapp.service.constants.StopWords;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;

@Service
public class WordSanitizationService {
	
	private List<String> stopWords = StopWords.STOP_WORDS_LIST;
	POSModel posModel;
	
	public WordSanitizationService()  {
	}
	
	
	private void loadModel() {
		try {
			InputStream in = new ClassPathResource("en-pos-maxent.bin").getInputStream();
	        posModel = new POSModel(in);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}


	public String sanitizeWord(String word) {
		word = removeSpecialCharacters(word);
		word = removeStopWords(word);
		
		return word;
	}
	
	
	

	private void extractProperNouns(String name)  {
		 // Tokenize the text
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(name);

        // Load the POS model

        POSTaggerME posTagger = new POSTaggerME(posModel);

        // Tag the tokens
        String[] posTags = posTagger.tag(tokens);
        posTagger.getAllPosTags();

        // Extract proper nouns
        List<String> properNouns = new ArrayList<>();
        for (int i = 0; i < tokens.length; i++) {
            if (posTags[i].equals("NNP") || posTags[i].equals("NNPS")) {
                properNouns.add(tokens[i]);
            }
        }

        // Output the result
        System.out.println("Proper Nouns: " + properNouns);
    }


	private String removeStopWords(String word) {
		ArrayList<String> allWords = Stream.of(word.toLowerCase().split(" "))
				.collect(Collectors.toCollection(ArrayList<String>::new));
		
		allWords.removeAll(stopWords);
		return allWords.stream().collect(Collectors.joining(" "));
		
				
		
	}
	
	private String removeSpecialCharacters(String word) {
		return word.replaceAll("[–~`!@#$%^&*(){}\\[\\];:\"'<,.>?\\/\\\\|_+=-]", "");
	}
}
