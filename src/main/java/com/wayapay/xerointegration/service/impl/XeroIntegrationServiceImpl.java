package com.wayapay.xerointegration.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.wayapay.xerointegration.dto.waya.request.WayaTransactionRequest;
import com.wayapay.xerointegration.dto.waya.response.WayaTransactionResponse;
import com.wayapay.xerointegration.dto.xero.request.*;
import com.wayapay.xerointegration.dto.xero.response.XeroBankTransactionResponseDTO;
import com.wayapay.xerointegration.dto.xero.response.XeroBankTransactionResponsePayload;
import com.wayapay.xerointegration.dto.xero.response.XeroSingleBankTransactionResponsePayload;
import com.wayapay.xerointegration.service.GenericService;
import com.wayapay.xerointegration.service.XeroIntegrationService;
import lombok.NonNull;
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

    @Value("${waya.transaction}")
    private String wayaTransaction;

    @Value("${waya.token}")
    private String wayaToken;

    @Autowired
    private GenericService genericService;

    @Autowired
    private MessageSource messageSource;

    private RestTemplate restTemplate;

    private static final Gson JSON = new Gson();

    @Override
    public WayaTransactionResponse getTransactionFromWaya(WayaTransactionRequest wayaTransactionRequest) throws URISyntaxException, IOException {
        String url = wayaTransaction+wayaTransactionRequest.getTransactionId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", wayaToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(url,HttpMethod.GET, requestEntity, String.class);

        WayaTransactionResponse response = new ObjectMapper().readValue(result.getBody(), WayaTransactionResponse.class);
        log.info("response after mapping ------>>> {}", response);
        uploadToXero(response);
        return response;
    }

    public void uploadToXero(WayaTransactionResponse wayaTransactionResponse) throws URISyntaxException, JsonProcessingException {
        String xeroUpload = uploadTransaction;
        URI uri = new URI(xeroUpload);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "token to be gotten from xero"); // TODO: 21/05/2023 get token for post to Xero

        XeroUploadRequest xeroUploadRequest = createXeroUploadPayload(wayaTransactionResponse);

        HttpEntity<XeroUploadRequest> entity = new HttpEntity<>(xeroUploadRequest, headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri, entity, String.class);
        log.info("response code is -----> {} and response body is ------->>> {}", result.getStatusCode().value(), result.getBody());

        XeroBankTransactionResponsePayload responsePayload = new ObjectMapper().readValue(result.getBody(), XeroBankTransactionResponsePayload.class);
        log.info("response payload --------->>>>> {}", responsePayload);
    }

    private XeroUploadRequest createXeroUploadPayload(WayaTransactionResponse wayaTransactionResponse) {
        List<LineItems> lineItemsList = new ArrayList<>();
        List<Tracking> trackingList = new ArrayList<>();

        Tracking tracking = Tracking.builder().Name("Activity/Workstream").Option("Website management").build();
        trackingList.add(tracking);

        LineItems lineItem = LineItems.builder().Description(wayaTransactionResponse.getData().tranNarrate)
                .AccountCode(String.valueOf(wayaTransactionResponse.getData().relatedTransId))
                .UnitAmount(String.valueOf(wayaTransactionResponse.getData().tranAmount))
                .Tracking(trackingList).build();
        lineItemsList.add(lineItem);
        Contacts contacts = Contacts.builder().ContactID(wayaTransactionResponse.getData().tranId).build();
        BankAccount bankAccount = BankAccount.builder().Code(wayaTransactionResponse.getData().tranGL).build();

        XeroUploadRequest uploadRequest = XeroUploadRequest.builder().Type("RECEIVE-PREPAYMENT").Contact(contacts)
                .BankAccount(bankAccount).LineAmountTypes("Inclusive").LineItems(lineItemsList)
                .Url("http://www.accounting20.com").build();
        log.info("xero upload request ----->>>> {}", uploadRequest);

        return uploadRequest;
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
