package com.bassi.tmapp.service.mapper;

import org.mapstruct.Mapper;

import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.service.extended.dto.UserProfileDto;

@Mapper(componentModel = "spring")
public interface UserProfileMapper extends EntityMapper<UserProfile, UserProfileDto>{

}
