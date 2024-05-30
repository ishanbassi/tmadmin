package com.bassi.tmapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bassi.tmapp.domain.PublishedTmPhonetics} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PublishedTmPhoneticsDTO implements Serializable {

    private Long id;

    private String sanitizedTm;

    private String phoneticPk;

    private String phoneticSk;

    private Boolean complete;

    private PublishedTmDTO publishedTm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSanitizedTm() {
        return sanitizedTm;
    }

    public void setSanitizedTm(String sanitizedTm) {
        this.sanitizedTm = sanitizedTm;
    }

    public String getPhoneticPk() {
        return phoneticPk;
    }

    public void setPhoneticPk(String phoneticPk) {
        this.phoneticPk = phoneticPk;
    }

    public String getPhoneticSk() {
        return phoneticSk;
    }

    public void setPhoneticSk(String phoneticSk) {
        this.phoneticSk = phoneticSk;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public PublishedTmDTO getPublishedTm() {
        return publishedTm;
    }

    public void setPublishedTm(PublishedTmDTO publishedTm) {
        this.publishedTm = publishedTm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PublishedTmPhoneticsDTO)) {
            return false;
        }

        PublishedTmPhoneticsDTO publishedTmPhoneticsDTO = (PublishedTmPhoneticsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, publishedTmPhoneticsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PublishedTmPhoneticsDTO{" +
            "id=" + getId() +
            ", sanitizedTm='" + getSanitizedTm() + "'" +
            ", phoneticPk='" + getPhoneticPk() + "'" +
            ", phoneticSk='" + getPhoneticSk() + "'" +
            ", complete='" + getComplete() + "'" +
            ", publishedTm=" + getPublishedTm() +
            "}";
    }
}
