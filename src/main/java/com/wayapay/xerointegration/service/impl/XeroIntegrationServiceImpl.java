package com.wayapay.xerointegration.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.wayapay.xerointegration.dto.waya.request.WayaTransactionRequest;
import com.wayapay.xerointegration.dto.waya.response.TransactionData;
import com.wayapay.xerointegration.dto.waya.response.WayaTransactionResponse;
import com.wayapay.xerointegration.dto.xero.request.*;
import com.wayapay.xerointegration.dto.xero.response.*;
import com.wayapay.xerointegration.service.GenericService;
import com.wayapay.xerointegration.service.XeroAuthorizationService;
import com.wayapay.xerointegration.service.XeroIntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
@Slf4j
public class XeroIntegrationServiceImpl implements XeroIntegrationService {

    @Value("${xero.upload.journal}")
    private String uploadJournal;

    @Value("${xero.fetch.journal}")
    private String fetchJournal;

    @Value("${waya.transaction}")
    private String wayaTransaction;

    @Value("${waya.token}")
    private String wayaToken;

    @Autowired
    private XeroAuthorizationService xeroAuthorizationService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public XeroJournalResponsePayload getJournalTransaction() throws JsonProcessingException {

        String accessToken = xeroAuthorizationService.getXeroAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(fetchJournal, HttpMethod.GET, requestEntity, String.class);
        log.info("result ----->>>> {}", result.getBody());
        return new ObjectMapper().readValue(result.getBody(), XeroJournalResponsePayload.class);

    }

    @Override
    public UploadJournalResponse uploadJournalTransaction(XeroManualJournalUploadRequest xeroManualJournalUploadRequest) throws JsonProcessingException {
        String accessToken = xeroAuthorizationService.getXeroAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        String requestBodyJson = new ObjectMapper().writeValueAsString(xeroManualJournalUploadRequest);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson, headers);

        ResponseEntity<String> result = restTemplate.exchange(uploadJournal, HttpMethod.POST, requestEntity, String.class);
        return new ObjectMapper().readValue(result.getBody(), UploadJournalResponse.class);
    }


    public WayaTransactionResponse getTransactionFromWaya(WayaTransactionRequest wayaTransactionRequest) throws URISyntaxException, IOException {
        String url = wayaTransaction+wayaTransactionRequest.getTransactionId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", wayaToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(url,HttpMethod.GET, requestEntity, String.class);

        WayaTransactionResponse response = new ObjectMapper().readValue(result.getBody(), WayaTransactionResponse.class);
        uploadToXero(response);
        return response;
    }



    public void uploadToXero(WayaTransactionResponse wayaTransactionResponse) throws URISyntaxException, JsonProcessingException {
        log.info("now trying to upload to xero");
        for (TransactionData response : wayaTransactionResponse.getData()) {
            XeroManualJournalUploadRequest xeroUploadRequest = createXeroUploadPayload(response);
            uploadJournalTransaction(xeroUploadRequest);
        }
    }

    private XeroManualJournalUploadRequest createXeroUploadPayload(TransactionData wayaResponse) {

        JournalLine journalLineCredit = JournalLine.builder().description("").lineAmount(0).accountCode("")
                .taxType("").tracking(Collections.singletonList(new Tracking("Region", "West"))).build();

        JournalLine journalLineDebit = JournalLine.builder().description("").lineAmount(0).accountCode("")
                .taxType("").tracking(Collections.singletonList(new Tracking("Region", "West"))).build();

        List<JournalLine> journalLineList = new ArrayList<>();
        journalLineList.add(journalLineCredit);
        journalLineList.add(journalLineDebit);

        XeroManualJournalUploadRequest xeroManualJournalUploadRequest = XeroManualJournalUploadRequest.builder()
                .date(wayaResponse.getTranDate()).status("").narration(wayaResponse.getTranNarrate())
                .lineAmountTypes("").journalLines(journalLineList).showOnCashBasisReports("false").build();
        log.info("Xero Manual Journal Upload Request ----->>>> {}", xeroManualJournalUploadRequest);
        return xeroManualJournalUploadRequest;
    }


    private Map<String, String> getXeroAuthHeader(){
        String accessToken = xeroAuthorizationService.getXeroAccessToken();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        log.info("Xero Auth header: {}", headers);
        return headers;
    }
}
