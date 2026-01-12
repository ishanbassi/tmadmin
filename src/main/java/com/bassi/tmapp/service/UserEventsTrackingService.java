package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.UserEventsTracking;
import com.bassi.tmapp.repository.UserEventsTrackingRepository;
import com.bassi.tmapp.service.dto.UserEventsTrackingDTO;
import com.bassi.tmapp.service.mapper.UserEventsTrackingMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.UserEventsTracking}.
 */
@Service
@Transactional
public class UserEventsTrackingService {

    private static final Logger LOG = LoggerFactory.getLogger(UserEventsTrackingService.class);

    private final UserEventsTrackingRepository userEventsTrackingRepository;

    private final UserEventsTrackingMapper userEventsTrackingMapper;

    public UserEventsTrackingService(
        UserEventsTrackingRepository userEventsTrackingRepository,
        UserEventsTrackingMapper userEventsTrackingMapper
    ) {
        this.userEventsTrackingRepository = userEventsTrackingRepository;
        this.userEventsTrackingMapper = userEventsTrackingMapper;
    }

    /**
     * Save a userEventsTracking.
     *
     * @param userEventsTrackingDTO the entity to save.
     * @return the persisted entity.
     */
    public UserEventsTrackingDTO save(UserEventsTrackingDTO userEventsTrackingDTO) {
        LOG.debug("Request to save UserEventsTracking : {}", userEventsTrackingDTO);
        UserEventsTracking userEventsTracking = userEventsTrackingMapper.toEntity(userEventsTrackingDTO);
        userEventsTracking = userEventsTrackingRepository.save(userEventsTracking);
        return userEventsTrackingMapper.toDto(userEventsTracking);
    }

    /**
     * Update a userEventsTracking.
     *
     * @param userEventsTrackingDTO the entity to save.
     * @return the persisted entity.
     */
    public UserEventsTrackingDTO update(UserEventsTrackingDTO userEventsTrackingDTO) {
        LOG.debug("Request to update UserEventsTracking : {}", userEventsTrackingDTO);
        UserEventsTracking userEventsTracking = userEventsTrackingMapper.toEntity(userEventsTrackingDTO);
        userEventsTracking = userEventsTrackingRepository.save(userEventsTracking);
        return userEventsTrackingMapper.toDto(userEventsTracking);
    }

    /**
     * Partially update a userEventsTracking.
     *
     * @param userEventsTrackingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserEventsTrackingDTO> partialUpdate(UserEventsTrackingDTO userEventsTrackingDTO) {
        LOG.debug("Request to partially update UserEventsTracking : {}", userEventsTrackingDTO);

        return userEventsTrackingRepository
            .findById(userEventsTrackingDTO.getId())
            .map(existingUserEventsTracking -> {
                userEventsTrackingMapper.partialUpdate(existingUserEventsTracking, userEventsTrackingDTO);

                return existingUserEventsTracking;
            })
            .map(userEventsTrackingRepository::save)
            .map(userEventsTrackingMapper::toDto);
    }

    /**
     * Get all the userEventsTrackings.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserEventsTrackingDTO> findAll() {
        LOG.debug("Request to get all UserEventsTrackings");
        return userEventsTrackingRepository
            .findAll()
            .stream()
            .map(userEventsTrackingMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one userEventsTracking by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserEventsTrackingDTO> findOne(Long id) {
        LOG.debug("Request to get UserEventsTracking : {}", id);
        return userEventsTrackingRepository.findById(id).map(userEventsTrackingMapper::toDto);
    }

    /**
     * Delete the userEventsTracking by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UserEventsTracking : {}", id);
        userEventsTrackingRepository.deleteById(id);
    }
}
