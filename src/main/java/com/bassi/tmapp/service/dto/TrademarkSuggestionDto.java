package com.bassi.tmapp.service.dto;

import com.bassi.tmapp.domain.enumeration.TrademarkType;
import java.time.LocalDate;

public class TrademarkSuggestionDto {

    private String name;
    private Integer tmClass;
    private String details;
    private Long applicationNo;
    private String imgUrl;
    private TrademarkType type;
    private String url;
    private String proprietorName;
    private java.sql.Date applicationDate;

    public TrademarkSuggestionDto(String name, String details, Long applicationNo, Integer tmClass, String imgUrl, String type) {
        super();
        this.name = name;
        this.tmClass = tmClass;
        this.details = details;
        this.applicationNo = applicationNo;
        this.imgUrl = imgUrl;
        if (type != null) {
            try {
                this.type = TrademarkType.valueOf(type.toUpperCase());
            } catch (Exception e) {
                this.type = null;
            }
        }
    }

    public TrademarkSuggestionDto(
        String name,
        String details,
        Long applicationNo,
        Integer tmClass,
        String imgUrl,
        String type,
        String proprietorName,
        java.sql.Date applicationDate
    ) {
        super();
        this.name = name;
        this.tmClass = tmClass;
        this.details = details;
        this.applicationNo = applicationNo;
        this.imgUrl = imgUrl;
        this.proprietorName = proprietorName;
        this.applicationDate = applicationDate;
        if (type != null) {
            try {
                this.type = TrademarkType.valueOf(type.toUpperCase());
            } catch (Exception e) {
                this.type = null;
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTmClass() {
        return tmClass;
    }

    public void setTmClass(Integer tmClass) {
        this.tmClass = tmClass;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(Long applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TrademarkType getType() {
        return type;
    }

    public void setType(TrademarkType type) {
        this.type = type;
    }

    public String getProprietorName() {
        return proprietorName;
    }

    public void setProprietorName(String proprietorName) {
        this.proprietorName = proprietorName;
    }

    public java.sql.Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(java.sql.Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    @Override
    public String toString() {
        return (
            "TrademarkSuggestionDto [name=" +
            name +
            ", tmClass=" +
            tmClass +
            ", details=" +
            details +
            ", applicationNo=" +
            applicationNo +
            ", imgUrl=" +
            imgUrl +
            ", url=" +
            url +
            ", getName()=" +
            getName() +
            ", getTmClass()=" +
            getTmClass() +
            ", getDetails()=" +
            getDetails() +
            ", getApplicationNo()=" +
            getApplicationNo() +
            ", getImgUrl()=" +
            getImgUrl() +
            ", getUrl()=" +
            getUrl() +
            ", getClass()=" +
            getClass() +
            ", hashCode()=" +
            hashCode() +
            ", toString()=" +
            super.toString() +
            "]"
        );
    }
}
