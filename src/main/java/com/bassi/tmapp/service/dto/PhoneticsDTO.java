package com.bassi.tmapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bassi.tmapp.domain.Phonetics} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PhoneticsDTO implements Serializable {

    private Long id;

    private String sanitizedTm;

    private String phoneticPk;

    private String phoneticSk;

    private Boolean complete;

    private TrademarkDTO trademark;

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

    public TrademarkDTO getTrademark() {
        return trademark;
    }

    public void setTrademark(TrademarkDTO trademark) {
        this.trademark = trademark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhoneticsDTO)) {
            return false;
        }

        PhoneticsDTO phoneticsDTO = (PhoneticsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, phoneticsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PhoneticsDTO{" +
            "id=" + getId() +
            ", sanitizedTm='" + getSanitizedTm() + "'" +
            ", phoneticPk='" + getPhoneticPk() + "'" +
            ", phoneticSk='" + getPhoneticSk() + "'" +
            ", complete='" + getComplete() + "'" +
            ", trademark=" + getTrademark() +
            "}";
    }
}
