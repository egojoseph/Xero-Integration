package com.wayapay.xerointegration.service;

import com.wayapay.xerointegration.payload.request.XeroBankTransactionRequestPayload;
import com.wayapay.xerointegration.payload.response.XeroBankTransactionResponsePayload;
import com.wayapay.xerointegration.payload.response.XeroSingleBankTransactionResponsePayload;
import com.wayapay.xerointegration.pojos.XeroBankTransaction;

import java.net.URISyntaxException;

public interface XeroIntegrationService {
    void uploadTransactions(XeroBankTransaction xeroBankTransaction) throws URISyntaxException;

    XeroBankTransactionResponsePayload getTransactions(XeroBankTransactionRequestPayload requestPayload);

    XeroSingleBankTransactionResponsePayload getSingleBankTransaction(String bankTransactionId);
}
