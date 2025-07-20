package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.Employee;
import com.bassi.tmapp.domain.Lead;
import com.bassi.tmapp.service.dto.EmployeeDTO;
import com.bassi.tmapp.service.dto.LeadDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Lead} and its DTO {@link LeadDTO}.
 */
@Mapper(componentModel = "spring")
public interface LeadMapper extends EntityMapper<LeadDTO, Lead> {
    @Mapping(target = "assignedTo", source = "assignedTo", qualifiedByName = "employeeId")
    LeadDTO toDto(Lead s);

    @Named("employeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoEmployeeId(Employee employee);
}
