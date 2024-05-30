package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.service.dto.PublishedTmDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PublishedTm} and its DTO {@link PublishedTmDTO}.
 */
@Mapper(componentModel = "spring")
public interface PublishedTmMapper extends EntityMapper<PublishedTmDTO, PublishedTm> {}
