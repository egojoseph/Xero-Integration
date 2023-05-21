package com.wayapay.xerointegration.dto.xero.response;

import lombok.Data;

@Data
public class WayaBankBaseResponsePayload
{
    private boolean status = false;
    private String message;
    private String timeStamp;
}
