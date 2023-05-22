package com.wayapay.xerointegration.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wayapay.xerointegration.constant.Item;
import com.wayapay.xerointegration.dto.xero.response.*;
import com.wayapay.xerointegration.service.GenericService;
import com.wayapay.xerointegration.service.XeroAuthorizationService;
import com.wayapay.xerointegration.storage.ZooItemKeeper;
import kong.unirest.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class XeroAuthorizationServiceImpl implements XeroAuthorizationService
{
    @Value("${xero.grant-type}")
    private String xeroGrantType;
    @Value("${xero.refresh-grant-type}")
    private String refreshGrantType;
    @Value("${xero.scope}")
    private String xeroScope;
    @Value("${xero.client-id}")
    private String xeroClientId;
    @Value("${xero.client-secret}")
    private String xeroClientSecret;
    @Value("${xero.auth-url}")
    private String authUrl;
    @Value("${xero.redirect-uri}")
    private String xeroRedirectUri;
    @Value("${xero.response-type}")
    private String responseType;
    @Value("${xero.token-url}")
    private String xeroTokenUrl;
    @Value("${xero.tenant-url}")
    private String xeroTenantUrl;

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private GenericService genericService;

    private static final Gson JSON = new Gson();

    @Override
    public AuthorizationResponsePayload processXeroAuthorizationUrlRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("response_type", responseType);
        params.put("client_id", xeroClientId);
        params.put("redirect_uri", xeroRedirectUri);
        params.put("scope", xeroScope);
        params.put("state", UUID.randomUUID().toString());

        String fullAuthorizationPath = authUrl.concat("?").concat(getFullValueOfUrlParams(params));
        AuthorizationData authorizationData = new AuthorizationData();
        authorizationData.setAuthorizationUrl(fullAuthorizationPath);
        authorizationData.setCreatedAt(LocalDateTime.now().toString());
        authorizationData.setCreatedBy("SYSTEM");

        AuthorizationResponsePayload responsePayload = new AuthorizationResponsePayload();
        responsePayload.setStatus(true);
        responsePayload.setMessage(messageSource.getMessage("messages.request.success", null, Locale.ENGLISH));
        responsePayload.setTimeStamp(LocalDateTime.now().toString());
        responsePayload.setData(authorizationData);
        return responsePayload;
    }

    @Override
    public String processApplicationAccessTokenOnRedirect(Map<String, String> xeroParams) {
        String code = xeroParams.get("code");
        String state = xeroParams.get("state");
        String error = xeroParams.get("error");

        if(error == null && code != null && state != null){
            String accessToken = exchangeWithXeroForAccessToken(code);
            log.info("Access token retrieval success: {}", accessToken);
            return "success-page";
        }

        log.info("Access token retrieval failure: {}", error);
        return "error-page";
    }

    @Override
    public InternalAccessTokenResponse getXeroAccessTokenWithRefresh(){
        InternalAccessTokenResponse tokenResponse = new InternalAccessTokenResponse();

        AccessTokenResponseDTO responseDTO = ZooItemKeeper.getItem(Item.XERO_AUTH_TOKEN);
        if(responseDTO == null){
            tokenResponse.setPresent(false);
            tokenResponse.setAccessToken(null);
        }
        else{
            LocalDateTime accessTokenExpiration = LocalDateTime.parse(responseDTO.getSafeExpirationDateString());
            if(LocalDateTime.now().isAfter(accessTokenExpiration) || LocalDateTime.now().isEqual(accessTokenExpiration)){
                // Refresh the token.
                String freshAccessToken = exchangeWithXeroForRefreshToken();
                if(freshAccessToken != null){
                    tokenResponse.setPresent(true);
                    tokenResponse.setAccessToken(freshAccessToken);
                    return tokenResponse;
                }
                tokenResponse.setPresent(false);
                tokenResponse.setAccessToken(null);
                return tokenResponse;
            }
            tokenResponse.setPresent(true);
            tokenResponse.setAccessToken(responseDTO.getAccessToken());
        }
        return tokenResponse;
    }


    private String exchangeWithXeroForAccessToken(String code){
        String credentialConcat = String.join(":", xeroClientId, xeroClientSecret);
        String base64 = Base64.getEncoder().encodeToString(credentialConcat.getBytes(StandardCharsets.UTF_8));
        String auth = "Basic ".concat(base64);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", auth);

        Map<String, Object> fields = new HashMap<>();
        fields.put("grant_type", xeroGrantType);
        fields.put("code", code);
        fields.put("redirect_uri", xeroRedirectUri);

        log.info("RequestJson for access token: {}", fields.toString());

        String responseJson = genericService.postForForm(xeroTokenUrl, fields, headers, null);
        if(responseJson != null){
            AccessTokenResponseDTO responseDTO = JSON.fromJson(responseJson, AccessTokenResponseDTO.class);
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expirationDateTime = now.plusSeconds(responseDTO.getExpiresIn());
            LocalDateTime safeExpirationDateTime = expirationDateTime.minusMinutes(5);
            LocalDateTime refreshExpiration = now.plusDays(60);
            LocalDateTime refreshTokenSafeExpiration = refreshExpiration.minusDays(1);

            responseDTO.setExpirationDateString(expirationDateTime.toString());
            responseDTO.setSafeExpirationDateString(safeExpirationDateTime.toString());
            responseDTO.setRefreshTokenExpirationDateString(refreshExpiration.toString());
            responseDTO.setRefreshTokenSafeExpirationDateString(refreshTokenSafeExpiration.toString());

            String authToken = responseDTO.getAccessToken();
            if(authToken != null){
                ZooItemKeeper.saveItem(Item.XERO_AUTH_TOKEN, responseDTO);
                return authToken;
            }
        }
        return null;
    }

    private String exchangeWithXeroForRefreshToken(){
        String credentialConcat = String.join(":", xeroClientId, xeroClientSecret);
        String base64 = Base64.getEncoder().encodeToString(credentialConcat.getBytes(StandardCharsets.UTF_8));
        String auth = "Basic ".concat(base64);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", auth);

        // Get the old stored refresh token from the ZooItemKeeper.
        AccessTokenResponseDTO accessTokenResponseDTO = ZooItemKeeper.getItem(Item.XERO_AUTH_TOKEN);

        Map<String, Object> fields = new HashMap<>();
        fields.put("grant_type", refreshGrantType);
        fields.put("refresh_token", accessTokenResponseDTO.getRefreshToken());

        log.info("RequestJson for refresh token: {}", fields);

        String responseJson = genericService.postForForm(xeroTokenUrl, fields, headers, null);
        if(responseJson != null){
            AccessTokenResponseDTO responseDTO = JSON.fromJson(responseJson, AccessTokenResponseDTO.class);
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expirationDateTime = now.plusSeconds(accessTokenResponseDTO.getExpiresIn());
            LocalDateTime safeExpirationDateTime = expirationDateTime.minusMinutes(5);
            LocalDateTime refreshExpiration = now.plusDays(60);
            LocalDateTime refreshTokenSafeExpiration = refreshExpiration.minusDays(1);

            responseDTO.setExpirationDateString(expirationDateTime.toString());
            responseDTO.setSafeExpirationDateString(safeExpirationDateTime.toString());
            responseDTO.setRefreshTokenExpirationDateString(refreshExpiration.toString());
            responseDTO.setRefreshTokenSafeExpirationDateString(refreshTokenSafeExpiration.toString());

            String authToken = responseDTO.getAccessToken();
            if(authToken != null){
                ZooItemKeeper.saveItem(Item.XERO_AUTH_TOKEN, responseDTO);
                return authToken;
            }
        }
        return null;
    }


    @Override
    public XeroTenantResponseData getCurrentTenantConnection(){
        AccessTokenResponseDTO accessTokenResponseDTO = ZooItemKeeper.getItem(Item.XERO_AUTH_TOKEN);
        String accessToken = accessTokenResponseDTO.getAccessToken();
        String bodyPart = accessToken.split("\\.")[1];
        String bodyJson = new String(Base64.getUrlDecoder().decode(bodyPart));
        JSONObject jsonObject = new JSONObject(bodyJson);
        String authorizationEventId = jsonObject.getString("authentication_event_id");

        log.info("AuthorizationEventId: {}", authorizationEventId);

        // Call the tenant endpoint
        List<XeroTenantResponseData> connections = getAllConnectionsWithAccessToken(accessToken);
        return getCurrentConnectionWithAuthId(authorizationEventId, connections);
    }

    private List<XeroTenantResponseData> getAllConnectionsWithAccessToken(String accessToken){
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        String responseJsonList = genericService.getForObject(xeroTenantUrl, headers);

        log.info("ResponseJson for tenant connections: {}", responseJsonList);

        Type listType = new TypeToken<List<XeroTenantResponseData>>() {}.getType();
        List<XeroTenantResponseData> connections = JSON.fromJson(responseJsonList, listType);

        log.info("Connections: {}", connections);
        return connections;
    }

    private XeroTenantResponseData getCurrentConnectionWithAuthId(String authId, List<XeroTenantResponseData> connections){
        return connections.stream().filter(c -> c.getAuthEventId().equalsIgnoreCase(authId)).findFirst().orElse(connections.get(0));
    }
    private String getFullValueOfUrlParams(Map<String, String> params){
        StringJoiner stringJoiner = new StringJoiner("&");
        for(Map.Entry<String, String> entry: params.entrySet()){
            String paramConcat = entry.getKey().concat("=").concat(entry.getValue());
            stringJoiner.add(paramConcat);
        }
        return stringJoiner.toString();
    }
}
