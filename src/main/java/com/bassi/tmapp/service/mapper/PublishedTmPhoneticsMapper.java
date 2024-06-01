package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.domain.PublishedTmPhonetics;
import com.bassi.tmapp.service.dto.PublishedTmDTO;
import com.bassi.tmapp.service.dto.PublishedTmPhoneticsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PublishedTmPhonetics} and its DTO {@link PublishedTmPhoneticsDTO}.
 */
@Mapper(componentModel = "spring")
public interface PublishedTmPhoneticsMapper extends EntityMapper<PublishedTmPhoneticsDTO, PublishedTmPhonetics> {
    @Mapping(target = "publishedTm", source = "publishedTm", qualifiedByName = "publishedTmId")
    PublishedTmPhoneticsDTO toDto(PublishedTmPhonetics s);

    @Named("publishedTmId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PublishedTmDTO toDtoPublishedTmId(PublishedTm publishedTm);
}
