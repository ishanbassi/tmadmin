package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.TrademarkClass;
import com.bassi.tmapp.service.dto.TrademarkClassDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TrademarkClass} and its DTO {@link TrademarkClassDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrademarkClassMapper extends EntityMapper<TrademarkClassDTO, TrademarkClass> {}
