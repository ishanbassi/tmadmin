package com.bassi.tmapp.service.dto;

import com.bassi.tmapp.domain.enumeration.DocumentType;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class DashboardStatsDTO {

    private UserSummaryDTO userSummary;
    private List<TrademarkDTO> recentApplications;
    private List<StatusCountDTO> stats;
    private List<TaskDTO> pendingTasks;

    public List<StatusCountDTO> getStats() {
        return stats;
    }

    public void setStats(List<StatusCountDTO> stats) {
        this.stats = stats;
    }

    public UserSummaryDTO getUserSummary() {
        return userSummary;
    }

    public void setUserSummary(UserSummaryDTO userSummary) {
        this.userSummary = userSummary;
    }

    public List<TrademarkDTO> getRecentApplications() {
        return recentApplications;
    }

    public void setRecentApplications(List<TrademarkDTO> recentApplications) {
        this.recentApplications = recentApplications;
    }

    public List<TaskDTO> getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(List<TaskDTO> pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public static class UserSummaryDTO {

        private String firstName;
        private String lastName;
        private String email;
        private LocalDate onboardingDate;
        private boolean profileComplete;
        private String planType;
        private String phoneNumber;

        public UserSummaryDTO() {}

        public UserSummaryDTO(
            String firstName,
            String lastName,
            String email,
            LocalDate onboardingDate,
            boolean profileComplete,
            String planType
        ) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.onboardingDate = onboardingDate;
            this.profileComplete = profileComplete;
            this.planType = planType;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public LocalDate getOnboardingDate() {
            return onboardingDate;
        }

        public void setOnboardingDate(LocalDate onboardingDate) {
            this.onboardingDate = onboardingDate;
        }

        public boolean isProfileComplete() {
            return profileComplete;
        }

        public void setProfileComplete(boolean profileComplete) {
            this.profileComplete = profileComplete;
        }

        public String getPlanType() {
            return planType;
        }

        public void setPlanType(String planType) {
            this.planType = planType;
        }
    }

    public static class TaskDTO {

        private String title;
        private String description;
        private String type;
        private String link;
        private Long applicationId;
        private DocumentType documentType;
        private String orderId;

        public TaskDTO(String title, String description, String type, String link, Long applicationId) {
            this.title = title;
            this.description = description;
            this.type = type;
            this.link = link;
            this.applicationId = applicationId;
        }

        public TaskDTO(String title, String description, String type, String link, Long applicationId, DocumentType documentType) {
            this.title = title;
            this.description = description;
            this.type = type;
            this.link = link;
            this.applicationId = applicationId;
            this.documentType = documentType;
        }

        public TaskDTO(String title, String description, String type, String link, Long applicationId, String orderId) {
            this.title = title;
            this.description = description;
            this.type = type;
            this.link = link;
            this.applicationId = applicationId;
            this.orderId = orderId;
        }

        public Long getApplicationId() {
            return applicationId;
        }

        public void setApplicationId(Long applicationId) {
            this.applicationId = applicationId;
        }

        public DocumentType getDocumentType() {
            return documentType;
        }

        public void setDocumentType(DocumentType documentType) {
            this.documentType = documentType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
