package com.bassi.tmapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bassi.tmapp.domain.TmAgent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TmAgentDTO implements Serializable {

    private Long id;

    private Integer agentCode;

    private String firstName;

    private String lastName;

    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(Integer agentCode) {
        this.agentCode = agentCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TmAgentDTO)) {
            return false;
        }

        TmAgentDTO tmAgentDTO = (TmAgentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tmAgentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TmAgentDTO{" +
            "id=" + getId() +
            ", agentCode=" + getAgentCode() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
