package com.wayapay.xerointegration.dto.xero.request;

import lombok.Data;

@Data
public class LineItems {

    private String Description;
    private String UnitAmount;
    private String AccountCode;
}
