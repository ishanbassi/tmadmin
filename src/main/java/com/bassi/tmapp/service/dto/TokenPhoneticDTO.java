package com.bassi.tmapp.service.dto;

import com.bassi.tmapp.domain.enumeration.PhoneticAlgorithmType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bassi.tmapp.domain.TokenPhonetic} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TokenPhoneticDTO implements Serializable {

    private Long id;

    private PhoneticAlgorithmType algorithm;

    private String phoneticCode;

    private String secondaryPhoneticCode;

    private TrademarkTokenDTO trademarkToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PhoneticAlgorithmType getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(PhoneticAlgorithmType algorithm) {
        this.algorithm = algorithm;
    }

    public String getPhoneticCode() {
        return phoneticCode;
    }

    public void setPhoneticCode(String phoneticCode) {
        this.phoneticCode = phoneticCode;
    }

    public String getSecondaryPhoneticCode() {
        return secondaryPhoneticCode;
    }

    public void setSecondaryPhoneticCode(String secondaryPhoneticCode) {
        this.secondaryPhoneticCode = secondaryPhoneticCode;
    }

    public TrademarkTokenDTO getTrademarkToken() {
        return trademarkToken;
    }

    public void setTrademarkToken(TrademarkTokenDTO trademarkToken) {
        this.trademarkToken = trademarkToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TokenPhoneticDTO)) {
            return false;
        }

        TokenPhoneticDTO tokenPhoneticDTO = (TokenPhoneticDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tokenPhoneticDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TokenPhoneticDTO{" +
            "id=" + getId() +
            ", algorithm='" + getAlgorithm() + "'" +
            ", phoneticCode='" + getPhoneticCode() + "'" +
            ", secondaryPhoneticCode='" + getSecondaryPhoneticCode() + "'" +
            ", trademarkToken=" + getTrademarkToken() +
            "}";
    }
}
