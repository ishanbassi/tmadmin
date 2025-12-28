package com.bassi.tmapp.service;

import com.bassi.tmapp.config.Constants;
import com.bassi.tmapp.domain.Payment;
import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.domain.enumeration.DocumentType;
import com.bassi.tmapp.domain.enumeration.TrademarkType;
import com.bassi.tmapp.repository.TrademarkRepository;
import com.bassi.tmapp.service.dto.DashboardStatsDTO;
import com.bassi.tmapp.service.dto.DashboardStatsDTO.TaskDTO;
import com.bassi.tmapp.service.dto.DashboardStatsDTO.UserSummaryDTO;
import com.bassi.tmapp.service.dto.DocumentsDTO;
import com.bassi.tmapp.service.dto.PaymentDTO;
import com.bassi.tmapp.service.dto.StatusCountDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.mapper.TrademarkMapper;
import java.math.BigDecimal;
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
    private final PaymentService paymentService;
    List<String> allStatuses = List.of("FILED", "DRAFT", "REGISTERED", "OPPOSED");
    private final String DOCUMENT_UPLOAD = "DOCUMENT_UPLOAD";

    DashboardService(
        DocumentsService documentsService,
        CurrentUserService currentUserService,
        TrademarkRepository trademarkRepository,
        TrademarkMapper trademarkMapper,
        PaymentService paymentService
    ) {
        this.documentsService = documentsService;
        this.currentUserService = currentUserService;
        this.trademarkRepository = trademarkRepository;
        this.trademarkMapper = trademarkMapper;
        this.paymentService = paymentService;
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
            List<Payment> payments = paymentService.findPendingPaymentForTm(app);
            String missingDocumentDescription = app.getName() == null
                ? "Please upload the document for your trademark application."
                : "Please upload the document for your trademark application: '" + app.getName() + "'.";

            if (isTrademarkNameMissing(app)) {
                tasks.add(
                    new TaskDTO(
                        "Trademark Name Missing",
                        "Please provide the trademark name for your application.",
                        "DETAILS_MISSING",
                        "/applications/" + app.getId() + "/edit",
                        app.getId()
                    )
                );
            }
            if (isSelectPlanPending(app)) {
                tasks.add(
                    new TaskDTO(
                        "Select Plan",
                        String.format(
                            "Please select a plan for your trademark application: %s",
                            app.getName() == null ? "" : app.getName()
                        ),
                        "PLAN_PENDING",
                        "/applications/" + app.getId() + "/select-plan",
                        app.getId()
                    )
                );
            }
            for (Payment payment : payments) {
                tasks.add(
                    new TaskDTO(
                        "Fees Pending",
                        String.format(
                            "Please pay ₹%s for your trademark application: %s",
                            payment.getAmount(),
                            app.getName() == null ? "" : app.getName()
                        ),
                        "PAYMENT_PENDING",
                        "/applications/" + app.getId() + "/payment",
                        app.getId(),
                        payment.getOrderId()
                    )
                );
            }

            if (
                (app.getType() == TrademarkType.IMAGEMARK || app.getType() == TrademarkType.TRADEMARK_WITH_IMAGE) &&
                !isLogoUploaded(app, documentsDTOs)
            ) {
                tasks.add(
                    new TaskDTO(
                        "Upload Logo Image",
                        missingDocumentDescription,
                        DOCUMENT_UPLOAD,
                        "/upload-document?application=" + app.getId() + "&document=" + DocumentType.LOGO,
                        app.getId(),
                        DocumentType.LOGO
                    )
                );
            }

            if (!isPoaUploaded(app, documentsDTOs)) {
                tasks.add(
                    new TaskDTO(
                        "Upload Power of Attorney (POA)",
                        missingDocumentDescription,
                        DOCUMENT_UPLOAD,
                        "/upload-document?application=" + app.getId() + "&document=" + DocumentType.SIGNED_POA,
                        app.getId(),
                        DocumentType.SIGNED_POA
                    )
                );
            }
            if (!addressProofUploaded(app, documentsDTOs)) {
                tasks.add(
                    new TaskDTO(
                        "Upload Address Proof",
                        missingDocumentDescription,
                        DOCUMENT_UPLOAD,
                        "/upload-document?application=" + app.getId() + "&document=ADDRESS_PROOF" + DocumentType.ADDRESS_PROOF,
                        app.getId(),
                        DocumentType.ADDRESS_PROOF
                    )
                );
            }
            if (!applicantIdentityProofUploaded(app, documentsDTOs)) {
                tasks.add(
                    new TaskDTO(
                        "Upload Identity Proof",
                        missingDocumentDescription,
                        DOCUMENT_UPLOAD,
                        "/upload-document?application=" + app.getId() + "&document=" + DocumentType.APPLICANT_IDENTITY,
                        app.getId(),
                        DocumentType.APPLICANT_IDENTITY
                    )
                );
            }
        }

        return tasks;
    }

    private boolean applicantIdentityProofUploaded(TrademarkDTO app, List<DocumentsDTO> documentsDTOs) {
        return documentsDTOs.stream().anyMatch(d -> d.getDocumentType() == DocumentType.APPLICANT_IDENTITY);
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

    private boolean isFeesPending(TrademarkDTO app) {
        List<Payment> payments = paymentService.findByTrademark(trademarkMapper.toEntity(app));
        return payments.stream().anyMatch(p -> p.getStatus().equalsIgnoreCase("pending") || p.getStatus().equalsIgnoreCase("created"));
    }

    private boolean isSelectPlanPending(TrademarkDTO app) {
        return app.getTrademarkPlan() == null;
    }
}
