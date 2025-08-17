package com.bassi.tmapp.service.extended.dto;

import com.bassi.tmapp.service.dto.DocumentsDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import java.util.Arrays;

public class TrademarkWithLogoDto {

    private TrademarkDTO trademark;
    private DocumentsDTO document;
    private byte[] file;
    private String trademarkSlogan;

    public TrademarkDTO getTrademark() {
        return trademark;
    }

    public void setTrademark(TrademarkDTO trademark) {
        this.trademark = trademark;
    }

    public DocumentsDTO getDocument() {
        return document;
    }

    public void setDocument(DocumentsDTO document) {
        this.document = document;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getTrademarkSlogan() {
        return trademarkSlogan;
    }

    public void setTrademarkSlogan(String trademarkSlogan) {
        this.trademarkSlogan = trademarkSlogan;
    }

    @Override
    public String toString() {
        return (
            "TrademarkWithLogoDto [trademark=" +
            trademark +
            ", document=" +
            document +
            ", file=" +
            Arrays.toString(file) +
            ", trademarkSlogan=" +
            trademarkSlogan +
            "]"
        );
    }
}
