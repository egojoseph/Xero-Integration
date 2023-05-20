package com.wayapay.xerointegration.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wayapay.xerointegration.pojos.XeroBankTransaction;
import com.wayapay.xerointegration.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    @Value("${xero.upload.transactions}")
    private String uploadTransaction;

    private RestTemplate restTemplate;

    @Override
    public void uploadTransactions(XeroBankTransaction xeroBankTransaction) throws URISyntaxException {
        String url = uploadTransaction;
        URI uri = new URI(url);

        ResponseEntity<String> response = restTemplate.postForEntity(uri, xeroBankTransaction, String.class);
        log.info("response code --> {}", response.getStatusCode().value());
        log.info("response from log ----->>> {}", response);

    }
}
