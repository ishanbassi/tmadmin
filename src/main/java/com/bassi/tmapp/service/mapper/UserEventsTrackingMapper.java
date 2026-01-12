package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.UserEventsTracking;
import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.service.dto.UserEventsTrackingDTO;
import com.bassi.tmapp.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserEventsTracking} and its DTO {@link UserEventsTrackingDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserEventsTrackingMapper extends EntityMapper<UserEventsTrackingDTO, UserEventsTracking> {
    @Mapping(target = "userProfile", source = "userProfile", qualifiedByName = "userProfileId")
    UserEventsTrackingDTO toDto(UserEventsTracking s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
