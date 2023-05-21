package com.wayapay.xerointegration.controller;

import com.google.gson.Gson;
import com.wayapay.xerointegration.dto.waya.response.ValidationPayload;
import com.wayapay.xerointegration.dto.xero.request.XeroBankTransactionRequestPayload;
import com.wayapay.xerointegration.dto.xero.response.XeroBankTransactionResponsePayload;
import com.wayapay.xerointegration.dto.xero.response.XeroSingleBankTransactionResponsePayload;
import com.wayapay.xerointegration.service.XeroIntegrationService;
import com.wayapay.xerointegration.validation.ModelValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/xero")
public class XeroIntegrationRestController
{

    @Autowired
    private XeroIntegrationService xeroIntegrationService;

    @Autowired
    private ModelValidator modelValidator;

    private static final Gson JSON = new Gson();


    @GetMapping("/bank-transaction/single")
    public ResponseEntity<XeroSingleBankTransactionResponsePayload> handleGetBankTransactionRequest(@RequestParam("bankTransactionId") String bankTransactionId){
        XeroSingleBankTransactionResponsePayload responsePayload = xeroIntegrationService.getSingleBankTransaction(bankTransactionId);
        return ResponseEntity.ok(responsePayload);
    }

    @PostMapping("/bank-transaction")
    public ResponseEntity<XeroBankTransactionResponsePayload> handleGetBankTransactionRequest(@RequestBody XeroBankTransactionRequestPayload requestPayload){
        ValidationPayload validationPayload = modelValidator.doModelValidation(requestPayload);
        if(validationPayload.isHasError()){
            return ResponseEntity.badRequest().body(JSON.fromJson(validationPayload.getErrorJson(), XeroBankTransactionResponsePayload.class));
        }
        XeroBankTransactionResponsePayload responsePayload = xeroIntegrationService.getTransactions(requestPayload);
        return ResponseEntity.ok(responsePayload);
    }
}
