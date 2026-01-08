package com.bassi.tmapp.service.dto;

import com.bassi.tmapp.domain.enumeration.TrademarkTokenType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bassi.tmapp.domain.TrademarkToken} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrademarkTokenDTO implements Serializable {

    private Long id;

    private String tokenText;

    private TrademarkTokenType tokenType;

    private Integer position;

    private TrademarkDTO trademark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenText() {
        return tokenText;
    }

    public void setTokenText(String tokenText) {
        this.tokenText = tokenText;
    }

    public TrademarkTokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TrademarkTokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
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
        if (!(o instanceof TrademarkTokenDTO)) {
            return false;
        }

        TrademarkTokenDTO trademarkTokenDTO = (TrademarkTokenDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, trademarkTokenDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrademarkTokenDTO{" +
            "id=" + getId() +
            ", tokenText='" + getTokenText() + "'" +
            ", tokenType='" + getTokenType() + "'" +
            ", position=" + getPosition() +
            ", trademark=" + getTrademark() +
            "}";
    }
}
