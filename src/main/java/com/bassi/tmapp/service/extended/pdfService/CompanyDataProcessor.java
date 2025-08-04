package com.bassi.tmapp.service.extended.pdfService;

import com.bassi.tmapp.domain.Lead;
import com.bassi.tmapp.service.dto.LeadDTO;
import com.bassi.tmapp.service.mapper.LeadMapper;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CompanyDataProcessor {

    private static final Logger log = LoggerFactory.getLogger(CompanyDataProcessor.class);

    private final LeadMapper leadMapper;

    public CompanyDataProcessor(LeadMapper leadMapper) {
        this.leadMapper = leadMapper;
    }

    /**
     * Process extracted text and separate side-by-side company records
     * @param extractedText The raw text extracted from PDF
     * @return List of separated company records
     */
    public List<CompanyRecord> separateCompanyRecords(String extractedText) {
        List<CompanyRecord> records = new ArrayList<>();

        // Split text into sections based on company boundaries
        String[] sections = extractedText.split("(?=Contact Name:|Company Name:)");

        for (String section : sections) {
            if (section.trim().isEmpty()) continue;

            CompanyRecord record = extractCompanyData(section);
            if (record != null && record.isValid()) {
                records.add(record);
            }
        }

        return records;
    }

    /**
     * Extract company data from a text section
     */
    private CompanyRecord extractCompanyData(String text) {
        CompanyRecord record = new CompanyRecord();

        // Enhanced regex patterns for better extraction
        Pattern contactNamePattern = Pattern.compile(
            "Contact Name\\s*:\\s*(.*?)(?=\\s*(?:Designation|Address|Phone|Fax|Email|Business Activities|$))",
            Pattern.DOTALL
        );
        Pattern designationPattern = Pattern.compile(
            "Designation\\s*:\\s*(.*?)(?=\\s*(?:Address|Phone|Fax|Email|Business Activities|$))",
            Pattern.DOTALL
        );
        Pattern addressPattern = Pattern.compile("Address\\s*:\\s*(.*?)(?=\\s*(?:Phone|Fax|Email|Business Activities|$))", Pattern.DOTALL);
        Pattern phonePattern = Pattern.compile("Phone\\s*:\\s*([\\d\\-\\+\\(\\) ]+)");
        Pattern faxPattern = Pattern.compile("Fax\\s*:\\s*([\\d\\-\\+\\(\\) ]+)");
        Pattern emailPattern = Pattern.compile("Email\\s*:\\s*([\\w\\.-]+@[\\w\\.-]+)");
        Pattern businessActivitiesPattern = Pattern.compile("Business Activities\\s*:\\s*(.*)", Pattern.DOTALL);
        Pattern companyNamePattern = Pattern.compile(
            "Company Name\\s*:\\s*(.*?)(?=\\s*(?:Contact Name|Designation|Address|Phone|Fax|Email|Business Activities|$))",
            Pattern.DOTALL
        );

        // Extract data
        extractField(text, contactNamePattern, record::setContactName);
        extractField(text, designationPattern, record::setDesignation);
        extractField(text, addressPattern, record::setAddress);
        extractField(text, phonePattern, record::setPhone);
        extractField(text, faxPattern, record::setFax);
        extractField(text, emailPattern, record::setEmail);
        extractField(text, businessActivitiesPattern, record::setBusinessActivities);
        extractField(text, companyNamePattern, record::setCompanyName);

        return record;
    }

    private void extractField(String text, Pattern pattern, java.util.function.Consumer<String> setter) {
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String value = matcher.group(1).trim();
            if (!value.isEmpty()) {
                setter.accept(value);
            }
        }
    }

    /**
     * Convert company records to Lead entities
     */
    public List<Lead> convertToLeads(List<CompanyRecord> records) {
        List<Lead> leads = new ArrayList<>();

        for (CompanyRecord record : records) {
            Lead lead = new Lead();
            lead.setFullName(record.getContactName());
            lead.setPhoneNumber(record.getPhone());
            lead.setEmail(record.getEmail());
            lead.setLeadSource("PDF_EXTRACTION");
            lead.setCreatedDate(ZonedDateTime.now());
            lead.setModifiedDate(ZonedDateTime.now());

            leads.add(lead);
        }

        return leads;
    }

    /**
     * Save company records to CSV for verification
     */
    public void saveToCSV(List<CompanyRecord> records, String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                // Write header
                writer.write("Company Name,Contact Name,Designation,Address,Phone,Fax,Email,Business Activities\n");

                // Write data
                for (CompanyRecord record : records) {
                    writer.write(
                        String.format(
                            "\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                            escapeCsv(record.getCompanyName()),
                            escapeCsv(record.getContactName()),
                            escapeCsv(record.getDesignation()),
                            escapeCsv(record.getAddress()),
                            escapeCsv(record.getPhone()),
                            escapeCsv(record.getFax()),
                            escapeCsv(record.getEmail()),
                            escapeCsv(record.getBusinessActivities())
                        )
                    );
                }
            }

            log.info("CSV file saved successfully: {}", filePath);
        } catch (IOException e) {
            log.error("Error saving CSV file: {}", e.getMessage());
        }
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        return value.replace("\"", "\"\"").replace("\n", " ").replace("\r", " ");
    }

    /**
     * Inner class to represent a company record
     */
    public static class CompanyRecord {

        private String companyName;
        private String contactName;
        private String designation;
        private String address;
        private String phone;
        private String fax;
        private String email;
        private String businessActivities;

        public boolean isValid() {
            // At least contact name or company name should be present
            return (contactName != null && !contactName.trim().isEmpty()) || (companyName != null && !companyName.trim().isEmpty());
        }

        // Getters and Setters
        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getFax() {
            return fax;
        }

        public void setFax(String fax) {
            this.fax = fax;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getBusinessActivities() {
            return businessActivities;
        }

        public void setBusinessActivities(String businessActivities) {
            this.businessActivities = businessActivities;
        }
    }
}
