package com.bassi.tmapp.service.pdfService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.bassi.tmapp.service.dto.PublishedTmDTO;

import lombok.ToString;

@ToString
public class LineInfo {
    private List<WordInfo> words;

    public LineInfo(List<WordInfo> words) {
        this.words = words;
    }

    public List<WordInfo> getWords() {
        return words;
    }
    
    public String getAllWordsFromSameLineWithInfo() {
    	if(words == null  || words.isEmpty()) {
    		return null;
    	}
    	List<String> textList = words.stream().map(WordInfo::getText).toList();
    	return String.join("", textList);
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


