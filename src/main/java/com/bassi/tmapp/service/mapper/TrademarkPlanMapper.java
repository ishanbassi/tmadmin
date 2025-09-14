package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.TrademarkPlan;
import com.bassi.tmapp.service.dto.TrademarkPlanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TrademarkPlan} and its DTO {@link TrademarkPlanDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrademarkPlanMapper extends EntityMapper<TrademarkPlanDTO, TrademarkPlan> {}
