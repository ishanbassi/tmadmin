package com.bassi.tmapp.config;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "imap")
@Component
public class ImapProperties {

    private List<ImapAccount> accounts;

    public List<ImapAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<ImapAccount> accounts) {
        this.accounts = accounts;
    }

    public static class ImapAccount {

        private AccountType type;
        private String username;
        private String password;
        private String host;
        private int port;
        private String folder;

        private String phoneNumber;

        public boolean isEmail() {
            return type == AccountType.EMAIL;
        }

        public boolean isPhone() {
            return type == AccountType.PHONE;
        }

        public enum AccountType {
            EMAIL,
            PHONE,
        }

        public AccountType getType() {
            return type;
        }

        public void setType(AccountType type) {
            this.type = type;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getFolder() {
            return folder;
        }

        public void setFolder(String folder) {
            this.folder = folder;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}
