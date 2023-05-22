package com.wayapay.xerointegration.dto.xero.response;

import lombok.Data;

@Data
public class AuthorizationData
{
    private String authorizationUrl;
    private String createdAt;
    private String createdBy;
}
