package com.bassi.tmapp.service.extended.dto;

import com.bassi.tmapp.domain.TmAgent;
import com.bassi.tmapp.service.dto.AdminUserDTO;

public class AccountDto {
	private TmAgent tmAgent;
	private AdminUserDTO user;
	
	public TmAgent getTmAgent() {
		return tmAgent;
	}
	public void setTmAgent(TmAgent tmAgent) {
		this.tmAgent = tmAgent;
	}
	public AdminUserDTO getUser() {
		return user;
	}
	public void setUser(AdminUserDTO user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "AccountDto [tmAgent=" + tmAgent + ", user=" + user + "]";
	}
	
	
	

}
