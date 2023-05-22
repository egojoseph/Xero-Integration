package com.wayapay.xerointegration.dto.waya.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class WayaTransactionResponse {

    private boolean status;
    private int code;
    private String message;
    private TransactionData data;

}