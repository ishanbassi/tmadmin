package com.bassi.tmapp.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;


public class PdfReaderService extends PDFTextStripper  {
	
	@Autowired
	private ResourceLoader resourceLoader;


	
 	public PdfReaderService() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        for (TextPosition text : textPositions) {
            System.out.println("String[" + text.getXDirAdj() + "," +
                    text.getYDirAdj() + " fs=" + text.getFontSize() + " xscale=" + text.getXScale() + " height=" + text.getHeightDir() +
                    " width=" + text.getWidthDirAdj() + "] " + text.getUnicode());
        }
    } 
	
	public void readPdf(String path) throws IOException{
		Resource resource = resourceLoader.getResource(path);
		PDDocument document = PDDocument.load(resource.getFile());
		PdfReaderService stripper = new PdfReaderService();
        stripper.setSortByPosition(true);
        stripper.setStartPage(0);
        stripper.setEndPage(document.getNumberOfPages());
        stripper.getText(document);
        document.close();
		
	}
}
