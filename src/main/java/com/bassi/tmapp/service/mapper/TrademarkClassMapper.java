package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.TrademarkClass;
import com.bassi.tmapp.service.dto.TrademarkClassDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TrademarkClass} and its DTO {@link TrademarkClassDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrademarkClassMapper extends EntityMapper<TrademarkClassDTO, TrademarkClass> {
    @Mapping(target = "trademarks", source = "trademarks", qualifiedByName = "trademarkIdSet")
    TrademarkClassDTO toDto(TrademarkClass s);

    @Mapping(target = "trademarks", ignore = true)
    @Mapping(target = "removeTrademarks", ignore = true)
    TrademarkClass toEntity(TrademarkClassDTO trademarkClassDTO);

    @Named("trademarkId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TrademarkDTO toDtoTrademarkId(Trademark trademark);

    @Named("trademarkIdSet")
    default Set<TrademarkDTO> toDtoTrademarkIdSet(Set<Trademark> trademark) {
        return trademark.stream().map(this::toDtoTrademarkId).collect(Collectors.toSet());
    }
}
