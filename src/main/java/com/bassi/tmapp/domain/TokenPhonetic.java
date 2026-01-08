package com.bassi.tmapp.domain;

import com.bassi.tmapp.domain.enumeration.PhoneticAlgorithmType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TokenPhonetic.
 */
@Entity
@Table(name = "token_phonetic")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TokenPhonetic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "algorithm")
    private PhoneticAlgorithmType algorithm;

    @Column(name = "phonetic_code")
    private String phoneticCode;

    @Column(name = "secondary_phonetic_code")
    private String secondaryPhoneticCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "trademark" }, allowSetters = true)
    private TrademarkToken trademarkToken;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TokenPhonetic id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PhoneticAlgorithmType getAlgorithm() {
        return this.algorithm;
    }

    public TokenPhonetic algorithm(PhoneticAlgorithmType algorithm) {
        this.setAlgorithm(algorithm);
        return this;
    }

    public void setAlgorithm(PhoneticAlgorithmType algorithm) {
        this.algorithm = algorithm;
    }

    public String getPhoneticCode() {
        return this.phoneticCode;
    }

    public TokenPhonetic phoneticCode(String phoneticCode) {
        this.setPhoneticCode(phoneticCode);
        return this;
    }

    public void setPhoneticCode(String phoneticCode) {
        this.phoneticCode = phoneticCode;
    }

    public String getSecondaryPhoneticCode() {
        return this.secondaryPhoneticCode;
    }

    public TokenPhonetic secondaryPhoneticCode(String secondaryPhoneticCode) {
        this.setSecondaryPhoneticCode(secondaryPhoneticCode);
        return this;
    }

    public void setSecondaryPhoneticCode(String secondaryPhoneticCode) {
        this.secondaryPhoneticCode = secondaryPhoneticCode;
    }

    public TrademarkToken getTrademarkToken() {
        return this.trademarkToken;
    }

    public void setTrademarkToken(TrademarkToken trademarkToken) {
        this.trademarkToken = trademarkToken;
    }

    public TokenPhonetic trademarkToken(TrademarkToken trademarkToken) {
        this.setTrademarkToken(trademarkToken);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TokenPhonetic)) {
            return false;
        }
        return getId() != null && getId().equals(((TokenPhonetic) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TokenPhonetic{" +
            "id=" + getId() +
            ", algorithm='" + getAlgorithm() + "'" +
            ", phoneticCode='" + getPhoneticCode() + "'" +
            ", secondaryPhoneticCode='" + getSecondaryPhoneticCode() + "'" +
            "}";
    }
}
