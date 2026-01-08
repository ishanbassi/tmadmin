package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.TokenPhonetic;
import com.bassi.tmapp.domain.TrademarkToken;
import com.bassi.tmapp.service.dto.TokenPhoneticDTO;
import com.bassi.tmapp.service.dto.TrademarkTokenDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TokenPhonetic} and its DTO {@link TokenPhoneticDTO}.
 */
@Mapper(componentModel = "spring")
public interface TokenPhoneticMapper extends EntityMapper<TokenPhoneticDTO, TokenPhonetic> {
    @Mapping(target = "trademarkToken", source = "trademarkToken", qualifiedByName = "trademarkTokenId")
    TokenPhoneticDTO toDto(TokenPhonetic s);

    @Named("trademarkTokenId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TrademarkTokenDTO toDtoTrademarkTokenId(TrademarkToken trademarkToken);
}
