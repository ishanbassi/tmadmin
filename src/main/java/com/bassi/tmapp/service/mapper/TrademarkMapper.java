package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.TmAgent;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.service.dto.TmAgentDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Trademark} and its DTO {@link TrademarkDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrademarkMapper extends EntityMapper<TrademarkDTO, Trademark> {
	
	@Named("tmAgentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TmAgentDTO toDtoTmAgentId(TmAgent tmAgent);
	
    @Mapping(target = "tmAgent", source = "tmAgent", qualifiedByName = "tmAgentId")
    TrademarkDTO toDto(Trademark s);

    
}
