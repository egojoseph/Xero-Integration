package com.wayapay.xerointegration.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wayapay.xerointegration.dto.waya.request.WayaTransactionRequest;
import com.wayapay.xerointegration.dto.waya.response.WayaTransactionResponse;
import com.wayapay.xerointegration.dto.xero.request.XeroBankTransactionRequestPayload;
import com.wayapay.xerointegration.dto.xero.response.XeroBankTransactionResponsePayload;
import com.wayapay.xerointegration.dto.xero.response.XeroSingleBankTransactionResponsePayload;
import com.wayapay.xerointegration.dto.xero.request.XeroBankTransaction;

import java.io.IOException;
import java.net.URISyntaxException;

public interface XeroIntegrationService {

    XeroBankTransactionResponsePayload getTransactions(XeroBankTransactionRequestPayload requestPayload);

    XeroSingleBankTransactionResponsePayload getSingleBankTransaction(String bankTransactionId);
    WayaTransactionResponse getTransactionFromWaya(WayaTransactionRequest transaction) throws URISyntaxException, IOException;
}
