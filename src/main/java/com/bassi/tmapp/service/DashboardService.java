package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.domain.enumeration.DocumentType;
import com.bassi.tmapp.domain.enumeration.TrademarkType;
import com.bassi.tmapp.repository.TrademarkRepository;
import com.bassi.tmapp.service.dto.DashboardStatsDTO;
import com.bassi.tmapp.service.dto.DashboardStatsDTO.TaskDTO;
import com.bassi.tmapp.service.dto.DashboardStatsDTO.UserSummaryDTO;
import com.bassi.tmapp.service.dto.DocumentsDTO;
import com.bassi.tmapp.service.dto.StatusCountDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.mapper.TrademarkMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private final DocumentsService documentsService;
    private final CurrentUserService currentUserService;
    private final TrademarkRepository trademarkRepository;
    private final TrademarkMapper trademarkMapper;
    List<String> allStatuses = List.of("FILED", "DRAFT", "REGISTERED", "OPPOSED");

    DashboardService(
        DocumentsService documentsService,
        CurrentUserService currentUserService,
        TrademarkRepository trademarkRepository,
        TrademarkMapper trademarkMapper
    ) {
        this.documentsService = documentsService;
        this.currentUserService = currentUserService;
        this.trademarkRepository = trademarkRepository;
        this.trademarkMapper = trademarkMapper;
    }

    @Transactional(readOnly = true)
    public DashboardStatsDTO generateDashboardStats() {
        DashboardStatsDTO dashboardStatsDTO = new DashboardStatsDTO();
        UserProfile profile = currentUserService.getCurrentUserProfile();
        List<TrademarkDTO> recentApplications = trademarkMapper.toDto(trademarkRepository.findRecentTrademarkApplications(profile));
        List<StatusCountDTO> applicationStats = trademarkRepository.countByUserGroupByStatus(profile);
        applicationStats = addDefaultStatus(applicationStats);
        List<TaskDTO> pendingTasks = buildPendingTasks(recentApplications);
        dashboardStatsDTO.setRecentApplications(recentApplications);
        dashboardStatsDTO.setPendingTasks(pendingTasks);
        dashboardStatsDTO.setUserSummary(generateUserSummary(profile));
        dashboardStatsDTO.setStats(applicationStats);
        return dashboardStatsDTO;
    }

    private List<StatusCountDTO> addDefaultStatus(List<StatusCountDTO> applicationStats) {
        Map<String, Long> existing = applicationStats
            .stream()
            .collect(Collectors.toMap(row -> row.getTrademarkStatus(), row -> row.getCount()));
        for (String trademarkStatus : allStatuses) {
            if (!existing.containsKey(trademarkStatus)) {
                applicationStats.add(new StatusCountDTO(trademarkStatus, 0L));
            }
        }
        return applicationStats;
    }

    private UserSummaryDTO generateUserSummary(UserProfile userProfile) {
        UserSummaryDTO userSummaryDTO = new UserSummaryDTO();
        userSummaryDTO.setEmail(userProfile.getEmail());
        userSummaryDTO.setFirstName(userProfile.getFirstName());
        userSummaryDTO.setLastName(userProfile.getLastName());
        userSummaryDTO.setPhoneNumber(userProfile.getPhoneNumber());
        return userSummaryDTO;
    }

    private List<TaskDTO> buildPendingTasks(List<TrademarkDTO> apps) {
        List<TaskDTO> tasks = new ArrayList<>();

        for (TrademarkDTO app : apps) {
            List<DocumentsDTO> documentsDTOs = documentsService.findListByTrademark(app);
            String missingDocumentDescription = app.getName() == null
                ? "Please upload the document for your application."
                : "Please upload a logo for '" + app.getName() + "'.";

            if (isTrademarkNameMissing(app)) {
                tasks.add(
                    new TaskDTO(
                        "Trademark Name Missing",
                        "Please provide the trademark name for your application.",
                        "DETAILS_MISSING",
                        "/applications/" + app.getId() + "/edit"
                    )
                );
            }

            if (!isLogoUploaded(app, documentsDTOs)) {
                tasks.add(
                    new TaskDTO(
                        "Upload Logo Image",
                        missingDocumentDescription,
                        "DOCUMENT_UPLOAD",
                        "/applications/" + app.getId() + "/upload"
                    )
                );
            }

            if (!isPoaUploaded(app, documentsDTOs)) {
                tasks.add(
                    new TaskDTO(
                        "Upload Power of Attorney (POA)",
                        missingDocumentDescription,
                        "DOCUMENT_UPLOAD",
                        "/applications/" + app.getId() + "/poa"
                    )
                );
            }
            if (!addressProofUploaded(app, documentsDTOs)) {
                tasks.add(
                    new TaskDTO(
                        "Upload Address Proof",
                        missingDocumentDescription,
                        "DOCUMENT_UPLOAD",
                        "/applications/" + app.getId() + "/poa"
                    )
                );
            }
        }

        return tasks;
    }

    private boolean isTrademarkNameMissing(TrademarkDTO app) {
        return (
            app != null &&
            app.getType() != null &&
            (app.getType() == TrademarkType.TRADEMARK || app.getType() == TrademarkType.TRADEMARK_WITH_IMAGE) &&
            (app.getName() == null || app.getName().isBlank())
        );
    }

    private boolean addressProofUploaded(TrademarkDTO app, List<DocumentsDTO> documentsDTOs) {
        return documentsDTOs.stream().anyMatch(d -> d.getDocumentType() == DocumentType.ADDRESS_PROOF);
    }

    private boolean isPoaUploaded(TrademarkDTO app, List<DocumentsDTO> documentsDTOs) {
        return documentsDTOs.stream().anyMatch(d -> d.getDocumentType() == DocumentType.SIGNED_POA);
    }

    private boolean isLogoUploaded(TrademarkDTO app, List<DocumentsDTO> documentsDTOs) {
        return documentsDTOs
            .stream()
            .anyMatch(
                d ->
                    (app.getType() == TrademarkType.TRADEMARK_WITH_IMAGE || app.getType() == TrademarkType.IMAGEMARK) &&
                    d.getDocumentType() == DocumentType.LOGO
            );
    }
}
