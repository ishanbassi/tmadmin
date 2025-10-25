package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.enumeration.DocumentType;
import com.bassi.tmapp.domain.enumeration.TrademarkType;
import com.bassi.tmapp.service.dto.DashboardStatsDTO.TaskDTO;
import com.bassi.tmapp.service.dto.DocumentsDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final DocumentsService documentsService;

    DashboardService(DocumentsService documentsService) {
        this.documentsService = documentsService;
    }

    private List<TaskDTO> buildPendingTasks(List<TrademarkDTO> apps) {
        List<TaskDTO> tasks = new ArrayList<>();

        for (TrademarkDTO app : apps) {
            List<DocumentsDTO> documentsDTOs = documentsService.findListByTrademark(app);

            if (!isApplicantDetailsComplete(app)) {
                tasks.add(
                    new TaskDTO(
                        "Complete Applicant Details",
                        "Application " + app.getName() + " is missing applicant details.",
                        "DATA_ENTRY",
                        "/applications/" + app.getId() + "/details"
                    )
                );
            }

            if (!isLogoUploaded(app, documentsDTOs)) {
                tasks.add(
                    new TaskDTO(
                        "Upload Logo Image",
                        "Please upload a logo for '" + app.getName() + "'.",
                        "DOCUMENT_UPLOAD",
                        "/applications/" + app.getId() + "/upload"
                    )
                );
            }

            if (!isPoaUploaded(app, documentsDTOs)) {
                tasks.add(
                    new TaskDTO(
                        "Upload Power of Attorney (POA)",
                        "Upload POA document for '" + app.getName() + "'.",
                        "DOCUMENT_UPLOAD",
                        "/applications/" + app.getId() + "/poa"
                    )
                );
            }
            if (!addressProofUploaded(app, documentsDTOs)) {
                tasks.add(
                    new TaskDTO(
                        "Upload Address Proof",
                        "Upload Address Proof for '" + app.getName() + "'.",
                        "DOCUMENT_UPLOAD",
                        "/applications/" + app.getId() + "/poa"
                    )
                );
            }
        }

        return tasks;
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

    private boolean isApplicantDetailsComplete(TrademarkDTO app) {
        // TODO Auto-generated method stub
        return false;
    }
}
