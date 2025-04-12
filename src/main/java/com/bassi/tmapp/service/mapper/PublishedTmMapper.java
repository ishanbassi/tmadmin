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
    @Mapping(source = "agent", target = "tmAgent", qualifiedByName = "tmAgentId")
    public PublishedTm toEntity(PublishedTmDTO publishedTmDTO);

    @Named("tmAgentId")
    public TmAgent toTmAgent(TmAgentDTO agentDTO);
}
