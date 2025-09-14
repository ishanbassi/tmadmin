package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.Payment;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.service.dto.PaymentDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "trademark", source = "trademark", qualifiedByName = "trademarkId")
    PaymentDTO toDto(Payment s);

    @Named("trademarkId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TrademarkDTO toDtoTrademarkId(Trademark trademark);
}
