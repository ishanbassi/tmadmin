package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.Phonetics;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.service.dto.PhoneticsDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Phonetics} and its DTO {@link PhoneticsDTO}.
 */
@Mapper(componentModel = "spring")
public interface PhoneticsMapper extends EntityMapper<PhoneticsDTO, Phonetics> {
    @Mapping(target = "trademark", source = "trademark", qualifiedByName = "trademarkId")
    PhoneticsDTO toDto(Phonetics s);

    @Named("trademarkId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TrademarkDTO toDtoTrademarkId(Trademark trademark);
}
