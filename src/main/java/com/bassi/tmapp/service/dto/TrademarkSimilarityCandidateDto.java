package com.bassi.tmapp.service.dto;

import com.bassi.tmapp.domain.enumeration.TrademarkType;
import java.time.LocalDate;

public class TrademarkSimilarityCandidateDto {

    private Long id;
    private String normalizedName;
    private String name;
    private Long applicationNo;
    private Integer tmClass;
    private String imgUrl;
    private String proprietorName;
    private java.sql.Date applicationDate;
    private String details;
    private String url;
    private TrademarkType type;
    private double score;

    public TrademarkSimilarityCandidateDto(
        Long id,
        String normalizedName,
        String name,
        Long applicationNo,
        Integer tmClass,
        String imgUrl,
        String proprietorName,
        java.sql.Date applicationDate,
        String details,
        TrademarkType type
    ) {
        this.id = id;
        this.normalizedName = normalizedName;
        this.name = name;
        this.applicationNo = applicationNo;
        this.tmClass = tmClass;
        this.imgUrl = imgUrl;
        this.proprietorName = proprietorName;
        this.applicationDate = applicationDate;
        this.details = details;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(Long applicationNo) {
        this.applicationNo = applicationNo;
    }

    public Integer getTmClass() {
        return tmClass;
    }

    public void setTmClass(Integer tmClass) {
        this.tmClass = tmClass;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getProprietorName() {
        return proprietorName;
    }

    public void setProprietorName(String proprietorName) {
        this.proprietorName = proprietorName;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public java.sql.Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(java.sql.Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

    @Override
    public String toString() {
        return (
            "TrademarkSimilarityCandidateDto [id=" +
            id +
            ", normalizedName=" +
            normalizedName +
            ", name=" +
            name +
            ", applicationNo=" +
            applicationNo +
            ", tmClass=" +
            tmClass +
            ", imgUrl=" +
            imgUrl +
            ", proprietorName=" +
            proprietorName +
            ", applicationDate=" +
            applicationDate +
            ", score=" +
            score +
            ", getId()=" +
            getId() +
            ", getNormalizedName()=" +
            getNormalizedName() +
            ", getName()=" +
            getName() +
            ", getApplicationNo()=" +
            getApplicationNo() +
            ", getTmClass()=" +
            getTmClass() +
            ", getImgUrl()=" +
            getImgUrl() +
            ", getProprietorName()=" +
            getProprietorName() +
            ", getScore()=" +
            getScore() +
            ", getApplicationDate()=" +
            getApplicationDate() +
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
