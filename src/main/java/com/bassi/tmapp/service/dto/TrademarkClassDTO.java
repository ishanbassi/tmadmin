package com.bassi.tmapp.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.bassi.tmapp.domain.TrademarkClass} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrademarkClassDTO implements Serializable {

    private Long id;

    private Integer code;

    private Integer tmClass;

    private String keyword;

    private String title;

    private String description;

    private ZonedDateTime createdDate;

    private ZonedDateTime modifiedDate;

    private Boolean deleted;

    private Set<TrademarkDTO> trademarks = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getTmClass() {
        return tmClass;
    }

    public void setTmClass(Integer tmClass) {
        this.tmClass = tmClass;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<TrademarkDTO> getTrademarks() {
        return trademarks;
    }

    public void setTrademarks(Set<TrademarkDTO> trademarks) {
        this.trademarks = trademarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrademarkClassDTO)) {
            return false;
        }

        TrademarkClassDTO trademarkClassDTO = (TrademarkClassDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, trademarkClassDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrademarkClassDTO{" +
            "id=" + getId() +
            ", code=" + getCode() +
            ", tmClass=" + getTmClass() +
            ", keyword='" + getKeyword() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", trademarks=" + getTrademarks() +
            "}";
    }
}
