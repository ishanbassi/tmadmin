package com.bassi.tmapp.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

public class ContactDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 100)
    @NotBlank
    private String name;

    @Email
    @Size(min = 5, max = 254)
    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
