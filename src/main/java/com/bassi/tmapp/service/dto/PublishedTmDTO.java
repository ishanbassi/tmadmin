package com.bassi.tmapp.service.dto;

import com.bassi.tmapp.domain.enumeration.HeadOffice;
import com.bassi.tmapp.domain.enumeration.TrademarkStatus;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * A DTO for the {@link com.bassi.tmapp.domain.PublishedTm} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PublishedTmDTO implements Serializable {
	
	 public static final String APPLICATION_NUMBER_DATE = "APPLICATION_NUMBER_DATE";
	public static final String AGENT_NAME_ADDRESS = "AGENT_NAME_ADDRESS";
	public static final String TRADEMARK_USAGE = "TRADEMARK_USAGE";
	public static final String TM_CLASS = "TM_CLASS";
	public static final String PROPRIETOR_NAME_ADDRESS = "PROPRIETOR_NAME_ADDRESS";
	public static final String HEAD_OFFICE = "HEAD_OFFICE";
	private  final Map<String,Integer> textIndexes = new HashMap<>();

    private Long id;

    private String name;

    private String details;

    private Long applicationNo;

    private LocalDate applicationDate;

    private String agentName;

    private String agentAddress;

    private String proprietorName;

    private String proprietorAddress;

    private HeadOffice headOffice;

    private String imgUrl;

    private Integer tmClass;

    private Integer journalNo;

    private Boolean deleted;

    private String usage;

    private String associatedTms;

    private TrademarkStatus trademarkStatus;
    
    private ZonedDateTime createdDate;

    private ZonedDateTime modifiedDate;
    
    
    public Map<String,Integer> getTextIndexMap() {
    	return this.textIndexes;
    }
    

	public void setTextIndexes(String key ,int value) {
		this.textIndexes.put(key, value);
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentAddress() {
        return agentAddress;
    }

    public void setAgentAddress(String agentAddress) {
        this.agentAddress = agentAddress;
    }

    public String getProprietorName() {
        return proprietorName;
    }

    public void setProprietorName(String proprietorName) {
        this.proprietorName = proprietorName;
    }

    public String getProprietorAddress() {
        return proprietorAddress;
    }

    public void setProprietorAddress(String proprietorAddress) {
        this.proprietorAddress = proprietorAddress;
    }

    public HeadOffice getHeadOffice() {
        return headOffice;
    }

    public void setHeadOffice(HeadOffice headOffice) {
        this.headOffice = headOffice;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getTmClass() {
        return tmClass;
    }

    public void setTmClass(Integer tmClass) {
        this.tmClass = tmClass;
    }

    public Integer getJournalNo() {
        return journalNo;
    }

    public void setJournalNo(Integer journalNo) {
        this.journalNo = journalNo;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getAssociatedTms() {
        return associatedTms;
    }

    public void setAssociatedTms(String associatedTms) {
        this.associatedTms = associatedTms;
    }

    public TrademarkStatus getTrademarkStatus() {
        return trademarkStatus;
    }

    public void setTrademarkStatus(TrademarkStatus trademarkStatus) {
        this.trademarkStatus = trademarkStatus;
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


	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PublishedTmDTO)) {
            return false;
        }

        PublishedTmDTO publishedTmDTO = (PublishedTmDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, publishedTmDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
    
    

   


	// prettier-ignore
    @Override
    public String toString() {
        return "PublishedTmDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", details='" + getDetails() + "'" +
            ", applicationNo=" + getApplicationNo() +
            ", applicationDate='" + getApplicationDate() + "'" +
            ", agentName='" + getAgentName() + "'" +
            ", agentAddress='" + getAgentAddress() + "'" +
            ", proprietorName='" + getProprietorName() + "'" +
            ", proprietorAddress='" + getProprietorAddress() + "'" +
            ", headOffice='" + getHeadOffice() + "'" +
            ", imgUrl='" + getImgUrl() + "'" +
            ", tmClass=" + getTmClass() +
            ", journalNo=" + getJournalNo() +
            ", deleted='" + getDeleted() + "'" +
            ", usage='" + getUsage() + "'" +
            ", associatedTms='" + getAssociatedTms() + "'" +
            ", trademarkStatus='" + getTrademarkStatus() + "'" +
            ", createdDate='" + getCreatedDate()+ "'"+
            ", modifiedDate='" + getModifiedDate()+ "'"+
            "}";
    }
}
