package com.wayapay.xerointegration.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wayapay.xerointegration.dto.waya.request.WayaTransactionRequest;
import com.wayapay.xerointegration.dto.waya.response.WayaTransactionResponse;
import com.wayapay.xerointegration.dto.xero.request.XeroManualJournalUploadRequest;
import com.wayapay.xerointegration.dto.xero.response.UploadJournalResponse;
import com.wayapay.xerointegration.dto.xero.response.XeroJournalResponsePayload;

import java.net.URISyntaxException;

public interface XeroIntegrationService {
    XeroJournalResponsePayload getJournalTransaction() throws JsonProcessingException;
    UploadJournalResponse uploadJournalTransaction(XeroManualJournalUploadRequest xeroManualJournalUploadRequest) throws JsonProcessingException;


    WayaTransactionResponse getTransactionFromWaya(WayaTransactionRequest transaction) throws JsonProcessingException, URISyntaxException;
}
