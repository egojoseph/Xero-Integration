package com.wayapay.xerointegration.dto.waya.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WayaTransactionResponse {

    private boolean status;
    private int code;
    private String message;
    private List<TransactionData> data;

}