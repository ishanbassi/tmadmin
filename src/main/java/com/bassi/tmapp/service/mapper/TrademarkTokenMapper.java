package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.TrademarkToken;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.dto.TrademarkTokenDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TrademarkToken} and its DTO {@link TrademarkTokenDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrademarkTokenMapper extends EntityMapper<TrademarkTokenDTO, TrademarkToken> {
    @Mapping(target = "trademark", source = "trademark", qualifiedByName = "trademarkId")
    TrademarkTokenDTO toDto(TrademarkToken s);

    @Named("trademarkId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TrademarkDTO toDtoTrademarkId(Trademark trademark);
}
