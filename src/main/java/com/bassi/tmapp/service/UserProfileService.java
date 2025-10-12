package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.Payment;
import com.bassi.tmapp.domain.User;
import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.repository.UserProfileRepository;
import com.bassi.tmapp.service.dto.AdminUserDTO;
import com.bassi.tmapp.service.dto.LeadDTO;
import com.bassi.tmapp.service.dto.UserProfileDTO;
import com.bassi.tmapp.service.mapper.UserProfileMapper;
import com.bassi.tmapp.web.rest.errors.InternalServerAlertException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.UserProfile}.
 */
@Service
@Transactional
public class UserProfileService {

    private static final Logger log = LoggerFactory.getLogger(UserProfileService.class);

    private final UserProfileRepository userProfileRepository;

    private final UserProfileMapper userProfileMapper;

    private final UserService userService;

    private final LeadService leadService;

    public UserProfileService(
        UserProfileRepository userProfileRepository,
        UserProfileMapper userProfileMapper,
        UserService userService,
        LeadService leadService
    ) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
        this.userService = userService;
        this.leadService = leadService;
    }

    /**
     * Save a userProfile.
     *
     * @param userProfileDTO the entity to save.
     * @return the persisted entity.
     */
    public UserProfileDTO save(UserProfileDTO userProfileDTO) {
        log.debug("Request to save UserProfile : {}", userProfileDTO);
        UserProfile userProfile = userProfileMapper.toEntity(userProfileDTO);
        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toDto(userProfile);
    }

    /**
     * Update a userProfile.
     *
     * @param userProfileDTO the entity to save.
     * @return the persisted entity.
     */
    public UserProfileDTO update(UserProfileDTO userProfileDTO) {
        log.debug("Request to update UserProfile : {}", userProfileDTO);
        UserProfile userProfile = userProfileMapper.toEntity(userProfileDTO);
        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toDto(userProfile);
    }

    /**
     * Partially update a userProfile.
     *
     * @param userProfileDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserProfileDTO> partialUpdate(UserProfileDTO userProfileDTO) {
        log.debug("Request to partially update UserProfile : {}", userProfileDTO);

        return userProfileRepository
            .findById(userProfileDTO.getId())
            .map(existingUserProfile -> {
                userProfileMapper.partialUpdate(existingUserProfile, userProfileDTO);

                return existingUserProfile;
            })
            .map(userProfileRepository::save)
            .map(userProfileMapper::toDto);
    }

    /**
     * Get all the userProfiles.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserProfileDTO> findAll() {
        log.debug("Request to get all UserProfiles");
        return userProfileRepository.findAll().stream().map(userProfileMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one userProfile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserProfileDTO> findOne(Long id) {
        log.debug("Request to get UserProfile : {}", id);
        return userProfileRepository.findById(id).map(userProfileMapper::toDto);
    }

    /**
     * Delete the userProfile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserProfile : {}", id);
        userProfileRepository.deleteById(id);
    }

    public UserProfileDTO save(Payment payment, LeadDTO leadDTO) {
        if (leadDTO == null) {
            log.error("Unable to create user for the lead with the payment order id: {}", payment.getOrderId());
            throw new InternalServerAlertException("Unable to create User because leadDTO is null");
        }
        AdminUserDTO adminUserDTO = leadService.createAdminDtoFromLead(leadDTO);
        User newUser = userService.registerUser(adminUserDTO, RandomUtil.generatePassword());
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(newUser);
        return save(userProfileMapper.toDto(userProfile));
    }

    public Optional<UserProfile> findByUserEmail(String userLogin) {
        return userProfileRepository.findByUserEmail(userLogin);
    }

    public UserProfileDTO createUserProfile(User user) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        return save(userProfileMapper.toDto(userProfile));
    }
}
