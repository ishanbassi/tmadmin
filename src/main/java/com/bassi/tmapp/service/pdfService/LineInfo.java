package com.bassi.tmapp.service.pdfService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.bassi.tmapp.service.dto.PublishedTmDTO;

public class LineInfo {
    private List<WordInfo> words;

    public LineInfo(List<WordInfo> words) {
        this.words = words;
    }

    public List<WordInfo> getWords() {
        return words;
    }
    
    public WordInfo getAllWordsFromSameLineWithInfo() {
    	if(words == null  || words.isEmpty()) {
    		return null;
    	}
    	List<String> textList = words.stream().map(word -> word.getText()).toList();
    	String text = String.join("", textList);
    	Optional<WordInfo> wordInfo = words.stream().findFirst();
    	
    	if(wordInfo.isPresent()) {
    		return wordInfo.get().setText(text);
    	}
    	return null;
    }
    
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LineInfo)) {
            return false;
        }

        LineInfo lineInfo = (LineInfo) o;
        if (this.words == null) {
            return false;
        }
        return Objects.equals(this.words, lineInfo.words);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.words);
    }

}


