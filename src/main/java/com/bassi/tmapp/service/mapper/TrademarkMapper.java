package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.Lead;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.TrademarkClass;
import com.bassi.tmapp.domain.TrademarkPlan;
import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.service.dto.LeadDTO;
import com.bassi.tmapp.service.dto.TrademarkClassDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.dto.TrademarkPlanDTO;
import com.bassi.tmapp.service.dto.UserProfileDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Trademark} and its DTO {@link TrademarkDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrademarkMapper extends EntityMapper<TrademarkDTO, Trademark> {
    @Mapping(target = "lead", source = "lead", qualifiedByName = "leadId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    @Mapping(target = "trademarkClasses", source = "trademarkClasses", qualifiedByName = "trademarkClassSet")
    @Mapping(target = "trademarkPlan", source = "trademarkPlan", qualifiedByName = "trademarkPlanId")
    TrademarkDTO toDto(Trademark s);

    @Mapping(target = "removeTrademarkClasses", ignore = true)
    Trademark toEntity(TrademarkDTO trademarkDTO);

    @Named("leadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LeadDTO toDtoLeadId(Lead lead);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("trademarkPlanId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TrademarkPlanDTO toDtoTrademarkPlanId(TrademarkPlan trademarkPlan);

    @Named("trademarkClassId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TrademarkClassDTO toDtoTrademarkClassId(TrademarkClass trademarkClass);

    @Named("trademarkClass")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "tmClass", source = "tmClass")
    @Mapping(target = "keyword", source = "keyword")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "code", source = "code")
    TrademarkClassDTO toDtoTrademarkClass(TrademarkClass trademarkClass);

    //    @Named("trademarkClassIdSet")
    //    default Set<TrademarkClassDTO> toDtoTrademarkClassIdSet(Set<TrademarkClass> trademarkClass) {
    //        return trademarkClass.stream().map(this::toDtoTrademarkClassId).collect(Collectors.toSet());
    //    }

    @Named("trademarkClassSet")
    default Set<TrademarkClassDTO> toDtoTrademarkClassSet(Set<TrademarkClass> trademarkClass) {
        return trademarkClass.stream().map(this::toDtoTrademarkClass).collect(Collectors.toSet());
    }
}
