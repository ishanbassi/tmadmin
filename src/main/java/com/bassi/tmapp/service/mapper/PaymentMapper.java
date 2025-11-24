package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.Payment;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.service.dto.PaymentDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "trademark", source = "trademark", qualifiedByName = "trademarkId")
    @Mapping(target = "userProfile", source = "userProfile", qualifiedByName = "userProfileId")
    PaymentDTO toDto(Payment s);

    @Named("trademarkId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TrademarkDTO toDtoTrademarkId(Trademark trademark);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
