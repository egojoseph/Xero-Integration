package com.wayapay.xerointegration.dto.xero.response;

import lombok.Data;

@Data
public class XeroTenantResponseData
{
    public String id;
    public String authEventId;
    public String tenantId;
    public String tenantType;
    public String tenantName;
    public String createdDateUtc;
    public String updatedDateUtc;
}
