package com.bassi.tmapp.service.pdfService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

public class CustomTextExtractionStrategy implements IEventListener {
    private final Logger log = LoggerFactory.getLogger(CustomTextExtractionStrategy.class);
    
	private List<LineInfo> lines = new ArrayList<>();
	private PdfImage pdfImage; 
    private List<TextRenderInfo> currentLine = new ArrayList<>();
    private Vector lastBaseLine;
    

	@Override
	public void eventOccurred(IEventData data, EventType type) {
		if(type == EventType.RENDER_TEXT) {
			
			
			TextRenderInfo renderInfo = (TextRenderInfo) data;
			
//			to preserve the graphic state
			renderInfo.preserveGraphicsState();
			
			Vector currentBaseLine = renderInfo.getBaseline().getStartPoint();
			
			if(lastBaseLine == null || areSameLine(currentBaseLine)) {
				currentLine.add(renderInfo);
			}
			
			else {
				processLine(currentLine);
				currentLine.clear();
				currentLine.add(renderInfo);
				
			}
			lastBaseLine = currentBaseLine;
		}
		if(type == EventType.RENDER_IMAGE) {
	        ImageRenderInfo img = (ImageRenderInfo) data;
	        img.preserveGraphicsState();
	        
        	PdfImageXObject imgObject = img.getImage();
        	String imageType = imgObject.identifyImageFileExtension();
        	byte[] imageContent = imgObject.getImageBytes();
        	pdfImage = new PdfImage(imageType, imageContent);
		}
	}


	public void processLine(List<TextRenderInfo> textInfos) {
		 if (textInfos.isEmpty()) return;

	        List<WordInfo> words = new ArrayList<>();
	        for (TextRenderInfo info : textInfos) {
	            String text = info.getText();
	            Vector start = info.getBaseline().getStartPoint();
	            Vector end = info.getAscentLine().getEndPoint();
	            float x = start.get(Vector.I1);
	            float y = start.get(Vector.I2);
	            float width = end.get(Vector.I1) - x;
	            float height = end.get(Vector.I2) - info.getDescentLine().getEndPoint().get(Vector.I2);
	            String font = info.getFont().getFontProgram().toString();
	            float fontSize = info.getFontSize();

	            WordInfo wordInfo = new WordInfo(text, x, y, width, height, font, fontSize);
	            words.add(wordInfo);
	        }

	        LineInfo lineInfo = new LineInfo(words);
	        lines.add(lineInfo);
}

	private boolean areSameLine(Vector currentBaseLine) {
		final float tolerance = 1.0f;
		return Math.abs(lastBaseLine.get(Vector.I2) - currentBaseLine.get(Vector.I2)) < tolerance;
}

	public List<LineInfo> getLines() {
        return lines;
    }
	
	public PdfImage getImage() {
		return pdfImage;
	}


	@Override
	public Set<EventType> getSupportedEvents() {
		return null;
	}
	
}

