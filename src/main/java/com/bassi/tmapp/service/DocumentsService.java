package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.Documents;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.enumeration.DocumentType;
import com.bassi.tmapp.repository.DocumentsRepository;
import com.bassi.tmapp.service.dto.DocumentsDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.mapper.DocumentsMapper;
import com.bassi.tmapp.web.rest.errors.InternalServerAlertException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.Documents}.
 */
@Service
@Transactional
public class DocumentsService {

    @Value("${document-base-path}")
    private String documentBasePath;

    private static final Logger LOG = LoggerFactory.getLogger(DocumentsService.class);

    private final DocumentsRepository documentsRepository;

    private final DocumentsMapper documentsMapper;

    public DocumentsService(DocumentsRepository documentsRepository, DocumentsMapper documentsMapper) {
        this.documentsRepository = documentsRepository;
        this.documentsMapper = documentsMapper;
    }

    /**
     * Save a documents.
     *
     * @param documentsDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentsDTO save(DocumentsDTO documentsDTO) {
        LOG.debug("Request to save Documents : {}", documentsDTO);
        Documents documents = documentsMapper.toEntity(documentsDTO);
        documents = documentsRepository.save(documents);
        return documentsMapper.toDto(documents);
    }

    /**
     * Update a documents.
     *
     * @param documentsDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentsDTO update(DocumentsDTO documentsDTO) {
        LOG.debug("Request to update Documents : {}", documentsDTO);
        Documents documents = documentsMapper.toEntity(documentsDTO);
        documents = documentsRepository.save(documents);
        return documentsMapper.toDto(documents);
    }

    /**
     * Partially update a documents.
     *
     * @param documentsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentsDTO> partialUpdate(DocumentsDTO documentsDTO) {
        LOG.debug("Request to partially update Documents : {}", documentsDTO);

        return documentsRepository
            .findById(documentsDTO.getId())
            .map(existingDocuments -> {
                documentsMapper.partialUpdate(existingDocuments, documentsDTO);

                return existingDocuments;
            })
            .map(documentsRepository::save)
            .map(documentsMapper::toDto);
    }

    /**
     * Get all the documents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentsDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Documents");
        return documentsRepository.findAll(pageable).map(documentsMapper::toDto);
    }

    /**
     * Get one documents by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentsDTO> findOne(Long id) {
        LOG.debug("Request to get Documents : {}", id);
        return documentsRepository.findById(id).map(documentsMapper::toDto);
    }

    /**
     * Delete the documents by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Documents : {}", id);
        documentsRepository.deleteById(id);
    }

    public DocumentsDTO saveDocumentAndSaveFile(DocumentsDTO documentsDTO, byte[] content) {
        LOG.debug("Request to save Documents : {}", documentsDTO);
        Documents documents = documentsMapper.toEntity(documentsDTO);
        documents.setFileUrl(saveToFileSystem(documentsDTO, content));
        documents = documentsRepository.save(documents);
        return documentsMapper.toDto(documents);
    }

    private String saveToFileSystem(DocumentsDTO documentsDTO, byte[] content) {
        LOG.info("Going to save document file : {} in the file system", documentsDTO);
        String resourcesDir = Paths.get(documentBasePath).toAbsolutePath().toString();
        String extensionType = getFileExtensionType(documentsDTO.getFileContentType());
        String filePath =
            documentsDTO.getDocumentType() +
            "/" +
            new Date().getTime() +
            "-" +
            removePathSeperators(documentsDTO.getFileName()) +
            "." +
            extensionType;
        Path newFile = Paths.get(resourcesDir, filePath);
        LOG.info("Going to write the document in the file system at: {}", newFile);
        try {
            Files.createDirectories(newFile.getParent());
            Files.write(newFile, content);
        } catch (IOException e) {
            throw new InternalServerAlertException("Unable to Save document file Reason: " + e.getLocalizedMessage());
        }
        return filePath;
    }

    private String getFileExtensionType(String billImageContentType) {
        String extensionType = billImageContentType.split("/")[1];
        if (extensionType.contains("svg")) {
            extensionType = "svg";
        }
        return extensionType;
    }

    private String removePathSeperators(String fileName) {
        return fileName.replace("/", "-").replace("\\", "-").trim();
    }

    public Optional<DocumentsDTO> findByTrademark(Trademark trademark) {
        return documentsRepository.findByTrademark(trademark, DocumentType.LOGO).map(documentsMapper::toDto);
    }
}
