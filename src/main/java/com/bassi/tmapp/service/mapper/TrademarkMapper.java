
package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.TmAgent;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.service.dto.TmAgentDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.extended.dto.UserProfileDto;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Trademark} and its DTO {@link TrademarkDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrademarkMapper extends EntityMapper<TrademarkDTO, Trademark> {
	
	
	@Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDto toDtoUserProfileId(UserProfile userProfile);
	
    @Mapping(target = "userProfile", source = "userProfile", qualifiedByName = "userProfileId")
    TrademarkDTO toDto(Trademark s);

    
}
