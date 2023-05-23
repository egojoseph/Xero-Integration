package com.wayapay.xerointegration.dto.xero.response;

import lombok.Data;

@Data
public class XeroError {

    public String Title;
    public String Detail;
    public int Status;
    public String Instance;
}
