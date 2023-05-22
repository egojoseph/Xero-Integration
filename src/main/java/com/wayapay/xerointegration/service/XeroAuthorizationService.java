package com.wayapay.xerointegration.service;

import com.wayapay.xerointegration.dto.xero.response.AuthorizationResponsePayload;
import com.wayapay.xerointegration.dto.xero.response.InternalAccessTokenResponse;
import com.wayapay.xerointegration.dto.xero.response.XeroTenantResponseData;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface XeroAuthorizationService
{
    AuthorizationResponsePayload processXeroAuthorizationUrlRequest();
    String processApplicationAccessTokenOnRedirect(Map<String, String> xeroParams);

    InternalAccessTokenResponse getXeroAccessTokenWithRefresh();

    XeroTenantResponseData getCurrentTenantConnection();
}
