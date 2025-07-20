package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.Company;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.service.dto.CompanyDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Trademark} and its DTO {@link TrademarkDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrademarkMapper extends EntityMapper<TrademarkDTO, Trademark> {
    @Mapping(target = "company", source = "company", qualifiedByName = "companyId")
    TrademarkDTO toDto(Trademark s);

    @Named("companyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyDTO toDtoCompanyId(Company company);
}
