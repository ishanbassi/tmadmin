package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.TmAgent;
import com.bassi.tmapp.service.dto.TmAgentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TmAgent} and its DTO {@link TmAgentDTO}.
 */
@Mapper(componentModel = "spring")
public interface TmAgentMapper extends EntityMapper<TmAgentDTO, TmAgent> {}
