package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.Documents;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.service.dto.DocumentsDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Documents} and its DTO {@link DocumentsDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentsMapper extends EntityMapper<DocumentsDTO, Documents> {
    @Mapping(target = "trademark", source = "trademark", qualifiedByName = "trademarkId")
    @Mapping(target = "userProfile", source = "userProfile", qualifiedByName = "userProfileId")
    DocumentsDTO toDto(Documents s);

    @Named("trademarkId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TrademarkDTO toDtoTrademarkId(Trademark trademark);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
