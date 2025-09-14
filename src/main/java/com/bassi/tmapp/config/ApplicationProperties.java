package com.bassi.tmapp.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Tmapp.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Liquibase liquibase = new Liquibase();
    private final Captcha captcha = new Captcha();
    private List<String> adminNotificationsEmailAddress;
    private RazorPay razorPay;

    // jhipster-needle-application-properties-property

    public Liquibase getLiquibase() {
        return liquibase;
    }

    // jhipster-needle-application-properties-property-getter

    public Captcha getCaptcha() {
        return captcha;
    }

    public RazorPay getRazorPay() {
        return razorPay;
    }

    public void setRazorPay(RazorPay razorPay) {
        this.razorPay = razorPay;
    }

    public static class Liquibase {

        private Boolean asyncStart = true;

        public Boolean getAsyncStart() {
            return asyncStart;
        }

        public void setAsyncStart(Boolean asyncStart) {
            this.asyncStart = asyncStart;
        }
    }

    // jhipster-needle-application-properties-property-class

    public static class Captcha {

        private String userId;
        private String apiKey;
        private String url;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public List<String> getAdminNotificationsEmailAddress() {
        return adminNotificationsEmailAddress;
    }

    public void setAdminNotificationsEmailAddress(List<String> adminNotificationsEmailAddress) {
        this.adminNotificationsEmailAddress = adminNotificationsEmailAddress;
    }

    public static class RazorPay {

        private String keyId;
        private String keySecret;
        private String companyName;
        private String currency;

        public String getKeyId() {
            return keyId;
        }

        public void setKeyId(String keyId) {
            this.keyId = keyId;
        }

        public String getKeySecret() {
            return keySecret;
        }

        public void setKeySecret(String keySecret) {
            this.keySecret = keySecret;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }
}
