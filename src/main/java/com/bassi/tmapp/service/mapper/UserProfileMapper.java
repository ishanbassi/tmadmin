package com.bassi.tmapp.service.mapper;

import com.bassi.tmapp.domain.Authority;
import com.bassi.tmapp.domain.User;
import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.service.dto.UserDTO;
import com.bassi.tmapp.service.dto.UserProfileDTO;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserProfile} and its DTO {@link UserProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserProfileMapper extends EntityMapper<UserProfileDTO, UserProfile> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userDto")
    UserProfileDTO toDto(UserProfile s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("userDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "authorities", source = "authorities", qualifiedByName = "authoritySetToStringSet")
    UserDTO toDtoUser(User user);

    @Named("authoritySetToStringSet")
    public static Set<Authority> mapAuthorities(Set<Authority> authorities) {
        if (authorities == null) {
            return new HashSet<>();
        }
        return authorities;
    }
}
