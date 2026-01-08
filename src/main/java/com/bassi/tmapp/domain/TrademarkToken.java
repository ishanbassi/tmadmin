package com.bassi.tmapp.domain;

import com.bassi.tmapp.domain.enumeration.TrademarkTokenType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TrademarkToken.
 */
@Entity
@Table(name = "trademark_token")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrademarkToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "token_text")
    private String tokenText;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type")
    private TrademarkTokenType tokenType;

    @Column(name = "position")
    private Integer position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "lead", "user", "trademarkPlan", "trademarkClasses", "documents" }, allowSetters = true)
    private Trademark trademark;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TrademarkToken id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenText() {
        return this.tokenText;
    }

    public TrademarkToken tokenText(String tokenText) {
        this.setTokenText(tokenText);
        return this;
    }

    public void setTokenText(String tokenText) {
        this.tokenText = tokenText;
    }

    public TrademarkTokenType getTokenType() {
        return this.tokenType;
    }

    public TrademarkToken tokenType(TrademarkTokenType tokenType) {
        this.setTokenType(tokenType);
        return this;
    }

    public void setTokenType(TrademarkTokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getPosition() {
        return this.position;
    }

    public TrademarkToken position(Integer position) {
        this.setPosition(position);
        return this;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Trademark getTrademark() {
        return this.trademark;
    }

    public void setTrademark(Trademark trademark) {
        this.trademark = trademark;
    }

    public TrademarkToken trademark(Trademark trademark) {
        this.setTrademark(trademark);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrademarkToken)) {
            return false;
        }
        return getId() != null && getId().equals(((TrademarkToken) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrademarkToken{" +
            "id=" + getId() +
            ", tokenText='" + getTokenText() + "'" +
            ", tokenType='" + getTokenType() + "'" +
            ", position=" + getPosition() +
            "}";
    }
}
