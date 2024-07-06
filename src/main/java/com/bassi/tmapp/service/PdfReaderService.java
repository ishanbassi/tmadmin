package com.bassi.tmapp.service;

import java.io.File;
import java.io.IOException;
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

@Service
public class PdfReaderService extends PDFTextStripper  {
	
	@Autowired
	private ResourceLoader resourceLoader;
	private static final Logger log = LoggerFactory.getLogger(CSVExportService.class);
	
	private static final float MIN_TM_HEIGHT = 23f;
	private static final float MAX_TM_HEIGHT = 24.5f;
	private static final Pattern tmClassPattern = Pattern.compile("Class\\s+(\\d{1,2})");
	private static final Pattern applicationDatePattern  = Pattern.compile("^\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d");
	private static final Pattern applicationNoPattern  = Pattern.compile("^(\\d{5,7})$");

	
 	public PdfReaderService() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        for (TextPosition text : textPositions) {
        	
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
	@EventListener(ApplicationReadyEvent.class)
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
	
}
