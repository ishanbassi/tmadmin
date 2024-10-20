package com.bassi.tmapp.service.extended.dto;

import com.bassi.tmapp.domain.TmAgent;
import com.bassi.tmapp.service.dto.AdminUserDTO;

public class AccountDto {
	private AdminUserDTO user;
	
	public AdminUserDTO getUser() {
		return user;
	}
	public void setUser(AdminUserDTO user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "AccountDto [user=" + user + "]";
	}
	
	
	

}
