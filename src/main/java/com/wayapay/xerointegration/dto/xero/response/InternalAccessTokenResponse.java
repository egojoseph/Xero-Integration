package com.wayapay.xerointegration.dto.xero.response;

import lombok.Data;

@Data
public class InternalAccessTokenResponse
{
    private boolean isPresent;
    private String accessToken;
}
