package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.config.ApplicationProperties;
import com.bassi.tmapp.service.MailService;
import com.bassi.tmapp.service.dto.ContactDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/client")
public class ClientResource {

    private final MailService mailService;
    private ApplicationProperties applicationProperties;
    private RestTemplate restTemplate;

    ClientResource(MailService mailService, ApplicationProperties applicationProperties, RestTemplate restTemplate) {
        this.mailService = mailService;
        this.applicationProperties = applicationProperties;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/hcvk/contact-us")
    public void registerAccount(@Valid @RequestBody ContactDto contactUsVM) {
        mailService.sendContactUsMail(contactUsVM, "info@hcvkbolt.com");
    }

    @PostMapping("/senton/contact-us")
    public void sendEmailToSenton(@Valid @RequestBody ContactDto contactUsVM) {
        mailService.sendContactUsMail(contactUsVM, "ankushsports4@gmail.com");
    }
}
