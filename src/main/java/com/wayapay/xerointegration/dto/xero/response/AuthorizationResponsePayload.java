package com.wayapay.xerointegration.dto.xero.response;

import lombok.Data;

@Data
public class AuthorizationResponsePayload
{
    private boolean status = false;
    private String message;
    private String timeStamp;
    private AuthorizationData data;
}
