package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.service.extended.dto.UserProfileDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper extends EntityMapper<UserProfile, UserProfileDto> {}
