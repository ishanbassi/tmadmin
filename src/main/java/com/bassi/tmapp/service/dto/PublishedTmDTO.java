package com.bassi.tmapp.service.dto;

import com.bassi.tmapp.domain.TmAgent;
import com.bassi.tmapp.domain.enumeration.HeadOffice;
import com.bassi.tmapp.domain.enumeration.TrademarkStatus;
import com.bassi.tmapp.domain.enumeration.TrademarkType;

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
	
	public PublishedTmDTO() {}
	
	public PublishedTmDTO(PublishedTmDTO other) {
	    this.name = other.name;
	    this.details = other.details;
	    this.applicationDate = other.applicationDate;
	    this.applicationNo = other.applicationNo;
	    this.agentAddress = other.agentAddress;
	    this.agentName = other.agentName;
	    this.proprietorAddress = other.proprietorAddress;
	    this.proprietorName = other.proprietorName;
	    this.headOffice = other.headOffice;
	    this.imgUrl = other.imgUrl;
	    this.tmClass  = other.tmClass;
	    this.deleted = other.deleted;
	    this.journalNo = other.journalNo;
	    this.usage = other.usage;
	    this.associatedTms = other.associatedTms;
	    this.trademarkStatus = other.trademarkStatus;
	    this.createdDate = other.createdDate;
	    this.modifiedDate = other.modifiedDate;
	    this.pageNo = other.pageNo;
	    this.agent = other.agent;
	}
	

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

    private String trademarkStatus;
    
    private ZonedDateTime createdDate;

    private ZonedDateTime modifiedDate;
    
    private short pageNo;
    
    private TmAgentDTO agent;
    
    private String filePath;
    
    private String renewalDate;
    
    private TrademarkType trademarkType;
    
    
    public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

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
    	if(name != null) {
            this.name = name.strip();
    	}
    	else {
    		this.name = name;
    	}

    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
    	if(details != null) {
            this.details = details.strip();
    	}else {
    		this.details = details;
    	}

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
    	if(agentName != null) {
    		this.agentName = agentName.strip();
    	}
    	else {
    		this.agentName = agentName;
    	}
        
    }

    public String getAgentAddress() {
        return agentAddress;
    }

    public void setAgentAddress(String agentAddress) {
    	if(agentAddress != null) {
    		this.agentAddress = agentAddress.strip();
    	}
    	else {
    		this.agentAddress = agentAddress;
    	}
        
    }

    public String getProprietorName() {
        return proprietorName;
    }

    public void setProprietorName(String proprietorName) {
        if(proprietorName !=null) {
        	this.proprietorName = proprietorName.strip();
        }
        else {
        	this.proprietorName = proprietorName;
        }
    }

    public String getProprietorAddress() {
        return proprietorAddress;
    }

    public void setProprietorAddress(String proprietorAddress) {
    	if(proprietorAddress != null) {
    		this.proprietorAddress = proprietorAddress.strip();
    	}
    	else {
    		this.proprietorAddress = proprietorAddress;
    	}
        
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
    	if(usage != null) {
    		this.usage = usage.strip();
    	}
    	else {
    		this.usage = usage;
    	}
        
    }

    public String getAssociatedTms() {
        return associatedTms;
    }

    public void setAssociatedTms(String associatedTms) {
        if(associatedTms != null) {
        	this.associatedTms = associatedTms.strip();
        }
        else {
        	this.associatedTms = associatedTms;
        }
    }

    public String getTrademarkStatus() {
        return trademarkStatus;
    }

    public void setTrademarkStatus(String trademarkStatus) {
    	if(trademarkStatus != null) {
    		   this.trademarkStatus = trademarkStatus.strip()	;
    	}
    	else {
    		   this.trademarkStatus = trademarkStatus;
    	}
     
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
	
	


	public short getPageNo() {
		return pageNo;
	}


	public void setPageNo(short pageNo) {
		this.pageNo = pageNo;
	}
	
	
	
	


	public TmAgentDTO getAgent() {
		return agent;
	}

	public void setAgent(TmAgentDTO agent) {
		this.agent = agent;
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
    

	public String getRenewalDate() {
		return renewalDate;
	}

	public void setRenewalDate(String renewalDate) {
		this.renewalDate = renewalDate;
	}

	public TrademarkType getTrademarkType() {
		return trademarkType;
	}

	public void setTrademarkType(TrademarkType trademarkType) {
		this.trademarkType = trademarkType;
	}

	@Override
	public String toString() {
		return "PublishedTmDTO [textIndexes=" + textIndexes + ", id=" + id + ", name=" + name + ", details=" + details
				+ ", applicationNo=" + applicationNo + ", applicationDate=" + applicationDate + ", agentName="
				+ agentName + ", agentAddress=" + agentAddress + ", proprietorName=" + proprietorName
				+ ", proprietorAddress=" + proprietorAddress + ", headOffice=" + headOffice + ", imgUrl=" + imgUrl
				+ ", tmClass=" + tmClass + ", journalNo=" + journalNo + ", deleted=" + deleted + ", usage=" + usage
				+ ", associatedTms=" + associatedTms + ", trademarkStatus=" + trademarkStatus + ", createdDate="
				+ createdDate + ", modifiedDate=" + modifiedDate + ", pageNo=" + pageNo + ", agent=" + agent + "]";
	}
}
