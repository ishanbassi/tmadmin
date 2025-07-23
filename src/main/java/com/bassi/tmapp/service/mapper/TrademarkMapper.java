package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.Lead;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.service.dto.LeadDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Trademark} and its DTO {@link TrademarkDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrademarkMapper extends EntityMapper<TrademarkDTO, Trademark> {
    @Mapping(target = "lead", source = "lead", qualifiedByName = "leadId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    TrademarkDTO toDto(Trademark s);

    @Named("leadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LeadDTO toDtoLeadId(Lead lead);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
