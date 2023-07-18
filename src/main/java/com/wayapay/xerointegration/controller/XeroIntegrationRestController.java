package com.wayapay.xerointegration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wayapay.xerointegration.dto.xero.request.XeroManualJournalUploadRequest;
import com.wayapay.xerointegration.dto.xero.response.UploadJournalResponse;
import com.wayapay.xerointegration.dto.xero.response.XeroJournalResponsePayload;
import com.wayapay.xerointegration.service.XeroIntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class XeroIntegrationRestController {

    @Autowired
    private XeroIntegrationService xeroIntegrationService;


    @GetMapping("/journal")
    public ResponseEntity<XeroJournalResponsePayload> handleGetBankTransactionRequest() throws JsonProcessingException {
        XeroJournalResponsePayload responsePayload = xeroIntegrationService.getJournalTransaction();
        return ResponseEntity.ok(responsePayload);
    }


    @PostMapping("/upload")
    public ResponseEntity<?> uploadTransaction(@RequestBody XeroManualJournalUploadRequest xeroManualJournalUploadRequest) throws IOException {
        UploadJournalResponse response =  xeroIntegrationService.uploadJournalTransaction(xeroManualJournalUploadRequest);
        return ResponseEntity.ok(response);

    }
}
