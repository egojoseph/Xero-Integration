package com.wayapay.xerointegration.payload.response;

import com.wayapay.xerointegration.payload.response.WayaBankBaseResponsePayload;
import com.wayapay.xerointegration.pojos.XeroBankTransaction;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class XeroBankTransactionResponsePayload extends WayaBankBaseResponsePayload
{
    private List<XeroBankTransaction> data;
}
