package com.wayapay.xerointegration.dto.xero.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WayaBankBaseResponsePayload
{
    private boolean status = false;
    private String message;
    private String timeStamp;
}
