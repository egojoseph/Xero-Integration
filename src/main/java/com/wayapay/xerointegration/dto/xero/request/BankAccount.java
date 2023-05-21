package com.wayapay.xerointegration.dto.xero.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankAccount {
    private String Code;
}
