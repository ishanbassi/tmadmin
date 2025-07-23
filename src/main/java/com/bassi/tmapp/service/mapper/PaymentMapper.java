package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.Lead;
import com.bassi.tmapp.domain.Payment;
import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.service.dto.LeadDTO;
import com.bassi.tmapp.service.dto.PaymentDTO;
import com.bassi.tmapp.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "lead", source = "lead", qualifiedByName = "leadId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    PaymentDTO toDto(Payment s);

    @Named("leadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LeadDTO toDtoLeadId(Lead lead);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
