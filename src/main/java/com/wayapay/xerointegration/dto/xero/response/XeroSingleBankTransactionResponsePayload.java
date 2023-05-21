package com.wayapay.xerointegration.dto.xero.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wayapay.xerointegration.dto.xero.request.XeroBankTransaction;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class XeroSingleBankTransactionResponsePayload extends WayaBankBaseResponsePayload
{
    XeroBankTransaction data;
}
