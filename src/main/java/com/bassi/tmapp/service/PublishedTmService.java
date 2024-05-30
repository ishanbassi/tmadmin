package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.repository.PublishedTmRepository;
import com.bassi.tmapp.service.dto.PublishedTmDTO;
import com.bassi.tmapp.service.mapper.PublishedTmMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.PublishedTm}.
 */
@Service
@Transactional
public class PublishedTmService {

    private final Logger log = LoggerFactory.getLogger(PublishedTmService.class);

    private final PublishedTmRepository publishedTmRepository;

    private final PublishedTmMapper publishedTmMapper;

    public PublishedTmService(PublishedTmRepository publishedTmRepository, PublishedTmMapper publishedTmMapper) {
        this.publishedTmRepository = publishedTmRepository;
        this.publishedTmMapper = publishedTmMapper;
    }

    /**
     * Save a publishedTm.
     *
     * @param publishedTmDTO the entity to save.
     * @return the persisted entity.
     */
    public PublishedTmDTO save(PublishedTmDTO publishedTmDTO) {
        log.debug("Request to save PublishedTm : {}", publishedTmDTO);
        PublishedTm publishedTm = publishedTmMapper.toEntity(publishedTmDTO);
        publishedTm = publishedTmRepository.save(publishedTm);
        return publishedTmMapper.toDto(publishedTm);
    }

    /**
     * Update a publishedTm.
     *
     * @param publishedTmDTO the entity to save.
     * @return the persisted entity.
     */
    public PublishedTmDTO update(PublishedTmDTO publishedTmDTO) {
        log.debug("Request to update PublishedTm : {}", publishedTmDTO);
        PublishedTm publishedTm = publishedTmMapper.toEntity(publishedTmDTO);
        publishedTm = publishedTmRepository.save(publishedTm);
        return publishedTmMapper.toDto(publishedTm);
    }

    /**
     * Partially update a publishedTm.
     *
     * @param publishedTmDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PublishedTmDTO> partialUpdate(PublishedTmDTO publishedTmDTO) {
        log.debug("Request to partially update PublishedTm : {}", publishedTmDTO);

        return publishedTmRepository
            .findById(publishedTmDTO.getId())
            .map(existingPublishedTm -> {
                publishedTmMapper.partialUpdate(existingPublishedTm, publishedTmDTO);

                return existingPublishedTm;
            })
            .map(publishedTmRepository::save)
            .map(publishedTmMapper::toDto);
    }

    /**
     * Get all the publishedTms.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PublishedTmDTO> findAll() {
        log.debug("Request to get all PublishedTms");
        return publishedTmRepository.findAll().stream().map(publishedTmMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one publishedTm by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PublishedTmDTO> findOne(Long id) {
        log.debug("Request to get PublishedTm : {}", id);
        return publishedTmRepository.findById(id).map(publishedTmMapper::toDto);
    }

    /**
     * Delete the publishedTm by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PublishedTm : {}", id);
        publishedTmRepository.deleteById(id);
    }
}
