package com.wayapay.xerointegration.dto.waya.request;

import lombok.Data;

@Data
public class WayaTransactionRequest {
    private String userId;
    private String transactionId;
}