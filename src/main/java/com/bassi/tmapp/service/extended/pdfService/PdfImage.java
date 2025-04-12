package com.bassi.tmapp.service.extended.pdfService;

public class PdfImage {

    private String imageType;
    private byte[] imageContent;

    public PdfImage(String imageType, byte[] imageContent) {
        this.imageContent = imageContent;
        this.imageType = imageType;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public byte[] getImageContent() {
        return imageContent;
    }

    public void setImageContent(byte[] imageContent) {
        this.imageContent = imageContent;
    }
}
