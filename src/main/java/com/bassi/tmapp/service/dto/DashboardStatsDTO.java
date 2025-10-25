package com.bassi.tmapp.service.dto;

import java.time.LocalDate;
import java.util.List;

public class DashboardStatsDTO {

    private UserSummaryDTO userSummary;
    private List<TrademarkDTO> recentApplications;
    private ApplicationStatsDTO stats;
    private List<TaskDTO> pendingTasks;

    public class UserSummaryDTO {

        private String name;
        private String email;
        private LocalDate onboardingDate;
        private boolean profileComplete;
        private String planType;

        UserSummaryDTO(String name, String email, LocalDate onboardingDate, boolean profileComplete, String planType) {
            this.name = name;
            this.email = email;
            this.onboardingDate = onboardingDate;
            this.profileComplete = profileComplete;
            this.planType = planType;
        }
    }

    public static class TaskDTO {

        private String title; // "Upload Power of Attorney"
        private String description; // "Please upload POA for Application #12345"
        private String type; // "DOCUMENT_UPLOAD", "PAYMENT_PENDING", etc.
        private String link; // frontend route

        public TaskDTO(String title, String description, String type, String link) {
            this.title = title;
            this.description = description;
            this.type = type;
            this.link = link;
        }
    }

    public class ApplicationStatsDTO {

        private long totalFiled;
        private long underExamination;
        private long objected;
        private long registered;
        private long abandoned;
    }
}
