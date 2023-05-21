package com.wayapay.xerointegration.dto.waya.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WayaTransactionResponse {

    private boolean status;
    private int code;
    private String message;
    private Object data;
}