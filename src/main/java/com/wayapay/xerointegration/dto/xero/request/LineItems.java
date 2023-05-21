package com.wayapay.xerointegration.dto.xero.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LineItems {

    private String Description;
    private String Quantity;
    private String UnitAmount;
    private String AccountCode;
}
