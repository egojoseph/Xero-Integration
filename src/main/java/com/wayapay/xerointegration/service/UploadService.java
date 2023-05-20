package com.wayapay.xerointegration.service;

import com.wayapay.xerointegration.pojos.XeroBankTransaction;

import java.net.URISyntaxException;

public interface UploadService {
    void uploadTransactions(XeroBankTransaction xeroBankTransaction) throws URISyntaxException;
}
