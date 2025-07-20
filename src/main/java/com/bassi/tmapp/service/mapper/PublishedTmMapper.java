package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.domain.TmAgent;
import com.bassi.tmapp.service.dto.PublishedTmDTO;
import com.bassi.tmapp.service.dto.TmAgentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PublishedTm} and its DTO {@link PublishedTmDTO}.
 */
@Mapper(componentModel = "spring")
public interface PublishedTmMapper extends EntityMapper<PublishedTmDTO, PublishedTm> {
    @Mapping(target = "tmAgent", source = "tmAgent", qualifiedByName = "tmAgentId")
    PublishedTmDTO toDto(PublishedTm s);

    @Named("tmAgentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TmAgentDTO toDtoTmAgentId(TmAgent tmAgent);
}
