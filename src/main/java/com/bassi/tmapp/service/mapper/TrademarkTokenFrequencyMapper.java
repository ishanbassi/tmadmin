package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.TrademarkTokenFrequency;
import com.bassi.tmapp.service.dto.TrademarkTokenFrequencyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TrademarkTokenFrequency} and its DTO {@link TrademarkTokenFrequencyDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrademarkTokenFrequencyMapper extends EntityMapper<TrademarkTokenFrequencyDTO, TrademarkTokenFrequency> {}
