package com.bassi.tmapp.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.service.dto.PublishedTmDTO;

@Service
public class PdfReaderService extends PDFTextStripper  {
	
	@Autowired
	private ResourceLoader resourceLoader;
	private static final Logger log = LoggerFactory.getLogger(PdfReaderService.class);
	
	private static final float MIN_TM_HEIGHT = 23;
	private static final float MAX_TM_HEIGHT = 24.5f;
	private static final float MIN_APPLICATION_NO_HEIGHT = 11;
	private static final float MAX_APPLICATION_NO_HEIGHT = 13;
	private static final Pattern tmClassPattern = Pattern.compile("Class\\s+(\\d{1,2})");
	private static final Pattern applicationDatePattern  = Pattern.compile("^\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d");
	private static final Pattern applicationNoPattern  = Pattern.compile("^(\\d{5,7})$");

	private List<PublishedTmDTO> publishedTrademarks = new ArrayList<>();
	
	private Integer currentPageNo;
	
 	public PdfReaderService() throws IOException {
		super();
	}

	@Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
		PublishedTmDTO publishedTmDTO = new PublishedTmDTO();
		String trademark = null , details, agentName, agentAddress, proprietorName, proprietorAddress, usage, associatedTrademarks;
		Integer tmClass;
		Long applicationNo;
		
        for (TextPosition text : textPositions) {
        	if(isTrademark(text)) {
        		if(publishedTmDTO.getName() != null) {
        			publishedTmDTO.setName(publishedTmDTO.getName().concat(text.getUnicode()));
        			
        		}else {
        			publishedTmDTO.setName(text.getUnicode());
        		}
        	}
        	
        	System.out.println("String[" +
    				text.getXDirAdj() + "," +
                    text.getYDirAdj() +
                    " fs=" + text.getFontSize() +
                    " xscale=" + text.getXScale() + 
                    " yscale=" + text.getYScale() + 
                    " height=" + text.getHeightDir() +
                    " width=" + text.getWidthDirAdj() + "] " +
                    " X= " + text.getX() +
                    " Y= " + text.getY() + 
                   " Dir =" +  text.getDir() +
                   " Text Matrix=" +  text.getTextMatrix() + 
                   " Text =" +  text.getUnicode());
        }
    } 
    @Override
    public  void processPage(PDPage page) throws IOException {
    	currentPageNo = getCurrentPageNo();
        super.processPage(page);
    }
    @Override
    protected void writeLineSeparator() throws IOException {
        super.writeLineSeparator();
        
    }
//	@EventListener(ApplicationReadyEvent.class)
	public void readPdf() throws IOException{
		Resource resource = resourceLoader.getResource("classpath:pdfs/1-16.pdf");
		PDDocument document = PDDocument.load(resource.getFile());
		PdfReaderService stripper = new PdfReaderService();
        stripper.setSortByPosition(true);
        stripper.setStartPage(11);
        stripper.setEndPage(11);
        stripper.getText(document);
        document.close();
		
	}
	
	public boolean isTrademark(TextPosition textPosition) {
		return false;
	}
}
