package com.wayapay.xerointegration.payload.response;

import com.wayapay.xerointegration.pojos.XeroBankTransaction;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class XeroSingleBankTransactionResponsePayload extends WayaBankBaseResponsePayload
{
    XeroBankTransaction data;
}
