package com.wayapay.xerointegration.dto.xero.response;

import com.wayapay.xerointegration.dto.xero.request.XeroBankTransaction;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class XeroSingleBankTransactionResponsePayload extends WayaBankBaseResponsePayload
{
    XeroBankTransaction data;
}
