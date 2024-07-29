package com.bassi.tmapp.service.pdfService;

import java.util.Objects;

import lombok.ToString;

@ToString
public class WordInfo {
    private String text;
    private  String fontFamily;
    private  float fontSize;
    private  float spaceWidth;
    private float width;
    private  float height;
    private float x;
    private float y;
	public String getText() {
		return text;
	}
	WordInfo(String text, float x, float y , float width , float height, String fontFamily, float fontSize) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height=  height;
		this.fontFamily  = fontFamily;
		this.fontSize = fontSize;
	}
	
	public WordInfo setText(String text) {
		this.text = text;
		return this;
	}
	public String getFontFamily() {
		return fontFamily;
	}
	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}
	public float getFontSize() {
		return fontSize;
	}
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}
	public float getSpaceWidth() {
		return spaceWidth;
	}
	public void setSpaceWidth(float spaceWidth) {
		this.spaceWidth = spaceWidth;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WordInfo)) {
            return false;
        }

        WordInfo wordInfo = (WordInfo) o;
        if (this.text == null) {
            return false;
        }
        return Objects.equals(this.text, wordInfo.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.text);
    }
    
}
