package com.wayapay.xerointegration.dto.xero.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Contacts {
    private String ContactID;
    private String Name;
}
