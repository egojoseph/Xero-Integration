package com.wayapay.xerointegration.service.impl;

import com.google.gson.Gson;
import com.wayapay.xerointegration.constant.Item;
import com.wayapay.xerointegration.dto.request.AccessTokenRequestDTO;
import com.wayapay.xerointegration.dto.response.AccessTokenResponseDTO;
import com.wayapay.xerointegration.service.GenericService;
import com.wayapay.xerointegration.storage.ZooItemKeeper;
import kong.unirest.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class GenericServiceImpl implements GenericService
{
    @Value("${xero.token-url}")
    private String xeroTokenUrl;

    @Value("${xero.grant-type}")
    private String xeroGrantType;

    @Value("${xero.scope}")
    private String xeroScope;

    @Value("${xero.client-id}")
    private String xeroClientId;

    @Value("${xero.client-secret}")
    private String xeroClientSecret;

    private static final Gson JSON = new Gson();


    @Override
    public String getForObject(String url, Map<String, String> headers, Map<String, Object> params) {
        String responseJson = null;
        HttpResponse<String> response;
        Unirest.config().verifySsl(false);
        try{
            GetRequest getRequest = Unirest.get(url)
                    .header("Content-Type", "application/json");
            if(headers != null)
                getRequest = getRequest.headers(headers);
            if(params != null)
                getRequest = getRequest.queryString(params);
            response = getRequest.asString();

            if(response != null){
                responseJson = response.getBody();
                log.info("HttpConnection Success: {} GET", responseJson);
            }else{
                log.info("HttpConnection Service Unavailable GET: {}", "Third party service unavailable");
            }
        }catch (UnirestException ex){
            log.error("Internal Server error while performing HttpConnection GET: {}", ex.getMessage());
            ex.printStackTrace();
        }
        return responseJson;
    }


    @Override
    public String getForObject(String url, Map<String, String> headers){
        return getForObject(url, headers, null);
    }

    @Override
    public String getForObject(String url) {
        return getForObject(url, null, null);
    }

    @Override
    public String postForObject(String url, String requestJson, Map<String, String> headers, Map<String, Object> params) {
        String responseJson = null;
        HttpResponse<String> response;
        Unirest.config().verifySsl(false);
        try{
            RequestBodyEntity postRequest = Unirest.post(url)
                    .header("Content-Type", "application/json")
                    .body(requestJson);
            if(headers != null)
                postRequest = postRequest.headers(headers);
            if(params != null)
                postRequest = postRequest.queryString(params);
            response = postRequest.asString();

            if(response != null){
                responseJson = response.getBody();
                log.info("HttpConnection Success POST: {}", responseJson);
            }else{
                log.info("HttpConnection Service Unavailable POST: {}", "Third party service unavailable");
            }
        }catch (UnirestException ex){
            log.error("Internal Server error while performing HttpConnection POST: {}", ex.getMessage());
            ex.printStackTrace();
        }
        return responseJson;
    }

    @Override
    public String postForObject(String url, Object requestObject, Map<String, String> headers, Map<String, Object> params) {
        String requestJson = JSON.toJson(requestObject);
        return postForObject(url, requestJson, headers, params);
    }

    @Override
    public String postForObject(String url, Object requestObject) {
        return postForObject(url, requestObject, null, null);
    }

    @Override
    public String getXeroAuthAccessToken() {
        AccessTokenResponseDTO responseDTO = ZooItemKeeper.getItem(Item.XERO_AUTH_TOKEN);
        long expireInMilliseconds = responseDTO.getExpiresIn();
        if(isTimeNotExpired(expireInMilliseconds)){
            return responseDTO.getAccessToken();
        }else{
            return exchangeWithXeroForAccessToken();
        }
    }

    private String exchangeWithXeroForAccessToken(){
        String credentialConcat = String.join(":", xeroClientId, xeroClientSecret);
        String base64 = Base64.getEncoder().encodeToString(credentialConcat.getBytes(StandardCharsets.UTF_8));
        String auth = "Basic ".concat(base64);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", auth);

        AccessTokenRequestDTO requestDTO = new AccessTokenRequestDTO();
        requestDTO.setGrantType(xeroGrantType);
        requestDTO.setScope(xeroScope);
        String requestJson = JSON.toJson(requestDTO);

        String responseJson = this.postForObject(xeroTokenUrl, requestJson, headers, null);
        if(responseJson != null){
            AccessTokenResponseDTO responseDTO = JSON.fromJson(responseJson, AccessTokenResponseDTO.class);
            String authToken = responseDTO.getAccessToken();
            if(authToken != null){
                ZooItemKeeper.saveItem(Item.XERO_AUTH_TOKEN, responseDTO);
                return authToken;
            }
        }
        return null;
    }

    private boolean isTimeNotExpired(long timeInMilliseconds){
        Date date = Date.from(Instant.ofEpochMilli(timeInMilliseconds));
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime.minusSeconds(10).isAfter(LocalDateTime.now());
    }
}
