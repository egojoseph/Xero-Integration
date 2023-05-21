package com.wayapay.xerointegration.service.impl;

import com.google.gson.Gson;
import com.wayapay.xerointegration.dto.response.XeroBankTransactionResponseDTO;
import com.wayapay.xerointegration.payload.request.XeroBankTransactionRequestPayload;
import com.wayapay.xerointegration.payload.response.XeroBankTransactionResponsePayload;
import com.wayapay.xerointegration.payload.response.XeroSingleBankTransactionResponsePayload;
import com.wayapay.xerointegration.pojos.Param;
import com.wayapay.xerointegration.pojos.XeroBankTransaction;
import com.wayapay.xerointegration.service.GenericService;
import com.wayapay.xerointegration.service.XeroIntegrationService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class XeroIntegrationServiceImpl implements XeroIntegrationService {

    @Value("${xero.upload.transactions}")
    private String uploadTransaction;

    @Value("${xero.fetch.transactions}")
    private String fetchTransactions;

    @Autowired
    private GenericService genericService;

    @Autowired
    private MessageSource messageSource;

    private RestTemplate restTemplate;

    private static final Gson JSON = new Gson();

    @Override
    public void uploadTransactions(XeroBankTransaction xeroBankTransaction) throws URISyntaxException {
        String url = uploadTransaction;
        URI uri = new URI(url);

        ResponseEntity<String> response = restTemplate.postForEntity(uri, xeroBankTransaction, String.class);
        log.info("response code --> {}", response.getStatusCode().value());
        log.info("response from log ----->>> {}", response);

    }

    @Override
    public XeroBankTransactionResponsePayload getTransactions(XeroBankTransactionRequestPayload requestPayload){
        XeroBankTransactionResponsePayload responsePayload = new XeroBankTransactionResponsePayload();

        List<Param> filters = requestPayload.getFilters();
        filters = filters.stream()
                .filter(f -> f.getParamName() != null && f.getParamValue() != null)
                .collect(Collectors.toList());

        String baseUrl;
        if(!filters.isEmpty()){
            baseUrl = fetchTransactions.concat("?where=");
            StringJoiner stringJoiner = new StringJoiner("AND");
            filters.forEach(filter -> {
                String paramName = filter.getParamName();
                String paramValue = filter.getParamValue();
                String filterQuery = paramName.concat("==").concat(paramValue);
                stringJoiner.add(filterQuery);
            });
            baseUrl = baseUrl.concat(stringJoiner.toString());
        }else{
            baseUrl = fetchTransactions;
        }

        String completeQuery;
        if(requestPayload.getPage() > 0) {
            if (!filters.isEmpty()) {
                completeQuery = baseUrl.concat("&").concat("page=")
                        .concat(String.valueOf(requestPayload.getPage()));
            } else {
                completeQuery = baseUrl.concat("?").concat("page=")
                        .concat(String.valueOf(requestPayload.getPage()));
            }
        }
        else{
            completeQuery = baseUrl;
        }

        log.info("Complete Bank transactions query url: {}", completeQuery);

        String responseJsonGet = genericService.getForObject(completeQuery, getXeroAuthHeader());

        log.info("ResponseJson on bank transactions query: {}", responseJsonGet);

        XeroBankTransactionResponseDTO responseDTO = JSON.fromJson(responseJsonGet, XeroBankTransactionResponseDTO.class);
        responsePayload.setTimeStamp(LocalDateTime.now().toString());

        if(responseDTO != null && responseDTO.getBankTransactions() != null){
            responsePayload.setStatus(true);
            responsePayload.setMessage(messageSource.getMessage("messages.request.success", null, Locale.ENGLISH));
            responsePayload.setData(responseDTO.getBankTransactions());
            return responsePayload;
        }

        responsePayload.setStatus(false);
        responsePayload.setMessage(messageSource.getMessage("messages.request.failure", null, Locale.ENGLISH));
        responsePayload.setData(new ArrayList<>());
        return responsePayload;
    }

    @Override
    public XeroSingleBankTransactionResponsePayload getSingleBankTransaction(@NonNull String bankTransactionId){
        XeroSingleBankTransactionResponsePayload responsePayload = new XeroSingleBankTransactionResponsePayload();

        String completeUrl = fetchTransactions.concat("/").concat(bankTransactionId);
        log.info("Complete query single bank transaction url: {}", completeUrl);

        String responseJsonGet = genericService.getForObject(completeUrl, getXeroAuthHeader());

        log.info("ResponseJson on bank transactions query: {}", responseJsonGet);

        XeroBankTransaction responseDTO = JSON.fromJson(responseJsonGet, XeroBankTransaction.class);
        responsePayload.setTimeStamp(LocalDateTime.now().toString());

        if(responseDTO != null && responseDTO.getReference() != null){
            responsePayload.setStatus(true);
            responsePayload.setMessage(messageSource.getMessage("messages.request.success", null, Locale.ENGLISH));
            responsePayload.setData(responseDTO);
            return responsePayload;
        }

        responsePayload.setStatus(false);
        responsePayload.setMessage(messageSource.getMessage("messages.request.failed", null, Locale.ENGLISH));
        responsePayload.setData(responseDTO);
        return responsePayload;
    }

    private Map<String, String> getXeroAuthHeader(){
        String bearerToken = genericService.getXeroAuthAccessToken();
        String authHeader = "Bearer ".concat(bearerToken);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", authHeader);
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        return headers;
    }
}
