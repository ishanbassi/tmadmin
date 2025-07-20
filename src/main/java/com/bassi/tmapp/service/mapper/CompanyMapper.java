package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.Company;
import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.service.dto.CompanyDTO;
import com.bassi.tmapp.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    CompanyDTO toDto(Company s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
