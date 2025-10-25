package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.service.MemberPortalService;
import com.bassi.tmapp.service.dto.TrademarkOrderSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/portal")
public class MemberPortalResource {

    private static final Logger log = LoggerFactory.getLogger(MemberPortalResource.class);

    private final MemberPortalService memberPortalService;

    public MemberPortalResource(MemberPortalService memberPortalService) {
        this.memberPortalService = memberPortalService;
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<TrademarkOrderSummary> getDashboardStats() {
        //	        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(trademarkOrderSummary);
        return null;
    }
}
